package net.swofty.types.generic.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

import java.util.UUID;

@EventParameters(description = "Join miscellaneous stuff with data being loaded",
        node = EventNodes.PLAYER,
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

        if (SkyBlockConst.isIslandServer()) return;
        PlayerHolograms.spawnAll(player);
        SkyBlockNPC.updateForPlayer(player);
    }
}
