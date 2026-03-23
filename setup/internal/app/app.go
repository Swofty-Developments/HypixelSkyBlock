package app

import (
	"errors"
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"runtime"
	"time"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/ops"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/render"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/tui"
)

func Run() error {
	repoRoot, err := findRepoRoot()
	if err != nil {
		return err
	}

	defaultInstallDir, err := profile.DefaultInstallDir()
	if err != nil {
		return err
	}

	var (
		installDir = flag.String("dir", defaultInstallDir, "Install directory")
		action     = flag.String("action", "", "Action to run")
		status     = flag.Bool("status", false, "Show current environment status")
		watch      = flag.Bool("watch", false, "Watch current environment status")
	)
	flag.Usage = func() {
		fmt.Println("Usage: hypixel-setup [--dir PATH] [--action ACTION] [--status] [--watch]")
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

	p.Normalize()
	p.Touch(runAction)
	if err := profile.Save(p); err != nil {
		return err
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
		return ops.BuildAndPushImages(p)
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

func findRepoRoot() (string, error) {
	wd, err := os.Getwd()
	if err != nil {
		return "", err
	}
	for dir := wd; ; dir = filepath.Dir(dir) {
		if exists(filepath.Join(dir, "settings.gradle.kts")) && exists(filepath.Join(dir, "configuration", "velocity.toml")) {
			return dir, nil
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", errors.New("could not locate repository root")
		}
	}
}

func exists(path string) bool {
	_, err := os.Stat(path)
	return err == nil
}
