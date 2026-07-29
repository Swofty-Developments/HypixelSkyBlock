package installer

const (
	Version           = "2.1.0"
	GitHubRepo        = "Swofty-Developments/HypixelSkyBlock"
	GitHubAPI         = "https://api.github.com/repos/" + GitHubRepo
	LimboAssetsURL    = "https://files.catbox.moe/oybade.zip"
	StateFileName     = ".state.json"
	DefaultInstallSub = ".hypixel-skyblock"
)

var SkyBlockServers = []string{
	"SKYBLOCK_HUB",
	"SKYBLOCK_ISLAND",
	"SKYBLOCK_SPIDERS_DEN",
	"SKYBLOCK_THE_END",
	"SKYBLOCK_CRIMSON_ISLE",
	"SKYBLOCK_DUNGEON_HUB",
	"SKYBLOCK_THE_FARMING_ISLANDS",
	"SKYBLOCK_GOLD_MINE",
	"SKYBLOCK_DEEP_CAVERNS",
	"SKYBLOCK_DWARVEN_MINES",
	"SKYBLOCK_THE_PARK",
	"SKYBLOCK_GALATEA",
	"SKYBLOCK_BACKWATER_BAYOU",
	"SKYBLOCK_JERRYS_WORKSHOP",
}

var RequiredServers = []string{"PROTOTYPE_LOBBY"}

var MinigameServers = []string{
	"BEDWARS_LOBBY",
	"BEDWARS_GAME",
	"BEDWARS_CONFIGURATOR",
	"MURDER_MYSTERY_LOBBY",
	"MURDER_MYSTERY_GAME",
	"MURDER_MYSTERY_CONFIGURATOR",
	"SKYWARS_LOBBY",
	"SKYWARS_GAME",
	"SKYWARS_CONFIGURATOR",
	"RAVENGARD_LOBBY",
}

var AllServices = []string{
	"ServiceDataMutex",
	"ServiceParty",
	"ServiceAPI",
	"ServiceAuctionHouse",
	"ServiceBazaar",
	"ServiceItemTracker",
	"ServiceDarkAuction",
	"ServiceOrchestrator",
	"ServiceFriend",
}

var RequiredServices = []string{"ServiceDataMutex", "ServiceParty"}

var DefaultServices = []string{
	"ServiceDataMutex",
	"ServiceParty",
	"ServiceAPI",
	"ServiceAuctionHouse",
	"ServiceBazaar",
	"ServiceItemTracker",
}
