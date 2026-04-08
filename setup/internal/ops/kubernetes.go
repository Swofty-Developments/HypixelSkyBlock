package ops

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/render"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

func BuildImages(p profile.Profile) error {
	builder, builderArgsPrefix, err := kubernetesBuilder(p)
	if err != nil {
		return err
	}
	if err := Require(builder); err != nil {
		return err
	}

	proxyImage := render.ImageRef("hypixel-proxy", p.ImageTag)
	if err := Run(p.RepoRoot, nil, builder, append(builderArgsPrefix, "build", "-f", "DockerFiles/Dockerfile.proxy", "-t", proxyImage, ".")...); err != nil {
		return err
	}

	gameImage := render.ImageRef("hypixel-game", p.ImageTag)
	if err := Run(p.RepoRoot, nil, builder, append(builderArgsPrefix, "build", "-f", "DockerFiles/Dockerfile.game_server", "-t", gameImage, ".")...); err != nil {
		return err
	}

	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		image := render.ImageRef(svc.ImageName, p.ImageTag)
		args := append(builderArgsPrefix,
			"build", "-f", "DockerFiles/Dockerfile.service",
			"--build-arg", "SERVICE_MODULE="+svc.Module,
			"--build-arg", "SERVICE_JAR="+svc.Jar,
			"-t", image, ".",
		)
		if err := Run(p.RepoRoot, nil, builder, args...); err != nil {
			return err
		}
	}
	if p.KubernetesTarget == profile.KubernetesTargetMinikube {
		if err := loadImagesIntoMinikube(p); err != nil {
			return err
		}
	}
	if p.KubernetesTarget == profile.KubernetesTargetK3d {
		if err := loadImagesIntoK3d(p); err != nil {
			return err
		}
	}
	return nil
}

func kubernetesBuilder(p profile.Profile) (string, []string, error) {
	switch p.KubernetesTarget {
	case profile.KubernetesTargetK3d:
		return "docker", nil, nil
	case profile.KubernetesTargetMinikube:
		return "docker", nil, nil
	case profile.KubernetesTargetStandard:
		return "nerdctl", []string{"--namespace", "k8s.io"}, nil
	default:
		return "", nil, fmt.Errorf("unsupported kubernetes target %q", p.KubernetesTarget)
	}
}

func loadImagesIntoMinikube(p profile.Profile) error {
	if err := Require("minikube"); err != nil {
		return err
	}
	images := []string{
		render.ImageRef("hypixel-proxy", p.ImageTag),
		render.ImageRef("hypixel-game", p.ImageTag),
	}
	for _, serviceName := range p.SelectedServices {
		images = append(images, render.ImageRef(spec.ServiceByName(serviceName).ImageName, p.ImageTag))
	}
	args := []string{"-p", strings.TrimSpace(p.MinikubeProfile), "image", "load"}
	args = append(args, images...)
	return Run("", nil, "minikube", args...)
}

func loadImagesIntoK3d(p profile.Profile) error {
	if err := Require("k3d"); err != nil {
		return err
	}
	images := []string{
		render.ImageRef("hypixel-proxy", p.ImageTag),
		render.ImageRef("hypixel-game", p.ImageTag),
	}
	for _, serviceName := range p.SelectedServices {
		images = append(images, render.ImageRef(spec.ServiceByName(serviceName).ImageName, p.ImageTag))
	}
	args := []string{"image", "import", "-c", strings.TrimSpace(p.KubernetesClusterName)}
	args = append(args, images...)
	return Run("", nil, "k3d", args...)
}

func InstallMonitoring(p profile.Profile) error {
	if err := Require("helm"); err != nil {
		return err
	}
	kubeEnv, err := KubernetesEnv(p)
	if err != nil {
		return err
	}
	if err := Run("", kubeEnv, "helm", "repo", "add", "prometheus-community", "https://prometheus-community.github.io/helm-charts"); err != nil {
		return err
	}
	if err := Run("", kubeEnv, "helm", "repo", "add", "kedacore", "https://kedacore.github.io/charts"); err != nil {
		return err
	}
	if err := Run("", kubeEnv, "helm", "repo", "update"); err != nil {
		return err
	}
	if err := Run("", kubeEnv, "helm", HelmArgs(p, "upgrade", "--install", "kube-prometheus", "prometheus-community/kube-prometheus-stack", "--namespace", "monitoring", "--create-namespace")...); err != nil {
		return err
	}
	return Run("", kubeEnv, "helm", HelmArgs(p, "upgrade", "--install", "keda", "kedacore/keda", "--namespace", "keda", "--create-namespace")...)
}

func DeployKubernetes(p profile.Profile) error {
	if err := Require("kubectl"); err != nil {
		return err
	}
	kubeEnv, err := KubernetesEnv(p)
	if err != nil {
		return err
	}
	renderDir := filepath.Join(p.InstallDir, render.K8sDirName)
	if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "apply", "-f", filepath.Join(renderDir, "namespace.yaml"))...); err != nil {
		return err
	}
	if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "apply", "--prune=true", "-l", "app.kubernetes.io/managed-by=hypixel-setup", "-f", renderDir)...); err != nil {
		return err
	}
	if p.InstallManagedDatastore {
		if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/mongodb", "--timeout=240s")...); err != nil {
			return err
		}
		if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/redis", "--timeout=240s")...); err != nil {
			return err
		}
	}
	if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/hypixel-proxy", "--timeout=300s")...); err != nil {
		return err
	}
	for _, serviceName := range p.SelectedServices {
		if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+spec.ServiceByName(serviceName).DeploymentName, "--timeout=300s")...); err != nil {
			return err
		}
	}
	for _, serverType := range p.SelectedServers {
		if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+spec.ServerByType(serverType).DeploymentName, "--timeout=300s")...); err != nil {
			return err
		}
	}
	return nil
}

func KubernetesStatus(p profile.Profile) error {
	if err := Require("kubectl"); err != nil {
		return err
	}
	kubeEnv, err := KubernetesEnv(p)
	if err != nil {
		return err
	}
	if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "pods")...); err != nil {
		return err
	}
	if err := Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "deployments")...); err != nil {
		return err
	}
	if p.EnableAutoscaling {
		_ = Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "scaledobjects")...)
		_ = Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "hpa")...)
	}
	return nil
}

func FullKubernetesSetup(p profile.Profile) error {
	if err := EnsureKubernetesCluster(p); err != nil {
		return err
	}
	if p.InstallMonitoring {
		if err := InstallMonitoring(p); err != nil {
			return err
		}
	}
	if err := BuildImages(p); err != nil {
		return err
	}
	if err := DeployKubernetes(p); err != nil {
		return err
	}
	return KubernetesStatus(p)
}

func EnsureRendered(p profile.Profile) error {
	if _, err := filepath.Abs(filepath.Join(p.InstallDir, render.K8sDirName)); err != nil {
		return fmt.Errorf("resolve render directory: %w", err)
	}
	return nil
}

func KubernetesEnv(p profile.Profile) ([]string, error) {
	resolution, err := resolveKubeconfig(p, os.Getenv("KUBECONFIG"), kubeconfigCandidates())
	if err != nil {
		return nil, err
	}
	if resolution.description == "" {
		return nil, missingKubeconfigError()
	}
	return resolution.env, nil
}

func EnsureKubernetesCluster(p profile.Profile) error {
	switch p.KubernetesTarget {
	case profile.KubernetesTargetK3d:
		return ensureK3dCluster(p)
	case profile.KubernetesTargetMinikube:
		return ensureMinikubeCluster(p)
	default:
		return nil
	}
}

func ensureK3dCluster(p profile.Profile) error {
	clusterName := strings.TrimSpace(p.KubernetesClusterName)
	if clusterName == "" {
		return fmt.Errorf("k3d cluster name is required")
	}
	if err := Require("k3d"); err != nil {
		return err
	}
	if err := Require("docker"); err != nil {
		return err
	}
	if runQuiet("", nil, "k3d", "cluster", "get", clusterName) == nil {
		if err := runQuiet("", nil, "k3d", "cluster", "start", clusterName); err != nil && !strings.Contains(err.Error(), "already running") {
			return err
		}
		return nil
	}
	return Run("", nil, "k3d", "cluster", "create", clusterName,
		"--wait",
		"--kubeconfig-switch-context",
		"-p", "25565:25565@loadbalancer",
		"--k3s-arg", "--disable=traefik@server:0",
	)
}

func ensureMinikubeCluster(p profile.Profile) error {
	minikubeProfile := strings.TrimSpace(p.MinikubeProfile)
	if minikubeProfile == "" {
		return fmt.Errorf("minikube profile is required")
	}
	if err := Require("minikube"); err != nil {
		return err
	}
	if Run("", nil, "minikube", "-p", minikubeProfile, "status") == nil {
		return nil
	}
	return Run("", nil, "minikube", "start",
		"-p", minikubeProfile,
		"--driver=docker",
		"--cpus=4",
		"--memory=8192",
		"--disk-size=50g",
	)
}
