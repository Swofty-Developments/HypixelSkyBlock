package net.swofty.pvp.feature.attack;

import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.state.PlayerStateFeature;
import net.swofty.pvp.utils.CombatVersion;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.potion.PotionEffect;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Vanilla implementation of {@link CriticalFeature}
 */
public class VanillaCriticalFeature implements CriticalFeature {
	public static final DefinedFeature<VanillaCriticalFeature> DEFINED = new DefinedFeature<>(
			FeatureType.CRITICAL, VanillaCriticalFeature::new,
			FeatureType.PLAYER_STATE, FeatureType.VERSION
	);
	
	private final FeatureConfiguration configuration;
	
	private PlayerStateFeature playerStateFeature;
	private CombatVersion version;
	
	public VanillaCriticalFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void initDependencies() {
		this.playerStateFeature = configuration.get(FeatureType.PLAYER_STATE);
		this.version = configuration.get(FeatureType.VERSION);
	}
	
	@Override
	public boolean shouldCrit(LivingEntity attacker, AttackValues.PreCritical values) {
		boolean critical = values.strong() && !playerStateFeature.isClimbing(attacker)
				&& attacker.getVelocity().y() < 0 && !attacker.isOnGround()
				&& !attacker.hasEffect(PotionEffect.BLINDNESS)
				&& attacker.getVehicle() == null;
		if (version.legacy()) return critical;
		
		// Not sprinting required for critical in 1.9+
		return critical && !attacker.isSprinting();
	}
	
	@Override
	public float applyToDamage(float damage) {
		if (version.legacy()) {
			return damage + ThreadLocalRandom.current().nextInt((int) (damage / 2 + 2));
		} else {
			return damage * 1.5f;
		}
	}
}
