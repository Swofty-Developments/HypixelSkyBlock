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
