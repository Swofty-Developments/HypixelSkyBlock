package main

import (
	"flag"
	"fmt"
	"os"

	tea "charm.land/bubbletea/v2"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/installer"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/tui"
)

func main() {
	var dir string
	flag.StringVar(&dir, "dir", installer.DefaultInstallDir(), "installation directory")
	flag.Parse()

	model := tui.New(dir)
	program := tea.NewProgram(model)
	if _, err := program.Run(); err != nil {
		fmt.Fprintf(os.Stderr, "installer failed: %v\n", err)
		os.Exit(1)
	}
}
