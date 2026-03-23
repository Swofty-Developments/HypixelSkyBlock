package render

import (
	"fmt"
	"os"
	"path/filepath"
	"regexp"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/files"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

func GenerateComposeAssets(p profile.Profile) error {
	if err := p.Validate(); err != nil {
		return err
	}
	if err := files.EnsureDir(p.InstallDir); err != nil {
		return err
	}
	if err := files.CopyDir(filepath.Join(p.RepoRoot, "configuration"), filepath.Join(p.InstallDir, "configuration")); err != nil {
		return err
	}
	if err := writeComposeConfig(p); err != nil {
		return err
	}
	if err := writeEnvFile(p); err != nil {
		return err
	}
	if err := os.WriteFile(filepath.Join(p.InstallDir, ComposeFileName), []byte(composeYAML(p)), 0o644); err != nil {
		return err
	}
	return os.WriteFile(filepath.Join(p.InstallDir, "README.generated.md"), []byte(ComposeNotes(p)), 0o644)
}

func writeComposeConfig(p profile.Profile) error {
	configPath := filepath.Join(p.InstallDir, "configuration", "config.example.yml")
	configContent := fmt.Sprintf(`host-name: 0.0.0.0
transfer-timeout: 800
mongodb: mongodb://hypixel_mongo
redis-uri: redis://hypixel_redis:6379
velocity-secret: '%s'
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
management:
    enabled: true
    host-name: 0.0.0.0
    port: 9090
`, p.VelocitySecret)
	if err := os.WriteFile(configPath, []byte(configContent), 0o644); err != nil {
		return err
	}
	if err := os.WriteFile(filepath.Join(p.InstallDir, "configuration", "config.yml"), []byte(configContent), 0o644); err != nil {
		return err
	}
	velocityPath := filepath.Join(p.InstallDir, "configuration", "velocity.toml")
	velocityContent, err := os.ReadFile(velocityPath)
	if err != nil {
		return err
	}
	updated := regexp.MustCompile(`(?m)^bind = .*$`).ReplaceAllString(string(velocityContent), fmt.Sprintf(`bind = "%s:25565"`, p.BindIP))
	updated = regexp.MustCompile(`(?m)^online-mode = .*$`).ReplaceAllString(updated, fmt.Sprintf("online-mode = %t", p.OnlineMode))
	return os.WriteFile(velocityPath, []byte(updated), 0o644)
}

func writeEnvFile(p profile.Profile) error {
	content := fmt.Sprintf("FORWARDING_SECRET=%s\nHYPIXEL_PROFILE_DIR=%s\nHYPIXEL_REPO_ROOT=%s\n", p.ForwardingSecret, p.InstallDir, p.RepoRoot)
	return os.WriteFile(filepath.Join(p.InstallDir, ".env"), []byte(content), 0o600)
}

func composeYAML(p profile.Profile) string {
	var b strings.Builder
	b.WriteString("x-forwarding-env: &forwarding_env\n")
	b.WriteString("  FORWARDING_SECRET: ${FORWARDING_SECRET}\n")
	b.WriteString("services:\n")
	b.WriteString("  mongodb:\n")
	b.WriteString("    image: mongo:8.0.9\n")
	b.WriteString("    container_name: hypixel_mongo\n")
	if p.ExposeDBPorts {
		b.WriteString("    ports:\n      - \"27017:27017\"\n")
	}
	b.WriteString("    volumes:\n      - mongodb-data:/data/db\n")
	b.WriteString("    networks:\n      - hypixel_network\n")
	b.WriteString("    healthcheck:\n")
	b.WriteString("      test: [\"CMD-SHELL\", \"mongosh --eval 'db.adminCommand(\\\"ping\\\").ok' --quiet >/dev/null 2>&1\"]\n")
	b.WriteString("      interval: 10s\n      timeout: 5s\n      retries: 5\n")
	b.WriteString("  redis:\n")
	b.WriteString("    image: redis:7.4\n")
	b.WriteString("    container_name: hypixel_redis\n")
	if p.ExposeDBPorts {
		b.WriteString("    ports:\n      - \"6379:6379\"\n")
	}
	b.WriteString("    networks:\n      - hypixel_network\n")
	b.WriteString("    healthcheck:\n")
	b.WriteString("      test: [\"CMD\", \"redis-cli\", \"ping\"]\n")
	b.WriteString("      interval: 10s\n      timeout: 5s\n      retries: 5\n")
	b.WriteString("  proxy:\n")
	b.WriteString("    build:\n")
	b.WriteString(fmt.Sprintf("      context: %q\n", p.RepoRoot))
	b.WriteString("      dockerfile: DockerFiles/Dockerfile.proxy\n")
	b.WriteString("    container_name: hypixel_proxy\n")
	b.WriteString("    environment:\n      <<: *forwarding_env\n")
	b.WriteString("    ports:\n      - \"25565:25565\"\n")
	b.WriteString("    depends_on:\n      mongodb:\n        condition: service_healthy\n      redis:\n        condition: service_healthy\n")
	b.WriteString(fmt.Sprintf("    volumes:\n      - %q:/app/configuration_files\n", filepath.Join(p.InstallDir, "configuration")))
	b.WriteString("    networks:\n      - hypixel_network\n")
	b.WriteString("    healthcheck:\n")
	b.WriteString("      test: [\"CMD\", \"nc\", \"-z\", \"localhost\", \"25565\"]\n")
	b.WriteString("      interval: 10s\n      timeout: 5s\n      retries: 5\n      start_period: 10s\n")

	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		b.WriteString(fmt.Sprintf("  %s:\n", svc.DeploymentName))
		b.WriteString("    build:\n")
		b.WriteString(fmt.Sprintf("      context: %q\n", p.RepoRoot))
		b.WriteString("      dockerfile: DockerFiles/Dockerfile.service\n")
		b.WriteString("      args:\n")
		b.WriteString(fmt.Sprintf("        SERVICE_MODULE: %s\n", svc.Module))
		b.WriteString(fmt.Sprintf("        SERVICE_JAR: %s\n", svc.Jar))
		b.WriteString(fmt.Sprintf("    container_name: %s\n", svc.DeploymentName))
		b.WriteString("    environment:\n      <<: *forwarding_env\n")
		if serviceName == "ServiceAPI" {
			b.WriteString(fmt.Sprintf("      SERVICE_CMD: java $JAVA_OPTS -jar /app/service.jar --port=%d\n", p.APIPort))
			b.WriteString(fmt.Sprintf("    ports:\n      - \"%d:%d\"\n", p.APIPort, p.APIPort))
		}
		b.WriteString("    depends_on:\n      proxy:\n        condition: service_healthy\n")
		b.WriteString(fmt.Sprintf("    volumes:\n      - %q:/app/configuration_files\n", filepath.Join(p.InstallDir, "configuration")))
		b.WriteString("    networks:\n      - hypixel_network\n")
	}

	for _, serverName := range p.SelectedServers {
		server := spec.ServerByType(serverName)
		b.WriteString(fmt.Sprintf("  %s:\n", server.DeploymentName))
		b.WriteString("    build:\n")
		b.WriteString(fmt.Sprintf("      context: %q\n", p.RepoRoot))
		b.WriteString("      dockerfile: DockerFiles/Dockerfile.game_server\n")
		b.WriteString(fmt.Sprintf("    container_name: %s\n", server.DeploymentName))
		b.WriteString("    environment:\n")
		b.WriteString("      <<: *forwarding_env\n")
		b.WriteString(fmt.Sprintf("      SERVICE_CMD: java $JAVA_OPTS -jar HypixelCore.jar %s\n", serverName))
		b.WriteString("    depends_on:\n      proxy:\n        condition: service_healthy\n")
		b.WriteString(fmt.Sprintf("    volumes:\n      - %q:/app/configuration_files\n", filepath.Join(p.InstallDir, "configuration")))
		b.WriteString("    networks:\n      - hypixel_network\n")
	}

	b.WriteString("volumes:\n  mongodb-data:\n    driver: local\n")
	b.WriteString("networks:\n  hypixel_network:\n    driver: bridge\n")
	return b.String()
}
