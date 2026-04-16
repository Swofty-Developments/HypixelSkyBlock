rootProject.name = "HypixelSkyBlock"

fun includeIfPresent(projectPath: String) {
    val relativeDir = projectPath.removePrefix(":")
    if (file(relativeDir).isDirectory) {
        include(projectPath)
    }
}

listOf(
    ":packer",
    ":commons",
    ":dungeons",
    ":velocity.extension",
    ":proxy.api",
    ":service.generic",
    ":type.generic",
    ":type.skyblockgeneric",
    ":type.lobby",
    ":spark",
    ":loader",
    ":pvp",
    ":type.prototypelobby",
    ":type.thefarmingislands",
    ":type.spidersden",
    ":type.theend",
    ":type.crimsonisle",
    ":type.goldmine",
    ":type.deepcaverns",
    ":type.dwarvenmines",
    ":type.thepark",
    ":type.galatea",
    ":type.backwaterbayou",
    ":type.jerrysworkshop",
    ":type.island",
    ":type.hub",
    ":type.dungeonhub",
    ":type.bedwarslobby",
    ":type.bedwarsgame",
    ":type.bedwarsconfigurator",
    ":type.murdermysterylobby",
    ":type.murdermysterygame",
    ":type.murdermysteryconfigurator",
    ":type.skywarslobby",
    ":type.skywarsgame",
    ":type.skywarsconfigurator",
    ":type.ravengardgeneric",
    ":type.ravengardlobby",
    ":service.auctionhouse",
    ":service.bazaar",
    ":service.itemtracker",
    ":service.api",
    ":service.datamutex",
    ":service.party",
    ":service.orchestrator",
    ":service.darkauction",
    ":service.friend",
    ":service.punishment",
    ":service.elections",
    ":anticheat"
).forEach(::includeIfPresent)
