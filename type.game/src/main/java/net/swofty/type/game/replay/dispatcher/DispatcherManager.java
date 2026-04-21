package net.swofty.type.game.replay.dispatcher;

import net.swofty.type.game.replay.ReplayRecorder;

import java.util.ArrayList;
import java.util.List;

public class DispatcherManager {
	private final ReplayRecorder recorder;
	private final List<ReplayDispatcher> dispatchers = new ArrayList<>();

	public DispatcherManager(ReplayRecorder recorder) {
		this.recorder = recorder;
	}

	public void register(ReplayDispatcher dispatcher) {
		dispatcher.initialize(recorder);
		dispatchers.add(dispatcher);
	}

	public void tick() {
		for (ReplayDispatcher dispatcher : dispatchers) {
			try {
				dispatcher.tick();
			} catch (Exception e) {
				// Log but don't crash
				org.tinylog.Logger.error(e, "Dispatcher {} tick failed", dispatcher.getName());
			}
		}
	}

	public void cleanup() {
		for (ReplayDispatcher dispatcher : dispatchers) {
			try {
				dispatcher.cleanup();
			} catch (Exception e) {
				org.tinylog.Logger.error(e, "Dispatcher {} cleanup failed", dispatcher.getName());
			}
		}
		dispatchers.clear();
	}

	@SuppressWarnings("unchecked")
	public <T extends ReplayDispatcher> T getDispatcher(Class<T> type) {
		for (ReplayDispatcher dispatcher : dispatchers) {
			if (type.isInstance(dispatcher)) {
				return (T) dispatcher;
			}
		}
		return null;
	}
}
