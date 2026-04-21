package net.swofty.type.game.game;

import net.minestom.server.entity.Player;

import java.util.UUID;

public interface GameParticipant {
	UUID getUuid();

	boolean isOnline();

	String getGameId();

	void setGameId(String gameId);

	Player getServerPlayer();
}
