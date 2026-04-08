package ops

import (
	"errors"
	"fmt"
	"path/filepath"
	"strconv"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func ApplyCompose(p profile.Profile) error {
	if err := Require("docker"); err != nil {
		return err
	}
	if err := Run(p.InstallDir, nil, "docker", "compose", "version"); err != nil {
		return errors.New("docker compose v2 is required")
	}
	return Run(p.InstallDir, composeEnv(p), "docker", "compose", "--env-file", composeEnvFile(p), "up", "-d", "--build", "--remove-orphans")
}

func ComposeStatus(p profile.Profile) error {
	if err := Require("docker"); err != nil {
		return err
	}
	return Run(p.InstallDir, nil, "docker", "compose", "--env-file", composeEnvFile(p), "ps")
}

func PromoteComposeUserToStaff(p profile.Profile, username string) error {
	if err := Require("docker"); err != nil {
		return err
	}
	if err := Run(p.InstallDir, nil, "docker", "compose", "version"); err != nil {
		return errors.New("docker compose v2 is required")
	}

	username = strings.TrimSpace(username)
	if username == "" {
		return errors.New("username is required")
	}

	return Run(
		p.InstallDir,
		composeEnv(p),
		"docker",
		"compose",
		"--env-file",
		composeEnvFile(p),
		"exec",
		"-T",
		"mongodb",
		"mongosh",
		"--quiet",
		"--eval",
		composePromoteUserScript(username),
	)
}

func composeEnv(p profile.Profile) []string {
	return []string{
		"FORWARDING_SECRET=" + p.SharedSecret,
		"HYPIXEL_PROFILE_DIR=" + p.InstallDir,
		"HYPIXEL_REPO_ROOT=" + p.RepoRoot,
	}
}

func composeEnvFile(p profile.Profile) string {
	return filepath.Join(p.InstallDir, ".env")
}

func composePromoteUserScript(username string) string {
	storedUsername := composeStoredString(strings.ToLower(username))
	storedRank := composeStoredString("STAFF")
	displayName := strconv.Quote(username)
	return fmt.Sprintf(`const result = db.getSiblingDB("Minestom").profiles.updateOne({ignLowercase: %s}, {$set: {rank: %s}});
if (result.matchedCount === 0) {
  print("User " + %s + " was not found in Minestom.profiles.");
  quit(1);
}
print("User " + %s + " now has rank STAFF.");`, storedUsername, storedRank, displayName, displayName)
}

func composeStoredString(value string) string {
	return strconv.Quote(strconv.Quote(value))
}
