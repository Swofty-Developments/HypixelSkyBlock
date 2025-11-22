package net.swofty.pvp.potion.item;

import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionType;

import java.util.List;

public class CombatPotionType {
	private final PotionType potionType;
	private final List<Potion> effects;
	private List<Potion> legacyEffects;
	
	public CombatPotionType(PotionType potionType, Potion... effects) {
		this.potionType = potionType;
		this.effects = List.of(effects);
	}
	
	public CombatPotionType legacy(Potion... effects) {
		legacyEffects = List.of(effects);
		return this;
	}
	
	public PotionType getPotionType() {
		return potionType;
	}
	
	public List<Potion> getEffects(CombatVersion version) {
		if (legacyEffects == null) return effects;
		return version.legacy() ? legacyEffects : effects;
	}
}
