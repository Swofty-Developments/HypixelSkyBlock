package spec

import "strings"

type Service struct {
	Name           string
	Module         string
	Jar            string
	DeploymentName string
	ImageName      string
	Replicas       int
	RequestCPU     string
	RequestMemory  string
	LimitCPU       string
	LimitMemory    string
}

type Server struct {
	ServerType     string
	DeploymentName string
	Replicas       int
	MinReplicas    int
	MaxReplicas    int
	Threshold      int
	RequestCPU     string
	RequestMemory  string
	LimitCPU       string
	LimitMemory    string
}

func ServiceByName(name string) Service {
	specs := map[string]Service{
		"ServiceAPI":          {Name: name, Module: "service.api", Jar: "ServiceAPI.jar", DeploymentName: "service-api", ImageName: "hypixel-service-api", Replicas: 1, RequestCPU: "150m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceAuctionHouse": {Name: name, Module: "service.auctionhouse", Jar: "ServiceAuctionHouse.jar", DeploymentName: "service-auctionhouse", ImageName: "hypixel-service-auctionhouse", Replicas: 1, RequestCPU: "150m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceBazaar":       {Name: name, Module: "service.bazaar", Jar: "ServiceBazaar.jar", DeploymentName: "service-bazaar", ImageName: "hypixel-service-bazaar", Replicas: 1, RequestCPU: "150m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceDarkAuction":  {Name: name, Module: "service.darkauction", Jar: "ServiceDarkAuction.jar", DeploymentName: "service-darkauction", ImageName: "hypixel-service-darkauction", Replicas: 1, RequestCPU: "100m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
		"ServiceDataMutex":    {Name: name, Module: "service.datamutex", Jar: "ServiceDataMutex.jar", DeploymentName: "service-datamutex", ImageName: "hypixel-service-datamutex", Replicas: 1, RequestCPU: "75m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
		"ServiceFriend":       {Name: name, Module: "service.friend", Jar: "ServiceFriend.jar", DeploymentName: "service-friend", ImageName: "hypixel-service-friend", Replicas: 1, RequestCPU: "75m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
		"ServiceItemTracker":  {Name: name, Module: "service.itemtracker", Jar: "ServiceItemTracker.jar", DeploymentName: "service-itemtracker", ImageName: "hypixel-service-itemtracker", Replicas: 1, RequestCPU: "150m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceOrchestrator": {Name: name, Module: "service.orchestrator", Jar: "ServiceOrchestrator.jar", DeploymentName: "service-orchestrator", ImageName: "hypixel-service-orchestrator", Replicas: 1, RequestCPU: "75m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
		"ServiceParty":        {Name: name, Module: "service.party", Jar: "ServiceParty.jar", DeploymentName: "service-party", ImageName: "hypixel-service-party", Replicas: 1, RequestCPU: "75m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
		"ServicePunishment":   {Name: name, Module: "service.punishment", Jar: "ServicePunishment.jar", DeploymentName: "service-punishment", ImageName: "hypixel-service-punishment", Replicas: 1, RequestCPU: "75m", RequestMemory: "192Mi", LimitCPU: "500m", LimitMemory: "512Mi"},
	}
	if spec, ok := specs[name]; ok {
		return spec
	}
	return Service{
		Name:           name,
		Module:         strings.ToLower(strings.TrimPrefix(name, "Service")),
		Jar:            name + ".jar",
		DeploymentName: strings.ToLower(strings.ReplaceAll(name, "Service", "service-")),
		ImageName:      "hypixel-" + strings.ToLower(name),
		Replicas:       1,
		RequestCPU:     "75m",
		RequestMemory:  "192Mi",
		LimitCPU:       "500m",
		LimitMemory:    "512Mi",
	}
}

func ServerByType(name string) Server {
	specs := map[string]Server{
		"PROTOTYPE_LOBBY": {ServerType: name, DeploymentName: "prototype-lobby", Replicas: 1, MinReplicas: 1, MaxReplicas: 6, Threshold: 80, RequestCPU: "300m", RequestMemory: "512Mi", LimitCPU: "1500m", LimitMemory: "1536Mi"},
		"BEDWARS_LOBBY":   {ServerType: name, DeploymentName: "bedwars-lobby", Replicas: 1, MinReplicas: 1, MaxReplicas: 6, Threshold: 80, RequestCPU: "300m", RequestMemory: "512Mi", LimitCPU: "1500m", LimitMemory: "1536Mi"},
		"BEDWARS_GAME":    {ServerType: name, DeploymentName: "bedwars-game", Replicas: 1, MinReplicas: 1, MaxReplicas: 16, Threshold: 16, RequestCPU: "500m", RequestMemory: "768Mi", LimitCPU: "2", LimitMemory: "2Gi"},
		"SKYBLOCK_HUB":    {ServerType: name, DeploymentName: "skyblock-hub", Replicas: 1, MinReplicas: 1, MaxReplicas: 10, Threshold: 80, RequestCPU: "400m", RequestMemory: "768Mi", LimitCPU: "2", LimitMemory: "2Gi"},
	}
	if spec, ok := specs[name]; ok {
		return spec
	}
	return Server{
		ServerType:     name,
		DeploymentName: strings.ToLower(strings.ReplaceAll(name, "_", "-")),
		Replicas:       1,
		MinReplicas:    1,
		MaxReplicas:    8,
		Threshold:      32,
		RequestCPU:     "300m",
		RequestMemory:  "512Mi",
		LimitCPU:       "1500m",
		LimitMemory:    "1536Mi",
	}
}
