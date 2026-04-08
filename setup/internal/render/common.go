package render

import (
	"fmt"
	"path/filepath"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

const (
	ComposeFileName = "docker-compose.yml"
	K8sDirName      = "k8s-rendered"
)

func ImageRef(name, tag string) string {
	return name + ":" + tag
}

func ComposeNotes(p profile.Profile) string {
	return fmt.Sprintf("# Generated Docker Compose Profile\n\n- Install dir: %s\n- Runtime: Docker Compose\n- Servers: %s\n- Services: %s\n\nApply:\n\n```bash\ncd %s\ndocker compose --env-file .env up -d --build --remove-orphans\n```\n", p.InstallDir, strings.Join(p.SelectedServers, ", "), strings.Join(p.SelectedServices, ", "), p.InstallDir)
}

func KubernetesNotes(p profile.Profile) string {
	targetDetails := "existing cluster / nerdctl"
	if p.KubernetesTarget == profile.KubernetesTargetK3d {
		targetDetails = "docker build + k3d image import"
	}
	if p.KubernetesTarget == profile.KubernetesTargetMinikube {
		targetDetails = "minikube image load"
	}
	return fmt.Sprintf("# Generated Kubernetes Profile\n\n- Target: %s\n- Image handling: %s\n- Namespace: %s\n- Image tag: %s\n- Servers: %s\n- Services: %s\n- Prometheus: %s\n- Autoscaling: %t\n\nRendered manifests:\n\n```bash\nkubectl apply -f %s\n```\n\nFull setup:\n\n```bash\n%s/install.sh --dir %s --action k8s-full\n```\n", p.KubernetesTarget, targetDetails, p.KubernetesNamespace, p.ImageTag, strings.Join(p.SelectedServers, ", "), strings.Join(p.SelectedServices, ", "), p.PrometheusAddress, p.EnableAutoscaling, filepath.Join(p.InstallDir, K8sDirName), filepath.Join(p.RepoRoot, "setup"), p.InstallDir)
}
