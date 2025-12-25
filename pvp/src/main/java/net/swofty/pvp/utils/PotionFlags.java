package net.swofty.pvp.utils;

public class PotionFlags {
	public static byte create(boolean ambient, boolean particles, boolean icon) {
		byte flags = 0;
		if (ambient) {
			flags = (byte) (flags | 0x01);
		}
		if (particles) {
			flags = (byte) (flags | 0x02);
		}
		if (icon) {
			flags = (byte) (flags | 0x04);
		}
		return flags;
	}
	
	private static final byte DEFAULT_FLAGS = create(false, true, true);
	public static byte defaultFlags() {
		return DEFAULT_FLAGS;
	}
}
