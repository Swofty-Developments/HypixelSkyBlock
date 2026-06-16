package installer

type RAMEstimate struct {
	MinimumMB     int
	RecommendedMB int
}

func EstimateRAMMB(serverCount, serviceCount int) RAMEstimate {
	if serverCount < 0 {
		serverCount = 0
	}
	if serviceCount < 0 {
		serviceCount = 0
	}

	minimum := 900 + serverCount*300 + serviceCount*140
	recommended := 1500 + serverCount*450 + serviceCount*220
	return RAMEstimate{MinimumMB: minimum, RecommendedMB: recommended}
}

func EstimateForConfig(cfg Config) RAMEstimate {
	return EstimateRAMMB(len(cfg.Servers), len(cfg.Services))
}
