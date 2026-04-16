package ops

import (
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

type resourceTotals struct {
	RequestMilliCPU int
	LimitMilliCPU   int
	RequestMemoryMi int
	LimitMemoryMi   int
}

type hostCapacity struct {
	MilliCPU int
	MemoryMi int
}

func kubernetesCapacityWarnings(p profile.Profile) []string {
	if p.Runtime != profile.RuntimeK8s {
		return nil
	}

	totals := estimateKubernetesResources(p)
	warnings := make([]string, 0, 3)
	if p.InstallMonitoring {
		warnings = append(warnings, "Prometheus and KEDA add a noticeable control-plane footprint; keep monitoring disabled unless this host really needs it.")
	}

	capacity, err := detectHostCapacity()
	if err != nil {
		return warnings
	}
	if capacity.MilliCPU > 0 && totals.RequestMilliCPU > capacity.MilliCPU*85/100 {
		warnings = append(warnings, fmt.Sprintf("selected workloads request about %.1f CPU cores out of %.1f available; reduce the selected services/servers or lower per-pod resources before deploying.", float64(totals.RequestMilliCPU)/1000, float64(capacity.MilliCPU)/1000))
	}
	if capacity.MemoryMi > 0 && totals.RequestMemoryMi > capacity.MemoryMi*85/100 {
		warnings = append(warnings, fmt.Sprintf("selected workloads request about %.1f GiB out of %.1f GiB available; this is likely to cause scheduling or eviction pressure.", float64(totals.RequestMemoryMi)/1024, float64(capacity.MemoryMi)/1024))
	}
	if capacity.MilliCPU > 0 && totals.LimitMilliCPU > capacity.MilliCPU*130/100 {
		warnings = append(warnings, fmt.Sprintf("configured CPU limits allow bursts up to %.1f cores on a %.1f-core host; expect heavy contention under load.", float64(totals.LimitMilliCPU)/1000, float64(capacity.MilliCPU)/1000))
	}
	if capacity.MemoryMi > 0 && totals.LimitMemoryMi > capacity.MemoryMi*130/100 {
		warnings = append(warnings, fmt.Sprintf("configured memory limits total about %.1f GiB on a %.1f GiB host; that leaves too little room for Kubernetes and the OS if many pods grow to their limits.", float64(totals.LimitMemoryMi)/1024, float64(capacity.MemoryMi)/1024))
	}
	return warnings
}

func estimateKubernetesResources(p profile.Profile) resourceTotals {
	total := resourceTotals{}

	addResources(&total, "250m", "384Mi", "1", "1Gi")
	if p.InstallManagedDatastore {
		addResources(&total, "150m", "256Mi", "1", "2Gi")
		addResources(&total, "75m", "128Mi", "500m", "512Mi")
	}
	if p.InstallMonitoring {
		addResources(&total, "1000m", "1536Mi", "2", "3Gi")
	}
	switch p.KubernetesTarget {
	case profile.KubernetesTargetK3d:
		addResources(&total, "300m", "512Mi", "1", "1Gi")
	case profile.KubernetesTargetMinikube:
		addResources(&total, "4000m", "8Gi", "4", "8Gi")
	}

	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		for range svc.Replicas {
			addResources(&total, svc.RequestCPU, svc.RequestMemory, svc.LimitCPU, svc.LimitMemory)
		}
	}
	for _, serverType := range p.SelectedServers {
		server := spec.ServerByType(serverType)
		for range server.Replicas {
			addResources(&total, server.RequestCPU, server.RequestMemory, server.LimitCPU, server.LimitMemory)
		}
	}

	return total
}

func addResources(total *resourceTotals, requestCPU, requestMemory, limitCPU, limitMemory string) {
	total.RequestMilliCPU += parseMilliCPU(requestCPU)
	total.RequestMemoryMi += parseMemoryMi(requestMemory)
	total.LimitMilliCPU += parseMilliCPU(limitCPU)
	total.LimitMemoryMi += parseMemoryMi(limitMemory)
}

func parseMilliCPU(value string) int {
	value = strings.TrimSpace(value)
	if strings.HasSuffix(value, "m") {
		parsed, _ := strconv.Atoi(strings.TrimSuffix(value, "m"))
		return parsed
	}
	parsed, _ := strconv.ParseFloat(value, 64)
	return int(parsed * 1000)
}

func parseMemoryMi(value string) int {
	value = strings.TrimSpace(value)
	switch {
	case strings.HasSuffix(value, "Gi"):
		parsed, _ := strconv.ParseFloat(strings.TrimSuffix(value, "Gi"), 64)
		return int(parsed * 1024)
	case strings.HasSuffix(value, "Mi"):
		parsed, _ := strconv.ParseFloat(strings.TrimSuffix(value, "Mi"), 64)
		return int(parsed)
	case strings.HasSuffix(value, "Ki"):
		parsed, _ := strconv.ParseFloat(strings.TrimSuffix(value, "Ki"), 64)
		return int(parsed / 1024)
	default:
		parsed, _ := strconv.ParseFloat(value, 64)
		return int(parsed / (1024 * 1024))
	}
}

func detectHostCapacity() (hostCapacity, error) {
	capacity := hostCapacity{MilliCPU: runtime.NumCPU() * 1000}
	data, err := os.ReadFile("/proc/meminfo")
	if err != nil {
		if capacity.MilliCPU == 0 {
			return hostCapacity{}, err
		}
		return capacity, nil
	}
	for _, line := range strings.Split(string(data), "\n") {
		if !strings.HasPrefix(line, "MemTotal:") {
			continue
		}
		fields := strings.Fields(line)
		if len(fields) < 2 {
			break
		}
		totalKiB, parseErr := strconv.Atoi(fields[1])
		if parseErr != nil {
			break
		}
		capacity.MemoryMi = totalKiB / 1024
		return capacity, nil
	}
	if capacity.MilliCPU == 0 {
		return hostCapacity{}, fmt.Errorf("host capacity is unavailable")
	}
	return capacity, nil
}
