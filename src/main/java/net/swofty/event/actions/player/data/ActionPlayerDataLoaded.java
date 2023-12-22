package net.swofty.event.actions.player.data;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.entity.hologram.PlayerHolograms;
import net.swofty.entity.npc.SkyBlockNPC;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.categories.CustomGroups;
import net.swofty.user.categories.Rank;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Join miscellaneous stuff with data being loaded",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionPlayerDataLoaded extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;
        SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        Rank rank = player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
        if (rank.isStaff()) {
            CustomGroups.staffMembers.add(player);
        }

        Team team = new TeamBuilder(player.getUsername(), MinecraftServer.getTeamManager())
                .prefix(Component.text(rank.getPrefix()))
                .teamColor(rank.getTextColor())
                .build();
        player.setTeam(team);
        player.getTeam().sendUpdatePacket();

        player.sendMessage("§eWelcome to §aHypixel SkyBlock§e!");
        player.setHearts(player.getMaxHealth());

        PlayerHolograms.spawnAll(player);
        SkyBlockNPC.updateForPlayer(player);
    }
}
