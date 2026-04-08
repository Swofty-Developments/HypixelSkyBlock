package ops

import (
	"context"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/tui"
)

type kubeconfigResolution struct {
	env         []string
	description string
	source      string
}

func Preflight(action string, p profile.Profile) ([]string, error) {
	warnings := kubernetesCapacityWarnings(p)

	required := requiredTools(action, p)
	missing := make([]string, 0, len(required))
	for _, tool := range required {
		if err := Require(tool); err != nil {
			missing = append(missing, tool)
		}
	}
	if len(missing) > 0 {
		return nil, fmt.Errorf("missing required dependencies for %s: %s", action, strings.Join(missing, ", "))
	}

	if requiresDockerCompose(action, p) {
		if err := runQuiet(p.InstallDir, nil, "docker", "compose", "version"); err != nil {
			return nil, errorsWithOutput("docker compose v2 is required", err)
		}
	}

	if requiresKubernetesAccess(action, p) {
		resolution, err := resolveKubeconfig(p, os.Getenv("KUBECONFIG"), kubeconfigCandidates())
		if err != nil {
			return nil, err
		}
		if resolution.description == "" {
			return nil, missingKubeconfigError()
		}
		if err := runQuiet("", resolution.env, "kubectl", KubectlArgs(p, "version", "--request-timeout=10s")...); err != nil {
			return nil, errorsWithOutput("kubectl could not reach the target cluster", err)
		}
	}

	return warnings, nil
}

func requiredTools(action string, p profile.Profile) []string {
	seen := map[string]struct{}{}
	tools := make([]string, 0, 4)
	add := func(names ...string) {
		for _, name := range names {
			if _, ok := seen[name]; ok {
				continue
			}
			seen[name] = struct{}{}
			tools = append(tools, name)
		}
	}

	switch action {
	case tui.ActionComposeApply:
		add("docker")
	case tui.ActionK8sBuild:
		builder, _, err := kubernetesBuilder(p)
		if err == nil {
			add(builder)
		}
		if p.KubernetesTarget == profile.KubernetesTargetK3d {
			add("k3d")
		}
		if p.KubernetesTarget == profile.KubernetesTargetMinikube {
			add("minikube")
		}
	case tui.ActionK8sDeploy:
		add("kubectl")
	case tui.ActionK8sFull:
		if p.InstallMonitoring {
			add("helm")
		}
		add("kubectl")
		builder, _, err := kubernetesBuilder(p)
		if err == nil {
			add(builder)
		}
		if p.KubernetesTarget == profile.KubernetesTargetK3d {
			add("k3d")
		}
		if p.KubernetesTarget == profile.KubernetesTargetMinikube {
			add("minikube")
		}
	case tui.ActionStatus, tui.ActionWatch:
		if p.Runtime == profile.RuntimeK8s {
			add("kubectl")
		} else {
			add("docker")
		}
	}

	return tools
}

func requiresDockerCompose(action string, p profile.Profile) bool {
	switch action {
	case tui.ActionComposeApply:
		return true
	case tui.ActionStatus, tui.ActionWatch:
		return p.Runtime == profile.RuntimeCompose
	default:
		return false
	}
}

func requiresKubernetesAccess(action string, p profile.Profile) bool {
	switch action {
	case tui.ActionK8sDeploy, tui.ActionK8sFull:
		return action == tui.ActionK8sDeploy || !isLocalBootstrapTarget(p)
	case tui.ActionStatus, tui.ActionWatch:
		return p.Runtime == profile.RuntimeK8s
	default:
		return false
	}
}

func isLocalBootstrapTarget(p profile.Profile) bool {
	return p.KubernetesTarget == profile.KubernetesTargetK3d || p.KubernetesTarget == profile.KubernetesTargetMinikube
}

func resolveKubeconfig(p profile.Profile, inheritedEnv string, candidates []string) (kubeconfigResolution, error) {
	if explicit := strings.TrimSpace(p.KubeconfigPath); explicit != "" {
		if !isReadableFile(explicit) {
			return kubeconfigResolution{}, fmt.Errorf("kubeconfig path %q is not readable", explicit)
		}
		return kubeconfigResolution{
			env:         []string{"KUBECONFIG=" + explicit},
			description: explicit,
			source:      "profile",
		}, nil
	}

	if inheritedEnv = strings.TrimSpace(inheritedEnv); inheritedEnv != "" {
		return kubeconfigResolution{
			description: inheritedEnv,
			source:      "environment",
		}, nil
	}

	defaultPath := defaultKubeconfigPath()
	if defaultPath != "" && isReadableFile(defaultPath) {
		return kubeconfigResolution{
			description: defaultPath,
			source:      "default-path",
		}, nil
	}

	for _, candidate := range candidates {
		if !isReadableFile(candidate) {
			continue
		}
		if defaultPath != "" {
			materializedPath, err := ensureDefaultKubeconfig(defaultPath, candidate)
			if err == nil {
				return kubeconfigResolution{
					description: materializedPath,
					source:      "materialized-default",
				}, nil
			}
		}
		return kubeconfigResolution{
			env:         []string{"KUBECONFIG=" + candidate},
			description: candidate,
			source:      "auto-discovery",
		}, nil
	}

	return kubeconfigResolution{}, nil
}

func kubeconfigCandidates() []string {
	return []string{
		"/etc/rancher/k3s/k3s.yaml",
		"/etc/rancher/rke2/rke2.yaml",
		"/etc/kubernetes/admin.conf",
		"/var/snap/microk8s/current/credentials/client.config",
	}
}

func missingKubeconfigError() error {
	candidates := kubeconfigCandidates()
	if defaultPath := defaultKubeconfigPath(); defaultPath != "" {
		candidates = append([]string{defaultPath}, candidates...)
	}
	return fmt.Errorf(
		"no kubeconfig found; export KUBECONFIG, pass --kubeconfig, or place a readable config at one of: %s",
		strings.Join(candidates, ", "),
	)
}

func defaultKubeconfigPath() string {
	home, err := os.UserHomeDir()
	if err != nil || strings.TrimSpace(home) == "" {
		return ""
	}
	return filepath.Join(home, ".kube", "config")
}

func isReadableFile(path string) bool {
	file, err := os.Open(path)
	if err != nil {
		return false
	}
	return file.Close() == nil
}

func ensureDefaultKubeconfig(defaultPath, sourcePath string) (string, error) {
	if defaultPath == "" {
		return "", fmt.Errorf("default kubeconfig path is not available")
	}
	if isReadableFile(defaultPath) {
		return defaultPath, nil
	}
	if err := os.MkdirAll(filepath.Dir(defaultPath), 0o700); err != nil {
		return "", err
	}

	source, err := os.Open(sourcePath)
	if err != nil {
		return "", err
	}
	defer source.Close()

	target, err := os.OpenFile(defaultPath, os.O_CREATE|os.O_WRONLY|os.O_TRUNC, 0o600)
	if err != nil {
		return "", err
	}
	if _, err := io.Copy(target, source); err != nil {
		_ = target.Close()
		return "", err
	}
	if err := target.Close(); err != nil {
		return "", err
	}
	return defaultPath, nil
}

func runQuiet(dir string, env []string, name string, args ...string) error {
	cmd := exec.CommandContext(context.Background(), name, args...)
	if dir != "" {
		cmd.Dir = dir
	}
	cmd.Env = append(os.Environ(), env...)
	if output, err := cmd.CombinedOutput(); err != nil {
		return fmt.Errorf("%w: %s", err, strings.TrimSpace(string(output)))
	}
	return nil
}

func errorsWithOutput(message string, err error) error {
	return fmt.Errorf("%s: %w", message, err)
}
