package tui

import (
	"errors"
	"strconv"
	"strings"

	"charm.land/huh/v2"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
)

type wizardState struct {
	profile          profile.Profile
	skyblockSelected []string
	minigameSelected []string
	apiPort          string
	action           string
	staffUsername    string
}

func RunWizard(p profile.Profile) (string, string, profile.Profile, error) {
	state := newWizardState(p)
	if err := runWizardGroup(state.installGroup()); err != nil {
		return "", "", p, err
	}
	if err := runWizardGroup(state.workloadGroup()); err != nil {
		return "", "", p, err
	}
	if state.profile.Runtime == profile.RuntimeK8s {
		if err := runWizardGroup(state.kubernetesGroup()); err != nil {
			return "", "", p, err
		}
	}
	if err := runWizardGroup(state.actionGroup()); err != nil {
		return "", "", p, err
	}
	if SupportsStaffPromotion(state.action) {
		if err := runWizardGroup(state.staffPromotionGroup()); err != nil {
			return "", "", p, err
		}
	}
	if err := state.apply(); err != nil {
		return "", "", p, err
	}
	return state.action, state.staffUsername, state.profile, nil
}

func newWizardState(p profile.Profile) *wizardState {
	return &wizardState{
		profile:          p,
		skyblockSelected: profile.FilterSelected(p.SelectedServers, profile.SkyBlockServers),
		minigameSelected: profile.FilterSelected(p.SelectedServers, profile.MinigameServers),
		apiPort:          strconv.Itoa(p.APIPort),
		action:           ActionSave,
	}
}
func (s *wizardState) installGroup() *huh.Group {
	return huh.NewGroup(
		huh.NewInput().Title("Install directory").Description("State, rendered assets, and local configuration live here.").Value(&s.profile.InstallDir),
		huh.NewSelect[string]().Title("Deployment runtime").Description("Choose Docker Compose or Kubernetes.").Options(
			huh.NewOption("Docker Compose", profile.RuntimeCompose),
			huh.NewOption("Kubernetes", profile.RuntimeK8s),
		).Value(&s.profile.Runtime),
		huh.NewInput().Title("Proxy bind IP").Description("Used by local Docker Compose proxy binding.").Value(&s.profile.BindIP),
		huh.NewConfirm().Title("Enable Mojang authentication").Description("Sets Velocity online-mode.").Value(&s.profile.OnlineMode),
		huh.NewInput().Title("Shared secret").Description("Used for both Velocity and backend forwarding.").Value(&s.profile.SharedSecret),
	)
}

func (s *wizardState) workloadGroup() *huh.Group {
	return huh.NewGroup(
		huh.NewMultiSelect[string]().Title("SkyBlock servers").Description("PROTOTYPE_LOBBY is always included.").Options(stringOptions(profile.SkyBlockServers)...).Value(&s.skyblockSelected),
		huh.NewMultiSelect[string]().Title("Minigame servers").Description("Optional game-server workloads.").Options(stringOptions(profile.MinigameServers)...).Value(&s.minigameSelected),
		huh.NewMultiSelect[string]().Title("Services").Description("ServiceDataMutex and ServiceParty remain enabled.").Options(stringOptions(profile.AllServices)...).Value(&s.profile.SelectedServices),
		huh.NewInput().Title("API port").Description("Used by ServiceAPI in Compose mode.").Value(&s.apiPort).Validate(validateAPIPort),
		huh.NewConfirm().Title("Expose MongoDB and Redis to the host").Description("Compose only. Keeps them isolated when disabled.").Value(&s.profile.ExposeDBPorts),
	)
}

func (s *wizardState) kubernetesGroup() *huh.Group {
	return huh.NewGroup(
		huh.NewInput().Title("Image tag").Description("Applied to proxy, service, and game images.").Value(&s.profile.ImageTag),
		huh.NewInput().Title("Image registry").Description("Optional for local targets. Required for standard clusters with pull-based policies, for example ghcr.io/swofty-developments.").Value(&s.profile.ImageRegistry),
		huh.NewSelect[string]().Title("Image pull policy").Description("Never for local image workflows, IfNotPresent for registry-backed clusters.").Options(
			huh.NewOption("Never", "Never"),
			huh.NewOption("IfNotPresent", "IfNotPresent"),
			huh.NewOption("Always", "Always"),
		).Value(&s.profile.ImagePullPolicy),
		huh.NewSelect[string]().Title("Kubernetes target").Description("Choose a single-host k3d cluster, an existing Kubernetes cluster, or Minikube. k3d is the recommended turnkey option on one machine.").Options(
			huh.NewOption("k3d (recommended single-host cluster)", profile.KubernetesTargetK3d),
			huh.NewOption("Kubernetes cluster", profile.KubernetesTargetStandard),
			huh.NewOption("Minikube", profile.KubernetesTargetMinikube),
		).Value(&s.profile.KubernetesTarget),
		huh.NewInput().Title("k3d cluster name").Description("Used when the setup creates or reuses a local k3d cluster.").Value(&s.profile.KubernetesClusterName),
		huh.NewInput().Title("Minikube profile").Description("Used when the setup creates or reuses a Minikube cluster.").Value(&s.profile.MinikubeProfile),
		huh.NewInput().Title("Kubernetes namespace").Description("Generated manifests target this namespace.").Value(&s.profile.KubernetesNamespace),
		huh.NewInput().Title("kubectl context").Description("Optional. Leave blank for the current context.").Value(&s.profile.KubeContext),
		huh.NewSelect[string]().Title("Proxy service type").Description("How the proxy is exposed to players.").Options(
			huh.NewOption("LoadBalancer", "LoadBalancer"),
			huh.NewOption("NodePort", "NodePort"),
			huh.NewOption("ClusterIP", "ClusterIP"),
		).Value(&s.profile.ProxyServiceType),
		huh.NewConfirm().Title("Install Prometheus and KEDA with Helm").Description("Matches the Kubernetes Linux setup guide.").Value(&s.profile.InstallMonitoring),
		huh.NewConfirm().Title("Enable KEDA autoscaling").Description("Generates ScaledObjects for selected game servers.").Value(&s.profile.EnableAutoscaling),
		huh.NewConfirm().Title("Deploy managed Redis and MongoDB").Description("Creates in-cluster stateful workloads.").Value(&s.profile.InstallManagedDatastore),
		huh.NewInput().Title("Prometheus address").Description("Used by generated ScaledObjects.").Value(&s.profile.PrometheusAddress),
		huh.NewInput().Title("MongoDB URI").Description("Used when managed datastores are disabled.").Value(&s.profile.MongoURI),
		huh.NewInput().Title("Redis URI").Description("Used when managed datastores are disabled.").Value(&s.profile.RedisURI),
		huh.NewInput().Title("MongoDB volume size").Description("PVC size when managed datastores are enabled.").Value(&s.profile.MongoStorageSize),
		huh.NewInput().Title("Redis volume size").Description("PVC size when managed datastores are enabled.").Value(&s.profile.RedisStorageSize),
		huh.NewInput().Title("Storage class").Description("Optional. Leave blank for the default class.").Value(&s.profile.StorageClassName),
	).WithHideFunc(func() bool {
		return s.profile.Runtime != profile.RuntimeK8s
	})
}

func (s *wizardState) actionGroup() *huh.Group {
	return huh.NewGroup(
		huh.NewSelect[string]().Title("Next action").Description("The profile is saved before execution.").Options(ActionOptions(s.profile.Runtime)...).Value(&s.action),
		huh.NewNote().Title("Summary").Description(s.summary()),
	)
}

func (s *wizardState) staffPromotionGroup() *huh.Group {
	return huh.NewGroup(
		huh.NewInput().Title("User to promote").Description("Compose only. Enter an existing username to set its rank to STAFF in MongoDB.").Value(&s.staffUsername),
	).WithHideFunc(func() bool {
		return !SupportsStaffPromotion(s.action)
	})
}

func (s *wizardState) apply() error {
	selectedServers := s.selectedServers()
	s.profile.SelectedServers = append([]string(nil), selectedServers...)
	if err := validateAPIPort(s.apiPort); err != nil {
		return err
	}
	parsedPort, _ := strconv.Atoi(strings.TrimSpace(s.apiPort))
	s.profile.APIPort = parsedPort
	s.profile.Normalize()

	if SupportsStaffPromotion(s.action) {
		s.staffUsername = strings.TrimSpace(s.staffUsername)
		if s.staffUsername == "" {
			return errors.New("username is required for compose-make-staff")
		}
	}

	return s.profile.Validate()
}

func runWizardGroup(group *huh.Group) error {
	return huh.NewForm(group).WithTheme(Theme()).Run()
}

func (s *wizardState) selectedServers() []string {
	servers := make([]string, 0, len(s.skyblockSelected)+len(s.minigameSelected))
	servers = append(servers, s.skyblockSelected...)
	servers = append(servers, s.minigameSelected...)
	return servers
}

func (s *wizardState) summary() string {
	lines := []string{
		"Runtime: " + s.profile.Runtime,
		"Install dir: " + s.profile.InstallDir,
		"Servers: " + strings.Join(append([]string{"PROTOTYPE_LOBBY"}, s.selectedServers()...), ", "),
		"Services: " + strings.Join(profile.FilterSelected(s.profile.SelectedServices, profile.AllServices), ", "),
	}

	if s.profile.Runtime == profile.RuntimeK8s {
		lines = append(lines, "Target: "+s.profile.KubernetesTarget)
		switch s.profile.KubernetesTarget {
		case profile.KubernetesTargetK3d:
			lines = append(lines, "k3d cluster: "+s.profile.KubernetesClusterName)
		case profile.KubernetesTargetMinikube:
			lines = append(lines, "Minikube profile: "+s.profile.MinikubeProfile)
		}
		lines = append(lines,
			"Namespace: "+s.profile.KubernetesNamespace,
			"Image tag: "+s.profile.ImageTag,
			"Image registry: "+s.profile.ImageRegistry,
			"Image pull policy: "+s.profile.ImagePullPolicy,
			"Proxy exposure: "+s.profile.ProxyServiceType,
		)
	}

	return strings.Join(lines, "\n")
}

func stringOptions(items []string) []huh.Option[string] {
	options := make([]huh.Option[string], 0, len(items))
	for _, item := range items {
		options = append(options, huh.NewOption(item, item))
	}
	return options
}

func validateAPIPort(value string) error {
	trimmed := strings.TrimSpace(value)
	if trimmed == "" {
		return errors.New("API port is required")
	}
	parsed, err := strconv.Atoi(trimmed)
	if err != nil {
		return errors.New("API port must be a valid integer")
	}
	if parsed < 1 || parsed > 65535 {
		return errors.New("API port must be between 1 and 65535")
	}
	return nil
}
