package profile

import (
	"encoding/json"
	"errors"
	"os"
	"path/filepath"
)

type storedProfile struct {
	Profile
	ForwardingSecret string `json:"forwarding_secret"`
	VelocitySecret   string `json:"velocity_secret"`
}

func Load(installDir string) (Profile, error) {
	data, err := os.ReadFile(filepath.Join(installDir, StateFileName))
	if err != nil {
		return Profile{}, err
	}
	var stored storedProfile
	var p Profile
	if err := json.Unmarshal(data, &stored); err != nil {
		return Profile{}, err
	}
	p = stored.Profile
	if p.SharedSecret == "" {
		switch {
		case stored.ForwardingSecret != "":
			p.SharedSecret = stored.ForwardingSecret
		case stored.VelocitySecret != "":
			p.SharedSecret = stored.VelocitySecret
		}
	}
	p.Normalize()
	return p, nil
}

func Save(p Profile) error {
	if err := os.MkdirAll(p.InstallDir, 0o755); err != nil {
		return err
	}
	data, err := json.MarshalIndent(p, "", "  ")
	if err != nil {
		return err
	}
	return os.WriteFile(filepath.Join(p.InstallDir, StateFileName), data, 0o600)
}

func LoadOrDefault(repoRoot, installDir string) (Profile, error) {
	p, err := Load(installDir)
	if err != nil {
		if !errors.Is(err, os.ErrNotExist) {
			return Profile{}, err
		}
		p = Default(repoRoot, installDir)
	}
	p.RepoRoot = repoRoot
	p.InstallDir = installDir
	p.Normalize()
	return p, nil
}
