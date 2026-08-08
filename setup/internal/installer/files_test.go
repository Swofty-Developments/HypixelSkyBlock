package installer

import (
	"os"
	"path/filepath"
	"testing"
)

func TestEnsureSkyBlockCatalogsPreservesExistingFiles(t *testing.T) {
	source := t.TempDir()
	target := t.TempDir()

	sourceSkyBlock := filepath.Join(source, "skyblock")
	targetSkyBlock := filepath.Join(target, "skyblock")
	if err := os.MkdirAll(sourceSkyBlock, 0o755); err != nil {
		t.Fatal(err)
	}
	if err := os.MkdirAll(targetSkyBlock, 0o755); err != nil {
		t.Fatal(err)
	}

	for _, name := range skyBlockCatalogFiles {
		if err := os.WriteFile(filepath.Join(sourceSkyBlock, name), []byte(name), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	const existing = "custom catalog"
	if err := os.WriteFile(
		filepath.Join(targetSkyBlock, "Minestom.crystals.yml"),
		[]byte(existing),
		0o600,
	); err != nil {
		t.Fatal(err)
	}

	if err := ensureSkyBlockCatalogs(sourceSkyBlock, targetSkyBlock); err != nil {
		t.Fatal(err)
	}

	data, err := os.ReadFile(filepath.Join(targetSkyBlock, "Minestom.crystals.yml"))
	if err != nil {
		t.Fatal(err)
	}
	if string(data) != existing {
		t.Fatalf("existing catalog was overwritten: %q", data)
	}
	for _, name := range skyBlockCatalogFiles[1:] {
		data, err := os.ReadFile(filepath.Join(targetSkyBlock, name))
		if err != nil {
			t.Fatal(err)
		}
		if string(data) != name {
			t.Fatalf("catalog %s was not copied", name)
		}
	}
}
