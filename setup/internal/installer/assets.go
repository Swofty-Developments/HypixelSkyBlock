package installer

import (
	"archive/zip"
	"context"
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"
	"strings"
	"time"
)

// use this so you can replace the assets
const limboAssetsEnv = "SKYBLOCK_WORLD_ASSETS"

func DownloadLimboAssets(ctx context.Context, installDir string) error {
	configDir := filepath.Join(installDir, "configuration")
	if LimboAssetsPresent(configDir) {
		return nil
	}

	if localPath := strings.TrimSpace(os.Getenv(limboAssetsEnv)); localPath != "" {
		return importLocalLimboAssets(localPath, configDir)
	}

	reqCtx, cancel := context.WithTimeout(ctx, 5*time.Minute)
	defer cancel()

	req, err := http.NewRequestWithContext(reqCtx, http.MethodGet, LimboAssetsURL, nil)
	if err != nil {
		return err
	}
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode < 200 || resp.StatusCode > 299 {
		return fmt.Errorf("download returned %s", resp.Status)
	}

	tmp, err := os.CreateTemp("", "skyblock-limbo-assets-*.zip")
	if err != nil {
		return err
	}
	tmpPath := tmp.Name()
	defer os.Remove(tmpPath)

	if _, err := io.Copy(tmp, resp.Body); err != nil {
		_ = tmp.Close()
		return err
	}
	if err := tmp.Close(); err != nil {
		return err
	}

	return extractZip(tmpPath, configDir)
}

func LimboAssetsPresent(configDir string) bool {
	if _, err := os.Stat(filepath.Join(configDir, "limbo.schem")); err != nil {
		return false
	}
	if info, err := os.Stat(filepath.Join(configDir, "world")); err != nil || !info.IsDir() {
		return false
	}
	return true
}

func LimboAssetsManualHint() string {
	return fmt.Sprintf("set %s to a local oybade.zip or an extracted folder containing limbo.schem and world/", limboAssetsEnv)
}

func importLocalLimboAssets(path, configDir string) error {
	info, err := os.Stat(path)
	if err != nil {
		return err
	}
	if info.IsDir() {
		return CopyDir(path, configDir)
	}
	return extractZip(path, configDir)
}

func extractZip(zipPath, dst string) error {
	reader, err := zip.OpenReader(zipPath)
	if err != nil {
		return err
	}
	defer reader.Close()

	cleanDst, err := filepath.Abs(dst)
	if err != nil {
		return err
	}
	if err := os.MkdirAll(cleanDst, 0o755); err != nil {
		return err
	}

	for _, file := range reader.File {
		target, err := safeZipTarget(cleanDst, file.Name)
		if err != nil {
			return err
		}
		if file.FileInfo().IsDir() {
			if err := os.MkdirAll(target, file.Mode()); err != nil {
				return err
			}
			continue
		}
		if err := extractZipFile(file, target); err != nil {
			return err
		}
	}
	return nil
}

func safeZipTarget(dst, name string) (string, error) {
	cleanName := filepath.Clean(strings.ReplaceAll(name, "\\", "/"))
	if cleanName == "." || strings.HasPrefix(cleanName, "../") || filepath.IsAbs(cleanName) {
		return "", fmt.Errorf("unsafe zip entry path: %s", name)
	}

	target := filepath.Join(dst, cleanName)
	cleanTarget, err := filepath.Abs(target)
	if err != nil {
		return "", err
	}
	if cleanTarget != dst && !strings.HasPrefix(cleanTarget, dst+string(os.PathSeparator)) {
		return "", fmt.Errorf("unsafe zip entry path: %s", name)
	}
	return cleanTarget, nil
}

func extractZipFile(file *zip.File, target string) error {
	if err := os.MkdirAll(filepath.Dir(target), 0o755); err != nil {
		return err
	}

	in, err := file.Open()
	if err != nil {
		return err
	}
	defer in.Close()

	out, err := os.OpenFile(target, os.O_CREATE|os.O_TRUNC|os.O_WRONLY, file.Mode())
	if err != nil {
		return err
	}
	defer out.Close()

	_, err = io.Copy(out, in)
	return err
}
