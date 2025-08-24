package net.swofty.pvp.utils;

import net.swofty.pvp.feature.CombatFeature;

public final class CombatVersion implements CombatFeature {
	public static CombatVersion MODERN = new CombatVersion(false);
	public static CombatVersion LEGACY = new CombatVersion(true);
	
	private final boolean legacy;
	
	CombatVersion(boolean legacy) {
		this.legacy = legacy;
	}
	
	public boolean modern() {
		return !legacy;
	}
	
	public boolean legacy() {
		return legacy;
	}
	
	public static CombatVersion fromLegacy(boolean legacy) {
		return legacy ? LEGACY : MODERN;
	}
	
	@Override
	public String toString() {
		return "CombatVersion[legacy=" + legacy + "]";
	}
	
}
