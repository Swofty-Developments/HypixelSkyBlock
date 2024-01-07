package net.swofty.commons.skyblock.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointRank;
import net.swofty.commons.skyblock.data.datapoints.DatapointString;
import net.swofty.commons.skyblock.data.datapoints.DatapointUUID;
import net.swofty.commons.skyblock.entity.hologram.PlayerHolograms;
import net.swofty.commons.skyblock.entity.npc.SkyBlockNPC;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.CustomGroups;
import net.swofty.commons.skyblock.user.categories.Rank;

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
