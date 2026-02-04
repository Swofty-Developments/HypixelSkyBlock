package net.swofty.commons.replay;

public final class ReplayVersion {
	public static final int CURRENT_VERSION = 1;

	public static final int MIN_SUPPORTED_VERSION = 1;

	static {
        //noinspection ConstantValue
        if (CURRENT_VERSION < MIN_SUPPORTED_VERSION) {
			throw new IllegalStateException("Current replay version is less than minimum supported version");
		}
	}

	private ReplayVersion() {
	}
}
