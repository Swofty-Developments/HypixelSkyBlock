package render

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func TestGenerateKubernetesAssetsUsesConfiguredImagePullPolicy(t *testing.T) {
	installDir := t.TempDir()
	repoRoot := t.TempDir()

	p := profile.Default(repoRoot, installDir)
	p.Runtime = profile.RuntimeK8s
	p.ImagePullPolicy = "IfNotPresent"

	if err := GenerateKubernetesAssets(p); err != nil {
		t.Fatalf("GenerateKubernetesAssets returned error: %v", err)
	}

	proxyYAML, err := os.ReadFile(filepath.Join(installDir, K8sDirName, "proxy.yaml"))
	if err != nil {
		t.Fatalf("ReadFile proxy.yaml returned error: %v", err)
	}
	if !strings.Contains(string(proxyYAML), "imagePullPolicy: IfNotPresent") {
		t.Fatalf("proxy.yaml did not include configured image pull policy: %s", string(proxyYAML))
	}

	servicesYAML, err := os.ReadFile(filepath.Join(installDir, K8sDirName, "services.yaml"))
	if err != nil {
		t.Fatalf("ReadFile services.yaml returned error: %v", err)
	}
	if !strings.Contains(string(servicesYAML), "imagePullPolicy: IfNotPresent") {
		t.Fatalf("services.yaml did not include configured image pull policy: %s", string(servicesYAML))
	}
}

func TestGenerateKubernetesAssetsUsesConfiguredImageRegistry(t *testing.T) {
	installDir := t.TempDir()
	repoRoot := t.TempDir()

	p := profile.Default(repoRoot, installDir)
	p.Runtime = profile.RuntimeK8s
	p.ImageRegistry = "ghcr.io/swofty"

	if err := GenerateKubernetesAssets(p); err != nil {
		t.Fatalf("GenerateKubernetesAssets returned error: %v", err)
	}

	proxyYAML, err := os.ReadFile(filepath.Join(installDir, K8sDirName, "proxy.yaml"))
	if err != nil {
		t.Fatalf("ReadFile proxy.yaml returned error: %v", err)
	}
	if !strings.Contains(string(proxyYAML), "image: ghcr.io/swofty/hypixel-proxy:latest") {
		t.Fatalf("proxy.yaml did not include registry-prefixed image reference: %s", string(proxyYAML))
	}

	servicesYAML, err := os.ReadFile(filepath.Join(installDir, K8sDirName, "services.yaml"))
	if err != nil {
		t.Fatalf("ReadFile services.yaml returned error: %v", err)
	}
	if !strings.Contains(string(servicesYAML), "image: ghcr.io/swofty/hypixel-service-datamutex:latest") {
		t.Fatalf("services.yaml did not include registry-prefixed service image reference: %s", string(servicesYAML))
	}
}
