package net.swofty.type.game.game.team;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleGameTeam implements GameTeam {
	private final String id;
	private final String name;
	private final String colorCode;
	private final Set<UUID> playerIds = ConcurrentHashMap.newKeySet();

	public SimpleGameTeam(String id, String name, String colorCode) {
		this.id = id;
		this.name = name;
		this.colorCode = colorCode;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getColorCode() {
		return colorCode;
	}

	@Override
	public Collection<UUID> getPlayerIds() {
		return Collections.unmodifiableSet(playerIds);
	}

	@Override
	public void addPlayer(UUID playerId) {
		playerIds.add(playerId);
	}

	@Override
	public void removePlayer(UUID playerId) {
		playerIds.remove(playerId);
	}
}
