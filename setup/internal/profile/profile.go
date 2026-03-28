package profile

import (
	"crypto/rand"
	"encoding/hex"
	"errors"
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"time"
)

const (
	StateFileName   = ".state.json"
	RuntimeCompose  = "compose"
	RuntimeK8s      = "kubernetes"
	DefaultVersion  = "2.0.0"
	DefaultRegistry = "ghcr.io/swofty-developments"
)

var (
	RequiredServices = []string{"ServiceDataMutex", "ServiceParty"}
	RequiredServers  = []string{"PROTOTYPE_LOBBY"}

	SkyBlockServers = []string{
		"SKYBLOCK_HUB",
		"SKYBLOCK_ISLAND",
		"SKYBLOCK_SPIDERS_DEN",
		"SKYBLOCK_THE_END",
		"SKYBLOCK_CRIMSON_ISLE",
		"SKYBLOCK_DUNGEON_HUB",
		"SKYBLOCK_THE_FARMING_ISLANDS",
		"SKYBLOCK_GOLD_MINE",
		"SKYBLOCK_DEEP_CAVERNS",
		"SKYBLOCK_DWARVEN_MINES",
		"SKYBLOCK_THE_PARK",
		"SKYBLOCK_GALATEA",
		"SKYBLOCK_BACKWATER_BAYOU",
		"SKYBLOCK_JERRYS_WORKSHOP",
	}

	MinigameServers = []string{
		"BEDWARS_LOBBY",
		"BEDWARS_GAME",
		"BEDWARS_CONFIGURATOR",
		"MURDER_MYSTERY_LOBBY",
		"MURDER_MYSTERY_GAME",
		"MURDER_MYSTERY_CONFIGURATOR",
		"SKYWARS_LOBBY",
		"SKYWARS_GAME",
		"SKYWARS_CONFIGURATOR",
		"RAVENGARD_LOBBY",
	}

	AllServices = []string{
		"ServiceDataMutex",
		"ServiceParty",
		"ServiceAPI",
		"ServiceAuctionHouse",
		"ServiceBazaar",
		"ServiceItemTracker",
		"ServiceDarkAuction",
		"ServiceOrchestrator",
		"ServiceFriend",
		"ServicePunishment",
	}
)

type Profile struct {
	Version                 string   `json:"version"`
	RepoRoot                string   `json:"repo_root"`
	InstallDir              string   `json:"install_dir"`
	Runtime                 string   `json:"runtime"`
	BindIP                  string   `json:"bind_ip"`
	OnlineMode              bool     `json:"online_mode"`
	SelectedServers         []string `json:"selected_servers"`
	SelectedServices        []string `json:"selected_services"`
	ExposeDBPorts           bool     `json:"expose_db_ports"`
	APIPort                 int      `json:"api_port"`
	ForwardingSecret        string   `json:"forwarding_secret"`
	VelocitySecret          string   `json:"velocity_secret"`
	ManagedByInstaller      bool     `json:"managed_by_installer"`
	Registry                string   `json:"registry"`
	ImageTag                string   `json:"image_tag"`
	KubernetesNamespace     string   `json:"kubernetes_namespace"`
	KubeContext             string   `json:"kube_context"`
	ProxyServiceType        string   `json:"proxy_service_type"`
	InstallMonitoring       bool     `json:"install_monitoring"`
	EnableAutoscaling       bool     `json:"enable_autoscaling"`
	InstallManagedDatastore bool     `json:"install_managed_datastore"`
	MongoURI                string   `json:"mongo_uri"`
	RedisURI                string   `json:"redis_uri"`
	PrometheusAddress       string   `json:"prometheus_address"`
	MongoStorageSize        string   `json:"mongo_storage_size"`
	RedisStorageSize        string   `json:"redis_storage_size"`
	StorageClassName        string   `json:"storage_class_name"`
	LastAction              string   `json:"last_action"`
	UpdatedAt               string   `json:"updated_at"`
}

func Default(repoRoot, installDir string) Profile {
	return Profile{
		Version:                 DefaultVersion,
		RepoRoot:                repoRoot,
		InstallDir:              installDir,
		Runtime:                 RuntimeK8s,
		BindIP:                  "0.0.0.0",
		OnlineMode:              true,
		SelectedServers:         []string{"PROTOTYPE_LOBBY", "SKYBLOCK_HUB", "SKYBLOCK_ISLAND"},
		SelectedServices:        []string{"ServiceDataMutex", "ServiceParty", "ServiceAPI", "ServiceAuctionHouse", "ServiceBazaar", "ServiceItemTracker", "ServiceOrchestrator"},
		ExposeDBPorts:           false,
		APIPort:                 8080,
		ForwardingSecret:        RandomSecret(),
		VelocitySecret:          RandomSecret(),
		ManagedByInstaller:      true,
		Registry:                DefaultRegistry,
		ImageTag:                "latest",
		KubernetesNamespace:     "hypixel",
		ProxyServiceType:        "LoadBalancer",
		InstallMonitoring:       true,
		EnableAutoscaling:       true,
		InstallManagedDatastore: true,
		MongoURI:                "mongodb://mongodb.hypixel.svc.cluster.local:27017",
		RedisURI:                "redis://redis.hypixel.svc.cluster.local:6379",
		PrometheusAddress:       "http://prometheus-operated.monitoring.svc.cluster.local:9090",
		MongoStorageSize:        "50Gi",
		RedisStorageSize:        "20Gi",
	}
}

func DefaultInstallDir() (string, error) {
	home, err := os.UserHomeDir()
	if err != nil {
		return "", err
	}
	return filepath.Join(home, ".hypixel-skyblock"), nil
}

func (p *Profile) Normalize() {
	p.InstallDir = ExpandHome(strings.TrimSpace(p.InstallDir))
	p.SelectedServers = normalizeWithRequired(RequiredServers, p.SelectedServers)
	p.SelectedServices = normalizeWithRequired(RequiredServices, p.SelectedServices)
	if strings.TrimSpace(p.ForwardingSecret) == "" {
		p.ForwardingSecret = RandomSecret()
	}
	if strings.TrimSpace(p.VelocitySecret) == "" {
		p.VelocitySecret = RandomSecret()
	}
	if p.APIPort == 0 {
		p.APIPort = 8080
	}
}

func (p Profile) Validate() error {
	if p.InstallDir == "" {
		return errors.New("install directory is required")
	}
	if p.BindIP == "" {
		return errors.New("bind IP is required")
	}
	if p.Runtime != RuntimeCompose && p.Runtime != RuntimeK8s {
		return fmt.Errorf("unsupported runtime %q", p.Runtime)
	}
	if p.ForwardingSecret == "" {
		return errors.New("forwarding secret is required")
	}
	if p.Runtime != RuntimeK8s {
		return nil
	}
	if p.Registry == "" {
		return errors.New("registry is required for Kubernetes")
	}
	if p.ImageTag == "" {
		return errors.New("image tag is required for Kubernetes")
	}
	if p.KubernetesNamespace == "" {
		return errors.New("Kubernetes namespace is required")
	}
	if p.ProxyServiceType == "" {
		return errors.New("proxy service type is required")
	}
	if !p.InstallManagedDatastore && (p.MongoURI == "" || p.RedisURI == "") {
		return errors.New("mongo and redis URIs are required when managed datastores are disabled")
	}
	return nil
}

func (p *Profile) Touch(action string) {
	p.LastAction = action
	p.UpdatedAt = time.Now().UTC().Format(time.RFC3339)
}

func RandomSecret() string {
	var buf [24]byte
	if _, err := rand.Read(buf[:]); err != nil {
		return fmt.Sprintf("%d", time.Now().UnixNano())
	}
	return hex.EncodeToString(buf[:])
}

func ExpandHome(path string) string {
	if !strings.HasPrefix(path, "~") {
		return path
	}
	home, err := os.UserHomeDir()
	if err != nil {
		return path
	}
	return filepath.Join(home, strings.TrimPrefix(path, "~"))
}

func FilterSelected(selected, allowed []string) []string {
	allowedSet := make(map[string]struct{}, len(allowed))
	for _, item := range allowed {
		allowedSet[item] = struct{}{}
	}
	result := make([]string, 0, len(selected))
	for _, item := range selected {
		if _, ok := allowedSet[item]; ok {
			result = append(result, item)
		}
	}
	return result
}

func normalizeWithRequired(required, selected []string) []string {
	seen := make(map[string]struct{}, len(required)+len(selected))
	result := make([]string, 0, len(required)+len(selected))
	for _, item := range append(append([]string{}, required...), selected...) {
		item = strings.TrimSpace(item)
		if item == "" {
			continue
		}
		if _, ok := seen[item]; ok {
			continue
		}
		seen[item] = struct{}{}
		result = append(result, item)
	}
	return result
}
