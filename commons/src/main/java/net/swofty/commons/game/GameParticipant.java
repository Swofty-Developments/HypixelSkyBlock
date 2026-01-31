package net.swofty.commons.game;

import java.util.UUID;

public interface GameParticipant {
	UUID getUuid();

	String getUsername();

	boolean isOnline();

	String getGameId();

	void setGameId(String gameId);
}
