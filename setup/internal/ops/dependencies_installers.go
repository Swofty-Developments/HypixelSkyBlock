package ops

import (
	"errors"
	"fmt"
	"os"
	"os/exec"
	"runtime"
)

type dependencyInstaller interface {
	Install(tool string) error
}

type linuxDependencyInstaller struct{}

var aptIndexUpdated bool

func newDependencyInstaller() dependencyInstaller {
	return &linuxDependencyInstaller{}
}

func (i *linuxDependencyInstaller) Install(tool string) error {
	switch tool {
	case "docker":
		return i.installDocker()
	case "kubectl":
		return i.installKubectl()
	case "helm":
		return i.installHelm()
	case "k3d":
		return i.installK3d()
	case "minikube":
		return i.installMinikube()
	case "nerdctl":
		return i.installNerdctl()
	default:
		return fmt.Errorf("automatic installation for %s is not supported", tool)
	}
}

func (i *linuxDependencyInstaller) installDocker() error {
	if hasCommand("apt-get") {
		if err := i.ensureAptUpdated(); err != nil {
			return fmt.Errorf("update apt package index: %w", err)
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "curl", "ca-certificates", "docker.io"); err != nil {
			return fmt.Errorf("install docker.io: %w", err)
		}
		_ = i.runPrivileged("apt-get", "install", "-y", "docker-compose-plugin")
		_ = i.runPrivileged("systemctl", "enable", "--now", "docker")
		return nil
	}

	if err := i.ensureCurl(); err != nil {
		return err
	}
	return i.runPrivilegedShell("curl -fsSL https://get.docker.com | sh")
}

func (i *linuxDependencyInstaller) installKubectl() error {
	if hasCommand("apt-get") {
		if err := i.ensureAptUpdated(); err != nil {
			return fmt.Errorf("update apt package index: %w", err)
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "kubectl"); err == nil {
			return nil
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "kubernetes-client"); err == nil {
			return nil
		}
	}

	if err := i.ensureCurl(); err != nil {
		return err
	}
	arch, err := archForKubernetesBinaries()
	if err != nil {
		return err
	}
	script := fmt.Sprintf(`set -e
VERSION="$(curl -fsSL https://dl.k8s.io/release/stable.txt)"
curl -fsSL "https://dl.k8s.io/release/${VERSION}/bin/linux/%s/kubectl" -o /tmp/kubectl
install -m 0755 /tmp/kubectl /usr/local/bin/kubectl
rm -f /tmp/kubectl`, arch)
	return i.runPrivilegedShell(script)
}

func (i *linuxDependencyInstaller) installHelm() error {
	if hasCommand("apt-get") {
		if err := i.ensureAptUpdated(); err != nil {
			return fmt.Errorf("update apt package index: %w", err)
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "helm"); err == nil {
			return nil
		}
	}

	if err := i.ensureCurl(); err != nil {
		return err
	}
	return i.runPrivilegedShell("curl -fsSL https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash")
}

func (i *linuxDependencyInstaller) installK3d() error {
	if err := i.ensureCurl(); err != nil {
		return err
	}
	return i.runPrivilegedShell("curl -fsSL https://raw.githubusercontent.com/k3d-io/k3d/main/install.sh | bash")
}

func (i *linuxDependencyInstaller) installMinikube() error {
	if hasCommand("apt-get") {
		if err := i.ensureAptUpdated(); err != nil {
			return fmt.Errorf("update apt package index: %w", err)
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "minikube"); err == nil {
			return nil
		}
	}

	if err := i.ensureCurl(); err != nil {
		return err
	}
	arch, err := archForKubernetesBinaries()
	if err != nil {
		return err
	}
	script := fmt.Sprintf(`set -e
curl -fsSL "https://storage.googleapis.com/minikube/releases/latest/minikube-linux-%s" -o /tmp/minikube
install -m 0755 /tmp/minikube /usr/local/bin/minikube
rm -f /tmp/minikube`, arch)
	return i.runPrivilegedShell(script)
}

func (i *linuxDependencyInstaller) installNerdctl() error {
	if hasCommand("apt-get") {
		if err := i.ensureAptUpdated(); err != nil {
			return fmt.Errorf("update apt package index: %w", err)
		}
		if err := i.runPrivileged("apt-get", "install", "-y", "nerdctl"); err == nil {
			return nil
		}
	}
	return errors.New("automatic installation for nerdctl is unavailable on this host; install nerdctl manually or use the k3d/minikube target")
}

func (i *linuxDependencyInstaller) ensureAptUpdated() error {
	if aptIndexUpdated {
		return nil
	}
	if !hasCommand("apt-get") {
		return errors.New("apt-get is required but was not found")
	}
	if err := i.runPrivileged("apt-get", "update"); err != nil {
		return err
	}
	aptIndexUpdated = true
	return nil
}

func (i *linuxDependencyInstaller) ensureCurl() error {
	if hasCommand("curl") {
		return nil
	}
	if !hasCommand("apt-get") {
		return errors.New("curl is required but no supported package manager was detected")
	}
	if err := i.ensureAptUpdated(); err != nil {
		return err
	}
	return i.runPrivileged("apt-get", "install", "-y", "curl", "ca-certificates")
}

func (i *linuxDependencyInstaller) runPrivileged(name string, args ...string) error {
	if os.Geteuid() == 0 {
		return runQuiet("", nil, name, args...)
	}
	if !hasCommand("sudo") {
		return errors.New("sudo is required to install dependencies; rerun as root or install sudo")
	}
	if err := requireNonInteractiveSudo(); err != nil {
		return err
	}
	sudoArgs := append([]string{"-n", name}, args...)
	return runQuiet("", nil, "sudo", sudoArgs...)
}

func (i *linuxDependencyInstaller) runPrivilegedShell(script string) error {
	if os.Geteuid() == 0 {
		return runQuiet("", nil, "sh", "-c", script)
	}
	if !hasCommand("sudo") {
		return errors.New("sudo is required to install dependencies; rerun as root or install sudo")
	}
	if err := requireNonInteractiveSudo(); err != nil {
		return err
	}
	return runQuiet("", nil, "sudo", "-n", "sh", "-c", script)
}

func archForKubernetesBinaries() (string, error) {
	switch runtime.GOARCH {
	case "amd64":
		return "amd64", nil
	case "arm64":
		return "arm64", nil
	default:
		return "", fmt.Errorf("unsupported architecture %q for automatic dependency installation", runtime.GOARCH)
	}
}

func hasCommand(name string) bool {
	_, err := exec.LookPath(name)
	return err == nil
}

func requireNonInteractiveSudo() error {
	if err := runQuiet("", nil, "sudo", "-n", "true"); err != nil {
		return errors.New("automatic dependency installation requires root or passwordless sudo; rerun as root or configure passwordless sudo")
	}
	return nil
}
