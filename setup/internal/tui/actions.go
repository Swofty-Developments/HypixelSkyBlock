package tui

import (
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/charmbracelet/huh"
)

const (
	ActionSave          = "save"
	ActionComposeRender = "compose-render"
	ActionComposeApply  = "compose-apply"
	ActionComposeStaff  = "compose-make-staff"
	ActionK8sRender     = "k8s-render"
	ActionK8sBuild      = "k8s-build"
	ActionK8sDeploy     = "k8s-deploy"
	ActionK8sFull       = "k8s-full"
	ActionStatus        = "status"
	ActionWatch         = "watch"
)

func ActionOptions(runtime string) []huh.Option[string] {
	if runtime == profile.RuntimeK8s {
		return []huh.Option[string]{
			huh.NewOption("Save profile only", ActionSave),
			huh.NewOption("Render Kubernetes assets", ActionK8sRender),
			huh.NewOption("Build Kubernetes images locally", ActionK8sBuild),
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
		huh.NewOption("Promote user to STAFF", ActionComposeStaff),
		huh.NewOption("Show environment status", ActionStatus),
		huh.NewOption("Watch environment status", ActionWatch),
	}
}

func SupportsStaffPromotion(action string) bool {
	return action == ActionComposeStaff
}
