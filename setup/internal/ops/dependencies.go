package ops

import (
	"charm.land/bubbles/v2/progress"
	tea "charm.land/bubbletea/v2"
	"fmt"
	"os"
	"runtime"
	"strings"
)

type installProgressReporter func(tool string, status string, percent float64, detail string, err error)

type dependencyStep struct {
	name    string
	status  string
	percent float64
	detail  string
}

type dependencyEvent struct {
	tool    string
	status  string
	percent float64
	detail  string
	err     error
}

type dependencyDone struct{}

type dependencyModel struct {
	steps     []dependencyStep
	stepIndex map[string]int
	events    <-chan tea.Msg
	bar       progress.Model
	width     int
}

func newDependencyModel(missing []string, events <-chan tea.Msg) dependencyModel {
	steps := make([]dependencyStep, 0, len(missing))
	stepIndex := make(map[string]int, len(missing))
	for _, tool := range missing {
		stepIndex[tool] = len(steps)
		steps = append(steps, dependencyStep{name: tool, status: "pending", percent: 0})
	}
	bar := progress.New(progress.WithWidth(32))
	return dependencyModel{
		steps:     steps,
		stepIndex: stepIndex,
		events:    events,
		bar:       bar,
		width:     100,
	}
}

func (m dependencyModel) Init() tea.Cmd {
	return m.waitForEvent()
}

func (m dependencyModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		if m.width > 90 {
			m.bar.SetWidth(44)
		} else if m.width > 70 {
			m.bar.SetWidth(32)
		} else {
			m.bar.SetWidth(22)
		}
		return m, nil
	case dependencyEvent:
		idx, ok := m.stepIndex[msg.tool]
		if ok {
			m.steps[idx].status = msg.status
			m.steps[idx].percent = clampPercent(msg.percent)
			m.steps[idx].detail = msg.detail
		}
		return m, m.waitForEvent()
	case dependencyDone:
		return m, tea.Quit
	}
	return m, nil
}

func (m dependencyModel) View() tea.View {
	if len(m.steps) == 0 {
		return tea.NewView("No dependency installation needed.")
	}

	var b strings.Builder
	b.WriteString("Dependency bootstrap\n\n")
	for _, step := range m.steps {
		status := stepStatus(step)
		b.WriteString(fmt.Sprintf("%-10s %s %s\n", step.name, m.bar.ViewAs(step.percent), status))
	}

	complete := 0
	for _, step := range m.steps {
		if step.status == "done" {
			complete++
		}
	}
	overall := float64(complete) / float64(len(m.steps))
	b.WriteString("\n")
	b.WriteString(fmt.Sprintf("overall    %s %d/%d\n", m.bar.ViewAs(overall), complete, len(m.steps)))
	return tea.NewView(b.String())
}

func (m dependencyModel) waitForEvent() tea.Cmd {
	return func() tea.Msg {
		msg, ok := <-m.events
		if !ok {
			return dependencyDone{}
		}
		return msg
	}
}

func stepStatus(step dependencyStep) string {
	switch step.status {
	case "running":
		if step.detail == "" {
			return "running"
		}
		return step.detail
	case "done":
		return "ready"
	case "failed":
		if step.detail == "" {
			return "failed"
		}
		return "failed: " + step.detail
	default:
		return "pending"
	}
}

func clampPercent(value float64) float64 {
	if value < 0 {
		return 0
	}
	if value > 1 {
		return 1
	}
	return value
}

func InstallDependenciesWithProgress(missing []string) error {
	if len(missing) == 0 {
		return nil
	}
	if runtime.GOOS != "linux" {
		return fmt.Errorf("automatic dependency installation is supported only on Linux hosts")
	}

	if !stdoutIsTTY() {
		return installDependencies(missing, func(string, string, float64, string, error) {})
	}

	events := make(chan tea.Msg, len(missing)*6+4)
	result := make(chan error, 1)

	go func() {
		err := installDependencies(missing, func(tool string, status string, percent float64, detail string, installErr error) {
			events <- dependencyEvent{tool: tool, status: status, percent: percent, detail: detail, err: installErr}
		})
		result <- err
		events <- dependencyDone{}
		close(events)
	}()

	model := newDependencyModel(missing, events)
	if _, err := tea.NewProgram(model).Run(); err != nil {
		return err
	}
	return <-result
}

func installDependencies(missing []string, report installProgressReporter) error {
	installer := newDependencyInstaller()

	for _, tool := range missing {
		report(tool, "running", 0.1, "preparing", nil)
		if err := installer.Install(tool); err != nil {
			wrapped := fmt.Errorf("install %s: %w", tool, err)
			report(tool, "failed", 1, wrapped.Error(), wrapped)
			return wrapped
		}

		report(tool, "running", 0.85, "verifying", nil)
		if err := Require(tool); err != nil {
			wrapped := fmt.Errorf("%s was installed but is still unavailable in PATH: %w", tool, err)
			report(tool, "failed", 1, wrapped.Error(), wrapped)
			return wrapped
		}

		report(tool, "done", 1, "ready", nil)
	}
	return nil
}

func stdoutIsTTY() bool {
	info, err := os.Stdout.Stat()
	if err != nil {
		return false
	}
	return (info.Mode() & os.ModeCharDevice) != 0
}
