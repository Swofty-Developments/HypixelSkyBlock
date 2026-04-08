package profile

import (
	"path/filepath"
	"testing"
)

func TestNormalizeAddsRequiredSelections(t *testing.T) {
	p := Profile{
		SelectedServers:  []string{"SKYBLOCK_HUB"},
		SelectedServices: []string{"ServiceAPI"},
	}
	p.Normalize()

	if p.SelectedServers[0] != "PROTOTYPE_LOBBY" {
		t.Fatalf("expected required server first, got %v", p.SelectedServers)
	}
	if p.SelectedServices[0] != "ServiceDataMutex" || p.SelectedServices[1] != "ServiceParty" {
		t.Fatalf("expected required services first, got %v", p.SelectedServices)
	}
}

func TestDefaultUsesComposeForLocalInstalls(t *testing.T) {
	p := Default("/repo", "/tmp/install")

	if p.Runtime != RuntimeCompose {
		t.Fatalf("expected default runtime %q, got %q", RuntimeCompose, p.Runtime)
	}
	if p.SharedSecret == "" {
		t.Fatal("expected default shared secret to be populated")
	}
	if p.KubernetesTarget != KubernetesTargetK3d {
		t.Fatalf("expected default kubernetes target %q, got %q", KubernetesTargetK3d, p.KubernetesTarget)
	}
	if p.KubernetesClusterName != "hypixel" {
		t.Fatalf("expected default kubernetes cluster name %q, got %q", "hypixel", p.KubernetesClusterName)
	}
	if p.MinikubeProfile != "hypixel" {
		t.Fatalf("expected default minikube profile %q, got %q", "hypixel", p.MinikubeProfile)
	}
	if len(p.SelectedServers) != 1 || p.SelectedServers[0] != "PROTOTYPE_LOBBY" {
		t.Fatalf("expected only the required default server, got %v", p.SelectedServers)
	}
	if len(p.SelectedServices) != 2 || p.SelectedServices[0] != "ServiceDataMutex" || p.SelectedServices[1] != "ServiceParty" {
		t.Fatalf("expected only the required default services, got %v", p.SelectedServices)
	}
	if p.InstallMonitoring {
		t.Fatal("expected monitoring to be disabled by default")
	}
	if p.EnableAutoscaling {
		t.Fatal("expected autoscaling to be disabled by default")
	}
}

func TestFilterSelected(t *testing.T) {
	got := FilterSelected([]string{"A", "B", "C"}, []string{"B", "C", "D"})
	if len(got) != 2 || got[0] != "B" || got[1] != "C" {
		t.Fatalf("unexpected selection filter result: %v", got)
	}
}

func TestExpandHomeExpandsTildePaths(t *testing.T) {
	homeDir := t.TempDir()
	t.Setenv("HOME", homeDir)

	got := ExpandHome("~/.kube/config")
	want := filepath.Join(homeDir, ".kube", "config")
	if got != want {
		t.Fatalf("expected %q, got %q", want, got)
	}
}
