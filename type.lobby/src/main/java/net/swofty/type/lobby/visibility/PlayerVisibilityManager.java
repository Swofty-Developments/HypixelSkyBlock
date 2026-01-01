package net.swofty.type.lobby.visibility;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.hologram.HologramEntity;
import net.swofty.type.generic.entity.npc.impl.NPCEntityImpl;
import net.swofty.type.generic.user.HypixelPlayer;

public class PlayerVisibilityManager {

    public static void setupViewerRule(HypixelPlayer player, boolean showPlayers) {
        player.updateViewerRule((entity) -> {
            if (entity instanceof NPCEntityImpl) return true;
            if (entity instanceof HologramEntity) return true;
            if (entity.getEntityType() == EntityType.ITEM_FRAME) return true;
            return (entity instanceof Player) == showPlayers;
        });
    }

    public static void setupViewerRuleFromToggle(HypixelPlayer player) {
        boolean showPlayers = player.getToggles().get(DatapointToggles.Toggles.ToggleType.LOBBY_SHOW_PLAYERS);
        setupViewerRule(player, showPlayers);
    }

    public static boolean toggleVisibility(HypixelPlayer player) {
        boolean newState = player.getToggles().inverse(DatapointToggles.Toggles.ToggleType.LOBBY_SHOW_PLAYERS);
        setupViewerRule(player, newState);
        return newState;
    }
}
