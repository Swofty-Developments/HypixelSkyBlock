package net.swofty.commons.replay.dispatcher;

import net.swofty.commons.replay.ReplayRecorder;

public interface ReplayDispatcher {
	void initialize(ReplayRecorder recorder);

	default void tick() {
	}

	void cleanup();

	String getName();
}
