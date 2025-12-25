package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class GameEventManager {
	private final Game game;
	private final List<GameEventPosition> orderedEvents = new ArrayList<>(EnumSet.allOf(GameEventPosition.class));
	private final List<Listener> listeners = new ArrayList<>();
	@Getter
	private GameEventPosition currentEvent = GameEventPosition.BEGIN;
	@Getter
	private long secondsUntilNextEvent;
	private int eventIndex; // index within orderedEvents
	private Task ticker;

	public GameEventManager(Game game) {
		this.game = game;
		this.eventIndex = 0;
		this.secondsUntilNextEvent = getNextEvent() != null ? getNextEvent().getTime() : 0;
	}

	public void start() {
		if (ticker != null) return;
		ticker = MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;
			if (secondsUntilNextEvent > 0) {
				secondsUntilNextEvent--;
				return;
			}

			// advance to the next event
			GameEventPosition prev = currentEvent;
			GameEventPosition next = getNextEvent();
			if (next == null) return; // no more events

			currentEvent = next;
			eventIndex++;
			secondsUntilNextEvent = getNextEvent() != null ? getNextEvent().getTime() : 0;

			for (Listener l : listeners) {
				l.onEventChange(prev, currentEvent);
			}
		}).delay(TaskSchedule.seconds(1)).repeat(TaskSchedule.seconds(1)).schedule();
	}

	public void stop() {
		if (ticker != null) {
			ticker.cancel();
			ticker = null;
		}
	}

	public GameEventPosition getNextEvent() {
		int nextIdx = eventIndex + 1;
		return nextIdx < orderedEvents.size() ? orderedEvents.get(nextIdx) : null;
	}

	public long getDiamondDelaySeconds() {
		return currentEvent.getDiamondSeconds();
	}

	public long getEmeraldDelaySeconds() {
		return currentEvent.getEmeraldSeconds();
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public interface Listener {
		void onEventChange(GameEventPosition previous, GameEventPosition current);
	}
}
