package tui

import (
	"github.com/charmbracelet/huh"
	"github.com/charmbracelet/lipgloss"
)

func Theme() *huh.Theme {
	theme := huh.ThemeBase()
	pink := lipgloss.Color("#F3A7C0")
	blue := lipgloss.Color("#8FBFF5")

	theme.Focused.Base = theme.Focused.Base.Foreground(blue)
	theme.Focused.Title = theme.Focused.Title.Foreground(pink).Bold(true)
	theme.Focused.Description = theme.Focused.Description.Foreground(blue)
	theme.Focused.SelectedOption = theme.Focused.SelectedOption.Foreground(pink).Bold(true)
	theme.Focused.UnselectedOption = theme.Focused.UnselectedOption.Foreground(blue)
	theme.Focused.NextIndicator = theme.Focused.NextIndicator.Foreground(pink)
	theme.Focused.PrevIndicator = theme.Focused.PrevIndicator.Foreground(blue)
	theme.Focused.TextInput.Cursor = theme.Focused.TextInput.Cursor.Foreground(pink)
	theme.Focused.TextInput.Placeholder = theme.Focused.TextInput.Placeholder.Foreground(blue)
	return theme
}
