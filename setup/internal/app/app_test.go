package app

import (
	"os"
	"path/filepath"
	"testing"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

func TestResolveRepoRootFromPrefersCurrentWorkingTree(t *testing.T) {
	t.Parallel()

	baseDir := t.TempDir()
	repoRoot := filepath.Join(baseDir, "workspace", "HypixelSkyBlock")
	makeRepoRoot(t, repoRoot)

	installDir := filepath.Join(baseDir, "install")
	called := false
	got, err := resolveRepoRootFrom(filepath.Join(repoRoot, "setup"), installDir, func(string) error {
		called = true
		return nil
	})
	if err != nil {
		t.Fatalf("resolveRepoRootFrom returned error: %v", err)
	}
	if called {
		t.Fatalf("bootstrap should not run when current working tree is already a repo")
	}
	if got != repoRoot {
		t.Fatalf("expected repo root %q, got %q", repoRoot, got)
	}
}

func TestResolveRepoRootFromFallsBackToSavedProfileRepo(t *testing.T) {
	t.Parallel()

	baseDir := t.TempDir()
	savedRepoRoot := filepath.Join(baseDir, "existing-repo")
	makeRepoRoot(t, savedRepoRoot)

	installDir := filepath.Join(baseDir, "install")
	p := profile.Default(savedRepoRoot, installDir)
	if err := profile.Save(p); err != nil {
		t.Fatalf("profile.Save returned error: %v", err)
	}

	called := false
	got, err := resolveRepoRootFrom(baseDir, installDir, func(string) error {
		called = true
		return nil
	})
	if err != nil {
		t.Fatalf("resolveRepoRootFrom returned error: %v", err)
	}
	if called {
		t.Fatalf("bootstrap should not run when saved profile repo root is valid")
	}
	if got != savedRepoRoot {
		t.Fatalf("expected saved repo root %q, got %q", savedRepoRoot, got)
	}
}

func TestResolveRepoRootFromBootstrapsMissingCheckout(t *testing.T) {
	t.Parallel()

	baseDir := t.TempDir()
	installDir := filepath.Join(baseDir, "install")
	expectedRepoRoot := filepath.Join(installDir, bootstrapRepoDir)

	var bootstrapTarget string
	got, err := resolveRepoRootFrom(baseDir, installDir, func(target string) error {
		bootstrapTarget = target
		makeRepoRoot(t, target)
		return nil
	})
	if err != nil {
		t.Fatalf("resolveRepoRootFrom returned error: %v", err)
	}
	if bootstrapTarget != expectedRepoRoot {
		t.Fatalf("expected bootstrap target %q, got %q", expectedRepoRoot, bootstrapTarget)
	}
	if got != expectedRepoRoot {
		t.Fatalf("expected bootstrapped repo root %q, got %q", expectedRepoRoot, got)
	}
}

func TestResolveRepoRootFromRefreshesExistingBootstrappedCheckout(t *testing.T) {
	t.Parallel()

	baseDir := t.TempDir()
	installDir := filepath.Join(baseDir, "install")
	bootstrappedRepo := filepath.Join(installDir, bootstrapRepoDir)
	makeRepoRoot(t, bootstrappedRepo)

	called := false
	got, err := resolveRepoRootFrom(baseDir, installDir, func(target string) error {
		called = true
		if target != bootstrappedRepo {
			t.Fatalf("expected bootstrap target %q, got %q", bootstrappedRepo, target)
		}
		return nil
	})
	if err != nil {
		t.Fatalf("resolveRepoRootFrom returned error: %v", err)
	}
	if !called {
		t.Fatal("expected existing managed checkout to be refreshed")
	}
	if got != bootstrappedRepo {
		t.Fatalf("expected refreshed repo root %q, got %q", bootstrappedRepo, got)
	}
}

func TestResolveRepoRootFromRefreshesSavedManagedCheckout(t *testing.T) {
	t.Parallel()

	baseDir := t.TempDir()
	installDir := filepath.Join(baseDir, "install")
	bootstrappedRepo := filepath.Join(installDir, bootstrapRepoDir)
	makeRepoRoot(t, bootstrappedRepo)

	p := profile.Default(bootstrappedRepo, installDir)
	if err := profile.Save(p); err != nil {
		t.Fatalf("profile.Save returned error: %v", err)
	}

	called := false
	got, err := resolveRepoRootFrom(baseDir, installDir, func(target string) error {
		called = true
		if target != bootstrappedRepo {
			t.Fatalf("expected bootstrap target %q, got %q", bootstrappedRepo, target)
		}
		return nil
	})
	if err != nil {
		t.Fatalf("resolveRepoRootFrom returned error: %v", err)
	}
	if !called {
		t.Fatal("expected saved managed checkout to be refreshed")
	}
	if got != bootstrappedRepo {
		t.Fatalf("expected refreshed repo root %q, got %q", bootstrappedRepo, got)
	}
}

func makeRepoRoot(t *testing.T, dir string) {
	t.Helper()
	if err := os.MkdirAll(filepath.Join(dir, "configuration"), 0o755); err != nil {
		t.Fatalf("MkdirAll returned error: %v", err)
	}
	if err := os.WriteFile(filepath.Join(dir, "settings.gradle.kts"), []byte(""), 0o644); err != nil {
		t.Fatalf("WriteFile settings.gradle.kts returned error: %v", err)
	}
	if err := os.WriteFile(filepath.Join(dir, "configuration", "velocity.toml"), []byte(""), 0o644); err != nil {
		t.Fatalf("WriteFile velocity.toml returned error: %v", err)
	}
}
