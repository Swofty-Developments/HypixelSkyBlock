package main

import (
	"fmt"
	"os"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/app"
)

func main() {
	if err := app.Run(); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}
