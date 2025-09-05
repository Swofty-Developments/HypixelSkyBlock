package net.swofty.pvp.potion.effect;

import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.potion.PotionEffect;

public class HealthBoostPotionEffect extends CombatPotionEffect {
	public HealthBoostPotionEffect() {
		super(PotionEffect.HEALTH_BOOST);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, int amplifier, CombatVersion version) {
		super.onRemoved(entity, amplifier, version);
		
		if (entity.getHealth() > entity.getAttributeValue(Attribute.MAX_HEALTH)) {
			entity.setHealth((float) entity.getAttributeValue(Attribute.MAX_HEALTH));
		}
	}
}
