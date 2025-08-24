package net.swofty.pvp.feature.explosion;

import net.swofty.pvp.entity.explosion.TntEntity;
import net.swofty.pvp.events.ExplosivePrimeEvent;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla implementation of {@link ExplosionFeature}
 * <p>
 * Provides an explosion supplier which can be registered to an instance,
 * see {@link VanillaExplosionFeature#getExplosionSupplier()}.
 */
public class VanillaExplosionFeature implements ExplosionFeature {
	public static final DefinedFeature<VanillaExplosionFeature> DEFINED = new DefinedFeature<>(
			FeatureType.EXPLOSION, VanillaExplosionFeature::new,
			FeatureType.ENCHANTMENT
	);
	
	private final FeatureConfiguration configuration;
	
	private VanillaExplosionSupplier explosionSupplier;
	
	public VanillaExplosionFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void initDependencies() {
		this.explosionSupplier = new VanillaExplosionSupplier(this, configuration.get(FeatureType.ENCHANTMENT));
	}
	
	@Override
	public VanillaExplosionSupplier getExplosionSupplier() {
		return explosionSupplier;
	}
	
	@Override
	public void primeExplosive(Instance instance, Point blockPosition, @NotNull IgnitionCause cause, int fuse) {
		ExplosivePrimeEvent event = new ExplosivePrimeEvent(instance, blockPosition, cause, fuse);
		EventDispatcher.callCancellable(event, () -> {
			TntEntity entity = new TntEntity(cause.causingEntity());
			entity.setFuse(event.getFuse());
			entity.setInstance(instance, blockPosition.add(0.5, 0, 0.5));
			entity.getViewersAsAudience().playSound(Sound.sound(
					SoundEvent.ENTITY_TNT_PRIMED, Sound.Source.BLOCK,
					1.0f, 1.0f
			), entity);
		});
	}
}
