package ops

import (
	"errors"
	"os"
	"path/filepath"
	"reflect"
	"strings"
	"testing"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func TestKubernetesBuilderForStandardTarget(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s
	p.KubernetesTarget = profile.KubernetesTargetStandard

	name, args, err := kubernetesBuilder(p)
	if err != nil {
		t.Fatalf("kubernetesBuilder returned error: %v", err)
	}
	if name != "nerdctl" {
		t.Fatalf("expected nerdctl, got %q", name)
	}
	if len(args) != 2 || args[0] != "--namespace" || args[1] != "k8s.io" {
		t.Fatalf("unexpected args: %v", args)
	}
}

func TestKubernetesBuilderForK3dTarget(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s
	p.KubernetesTarget = profile.KubernetesTargetK3d

	name, args, err := kubernetesBuilder(p)
	if err != nil {
		t.Fatalf("kubernetesBuilder returned error: %v", err)
	}
	if name != "docker" {
		t.Fatalf("expected docker, got %q", name)
	}
	if len(args) != 0 {
		t.Fatalf("expected no args, got %v", args)
	}
}

func TestKubernetesBuilderForMinikubeTarget(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s
	p.KubernetesTarget = profile.KubernetesTargetMinikube

	name, args, err := kubernetesBuilder(p)
	if err != nil {
		t.Fatalf("kubernetesBuilder returned error: %v", err)
	}
	if name != "docker" {
		t.Fatalf("expected docker, got %q", name)
	}
	if len(args) != 0 {
		t.Fatalf("expected no args, got %v", args)
	}
}

func TestRequiresKubernetesAccessForLocalFullSetup(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s
	p.KubernetesTarget = profile.KubernetesTargetK3d

	if !requiresKubernetesAccess("k8s-deploy", p) {
		t.Fatal("expected deploy to require kubernetes access")
	}
	if requiresKubernetesAccess("k8s-full", p) {
		t.Fatal("expected full setup to bootstrap local targets before checking kubernetes access")
	}
}

func TestRunKubernetesStagesSkipsDisabledStages(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s

	called := make([]string, 0, 2)
	stages := []kubernetesSetupStage{
		{
			name: "first",
			run: func(profile.Profile) error {
				called = append(called, "first")
				return nil
			},
		},
		{
			name: "second",
			enabled: func(profile.Profile) bool {
				return false
			},
			run: func(profile.Profile) error {
				called = append(called, "second")
				return nil
			},
		},
		{
			name: "third",
			run: func(profile.Profile) error {
				called = append(called, "third")
				return nil
			},
		},
	}

	if err := runKubernetesStages(p, stages); err != nil {
		t.Fatalf("runKubernetesStages returned error: %v", err)
	}

	if !reflect.DeepEqual(called, []string{"first", "third"}) {
		t.Fatalf("unexpected stage order: %v", called)
	}
}

func TestRunKubernetesStagesWrapsStageError(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s

	err := runKubernetesStages(p, []kubernetesSetupStage{
		{
			name: "failing stage",
			run: func(profile.Profile) error {
				return errors.New("boom")
			},
		},
	})
	if err == nil {
		t.Fatal("expected an error")
	}
	if !strings.Contains(err.Error(), "failing stage failed") {
		t.Fatalf("expected stage context in error, got %v", err)
	}
}

func TestRunKubernetesStagesRunsCleanupOnFailure(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s

	cleanupCalled := false
	err := runKubernetesStages(p, []kubernetesSetupStage{
		{
			name: "failing stage",
			run: func(profile.Profile) error {
				return errors.New("boom")
			},
			cleanup: func(profile.Profile) error {
				cleanupCalled = true
				return nil
			},
		},
	})
	if err == nil {
		t.Fatal("expected an error")
	}
	if !cleanupCalled {
		t.Fatal("expected cleanup hook to be invoked")
	}
}

func TestRunKubernetesStagesResumesFromCheckpoint(t *testing.T) {
	installDir := t.TempDir()
	p := profile.Default("/repo", installDir)
	p.Runtime = profile.RuntimeK8s

	firstStageRuns := 0
	secondStageRuns := 0
	shouldFail := true

	stages := []kubernetesSetupStage{
		{
			name: "first",
			run: func(profile.Profile) error {
				firstStageRuns++
				return nil
			},
		},
		{
			name: "second",
			run: func(profile.Profile) error {
				secondStageRuns++
				if shouldFail {
					shouldFail = false
					return errors.New("transient")
				}
				return nil
			},
		},
	}

	if err := runKubernetesStages(p, stages); err == nil {
		t.Fatal("expected first run to fail")
	}
	if err := runKubernetesStages(p, stages); err != nil {
		t.Fatalf("expected resumed run to succeed, got %v", err)
	}

	if firstStageRuns != 1 {
		t.Fatalf("expected first stage to run once, got %d", firstStageRuns)
	}
	if secondStageRuns != 2 {
		t.Fatalf("expected second stage to run twice, got %d", secondStageRuns)
	}

	if _, err := os.Stat(filepath.Join(installDir, ".k8s-full-state.json")); !errors.Is(err, os.ErrNotExist) {
		t.Fatalf("expected checkpoint file to be removed after success, got %v", err)
	}
}

func TestBuiltImageRefsIncludesRegistryPrefix(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.Runtime = profile.RuntimeK8s
	p.ImageRegistry = "ghcr.io/swofty"

	images := builtImageRefs(p)
	if len(images) < 3 {
		t.Fatalf("expected multiple image refs, got %v", images)
	}
	if images[0] != "ghcr.io/swofty/hypixel-proxy:latest" {
		t.Fatalf("unexpected proxy image ref %q", images[0])
	}
	if images[1] != "ghcr.io/swofty/hypixel-game:latest" {
		t.Fatalf("unexpected game image ref %q", images[1])
	}
}
