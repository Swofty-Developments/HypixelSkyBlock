package ops

import (
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
