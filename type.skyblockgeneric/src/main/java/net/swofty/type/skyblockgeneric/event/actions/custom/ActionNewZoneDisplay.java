package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.List;

public class ActionNewZoneDisplay implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() == null || event.getTo().equals(event.getFrom())) {
            return;
        }

        DatapointStringList discoveredZones = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.VISITED_REGIONS,
                DatapointStringList.class
        );
        List<String> discoveredZonesList = discoveredZones.getValue();

        if (discoveredZonesList.contains(event.getTo().getName())) {
            return;
        }

        discoveredZonesList.add(event.getTo().getName());
        discoveredZones.setValue(discoveredZonesList);

        switch (event.getTo()) {
            case VILLAGE -> onNewZone(player, RegionType.VILLAGE,
                    "Purchase items at the Market.",
                    "Visit the Auction House.",
                    "Manage your Coins in the Bank.",
                    "Enchant items at the Library.");

            case AUCTION_HOUSE -> onNewZone(player, RegionType.AUCTION_HOUSE,
                    "Auction off your special items.",
                    "Bid on other player's items.");

            case BANK -> onNewZone(player, RegionType.BANK,
                    "Talk to the Banker.",
                    "Store your coins to keep them safe.",
                    "Earn interest on your coins.");

            case DEEP_CAVERNS -> onNewZone(player, RegionType.DEEP_CAVERNS,
                    "Talk to the Lift Operator",
                    "Mine valuable ores.",
                    "Watch out for mobs!");

            case DWARVEN_MINES -> onNewZone(player, RegionType.DWARVEN_MINES,
                    "Mine rare ores.");

            case GUNPOWDER_MINES -> onNewZone(player, RegionType.GUNPOWDER_MINES,
                    "Talk to the Lift Operator.",
                    "Explore the caverns.",
                    "Mine Coal, Iron ore, and Gold ore.");

            case LAPIS_QUARRY -> onNewZone(player, RegionType.LAPIS_QUARRY,
                    "The Lift Operator will now let you travel to the §bLapis Quarry.",
                    "Access to Lapis Lazuli ore.",
                    "Talk to the Lapis Miner.",
                    "Watch out for the zombies!");

            case PIGMENS_DEN -> onNewZone(player, RegionType.PIGMENS_DEN,
                    "The Lift Operator will now let you travel to the §bLapis Quarry.",
                    "Access to Lapis Lazuli ore.",
                    "Talk to the Lapis Miner.",
                    "Watch out for the zombies!");

            case SLIMEHILL -> onNewZone(player, RegionType.SLIMEHILL,
                    "The Lift Operator will now let you travel to the §bSlimehill.",
                    "Mine Emerald ore.",
                    "This area is covered with slimes!");

            case DIAMOND_RESERVE -> onNewZone(player, RegionType.DIAMOND_RESERVE,
                    "The Lift Operator will now let you travel to the §bDiamond Reserve.",
                    "Mine Diamond ore.",
                    "Beware of deadly monsters!");

            case OBSIDIAN_SANCTUARY -> onNewZone(player, RegionType.OBSIDIAN_SANCTUARY,
                    "The Lift Operator will now let you travel to the §bObsidian Sanctuary.",
                    "Mine Obsidian and Diamond.",
                    "Beware of deadly monsters!",
                    "Talk to §dRhys.");

            case GOLD_MINE -> onNewZone(player, RegionType.GOLD_MINE,
                    "Talk to the Lazy Miner.",
                    "Mine for gold, iron, and coal.",
                    "Visit the Iron and Gold Forgers.",
                    "Visit the Blacksmith.",
                    "Talk to Rusty.");

            case COAL_MINE -> onNewZone(player, RegionType.COAL_MINE,
                    "Mine coal.",
                    "Travel to the Gold Mine.");

            case FARM -> onNewZone(player, RegionType.FARM,
                    "Talk to the farmer.", "Travel to The Barn.");

            case BIRCH_PARK -> onNewZone(player, RegionType.BIRCH_PARK,
                    "Talk to Charlie.",
                    "Chop down Birch logs.");

            case FOREST -> onNewZone(player, RegionType.FOREST,
                    "Visit the §aLumber Jack.",
                    "Chop down trees.",
                    "Travel to the §aBirch Park§f.");

            case SPRUCE_WOODS -> onNewZone(player, RegionType.SPRUCE_WOODS,
                    "Chop down Spruce logs.");

            case DARK_THICKET -> onNewZone(player, RegionType.DARK_THICKET,
                    "Chop down Dark Oak Logs.",
                    "Talk to §cRyan §fabout the §6Trial of Fire§f.");

            case TRIALS_OF_FIRE -> onNewZone(player, RegionType.TRIALS_OF_FIRE,
                    "Compete in a §6Trial of Fire§f.");

            case SAVANNA_WOODLAND -> onNewZone(player, RegionType.SAVANNA_WOODLAND,
                    "Chop down Acacia logs.");

            case GRAVEYARD -> onNewZone(player, RegionType.GRAVEYARD,
                    "Fight Zombies.",
                    "Travel to the Spider's Den.",
                    "Talk to Pat.",
                    "Investigate the Catacombs.");

            case BAZAAR_ALLEY -> onNewZone(player, RegionType.BAZAAR_ALLEY,
                    "Buy and sell materials in bulk in the Bazaar.");

            case WILDERNESS -> onNewZone(player, RegionType.WILDERNESS,
                    "Fish.",
                    "Visit the Fisherman's Hut.",
                    "Visit the fairy at the Fairy Pond.",
                    "Discover hidden secrets.");

            case RUINS -> onNewZone(player, RegionType.RUINS,
                    "Explore the ancient ruins.",
                    "Watch out for the guard dogs!");

            case THE_END -> onNewZone(player, RegionType.RUINS,
                    "Talk to the Pearl Dealer.",
                    "Explore the End Shop.",
                    "Kill Endermen.",
                    "Fight Dragons!");

            case ARCHERY_RANGE -> onNewZone(player, RegionType.ARCHERY_RANGE,
                    "Talk to Jax to forge special arrows!");
        }
    }

    public void onNewZone(SkyBlockPlayer player, RegionType zone, String... features) {
        player.sendMessage("");
        player.sendMessage("§6§l NEW AREA DISCOVERED!");
        player.sendMessage("§7  ⏣ " + zone.getColor() + zone.getName());
        player.sendMessage("");
        if (features.length > 0) {
            for (String feature : features) {
                player.sendMessage("§7   ⬛ §f" + feature);
            }
        } else {
            player.sendMessage("§7   ⬛ §cNot much yet!");
        }
        player.sendMessage("");

        player.playSound(Sound.sound()
                .type(Key.key("random.levelup"))
                .volume(1f)
                .build());

        player.showTitle(Title.title(
                Component.text(zone.getColor() + zone.getName()),
                Component.text("§6§lNEW AREA DISCOVERED!"),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));
    }
}
