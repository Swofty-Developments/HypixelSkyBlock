package ops

import (
	"fmt"
	"path/filepath"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/render"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

func BuildAndPushImages(p profile.Profile) error {
	if err := Require("docker"); err != nil {
		return err
	}

	proxyImage := render.ImageRef(p.Registry, "hypixel-proxy", p.ImageTag)
	if err := Run(p.RepoRoot, nil, "docker", "build", "-f", "DockerFiles/Dockerfile.proxy", "-t", proxyImage, "."); err != nil {
		return err
	}
	if err := Run(p.RepoRoot, nil, "docker", "push", proxyImage); err != nil {
		return err
	}

	gameImage := render.ImageRef(p.Registry, "hypixel-game", p.ImageTag)
	if err := Run(p.RepoRoot, nil, "docker", "build", "-f", "DockerFiles/Dockerfile.game_server", "-t", gameImage, "."); err != nil {
		return err
	}
	if err := Run(p.RepoRoot, nil, "docker", "push", gameImage); err != nil {
		return err
	}

	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		image := render.ImageRef(p.Registry, svc.ImageName, p.ImageTag)
		if err := Run(p.RepoRoot, nil, "docker", "build", "-f", "DockerFiles/Dockerfile.service",
			"--build-arg", "SERVICE_MODULE="+svc.Module,
			"--build-arg", "SERVICE_JAR="+svc.Jar,
			"-t", image, ".",
		); err != nil {
			return err
		}
		if err := Run(p.RepoRoot, nil, "docker", "push", image); err != nil {
			return err
		}
	}
	return nil
}

func InstallMonitoring(p profile.Profile) error {
	if err := Require("helm"); err != nil {
		return err
	}
	if err := Run("", nil, "helm", "repo", "add", "prometheus-community", "https://prometheus-community.github.io/helm-charts"); err != nil {
		return err
	}
	if err := Run("", nil, "helm", "repo", "add", "kedacore", "https://kedacore.github.io/charts"); err != nil {
		return err
	}
	if err := Run("", nil, "helm", "repo", "update"); err != nil {
		return err
	}
	if err := Run("", nil, "helm", HelmArgs(p, "upgrade", "--install", "kube-prometheus", "prometheus-community/kube-prometheus-stack", "--namespace", "monitoring", "--create-namespace")...); err != nil {
		return err
	}
	return Run("", nil, "helm", HelmArgs(p, "upgrade", "--install", "keda", "kedacore/keda", "--namespace", "keda", "--create-namespace")...)
}

func DeployKubernetes(p profile.Profile) error {
	if err := Require("kubectl"); err != nil {
		return err
	}
	renderDir := filepath.Join(p.InstallDir, render.K8sDirName)
	if err := Run("", nil, "kubectl", KubectlArgs(p, "apply", "-f", filepath.Join(renderDir, "namespace.yaml"))...); err != nil {
		return err
	}
	if err := Run("", nil, "kubectl", KubectlArgs(p, "apply", "--prune=true", "-l", "app.kubernetes.io/managed-by=hypixel-setup", "-f", renderDir)...); err != nil {
		return err
	}
	if p.InstallManagedDatastore {
		if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/mongodb", "--timeout=240s")...); err != nil {
			return err
		}
		if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/redis", "--timeout=240s")...); err != nil {
			return err
		}
	}
	if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/hypixel-proxy", "--timeout=300s")...); err != nil {
		return err
	}
	for _, serviceName := range p.SelectedServices {
		if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+spec.ServiceByName(serviceName).DeploymentName, "--timeout=300s")...); err != nil {
			return err
		}
	}
	for _, serverType := range p.SelectedServers {
		if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+spec.ServerByType(serverType).DeploymentName, "--timeout=300s")...); err != nil {
			return err
		}
	}
	return nil
}

func KubernetesStatus(p profile.Profile) error {
	if err := Require("kubectl"); err != nil {
		return err
	}
	if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "pods")...); err != nil {
		return err
	}
	if err := Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "deployments")...); err != nil {
		return err
	}
	if p.EnableAutoscaling {
		_ = Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "scaledobjects")...)
		_ = Run("", nil, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "get", "hpa")...)
	}
	return nil
}

func FullKubernetesSetup(p profile.Profile) error {
	if p.InstallMonitoring {
		if err := InstallMonitoring(p); err != nil {
			return err
		}
	}
	if err := BuildAndPushImages(p); err != nil {
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
