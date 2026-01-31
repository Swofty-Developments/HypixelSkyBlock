package net.swofty.commons.replay.recordable;

public abstract class AbstractRecordable implements Recordable {
	protected int tick;

	@Override
	public int getTick() {
		return tick;
	}

	@Override
	public void setTick(int tick) {
		this.tick = tick;
	}
}
