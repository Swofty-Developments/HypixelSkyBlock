package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.swofty.packer.SkyBlockTexture;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.SkyBlockDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointUUID;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.CustomGroups;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.generic.warps.TravelScrollIslands;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class ActionPlayerDataLoaded implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!player.hasAuthenticated) return;
        if (!event.isFirstSpawn()) return;

        Logger.info("Loading SkyBlock data for spawned player: " + event.getPlayer().getUsername() + "...");

        UUID playerUuid = player.getUuid();
        PlayerProfiles profiles = player.getProfiles();
        SkyBlockDataHandler handler = player.getDataHandler();

        // Handle coop synchronization
        if (handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
            CoopDatabase.Coop coop = CoopDatabase.getFromMember(playerUuid);
            if (coop.members().size() != 1) {
                SkyBlockDataHandler data;

                if (SkyBlockGenericLoader.getLoadedPlayers().stream().anyMatch(player1 -> coop.members().contains(player1.getUuid()))) {
                    // A coop member is online, use their data
                    HypixelPlayer otherCoopMember = SkyBlockGenericLoader.getLoadedPlayers().stream()
                            .filter(player1 -> coop.members().contains(player1.getUuid())).findFirst().get();
                    data = otherCoopMember.getDataHandler();
                } else {
                    // No coop members are online, use the first member's data
                    UUID finalProfileId = profiles.getCurrentlySelected();
                    data = SkyBlockDataHandler.createFromDocument(
                            new ProfilesDatabase(coop.memberProfiles().stream()
                                    .filter(uuid -> !uuid.equals(finalProfileId))
                                    .findFirst().get().toString()).getDocument()
                    );
                }

                data.getCoopValues().forEach((key, value) -> {
                    handler.getDatapoint(key).setValueBypassCoop(value);
                });
            }
        }

        player.sendMessage("");

        // Run onLoad callbacks for SkyBlock functionality
        handler.runOnLoad(player);

        // Manually call region event with a delay
        MathUtility.delay(() -> {
            SkyBlockRegion playerRegion = player.getRegion();
            if (playerRegion != null && player.isOnline())
                HypixelEventHandler.callCustomEvent(new PlayerRegionChangeEvent(
                        player,
                        null,
                        playerRegion.getType()
                ));
        }, 50);

        Logger.info("Successfully loaded SkyBlock data for: " + player.getUsername());

        Thread.startVirtualThread(() -> {
            player.showTitle(Title.title(
                    Component.text(SkyBlockTexture.FULL_SCREEN_BLACK.toString()),
                    Component.empty(),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(300), Duration.ofSeconds(1))
            ));

            Rank rank = player.getRank();
            if (rank.isStaff()) {
                CustomGroups.staffMembers.add(player);
            }

            player.sendMessage("§7 ");
            player.sendMessage("§aYou are playing on profile: §e" + player.getDataHandler().get(
                    SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
            player.sendMessage("§8Profile ID: " + player.getProfiles().getCurrentlySelected());

            UUID islandUuid = player.getDataHandler().get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
            if (islandUuid != player.getProfiles().getCurrentlySelected())
                player.sendMessage("§8Island ID: " + islandUuid);
            player.sendMessage(" ");

            player.health = player.getMaxHealth();
            player.sendPacket(new UpdateHealthPacket(
                    (player.health / player.getMaxHealth()) * 20,
                    20,
                    20));

            MinecraftServer.getBossBarManager().removeAllBossBars(player);
            MathUtility.delay(() -> {
                if (!player.isOnline()) return;
                player.getPetData().updatePetEntityImpl(player);
            }, 20);

            TravelScrollIslands island = TravelScrollIslands.getFromType(HypixelConst.getTypeLoader().getType());
            if (island != null) {
                List<String> visitedIslands = player.getDataHandler().get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).getValue();
                if (!visitedIslands.contains(island.getInternalName())) {
                    visitedIslands.add(island.getInternalName());
                    player.getDataHandler().get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).setValue(visitedIslands);
                }
            }

            if (HypixelConst.isIslandServer()) return;
            PlayerHolograms.spawnAll(player);
            HypixelNPC.updateForPlayer(player);
        });
    }
}
