package net.swofty.type.lobby;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.swofty.commons.party.FullParty;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;

/**
 * Utility class for validating whether a player can join a game queue.
 * Handles common validation checks like existing searches and party status.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameQueueValidator {

    /**
     * Validates whether a player can join a game queue.
     * Checks if the player is already searching or if they're in a party
     * and not the leader.
     *
     * @param player The player to validate
     * @return true if the player can queue for a game, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canPlayerQueue(HypixelPlayer player) {
        if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
            player.sendMessage("§cYou are already searching for a game!");
            return false;
        }

        if (PartyManager.isInParty(player)) {
            FullParty party = PartyManager.getPartyFromPlayer(player);
            if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                return false;
            }
        }

        return true;
    }
}
