package profile

import (
	"encoding/json"
	"errors"
	"os"
	"path/filepath"
)

func Load(installDir string) (Profile, error) {
	data, err := os.ReadFile(filepath.Join(installDir, StateFileName))
	if err != nil {
		return Profile{}, err
	}
	var p Profile
	if err := json.Unmarshal(data, &p); err != nil {
		return Profile{}, err
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
