package ops

import (
	"bytes"
	"context"
	"fmt"
	"os"
	"os/exec"
	"strings"
	"time"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

type RetryPolicy struct {
	Attempts     int
	InitialDelay time.Duration
	MaxDelay     time.Duration
	Factor       float64
}

var runCommandOutputEnabled = true

func SetRunCommandOutput(enabled bool) {
	runCommandOutputEnabled = enabled
}

func RunWithRetry(policy RetryPolicy, fn func() error) error {
	attempts := policy.Attempts
	if attempts < 1 {
		attempts = 1
	}
	delay := policy.InitialDelay
	if delay <= 0 {
		delay = 2 * time.Second
	}
	maxDelay := policy.MaxDelay
	if maxDelay <= 0 {
		maxDelay = 15 * time.Second
	}
	factor := policy.Factor
	if factor < 1 {
		factor = 2
	}

	var lastErr error
	for attempt := 1; attempt <= attempts; attempt++ {
		if err := fn(); err == nil {
			return nil
		} else {
			lastErr = err
		}
		if attempt == attempts {
			break
		}
		time.Sleep(delay)
		nextDelay := time.Duration(float64(delay) * factor)
		if nextDelay > maxDelay {
			nextDelay = maxDelay
		}
		delay = nextDelay
	}

	return lastErr
}

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
	if runCommandOutputEnabled {
		cmd.Stdin = os.Stdin
		cmd.Stdout = os.Stdout
		cmd.Stderr = os.Stderr
		return cmd.Run()
	}

	var output bytes.Buffer
	cmd.Stdout = &output
	cmd.Stderr = &output
	if err := cmd.Run(); err != nil {
		trimmed := strings.TrimSpace(output.String())
		if trimmed == "" {
			return err
		}
		return fmt.Errorf("%w: %s", err, trimmed)
	}
	return nil
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
