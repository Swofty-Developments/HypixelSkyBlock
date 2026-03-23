package ops

import (
	"context"
	"fmt"
	"os"
	"os/exec"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func Require(name string) error {
	if _, err := exec.LookPath(name); err != nil {
		return fmt.Errorf("%s is required in PATH", name)
	}
	return nil
}

func Run(dir string, env []string, name string, args ...string) error {
	cmd := exec.CommandContext(context.Background(), name, args...)
	if dir != "" {
		cmd.Dir = dir
	}
	cmd.Env = append(os.Environ(), env...)
	cmd.Stdin = os.Stdin
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	return cmd.Run()
}

func KubectlArgs(p profile.Profile, args ...string) []string {
	if strings.TrimSpace(p.KubeContext) == "" {
		return args
	}
	return append([]string{"--context", p.KubeContext}, args...)
}

func HelmArgs(p profile.Profile, args ...string) []string {
	if strings.TrimSpace(p.KubeContext) == "" {
		return args
	}
	return append([]string{"--kube-context", p.KubeContext}, args...)
}
