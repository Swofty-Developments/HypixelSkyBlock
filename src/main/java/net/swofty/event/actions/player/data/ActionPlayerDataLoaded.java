package net.swofty.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.data.datapoints.DatapointString;
import net.swofty.data.datapoints.DatapointUUID;
import net.swofty.entity.hologram.PlayerHolograms;
import net.swofty.entity.npc.SkyBlockNPC;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.CustomGroups;
import net.swofty.user.categories.Rank;

import java.util.UUID;

@EventParameters(description = "Join miscellaneous stuff with data being loaded",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionPlayerDataLoaded extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;
        SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        Rank rank = player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
        if (rank.isStaff()) {
            CustomGroups.staffMembers.add(player);
        }

        player.sendMessage("§aYour profile is: §e" + player.getDataHandler().get(
                DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
        player.sendMessage("§8Profile ID: " + player.getProfiles().getCurrentlySelected());

        UUID islandUuid = player.getDataHandler().get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        if (islandUuid != player.getProfiles().getCurrentlySelected())
            player.sendMessage("§8Island ID: " + islandUuid);

        player.setHearts(player.getMaxHealth());

        PlayerHolograms.spawnAll(player);
        SkyBlockNPC.updateForPlayer(player);
    }
}
