package ops

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"time"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/render"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

type kubernetesSetupStage struct {
	name    string
	enabled func(profile.Profile) bool
	run     func(profile.Profile) error
	cleanup func(profile.Profile) error
}

type kubernetesSetupState struct {
	Key       string   `json:"key"`
	Completed []string `json:"completed"`
	UpdatedAt string   `json:"updated_at"`
}

var (
	monitoringRetryPolicy = RetryPolicy{Attempts: 3, InitialDelay: 2 * time.Second, MaxDelay: 10 * time.Second, Factor: 2}
	applyRetryPolicy      = RetryPolicy{Attempts: 3, InitialDelay: 2 * time.Second, MaxDelay: 8 * time.Second, Factor: 2}
	rolloutRetryPolicy    = RetryPolicy{Attempts: 2, InitialDelay: 5 * time.Second, MaxDelay: 10 * time.Second, Factor: 2}
	kubernetesStageUI     = true
)

func SetKubernetesStageProgressUI(enabled bool) {
	kubernetesStageUI = enabled
}

func BuildImages(p profile.Profile) error {
	builder, builderArgsPrefix, err := kubernetesBuilder(p)
	if err != nil {
		return err
	}
	if err := Require(builder); err != nil {
		return err
	}

	proxyImage := render.ImageRefForProfile(p, "hypixel-proxy")
	if err := Run(p.RepoRoot, nil, builder, append(builderArgsPrefix, "build", "-f", "DockerFiles/Dockerfile.proxy", "-t", proxyImage, ".")...); err != nil {
		return err
	}

	gameImage := render.ImageRefForProfile(p, "hypixel-game")
	if err := Run(p.RepoRoot, nil, builder, append(builderArgsPrefix, "build", "-f", "DockerFiles/Dockerfile.game_server", "-t", gameImage, ".")...); err != nil {
		return err
	}

	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		image := render.ImageRefForProfile(p, svc.ImageName)
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
	if p.KubernetesTarget == profile.KubernetesTargetStandard && strings.TrimSpace(p.ImageRegistry) != "" {
		if err := pushBuiltImages(p, builder, builderArgsPrefix); err != nil {
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
	images := builtImageRefs(p)
	args := []string{"-p", strings.TrimSpace(p.MinikubeProfile), "image", "load"}
	args = append(args, images...)
	return Run("", nil, "minikube", args...)
}

func loadImagesIntoK3d(p profile.Profile) error {
	if err := Require("k3d"); err != nil {
		return err
	}
	images := builtImageRefs(p)
	args := []string{"image", "import", "-c", strings.TrimSpace(p.KubernetesClusterName)}
	args = append(args, images...)
	return Run("", nil, "k3d", args...)
}

func pushBuiltImages(p profile.Profile, builder string, builderArgsPrefix []string) error {
	for _, image := range builtImageRefs(p) {
		if err := Run(p.RepoRoot, nil, builder, append(builderArgsPrefix, "push", image)...); err != nil {
			return err
		}
	}
	return nil
}

func builtImageRefs(p profile.Profile) []string {
	images := []string{
		render.ImageRefForProfile(p, "hypixel-proxy"),
		render.ImageRefForProfile(p, "hypixel-game"),
	}
	for _, serviceName := range p.SelectedServices {
		images = append(images, render.ImageRefForProfile(p, spec.ServiceByName(serviceName).ImageName))
	}
	return images
}

func InstallMonitoring(p profile.Profile) error {
	if err := Require("helm"); err != nil {
		return err
	}
	kubeEnv, err := KubernetesEnv(p)
	if err != nil {
		return err
	}
	if err := RunWithRetry(monitoringRetryPolicy, func() error {
		return Run("", kubeEnv, "helm", "repo", "add", "prometheus-community", "https://prometheus-community.github.io/helm-charts")
	}); err != nil {
		return err
	}
	if err := RunWithRetry(monitoringRetryPolicy, func() error {
		return Run("", kubeEnv, "helm", "repo", "add", "kedacore", "https://kedacore.github.io/charts")
	}); err != nil {
		return err
	}
	if err := RunWithRetry(monitoringRetryPolicy, func() error {
		return Run("", kubeEnv, "helm", "repo", "update")
	}); err != nil {
		return err
	}
	if err := RunWithRetry(monitoringRetryPolicy, func() error {
		return Run("", kubeEnv, "helm", HelmArgs(p, "upgrade", "--install", "kube-prometheus", "prometheus-community/kube-prometheus-stack", "--namespace", "monitoring", "--create-namespace")...)
	}); err != nil {
		return err
	}
	return RunWithRetry(monitoringRetryPolicy, func() error {
		return Run("", kubeEnv, "helm", HelmArgs(p, "upgrade", "--install", "keda", "kedacore/keda", "--namespace", "keda", "--create-namespace")...)
	})
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
	if err := RunWithRetry(applyRetryPolicy, func() error {
		return Run("", kubeEnv, "kubectl", KubectlArgs(p, "apply", "-f", filepath.Join(renderDir, "namespace.yaml"))...)
	}); err != nil {
		return err
	}
	if err := RunWithRetry(applyRetryPolicy, func() error {
		return Run("", kubeEnv, "kubectl", KubectlArgs(p, "apply", "--prune=true", "-l", "app.kubernetes.io/managed-by=hypixel-setup", "-f", renderDir)...)
	}); err != nil {
		return err
	}
	if p.InstallManagedDatastore {
		if err := RunWithRetry(rolloutRetryPolicy, func() error {
			return Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/mongodb", "--timeout=240s")...)
		}); err != nil {
			return err
		}
		if err := RunWithRetry(rolloutRetryPolicy, func() error {
			return Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "statefulset/redis", "--timeout=240s")...)
		}); err != nil {
			return err
		}
	}
	if err := RunWithRetry(rolloutRetryPolicy, func() error {
		return Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/hypixel-proxy", "--timeout=300s")...)
	}); err != nil {
		return err
	}
	for _, serviceName := range p.SelectedServices {
		deploymentName := spec.ServiceByName(serviceName).DeploymentName
		if err := RunWithRetry(rolloutRetryPolicy, func() error {
			return Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+deploymentName, "--timeout=300s")...)
		}); err != nil {
			return err
		}
	}
	for _, serverType := range p.SelectedServers {
		deploymentName := spec.ServerByType(serverType).DeploymentName
		if err := RunWithRetry(rolloutRetryPolicy, func() error {
			return Run("", kubeEnv, "kubectl", KubectlArgs(p, "-n", p.KubernetesNamespace, "rollout", "status", "deployment/"+deploymentName, "--timeout=300s")...)
		}); err != nil {
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
	stages := []kubernetesSetupStage{
		{
			name: "Ensure Kubernetes cluster",
			run:  EnsureKubernetesCluster,
		},
		{
			name: "Install monitoring stack",
			enabled: func(profile.Profile) bool {
				return p.InstallMonitoring
			},
			run:     InstallMonitoring,
			cleanup: cleanupMonitoringInstall,
		},
		{
			name: "Build images",
			run:  BuildImages,
		},
		{
			name: "Deploy rendered manifests",
			run:  DeployKubernetes,
		},
		{
			name: "Report Kubernetes status",
			run:  KubernetesStatus,
		},
	}
	return runKubernetesStages(p, stages)
}

func runKubernetesStages(p profile.Profile, stages []kubernetesSetupStage) error {
	checkpointPath := kubernetesSetupStatePath(p)
	checkpointKey := kubernetesSetupStateKey(p)
	completed, err := loadKubernetesSetupState(checkpointPath, checkpointKey)
	if err != nil {
		return err
	}

	stageNames := make([]string, 0, len(stages))
	total := 0
	for _, stage := range stages {
		if stage.enabled != nil && !stage.enabled(p) {
			continue
		}
		total++
		stageNames = append(stageNames, stage.name)
	}

	stageProgress := newKubernetesStageProgress(stageNames, kubernetesStageUI && stdoutIsTTY())
	if stageProgress.Enabled() {
		SetRunCommandOutput(false)
		defer SetRunCommandOutput(true)
		stageProgress.Render()
	}

	current := 0
	for _, stage := range stages {
		if stage.enabled != nil && !stage.enabled(p) {
			continue
		}
		current++
		if _, ok := completed[stage.name]; ok {
			stageProgress.MarkSkipped(stage.name)
			if !stageProgress.Enabled() {
				fmt.Printf("[%d/%d] %s... skipped (already completed)\n", current, total, stage.name)
			}
			continue
		}

		stageProgress.MarkRunning(stage.name)
		if !stageProgress.Enabled() {
			fmt.Printf("[%d/%d] %s...\n", current, total, stage.name)
		}

		started := time.Now()
		if err := stage.run(p); err != nil {
			stageProgress.MarkFailed(stage.name, err)
			if cleanupErr := runKubernetesStageCleanup(p, stage); cleanupErr != nil {
				return fmt.Errorf("%s failed: %w (cleanup failed: %v)", stage.name, err, cleanupErr)
			}
			return fmt.Errorf("%s failed: %w", stage.name, err)
		}
		completed[stage.name] = struct{}{}
		if err := saveKubernetesSetupState(checkpointPath, checkpointKey, completed); err != nil {
			return fmt.Errorf("persist setup checkpoint: %w", err)
		}
		stageProgress.MarkDone(stage.name, time.Since(started))
		if !stageProgress.Enabled() {
			fmt.Printf("[ok] %s (%s)\n", stage.name, time.Since(started).Round(time.Second))
		}
	}

	if err := clearKubernetesSetupState(checkpointPath); err != nil {
		return fmt.Errorf("clear setup checkpoint: %w", err)
	}

	return nil
}

func runKubernetesStageCleanup(p profile.Profile, stage kubernetesSetupStage) error {
	if stage.cleanup == nil {
		return nil
	}
	return stage.cleanup(p)
}

func cleanupMonitoringInstall(p profile.Profile) error {
	kubeEnv, err := KubernetesEnv(p)
	if err != nil {
		return err
	}

	errMessages := make([]string, 0, 2)
	if err := Run("", kubeEnv, "helm", HelmArgs(p, "uninstall", "keda", "--namespace", "keda")...); err != nil && !isHelmReleaseMissing(err) {
		errMessages = append(errMessages, err.Error())
	}
	if err := Run("", kubeEnv, "helm", HelmArgs(p, "uninstall", "kube-prometheus", "--namespace", "monitoring")...); err != nil && !isHelmReleaseMissing(err) {
		errMessages = append(errMessages, err.Error())
	}

	if len(errMessages) > 0 {
		return fmt.Errorf("%s", strings.Join(errMessages, "; "))
	}
	return nil
}

func isHelmReleaseMissing(err error) bool {
	if err == nil {
		return false
	}
	errText := strings.ToLower(err.Error())
	return strings.Contains(errText, "release: not found") || strings.Contains(errText, "not found")
}

func kubernetesSetupStatePath(p profile.Profile) string {
	return filepath.Join(p.InstallDir, ".k8s-full-state.json")
}

func kubernetesSetupStateKey(p profile.Profile) string {
	return strings.Join([]string{
		p.KubernetesTarget,
		p.KubernetesClusterName,
		p.MinikubeProfile,
		p.KubernetesNamespace,
		p.ImageTag,
		p.ImageRegistry,
		p.ImagePullPolicy,
		p.ProxyServiceType,
		fmt.Sprintf("monitoring=%t", p.InstallMonitoring),
		fmt.Sprintf("autoscaling=%t", p.EnableAutoscaling),
		fmt.Sprintf("managed_datastore=%t", p.InstallManagedDatastore),
		"services=" + strings.Join(p.SelectedServices, ","),
		"servers=" + strings.Join(p.SelectedServers, ","),
	}, "|")
}

func loadKubernetesSetupState(path, key string) (map[string]struct{}, error) {
	completed := map[string]struct{}{}

	data, err := os.ReadFile(path)
	if err != nil {
		if os.IsNotExist(err) {
			return completed, nil
		}
		return nil, err
	}

	var state kubernetesSetupState
	if err := json.Unmarshal(data, &state); err != nil {
		_ = os.Remove(path)
		return completed, nil
	}
	if state.Key != key {
		_ = os.Remove(path)
		return completed, nil
	}

	for _, stage := range state.Completed {
		completed[stage] = struct{}{}
	}
	return completed, nil
}

func saveKubernetesSetupState(path, key string, completed map[string]struct{}) error {
	stages := make([]string, 0, len(completed))
	for stage := range completed {
		stages = append(stages, stage)
	}
	sort.Strings(stages)

	state := kubernetesSetupState{
		Key:       key,
		Completed: stages,
		UpdatedAt: time.Now().UTC().Format(time.RFC3339),
	}
	data, err := json.MarshalIndent(state, "", "  ")
	if err != nil {
		return err
	}
	if err := os.MkdirAll(filepath.Dir(path), 0o755); err != nil {
		return err
	}
	return os.WriteFile(path, data, 0o600)
}

func clearKubernetesSetupState(path string) error {
	if err := os.Remove(path); err != nil && !os.IsNotExist(err) {
		return err
	}
	return nil
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
