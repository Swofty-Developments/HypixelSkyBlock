package profile

import "testing"

func TestNormalizeAddsRequiredSelections(t *testing.T) {
	p := Profile{
		SelectedServers:  []string{"SKYBLOCK_HUB"},
		SelectedServices: []string{"ServiceAPI"},
	}
	p.Normalize()

	if p.SelectedServers[0] != "PROTOTYPE_LOBBY" {
		t.Fatalf("expected required server first, got %v", p.SelectedServers)
	}
	if p.SelectedServices[0] != "ServiceDataMutex" || p.SelectedServices[1] != "ServiceParty" {
		t.Fatalf("expected required services first, got %v", p.SelectedServices)
	}
}

func TestFilterSelected(t *testing.T) {
	got := FilterSelected([]string{"A", "B", "C"}, []string{"B", "C", "D"})
	if len(got) != 2 || got[0] != "B" || got[1] != "C" {
		t.Fatalf("unexpected selection filter result: %v", got)
	}
}
