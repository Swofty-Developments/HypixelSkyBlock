package net.swofty.pvp.potion.effect;

import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.potion.PotionEffect;

public class GlowingPotionEffect extends CombatPotionEffect {
	public GlowingPotionEffect() {
		super(PotionEffect.GLOWING);
	}
	
	@Override
	public void onApplied(LivingEntity entity, int amplifier, CombatVersion version) {
		entity.setGlowing(true);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, int amplifier, CombatVersion version) {
		entity.setGlowing(false);
	}
}
