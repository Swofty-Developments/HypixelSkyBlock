package render

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func TestAutoscalingYAMLDisabled(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.EnableAutoscaling = false
	if strings.TrimSpace(autoscalingYAML(p)) != "" {
		t.Fatal("expected empty autoscaling manifest when autoscaling is disabled")
	}
}

func TestComposeYAMLUsesSingleSharedGameServerImageBuild(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.SelectedServers = []string{"PROTOTYPE_LOBBY", "SKYBLOCK_HUB", "BEDWARS_GAME"}
	p.Normalize()

	yaml := composeYAML(p)
	if strings.Count(yaml, "dockerfile: DockerFiles/Dockerfile.game_server") != 1 {
		t.Fatalf("expected one shared game server build definition, got:\n%s", yaml)
	}
	if strings.Count(yaml, "image: hypixel-game:compose") != 1 {
		t.Fatalf("expected one shared game server image definition, got:\n%s", yaml)
	}
	if strings.Count(yaml, "<<: *game_server_base") != len(p.SelectedServers) {
		t.Fatalf("expected each game server to reuse shared base, got:\n%s", yaml)
	}
}

func TestGenerateKubernetesAssetsRemovesStaleOptionalFiles(t *testing.T) {
	repoRoot := t.TempDir()
	installDir := t.TempDir()
	p := profile.Default(repoRoot, installDir)
	p.Runtime = profile.RuntimeK8s
	p.InstallMonitoring = false
	p.EnableAutoscaling = false
	p.InstallManagedDatastore = false
	p.SelectedServices = []string{"ServiceDataMutex", "ServiceParty"}
	p.Normalize()

	renderDir := filepath.Join(installDir, K8sDirName)
	if err := os.MkdirAll(renderDir, 0o755); err != nil {
		t.Fatalf("MkdirAll returned error: %v", err)
	}
	for _, name := range []string{"autoscaling.yaml", "datastores.yaml", "legacy.yaml"} {
		if err := os.WriteFile(filepath.Join(renderDir, name), []byte("stale"), 0o644); err != nil {
			t.Fatalf("WriteFile returned error: %v", err)
		}
	}

	if err := GenerateKubernetesAssets(p); err != nil {
		t.Fatalf("GenerateKubernetesAssets returned error: %v", err)
	}

	for _, name := range []string{"autoscaling.yaml", "datastores.yaml", "legacy.yaml"} {
		if _, err := os.Stat(filepath.Join(renderDir, name)); !os.IsNotExist(err) {
			t.Fatalf("expected %s to be removed, got err=%v", name, err)
		}
	}
}
