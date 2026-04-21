package net.swofty.pvp.feature.attack;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.potion.PotionEffect;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.fall.FallFeature;
import net.swofty.pvp.feature.state.PlayerStateFeature;
import net.swofty.pvp.utils.CombatVersion;
import net.swofty.pvp.utils.FluidUtil;

/**
 * Vanilla implementation of {@link CriticalFeature}
 */
public class VanillaCriticalFeature implements CriticalFeature {
	public static final DefinedFeature<VanillaCriticalFeature> DEFINED = new DefinedFeature<>(
			FeatureType.CRITICAL, VanillaCriticalFeature::new,
			FeatureType.PLAYER_STATE, FeatureType.VERSION, FeatureType.FALL
	);

	private final FeatureConfiguration configuration;

	private PlayerStateFeature playerStateFeature;
	private CombatVersion version;
	private FallFeature fallFeature;

	public VanillaCriticalFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void initDependencies() {
		this.playerStateFeature = configuration.get(FeatureType.PLAYER_STATE);
		this.version = configuration.get(FeatureType.VERSION);
		this.fallFeature = configuration.get(FeatureType.FALL);
	}

	@Override
	public boolean shouldCrit(LivingEntity attacker, AttackValues.PreCritical values) {
		boolean isFalling = attacker.getVelocity().y() < 0;

		double fallDistance = fallFeature.getFallDistance(attacker);
		boolean hasFallDistance = fallDistance > 0.0;

		boolean critical = values.strong()
				&& !attacker.isOnGround()
				&& (isFalling || hasFallDistance)
				&& !playerStateFeature.isClimbing(attacker)
				&& !FluidUtil.isTouchingWater(attacker)
				&& !attacker.hasEffect(PotionEffect.BLINDNESS)
				&& attacker.getVehicle() == null;

		if (version.legacy()) return critical;

		// Not sprinting required for critical in 1.9+
		return critical && !attacker.isSprinting();
	}

	@Override
	public float applyToDamage(float damage) {
		return damage * 1.5f;
	}
}
