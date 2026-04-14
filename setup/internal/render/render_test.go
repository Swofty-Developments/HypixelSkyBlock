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
	p.SelectedServices = []string{"ServiceDataMutex", "ServiceParty", "ServiceAPI"}
	p.Normalize()

	yaml := composeYAML(p)
	if strings.Count(yaml, "dockerfile: DockerFiles/Dockerfile.game_server") != 1 {
		t.Fatalf("expected one shared game server build definition, got:\n%s", yaml)
	}
	if strings.Count(yaml, "image: hypixel-game:compose") != 1 {
		t.Fatalf("expected one shared game server image definition, got:\n%s", yaml)
	}
	if strings.Count(yaml, "<<: *game_server_base") != len(p.SelectedServers)+len(p.SelectedServices) {
		t.Fatalf("expected each selected service and game server to reuse shared base, got:\n%s", yaml)
	}
	if !strings.Contains(yaml, "  picolimbo:\n") {
		t.Fatalf("expected compose output to include the PicoLimbo service, got:\n%s", yaml)
	}
	if strings.Contains(yaml, "java $JAVA_OPTS") {
		t.Fatalf("expected runtime JAVA_OPTS references to be escaped for compose, got:\n%s", yaml)
	}
}

func TestComposeYAMLQuotesBindMounts(t *testing.T) {
	p := profile.Default("/repo", "/tmp/install")
	p.SelectedServices = []string{"ServiceDataMutex", "ServiceParty"}
	p.Normalize()

	yaml := composeYAML(p)
	if !strings.Contains(yaml, `- "/tmp/install/configuration:/app/configuration_files"`) {
		t.Fatalf("expected bind mount to be fully quoted, got:\n%s", yaml)
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

func TestGenerateComposeAssetsMakesRuntimeScriptsExecutable(t *testing.T) {
	repoRoot := t.TempDir()
	installDir := t.TempDir()
	configDir := filepath.Join(repoRoot, "configuration")
	if err := os.MkdirAll(configDir, 0o755); err != nil {
		t.Fatalf("MkdirAll returned error: %v", err)
	}
	for _, name := range []string{"entrypoint.sh", "bootstrap-config.sh", "mongo-init.sh", "velocity.toml"} {
		if err := os.WriteFile(filepath.Join(configDir, name), []byte("#!/bin/sh\n"), 0o644); err != nil {
			t.Fatalf("WriteFile returned error: %v", err)
		}
	}
	p := profile.Default(repoRoot, installDir)
	p.Normalize()

	if err := GenerateComposeAssets(p); err != nil {
		t.Fatalf("GenerateComposeAssets returned error: %v", err)
	}

	for _, name := range []string{"entrypoint.sh", "bootstrap-config.sh", "mongo-init.sh"} {
		info, err := os.Stat(filepath.Join(installDir, "configuration", name))
		if err != nil {
			t.Fatalf("Stat returned error for %s: %v", name, err)
		}
		if info.Mode().Perm()&0o111 == 0 {
			t.Fatalf("expected %s to be executable, got mode %o", name, info.Mode().Perm())
		}
	}
}
