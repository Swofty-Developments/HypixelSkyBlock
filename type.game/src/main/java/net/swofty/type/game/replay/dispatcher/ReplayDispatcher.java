package net.swofty.type.game.replay.dispatcher;

import net.swofty.type.game.replay.ReplayRecorder;

public interface ReplayDispatcher {
	void initialize(ReplayRecorder recorder);

	default void tick() {
	}

	void cleanup();

	String getName();
}
