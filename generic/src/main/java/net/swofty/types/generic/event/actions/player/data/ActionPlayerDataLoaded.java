package net.swofty.types.generic.event.actions.player.data;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.scoreboard.Team;
import net.swofty.commons.ServerType;
import net.swofty.packer.SkyBlockTexture;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.MathUtility;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.UUID;

@EventParameters(description = "Join miscellaneous stuff with data being loaded",
        node = EventNodes.PLAYER_DATA,
        requireDataLoaded = true)
public class ActionPlayerDataLoaded extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerSpawnEvent playerLoginEvent = (PlayerSpawnEvent) event;
        SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        if (!playerLoginEvent.isFirstSpawn()) return;

        Thread.startVirtualThread(() -> {
            player.showTitle(Title.title(
                    Component.text(SkyBlockTexture.FULL_SCREEN_BLACK.toString()),
                    Component.empty(),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(300), Duration.ofSeconds(1))
            ));

            Rank rank = player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
            if (rank.isStaff()) {
                CustomGroups.staffMembers.add(player);
            }
            player.sendMessage("§7Sending to server mini" + SkyBlockConst.getServerName() + "...");
            player.sendMessage("§7 ");
            player.sendMessage("§aYour profile is: §e" + player.getDataHandler().get(
                    DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
            player.sendMessage("§8Profile ID: " + player.getProfiles().getCurrentlySelected());

            UUID islandUuid = player.getDataHandler().get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
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
                player.getPetData().updatePetEntityImpl(player);
            }, 20);

            if (SkyBlockConst.isIslandServer()) return;
            PlayerHolograms.spawnAll(player);
            SkyBlockNPC.updateForPlayer(player);
        });
    }
}
