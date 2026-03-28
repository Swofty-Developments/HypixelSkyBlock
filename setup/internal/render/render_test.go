package render

import (
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
