package net.swofty.types.generic.event.actions.custom;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.List;

public class ActionNewZoneDisplay implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() == null || event.getTo().equals(event.getFrom())) {
            return;
        }

        DatapointStringList discoveredZones = player.getDataHandler().get(
                DataHandler.Data.VISITED_REGIONS,
                DatapointStringList.class
        );
        List<String> discoveredZonesList = discoveredZones.getValue();

        if (discoveredZonesList.contains(event.getTo().getName())) {
            return;
        }

        discoveredZonesList.add(event.getTo().getName());
        discoveredZones.setValue(discoveredZonesList);

        switch (event.getTo()) {
            case VILLAGE -> {
                onNewZone(player, RegionType.VILLAGE,
                        "Purchase items at the Market.",
                        "Visit the Auction House.",
                        "Manage your Coins in the Bank.",
                        "Enchant items at the Library.");
            }
            case AUCTION_HOUSE -> {
                onNewZone(player, RegionType.AUCTION_HOUSE,
                        "Auction off your special items.",
                        "Bid on other player's items.");
            }
            case BANK -> {
                onNewZone(player, RegionType.BANK,
                        "Talk to the Banker.",
                        "Store your coins to keep them safe.",
                        "Earn interest on your coins.");
            }
            case GOLD_MINE -> {
                onNewZone(player, RegionType.GOLD_MINE,
                        "Talk to the Lazy Miner.",
                        "Find the hidden gold mine.");
            }
            case COAL_MINE -> {
                onNewZone(player, RegionType.COAL_MINE,
                        "Mine coal.",
                        "Travel to the Gold Mine.");
            }
            case FARM -> {
                onNewZone(player, RegionType.FARM,
                        "Talk to the farmer.", "Travel to The Barn.");
            }
            case BIRCH_PARK -> {
                onNewZone(player, RegionType.BIRCH_PARK,
                        "Chop down trees.",
                        "Collect all Log types.");
            }
            case FOREST -> {
                onNewZone(player, RegionType.FOREST,
                        "Visit the Lumberjack.",
                        "Chop down trees.",
                        "Travel to the Birch Park.");
            }
            case GRAVEYARD -> {
                onNewZone(player, RegionType.GRAVEYARD,
                        "Fight Zombies.",
                        "Travel to the Spider's Den.",
                        "Talk to Pat.",
                        "Investigate the Catacombs.");
            }
            case BAZAAR_ALLEY -> {
                onNewZone(player, RegionType.BAZAAR_ALLEY,
                        "Buy and sell materials in bulk in the Bazaar.");
            }
            case WILDERNESS -> {
                onNewZone(player, RegionType.WILDERNESS,
                        "Fish.",
                        "Visit the Fisherman's Hut.",
                        "Visit the fairy at the Fairy Pond.",
                        "Discover hidden secrets.");
            }
            case RUINS -> {
                onNewZone(player, RegionType.RUINS,
                        "Explore the ancient ruins.",
                        "Watch out for the guard dogs!");
            }
            case THE_END -> {
                onNewZone(player, RegionType.RUINS,
                        "Talk to the Pearl Dealer.",
                        "Explore the End Shop.",
                        "Kill Endermen.",
                        "Fight Dragons!");
            }
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
