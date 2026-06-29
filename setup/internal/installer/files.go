package installer

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"
)

func PrepareFiles(cfg Config) error {
	if cfg.InstallDir == "" {
		return fmt.Errorf("install directory is required")
	}
	if err := os.MkdirAll(filepath.Join(cfg.InstallDir, "configuration", "skyblock"), 0o755); err != nil {
		return err
	}
	if err := os.MkdirAll(filepath.Join(cfg.InstallDir, "DockerFiles"), 0o755); err != nil {
		return err
	}
	if err := EnsureForwardingSecret(cfg.InstallDir); err != nil {
		return err
	}
	if err := writeConfigYAML(cfg); err != nil {
		return err
	}
	if err := writeVelocityTOML(cfg); err != nil {
		return err
	}
	if err := writeDockerfiles(cfg.InstallDir); err != nil {
		return err
	}
	if err := writeCompose(cfg); err != nil {
		return err
	}
	return SaveState(cfg)
}

func EnsureForwardingSecret(dir string) error {
	envFile := filepath.Join(dir, ".env")
	secretFile := filepath.Join(dir, "configuration", "forwarding.secret")

	secret, err := readExistingSecret(envFile, secretFile)
	if err != nil {
		return err
	}
	if secret == "" {
		secret, err = randomHex(24)
		if err != nil {
			return err
		}
	}

	var preserved []string
	if data, err := os.ReadFile(envFile); err == nil {
		for _, line := range strings.Split(string(data), "\n") {
			if line != "" && !strings.HasPrefix(line, "FORWARDING_SECRET=") {
				preserved = append(preserved, line)
			}
		}
	}
	preserved = append(preserved, "FORWARDING_SECRET="+secret)
	if err := os.WriteFile(envFile, []byte(strings.Join(preserved, "\n")+"\n"), 0o600); err != nil {
		return err
	}
	return os.WriteFile(secretFile, []byte(secret), 0o600)
}

func readExistingSecret(envFile, secretFile string) (string, error) {
	if data, err := os.ReadFile(envFile); err == nil {
		for _, line := range strings.Split(string(data), "\n") {
			if strings.HasPrefix(line, "FORWARDING_SECRET=") {
				return strings.TrimPrefix(line, "FORWARDING_SECRET="), nil
			}
		}
	} else if !os.IsNotExist(err) {
		return "", err
	}

	if data, err := os.ReadFile(secretFile); err == nil {
		return strings.TrimSpace(string(data)), nil
	} else if !os.IsNotExist(err) {
		return "", err
	}
	return "", nil
}

func randomHex(bytes int) (string, error) {
	buf := make([]byte, bytes)
	if _, err := rand.Read(buf); err != nil {
		return "", err
	}
	return hex.EncodeToString(buf), nil
}

func CopyDir(src, dst string) error {
	return filepath.WalkDir(src, func(path string, entry os.DirEntry, walkErr error) error {
		if walkErr != nil {
			return walkErr
		}
		rel, err := filepath.Rel(src, path)
		if err != nil {
			return err
		}
		if rel == "." {
			return nil
		}
		target := filepath.Join(dst, rel)
		info, err := entry.Info()
		if err != nil {
			return err
		}
		if entry.IsDir() {
			return os.MkdirAll(target, info.Mode())
		}
		return copyFile(path, target, info.Mode())
	})
}

func copyFile(src, dst string, mode os.FileMode) error {
	if err := os.MkdirAll(filepath.Dir(dst), 0o755); err != nil {
		return err
	}
	in, err := os.Open(src)
	if err != nil {
		return err
	}
	defer func(in *os.File) {
		err := in.Close()
		if err != nil {
			fmt.Println(err)
		}
	}(in)

	out, err := os.OpenFile(dst, os.O_CREATE|os.O_TRUNC|os.O_WRONLY, mode)
	if err != nil {
		return err
	}
	defer func(out *os.File) {
		err := out.Close()
		if err != nil {
			fmt.Println(err)
		}
	}(out)

	_, err = io.Copy(out, in)
	return err
}

func writeConfigYAML(cfg Config) error {
	body := `host-name: 0.0.0.0
transfer-timeout: 800
mongodb: mongodb://hypixel_mongo
redis-uri: redis://hypixel_redis:6379
velocity-secret: placeholder
require-auth: false
sandbox: false
integrations:
    spark: false
    anticheat: false
    via-version: false
    sentry-dsn: ''
limbo:
    host-name: picolimbo
    port: 65535
resource-pack:
    public-url: ''
    pack-hash: ''
resource-packs: {}
`
	configPath := filepath.Join(cfg.InstallDir, "configuration", "config.yml")
	if err := os.WriteFile(configPath, []byte(body), 0o644); err != nil {
		return err
	}
	return os.WriteFile(filepath.Join(cfg.InstallDir, "configuration", "config.example.yml"), []byte(body), 0o644)
}

func writeVelocityTOML(cfg Config) error {
	path := filepath.Join(cfg.InstallDir, "configuration", "velocity.toml")
	data, err := os.ReadFile(path)
	if err != nil {
		if os.IsNotExist(err) {
			return nil
		}
		return err
	}
	lines := strings.Split(string(data), "\n")
	for i, line := range lines {
		if strings.HasPrefix(line, "bind = ") {
			lines[i] = fmt.Sprintf("bind = %q", cfg.BindIP+":25565")
		}
		if strings.HasPrefix(line, "online-mode = ") {
			lines[i] = fmt.Sprintf("online-mode = %t", cfg.OnlineMode)
		}
	}
	return os.WriteFile(path, []byte(strings.Join(lines, "\n")), 0o644)
}

func writeDockerfiles(dir string) error {
	const velocityURL = "https://fill-data.papermc.io/v1/objects/0ec616020166465dacca3b790d3db2b246f8f7c13b3aaacaae60c825744a66e0/velocity-3.5.0-SNAPSHOT-605.jar"
	releaseURL := "https://github.com/" + GitHubRepo + "/releases/download/latest"

	proxy := fmt.Sprintf(`FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y jq netcat-traditional curl && apt-get clean
RUN curl -fSL -o velocity.jar %q
RUN mkdir -p plugins && \
    curl -fSL -o plugins/SkyBlockProxy.jar %q
COPY ./configuration /app/configuration_files
RUN rm -f velocity.toml && \
    cp configuration_files/velocity.toml velocity.toml
RUN mkdir -p configuration && \
    cp configuration_files/config.example.yml ./configuration/config.yml
EXPOSE 25565
CMD ["sh", "-c", "[ -n \"$FORWARDING_SECRET\" ] || { echo 'FORWARDING_SECRET is required' >&2; exit 1; }; printf '%%s' \"$FORWARDING_SECRET\" > /app/forwarding.secret; sed -i \"s/velocity-secret: .*/velocity-secret: '$FORWARDING_SECRET'/\" /app/configuration/config.yml; java -jar velocity.jar"]
`, velocityURL, releaseURL+"/SkyBlockProxy.jar")

	jars := []string{
		"HypixelCore", "ServiceAPI", "ServiceAuctionHouse", "ServiceBazaar",
		"ServiceItemTracker", "ServiceDataMutex", "ServiceParty",
		"ServiceDarkAuction", "ServiceOrchestrator", "ServiceFriend",
	}
	var downloads strings.Builder
	for i, jar := range jars {
		if i == 0 {
			fmt.Fprintf(&downloads, "RUN curl -fSL -o %[1]s.jar \"%[2]s/%[1]s.jar\"", jar, releaseURL)
		} else {
			fmt.Fprintf(&downloads, " && \\\n    curl -fSL -o %[1]s.jar \"%[2]s/%[1]s.jar\"", jar, releaseURL)
		}
	}
	downloads.WriteByte('\n')

	game := fmt.Sprintf(`FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y jq curl && apt-get clean
%sCOPY ./configuration /app/configuration_files
RUN mkdir -p configuration && \
    cp configuration_files/config.yml ./configuration/config.yml
EXPOSE 25565 65535 8080 20000
RUN cp configuration_files/server.toml ./server.toml && \
    cp -a configuration_files/skyblock/. configuration/skyblock/ && \
    cp configuration_files/entrypoint.sh ./entrypoint.sh && \
    chmod +x entrypoint.sh
CMD ["sh", "entrypoint.sh"]
`, downloads.String())

	if err := os.WriteFile(filepath.Join(dir, "DockerFiles", "Dockerfile.proxy"), []byte(proxy), 0o644); err != nil {
		return err
	}
	return os.WriteFile(filepath.Join(dir, "DockerFiles", "Dockerfile.game_server"), []byte(game), 0o644)
}

func writeCompose(cfg Config) error {
	body := ComposeYAML(cfg)
	return os.WriteFile(filepath.Join(cfg.InstallDir, "docker-compose.yml"), []byte(body), 0o644)
}
