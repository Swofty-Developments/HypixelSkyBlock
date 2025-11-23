package net.swofty.pvp.potion.effect;

import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.PotionEffect;

public class AbsorptionPotionEffect extends CombatPotionEffect {
	public AbsorptionPotionEffect() {
		super(PotionEffect.ABSORPTION);
	}
	
	@Override
	public void onApplied(LivingEntity entity, int amplifier, CombatVersion version) {
		if (entity instanceof Player player) {
			player.setAdditionalHearts(player.getAdditionalHearts() + (float) (4 * (amplifier + 1)));
		}
		
		super.onApplied(entity, amplifier, version);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, int amplifier, CombatVersion version) {
		if (entity instanceof Player player) {
			player.setAdditionalHearts(Math.max(player.getAdditionalHearts() - (float) (4 * (amplifier + 1)), 0));
		}
		
		super.onRemoved(entity, amplifier, version);
	}
}
