package app

import (
	"bufio"
	"errors"
	"flag"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"runtime"
	"strings"
	"time"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/ops"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/render"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/tui"
)

const (
	defaultRepoURL    = "https://github.com/Swofty-Developments/HypixelSkyBlock.git"
	defaultRepoBranch = "master"
	bootstrapRepoDir  = "repo"
)

type cliOptions struct {
	installDir string
	action     string
	kubeconfig string
	staffUser  string
	status     bool
	watch      bool
	autoDeps   bool
	stageUI    bool
}

func Run() error {
	defaultInstallDir, err := profile.DefaultInstallDir()
	if err != nil {
		return err
	}

	opts := parseFlags(defaultInstallDir)

	selectedDir := profile.ExpandHome(opts.installDir)
	repoRoot, err := resolveRepoRoot(selectedDir)
	if err != nil {
		return err
	}
	p, err := profile.LoadOrDefault(repoRoot, selectedDir)
	if err != nil {
		return err
	}

	runAction, actionArg, updatedProfile, err := selectAction(p, opts)
	if err != nil {
		return err
	}
	p = updatedProfile
	if runAction == "" {
		return errors.New("no action selected")
	}

	if trimmed := profile.ExpandHome(opts.kubeconfig); trimmed != "" {
		p.KubeconfigPath = trimmed
	}
	if runAction == tui.ActionComposeStaff && actionArg == "" {
		return errors.New("staff username is required for compose-make-staff")
	}

	ops.SetKubernetesStageProgressUI(opts.stageUI)

	if err := persistProfile(&p, runAction); err != nil {
		return err
	}
	if opts.autoDeps {
		if err := ensureDependencies(runAction, p); err != nil {
			return err
		}
	}
	if warnings, err := ops.Preflight(runAction, p); err != nil {
		return err
	} else {
		for _, warning := range warnings {
			fmt.Printf("note: %s\n", warning)
		}
	}

	return executeAction(runAction, actionArg, p)
}

func showStatus(p profile.Profile) error {
	if p.Runtime == profile.RuntimeK8s {
		return ops.KubernetesStatus(p)
	}
	return ops.ComposeStatus(p)
}

func watchStatus(p profile.Profile) error {
	for {
		fmt.Print("\033[H\033[2J")
		fmt.Printf("Watching %s status at %s\n\n", p.Runtime, time.Now().Format(time.RFC3339))
		if err := showStatus(p); err != nil {
			return err
		}
		time.Sleep(3 * time.Second)
	}
}

func parseFlags(defaultInstallDir string) cliOptions {
	var opts cliOptions

	flag.StringVar(&opts.installDir, "dir", defaultInstallDir, "Install directory")
	flag.StringVar(&opts.action, "action", "", "Action to run")
	flag.StringVar(&opts.kubeconfig, "kubeconfig", "", "Optional kubeconfig path override for Kubernetes actions")
	flag.StringVar(&opts.staffUser, "staff-user", "", "Username to promote when using compose-make-staff")
	flag.BoolVar(&opts.status, "status", false, "Show current environment status")
	flag.BoolVar(&opts.watch, "watch", false, "Watch current environment status")
	flag.BoolVar(&opts.autoDeps, "auto-install-deps", true, "Install missing Linux dependencies after confirmation")
	flag.BoolVar(&opts.stageUI, "k8s-stage-progress-ui", true, "Render stage-level Kubernetes progress UI")
	flag.Usage = func() {
		fmt.Println("Usage: hypixel-setup [--dir PATH] [--action ACTION] [--kubeconfig PATH] [--staff-user USERNAME] [--status] [--watch] [--auto-install-deps=true|false] [--k8s-stage-progress-ui=true|false]")
		fmt.Println()
		fmt.Println("Actions:")
		fmt.Printf("  %s, %s, %s, %s\n", tui.ActionSave, tui.ActionComposeRender, tui.ActionComposeApply, tui.ActionComposeStaff)
		fmt.Printf("  %s, %s, %s, %s\n", tui.ActionK8sRender, tui.ActionK8sBuild, tui.ActionK8sDeploy, tui.ActionK8sFull)
		fmt.Printf("  %s, %s\n", tui.ActionStatus, tui.ActionWatch)
		fmt.Println()
		fmt.Printf("Go runtime: %s %s\n", runtime.Version(), runtime.GOOS)
	}
	flag.Parse()

	return opts
}

func selectAction(p profile.Profile, opts cliOptions) (string, string, profile.Profile, error) {
	action := strings.TrimSpace(opts.action)
	argument := strings.TrimSpace(opts.staffUser)

	switch {
	case opts.status:
		action = tui.ActionStatus
	case opts.watch:
		action = tui.ActionWatch
	}

	if action != "" {
		return action, argument, p, nil
	}

	nextAction, nextArg, updated, err := tui.RunWizard(p)
	if err != nil {
		return "", "", p, err
	}
	return nextAction, nextArg, updated, nil
}

func persistProfile(p *profile.Profile, action string) error {
	p.Normalize()
	p.Touch(action)
	return profile.Save(*p)
}

func executeAction(action, argument string, p profile.Profile) error {
	switch action {
	case tui.ActionSave:
		fmt.Printf("Profile saved to %s\n", filepath.Join(p.InstallDir, profile.StateFileName))
		return nil
	case tui.ActionComposeRender:
		return render.GenerateComposeAssets(p)
	case tui.ActionComposeApply:
		if err := render.GenerateComposeAssets(p); err != nil {
			return err
		}
		return ops.ApplyCompose(p)
	case tui.ActionComposeStaff:
		return ops.PromoteComposeUserToStaff(p, argument)
	case tui.ActionK8sRender:
		return render.GenerateKubernetesAssets(p)
	case tui.ActionK8sBuild:
		return ops.BuildImages(p)
	case tui.ActionK8sDeploy:
		if err := render.GenerateKubernetesAssets(p); err != nil {
			return err
		}
		if err := ops.DeployKubernetes(p); err != nil {
			return err
		}
		return ops.KubernetesStatus(p)
	case tui.ActionK8sFull:
		if err := render.GenerateKubernetesAssets(p); err != nil {
			return err
		}
		return ops.FullKubernetesSetup(p)
	case tui.ActionStatus:
		return showStatus(p)
	case tui.ActionWatch:
		return watchStatus(p)
	default:
		return fmt.Errorf("unknown action %q", action)
	}
}

func ensureDependencies(action string, p profile.Profile) error {
	required := ops.RequiredTools(action, p)
	missing := make([]string, 0, len(required))
	for _, tool := range required {
		if err := ops.Require(tool); err != nil {
			missing = append(missing, tool)
		}
	}
	if len(missing) == 0 {
		return nil
	}

	if runtime.GOOS != "linux" {
		return fmt.Errorf("missing required dependencies for %s: %s", action, strings.Join(missing, ", "))
	}

	confirmed, err := confirmDependencyInstall(missing)
	if err != nil {
		return err
	}
	if !confirmed {
		return fmt.Errorf("missing required dependencies for %s: %s", action, strings.Join(missing, ", "))
	}

	if err := ops.InstallDependenciesWithProgress(missing); err != nil {
		return err
	}
	return nil
}

func confirmDependencyInstall(missing []string) (bool, error) {
	if !stdinIsTTY() {
		return false, fmt.Errorf("missing dependencies require confirmation in interactive mode: %s", strings.Join(missing, ", "))
	}

	fmt.Printf("Missing dependencies: %s\n", strings.Join(missing, ", "))
	fmt.Print("Install these dependencies automatically now? [Y/n]: ")
	reader := bufio.NewReader(os.Stdin)
	response, err := reader.ReadString('\n')
	if err != nil && !errors.Is(err, io.EOF) {
		return false, err
	}
	answer := strings.ToLower(strings.TrimSpace(response))
	if answer == "" || answer == "y" || answer == "yes" {
		return true, nil
	}
	if answer == "n" || answer == "no" {
		return false, nil
	}
	return false, fmt.Errorf("invalid answer %q, expected y or n", answer)
}

func stdinIsTTY() bool {
	info, err := os.Stdin.Stat()
	if err != nil {
		return false
	}
	return (info.Mode() & os.ModeCharDevice) != 0
}

func resolveRepoRoot(installDir string) (string, error) {
	wd, err := os.Getwd()
	if err != nil {
		return "", err
	}
	return resolveRepoRootFrom(wd, installDir, bootstrapRepoCheckout)
}

func resolveRepoRootFrom(wd, installDir string, bootstrap func(string) error) (string, error) {
	if repoRoot, err := findRepoRootFrom(wd); err == nil {
		return repoRoot, nil
	}

	bootstrappedRoot := filepath.Join(installDir, bootstrapRepoDir)
	if saved, err := profile.Load(installDir); err == nil && isRepoRoot(saved.RepoRoot) {
		if samePath(saved.RepoRoot, bootstrappedRoot) {
			if err := bootstrap(bootstrappedRoot); err != nil {
				return "", fmt.Errorf("could not refresh managed repository checkout in %s: %w", bootstrappedRoot, err)
			}
			return bootstrappedRoot, nil
		}
		return saved.RepoRoot, nil
	}

	if isRepoRoot(bootstrappedRoot) {
		if err := bootstrap(bootstrappedRoot); err != nil {
			return "", fmt.Errorf("could not refresh managed repository checkout in %s: %w", bootstrappedRoot, err)
		}
		return bootstrappedRoot, nil
	}

	if err := bootstrap(bootstrappedRoot); err != nil {
		return "", fmt.Errorf("could not locate repository root and failed to bootstrap checkout in %s: %w", bootstrappedRoot, err)
	}
	if !isRepoRoot(bootstrappedRoot) {
		return "", fmt.Errorf("bootstrapped checkout in %s is missing expected repository files", bootstrappedRoot)
	}
	return bootstrappedRoot, nil
}

func bootstrapRepoCheckout(repoRoot string) error {
	parentDir := filepath.Dir(repoRoot)
	if err := os.MkdirAll(parentDir, 0o755); err != nil {
		return err
	}

	if exists(repoRoot) && !exists(filepath.Join(repoRoot, ".git")) {
		return fmt.Errorf("%s already exists but is not a git checkout", repoRoot)
	}

	repoURL := envOrDefault("HYPIXEL_SETUP_REPO_URL", defaultRepoURL)
	repoBranch := envOrDefault("HYPIXEL_SETUP_REPO_BRANCH", defaultRepoBranch)

	if exists(filepath.Join(repoRoot, ".git")) {
		if err := runCommand(parentDir, "git", "-C", repoRoot, "fetch", "origin", repoBranch, "--depth", "1"); err != nil {
			return err
		}
		return runCommand(parentDir, "git", "-C", repoRoot, "checkout", "origin/"+repoBranch)
	}

	return runCommand(parentDir, "git", "clone", "--depth", "1", "--branch", repoBranch, repoURL, repoRoot)
}

func runCommand(dir string, name string, args ...string) error {
	cmd := exec.Command(name, args...)
	cmd.Dir = dir
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	return cmd.Run()
}

func envOrDefault(key, fallback string) string {
	value := os.Getenv(key)
	if value == "" {
		return fallback
	}
	return value
}

func findRepoRootFrom(wd string) (string, error) {
	for dir := wd; ; dir = filepath.Dir(dir) {
		if isRepoRoot(dir) {
			return dir, nil
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", errors.New("could not locate repository root")
		}
	}
}

func isRepoRoot(dir string) bool {
	return exists(filepath.Join(dir, "settings.gradle.kts")) && exists(filepath.Join(dir, "configuration", "velocity.toml"))
}

func exists(path string) bool {
	_, err := os.Stat(path)
	return err == nil
}

func samePath(a, b string) bool {
	return filepath.Clean(a) == filepath.Clean(b)
}
