package ops

import (
	"charm.land/bubbles/v2/progress"
	"fmt"
	"strings"
	"time"
)

type kubernetesStageProgress struct {
	enabled bool
	names   []string
	states  map[string]kubernetesStageState
	bar     progress.Model
}

type kubernetesStageState struct {
	value  float64
	status string
}

func newKubernetesStageProgress(names []string, enabled bool) *kubernetesStageProgress {
	bar := progress.New(progress.WithWidth(30))

	states := make(map[string]kubernetesStageState, len(names))
	for _, name := range names {
		states[name] = kubernetesStageState{value: 0, status: "pending"}
	}

	return &kubernetesStageProgress{
		enabled: enabled,
		names:   names,
		states:  states,
		bar:     bar,
	}
}

func (p *kubernetesStageProgress) Enabled() bool {
	return p.enabled
}

func (p *kubernetesStageProgress) Render() {
	if !p.enabled {
		return
	}
	fmt.Print("\033[H\033[2J")
	fmt.Println("Kubernetes setup")
	fmt.Println()

	total := 0.0
	for _, name := range p.names {
		state := p.states[name]
		total += state.value
		fmt.Printf("%-28s %s %s\n", name, p.bar.ViewAs(state.value), state.status)
	}

	overall := 0.0
	if len(p.names) > 0 {
		overall = total / float64(len(p.names))
	}
	fmt.Println()
	fmt.Printf("%-28s %s\n", "overall", p.bar.ViewAs(overall))
}

func (p *kubernetesStageProgress) MarkSkipped(name string) {
	if !p.enabled {
		return
	}
	p.states[name] = kubernetesStageState{value: 1, status: "skipped"}
	p.Render()
}

func (p *kubernetesStageProgress) MarkRunning(name string) {
	if !p.enabled {
		return
	}
	p.states[name] = kubernetesStageState{value: 0.5, status: "running"}
	p.Render()
}

func (p *kubernetesStageProgress) MarkDone(name string, duration time.Duration) {
	if !p.enabled {
		return
	}
	p.states[name] = kubernetesStageState{value: 1, status: "done in " + duration.Round(time.Second).String()}
	p.Render()
}

func (p *kubernetesStageProgress) MarkFailed(name string, err error) {
	if !p.enabled {
		return
	}
	status := "failed"
	if err != nil {
		status = "failed: " + compactError(err)
	}
	p.states[name] = kubernetesStageState{value: 1, status: status}
	p.Render()
}

func compactError(err error) string {
	text := strings.TrimSpace(err.Error())
	if len(text) <= 72 {
		return text
	}
	return text[:69] + "..."
}
