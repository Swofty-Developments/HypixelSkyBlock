package app

import (
	"errors"
	"flag"
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"runtime"
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

func Run() error {
	defaultInstallDir, err := profile.DefaultInstallDir()
	if err != nil {
		return err
	}

	var (
		installDir = flag.String("dir", defaultInstallDir, "Install directory")
		action     = flag.String("action", "", "Action to run")
		kubeconfig = flag.String("kubeconfig", "", "Optional kubeconfig path override for Kubernetes actions")
		status     = flag.Bool("status", false, "Show current environment status")
		watch      = flag.Bool("watch", false, "Watch current environment status")
	)
	flag.Usage = func() {
		fmt.Println("Usage: hypixel-setup [--dir PATH] [--action ACTION] [--kubeconfig PATH] [--status] [--watch]")
		fmt.Println()
		fmt.Println("Actions:")
		fmt.Printf("  %s, %s, %s\n", tui.ActionSave, tui.ActionComposeRender, tui.ActionComposeApply)
		fmt.Printf("  %s, %s, %s, %s\n", tui.ActionK8sRender, tui.ActionK8sBuild, tui.ActionK8sDeploy, tui.ActionK8sFull)
		fmt.Printf("  %s, %s\n", tui.ActionStatus, tui.ActionWatch)
		fmt.Println()
		fmt.Printf("Go runtime: %s %s\n", runtime.Version(), runtime.GOOS)
	}
	flag.Parse()

	selectedDir := profile.ExpandHome(*installDir)
	repoRoot, err := resolveRepoRoot(selectedDir)
	if err != nil {
		return err
	}
	p, err := profile.LoadOrDefault(repoRoot, selectedDir)
	if err != nil {
		return err
	}

	runAction := *action
	if *status {
		runAction = tui.ActionStatus
	}
	if *watch {
		runAction = tui.ActionWatch
	}
	if runAction == "" {
		nextAction, updated, err := tui.RunWizard(p)
		if err != nil {
			return err
		}
		p = updated
		runAction = nextAction
	}
	if trimmed := profile.ExpandHome(*kubeconfig); trimmed != "" {
		p.KubeconfigPath = trimmed
	}

	p.Normalize()
	p.Touch(runAction)
	if err := profile.Save(p); err != nil {
		return err
	}
	if warnings, err := ops.Preflight(runAction, p); err != nil {
		return err
	} else {
		for _, warning := range warnings {
			fmt.Printf("note: %s\n", warning)
		}
	}

	switch runAction {
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
		return fmt.Errorf("unknown action %q", runAction)
	}
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

	if saved, err := profile.Load(installDir); err == nil && isRepoRoot(saved.RepoRoot) {
		return saved.RepoRoot, nil
	}

	bootstrappedRoot := filepath.Join(installDir, bootstrapRepoDir)
	if isRepoRoot(bootstrappedRoot) {
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
