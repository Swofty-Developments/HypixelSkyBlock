package tui

import (
	"strconv"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/charmbracelet/huh"
)

const (
	ActionSave          = "save"
	ActionComposeRender = "compose-render"
	ActionComposeApply  = "compose-apply"
	ActionK8sRender     = "k8s-render"
	ActionK8sBuild      = "k8s-build"
	ActionK8sDeploy     = "k8s-deploy"
	ActionK8sFull       = "k8s-full"
	ActionStatus        = "status"
	ActionWatch         = "watch"
)

func RunWizard(p profile.Profile) (string, profile.Profile, error) {
	theme := Theme()

	skyblockSelected := profile.FilterSelected(p.SelectedServers, profile.SkyBlockServers)
	minigameSelected := profile.FilterSelected(p.SelectedServers, profile.MinigameServers)
	apiPort := strconv.Itoa(p.APIPort)
	action := ActionSave

	if err := huh.NewForm(
		huh.NewGroup(
			huh.NewNote().
				Title("Hypixel Setup").
				Description("This installer uses the Charmbracelet Bubble Tea stack for a persistent profile-driven setup flow. It can edit previous server and service selections, render Docker Compose or Kubernetes assets, and run the documented Kubernetes flow including monitoring and autoscaling."),
		),
		huh.NewGroup(
			huh.NewInput().Title("Install directory").Description("State, rendered assets, and local configuration live here.").Value(&p.InstallDir),
			huh.NewSelect[string]().Title("Deployment runtime").Description("Choose Docker Compose or Kubernetes.").Options(
				huh.NewOption("Docker Compose", profile.RuntimeCompose),
				huh.NewOption("Kubernetes", profile.RuntimeK8s),
			).Value(&p.Runtime),
			huh.NewInput().Title("Proxy bind IP").Description("Used by local Docker Compose proxy binding.").Value(&p.BindIP),
			huh.NewConfirm().Title("Enable Mojang authentication").Description("Sets Velocity online-mode.").Value(&p.OnlineMode),
			huh.NewInput().Title("Forwarding secret").Description("Shared by proxy and backend servers.").Value(&p.ForwardingSecret),
			huh.NewInput().Title("Velocity secret").Description("Stored in generated secrets for Kubernetes.").Value(&p.VelocitySecret),
		),
		huh.NewGroup(
			huh.NewMultiSelect[string]().Title("SkyBlock servers").Description("PROTOTYPE_LOBBY is always included.").Options(stringOptions(profile.SkyBlockServers)...).Value(&skyblockSelected),
			huh.NewMultiSelect[string]().Title("Minigame servers").Description("Optional game-server workloads.").Options(stringOptions(profile.MinigameServers)...).Value(&minigameSelected),
			huh.NewMultiSelect[string]().Title("Services").Description("ServiceDataMutex and ServiceParty remain enabled.").Options(stringOptions(profile.AllServices)...).Value(&p.SelectedServices),
			huh.NewInput().Title("API port").Description("Used by ServiceAPI in Compose mode.").Value(&apiPort),
			huh.NewConfirm().Title("Expose MongoDB and Redis to the host").Description("Compose only. Keeps them isolated when disabled.").Value(&p.ExposeDBPorts),
		),
		huh.NewGroup(
			huh.NewInput().Title("Container registry").Description("Example: ghcr.io/your-org").Value(&p.Registry),
			huh.NewInput().Title("Image tag").Description("Applied to proxy, service, and game images.").Value(&p.ImageTag),
			huh.NewInput().Title("Kubernetes namespace").Description("Generated manifests target this namespace.").Value(&p.KubernetesNamespace),
			huh.NewInput().Title("kubectl context").Description("Optional. Leave blank for the current context.").Value(&p.KubeContext),
			huh.NewSelect[string]().Title("Proxy service type").Description("How the proxy is exposed to players.").Options(
				huh.NewOption("LoadBalancer", "LoadBalancer"),
				huh.NewOption("NodePort", "NodePort"),
				huh.NewOption("ClusterIP", "ClusterIP"),
			).Value(&p.ProxyServiceType),
			huh.NewConfirm().Title("Install Prometheus and KEDA with Helm").Description("Matches the Kubernetes Linux setup guide.").Value(&p.InstallMonitoring),
			huh.NewConfirm().Title("Enable KEDA autoscaling").Description("Generates ScaledObjects for selected game servers.").Value(&p.EnableAutoscaling),
			huh.NewConfirm().Title("Deploy managed Redis and MongoDB").Description("Creates in-cluster stateful workloads.").Value(&p.InstallManagedDatastore),
			huh.NewInput().Title("Prometheus address").Description("Used by generated ScaledObjects.").Value(&p.PrometheusAddress),
			huh.NewInput().Title("MongoDB URI").Description("Used when managed datastores are disabled.").Value(&p.MongoURI),
			huh.NewInput().Title("Redis URI").Description("Used when managed datastores are disabled.").Value(&p.RedisURI),
			huh.NewInput().Title("MongoDB volume size").Description("PVC size when managed datastores are enabled.").Value(&p.MongoStorageSize),
			huh.NewInput().Title("Redis volume size").Description("PVC size when managed datastores are enabled.").Value(&p.RedisStorageSize),
			huh.NewInput().Title("Storage class").Description("Optional. Leave blank for the default class.").Value(&p.StorageClassName),
		).WithHideFunc(func() bool { return p.Runtime != profile.RuntimeK8s }),
		huh.NewGroup(
			huh.NewNote().Title("Summary").Description(summary(p, skyblockSelected, minigameSelected)),
			huh.NewSelect[string]().Title("Next action").Description("The profile is saved before execution.").Options(actionOptions(p.Runtime)...).Value(&action),
		),
	).WithTheme(theme).Run(); err != nil {
		return "", p, err
	}

	p.SelectedServers = append(skyblockSelected, minigameSelected...)
	p.Normalize()
	if parsed, err := strconv.Atoi(strings.TrimSpace(apiPort)); err == nil && parsed > 0 {
		p.APIPort = parsed
	}
	return action, p, p.Validate()
}

func stringOptions(items []string) []huh.Option[string] {
	options := make([]huh.Option[string], 0, len(items))
	for _, item := range items {
		options = append(options, huh.NewOption(item, item))
	}
	return options
}

func actionOptions(runtime string) []huh.Option[string] {
	if runtime == profile.RuntimeK8s {
		return []huh.Option[string]{
			huh.NewOption("Save profile only", ActionSave),
			huh.NewOption("Render Kubernetes assets", ActionK8sRender),
			huh.NewOption("Build and push Kubernetes images", ActionK8sBuild),
			huh.NewOption("Deploy rendered Kubernetes assets", ActionK8sDeploy),
			huh.NewOption("Run full Kubernetes setup", ActionK8sFull),
			huh.NewOption("Show environment status", ActionStatus),
			huh.NewOption("Watch environment status", ActionWatch),
		}
	}
	return []huh.Option[string]{
		huh.NewOption("Save profile only", ActionSave),
		huh.NewOption("Render Docker Compose assets", ActionComposeRender),
		huh.NewOption("Render and apply Docker Compose", ActionComposeApply),
		huh.NewOption("Show environment status", ActionStatus),
		huh.NewOption("Watch environment status", ActionWatch),
	}
}

func summary(p profile.Profile, skyblockSelected, minigameSelected []string) string {
	serverSelections := append([]string{"PROTOTYPE_LOBBY"}, append(skyblockSelected, minigameSelected...)...)
	lines := []string{
		"Runtime: " + p.Runtime,
		"Install dir: " + p.InstallDir,
		"Servers: " + strings.Join(serverSelections, ", "),
		"Services: " + strings.Join(profile.FilterSelected(p.SelectedServices, profile.AllServices), ", "),
	}
	if p.Runtime == profile.RuntimeK8s {
		lines = append(lines,
			"Namespace: "+p.KubernetesNamespace,
			"Registry: "+strings.TrimRight(p.Registry, "/")+":"+p.ImageTag,
			"Proxy exposure: "+p.ProxyServiceType,
		)
	}
	return strings.Join(lines, "\n")
}
