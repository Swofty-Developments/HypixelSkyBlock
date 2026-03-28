package ops

import (
	"errors"
	"path/filepath"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func ApplyCompose(p profile.Profile) error {
	if err := Require("docker"); err != nil {
		return err
	}
	if err := Run(p.InstallDir, nil, "docker", "compose", "version"); err != nil {
		return errors.New("docker compose v2 is required")
	}
	return Run(p.InstallDir, []string{
		"FORWARDING_SECRET=" + p.SharedSecret,
		"HYPIXEL_PROFILE_DIR=" + p.InstallDir,
		"HYPIXEL_REPO_ROOT=" + p.RepoRoot,
	}, "docker", "compose", "--env-file", filepath.Join(p.InstallDir, ".env"), "up", "-d", "--build", "--remove-orphans")
}

func ComposeStatus(p profile.Profile) error {
	if err := Require("docker"); err != nil {
		return err
	}
	return Run(p.InstallDir, nil, "docker", "compose", "--env-file", filepath.Join(p.InstallDir, ".env"), "ps")
}
