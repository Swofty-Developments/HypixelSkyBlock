package net.swofty.type.skyblockgeneric.event.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.swofty.type.skyblockgeneric.race.Race;

@Getter
@AllArgsConstructor
public class ActionPlayerFinishRace implements PlayerEvent {

	private final Player player;
	private final Race race;
	private final long time;

}
