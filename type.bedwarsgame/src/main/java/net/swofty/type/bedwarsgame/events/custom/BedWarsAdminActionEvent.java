package net.swofty.type.bedwarsgame.events.custom;

import net.minestom.server.event.Event;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;

public record BedWarsAdminActionEvent(BedWarsGame game, String adminName, ActionType actionType,
                                      TeamKey targetTeam) implements Event {
    public enum ActionType {
        FORCE_END_GAME,
        DESTROY_BED,
        RESPAWN_BED,
        FORCE_START
    }

}
