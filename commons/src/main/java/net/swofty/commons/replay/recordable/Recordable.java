package net.swofty.commons.replay.recordable;

import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;

/**
 * Represents a recordable event or state in a replay.
 */
public interface Recordable {
	RecordableType getType();

	void write(ReplayDataWriter writer) throws IOException;

	void read(ReplayDataReader reader) throws IOException;

	int getTick();

	void setTick(int tick);

	default int estimatedSize() {
		return 16;
	}

	default boolean isEntityState() {
		return false;
	}

	default int getEntityId() {
		return -1;
	}
}
