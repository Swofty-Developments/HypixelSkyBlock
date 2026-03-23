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
		"ServiceAPI":          {Name: name, Module: "service.api", Jar: "ServiceAPI.jar", DeploymentName: "service-api", ImageName: "hypixel-service-api", Replicas: 2, RequestCPU: "200m", RequestMemory: "384Mi", LimitCPU: "2", LimitMemory: "1Gi"},
		"ServiceAuctionHouse": {Name: name, Module: "service.auctionhouse", Jar: "ServiceAuctionHouse.jar", DeploymentName: "service-auctionhouse", ImageName: "hypixel-service-auctionhouse", Replicas: 1, RequestCPU: "200m", RequestMemory: "384Mi", LimitCPU: "2", LimitMemory: "1Gi"},
		"ServiceBazaar":       {Name: name, Module: "service.bazaar", Jar: "ServiceBazaar.jar", DeploymentName: "service-bazaar", ImageName: "hypixel-service-bazaar", Replicas: 1, RequestCPU: "200m", RequestMemory: "384Mi", LimitCPU: "2", LimitMemory: "1Gi"},
		"ServiceDarkAuction":  {Name: name, Module: "service.darkauction", Jar: "ServiceDarkAuction.jar", DeploymentName: "service-darkauction", ImageName: "hypixel-service-darkauction", Replicas: 1, RequestCPU: "150m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceDataMutex":    {Name: name, Module: "service.datamutex", Jar: "ServiceDataMutex.jar", DeploymentName: "service-datamutex", ImageName: "hypixel-service-datamutex", Replicas: 1, RequestCPU: "100m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceFriend":       {Name: name, Module: "service.friend", Jar: "ServiceFriend.jar", DeploymentName: "service-friend", ImageName: "hypixel-service-friend", Replicas: 1, RequestCPU: "100m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceItemTracker":  {Name: name, Module: "service.itemtracker", Jar: "ServiceItemTracker.jar", DeploymentName: "service-itemtracker", ImageName: "hypixel-service-itemtracker", Replicas: 1, RequestCPU: "200m", RequestMemory: "384Mi", LimitCPU: "2", LimitMemory: "1Gi"},
		"ServiceOrchestrator": {Name: name, Module: "service.orchestrator", Jar: "ServiceOrchestrator.jar", DeploymentName: "service-orchestrator", ImageName: "hypixel-service-orchestrator", Replicas: 1, RequestCPU: "100m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServiceParty":        {Name: name, Module: "service.party", Jar: "ServiceParty.jar", DeploymentName: "service-party", ImageName: "hypixel-service-party", Replicas: 1, RequestCPU: "100m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
		"ServicePunishment":   {Name: name, Module: "service.punishment", Jar: "ServicePunishment.jar", DeploymentName: "service-punishment", ImageName: "hypixel-service-punishment", Replicas: 1, RequestCPU: "100m", RequestMemory: "256Mi", LimitCPU: "1", LimitMemory: "768Mi"},
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
		RequestCPU:     "100m",
		RequestMemory:  "256Mi",
		LimitCPU:       "1",
		LimitMemory:    "768Mi",
	}
}

func ServerByType(name string) Server {
	specs := map[string]Server{
		"PROTOTYPE_LOBBY": {ServerType: name, DeploymentName: "prototype-lobby", Replicas: 2, MinReplicas: 2, MaxReplicas: 12, Threshold: 80, RequestCPU: "500m", RequestMemory: "768Mi", LimitCPU: "2", LimitMemory: "2Gi"},
		"BEDWARS_LOBBY":   {ServerType: name, DeploymentName: "bedwars-lobby", Replicas: 2, MinReplicas: 2, MaxReplicas: 12, Threshold: 80, RequestCPU: "500m", RequestMemory: "768Mi", LimitCPU: "2", LimitMemory: "2Gi"},
		"BEDWARS_GAME":    {ServerType: name, DeploymentName: "bedwars-game", Replicas: 1, MinReplicas: 1, MaxReplicas: 64, Threshold: 16, RequestCPU: "750m", RequestMemory: "1Gi", LimitCPU: "3", LimitMemory: "3Gi"},
		"SKYBLOCK_HUB":    {ServerType: name, DeploymentName: "skyblock-hub", Replicas: 2, MinReplicas: 2, MaxReplicas: 20, Threshold: 80, RequestCPU: "750m", RequestMemory: "1Gi", LimitCPU: "3", LimitMemory: "3Gi"},
	}
	if spec, ok := specs[name]; ok {
		return spec
	}
	return Server{
		ServerType:     name,
		DeploymentName: strings.ToLower(strings.ReplaceAll(name, "_", "-")),
		Replicas:       1,
		MinReplicas:    1,
		MaxReplicas:    12,
		Threshold:      32,
		RequestCPU:     "500m",
		RequestMemory:  "768Mi",
		LimitCPU:       "2",
		LimitMemory:    "2Gi",
	}
}
