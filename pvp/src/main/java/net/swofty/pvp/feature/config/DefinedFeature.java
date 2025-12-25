package net.swofty.pvp.feature.config;

import net.swofty.pvp.feature.CombatFeature;
import net.swofty.pvp.feature.FeatureType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represents a feature implementation which has been defined but not yet instantiated.
 * It contains the feature type, all the feature types that this feature depends on and its constructor.
 * The constructor always takes a {@link FeatureConfiguration} which the feature should store,
 * and which it can get the dependencies from once {@link CombatFeature#initDependencies()} is called.
 * <p>
 * A defined feature can also contain a {@link PlayerInit},
 * which describes everything that should happen when a players state is reset (they join, they respawn, etc.).
 * This can for example be used to add certain tags to a player that a feature needs.
 * Since features might be instantiated multiple times, this player init is contained in the feature definition
 * and not every time when it is instantiated.
 *
 * @param <F> the feature class
 */
public class DefinedFeature<F extends CombatFeature> {
	private final FeatureType<?> featureType;
	private final Set<FeatureType<?>> dependencies;
	private final Constructor<F> constructor;
	private final PlayerInit playerInit;
	
	public DefinedFeature(FeatureType<? super F> featureType, Constructor<F> constructor,
	                      FeatureType<?>... dependencies) {
		this(featureType, constructor, null, dependencies);
	}
	
	public DefinedFeature(FeatureType<? super F> featureType, Constructor<F> constructor,
	                      @Nullable PlayerInit playerInit, FeatureType<?>... dependencies) {
		this.featureType = featureType;
		this.dependencies = Set.of(dependencies);
		this.constructor = constructor;
		this.playerInit = playerInit;
	}
	
	public F construct(FeatureConfiguration configuration) {
		// Registers player init
		CombatFeatureRegistry.init(this);
		return constructor.construct(configuration);
	}
	
	public FeatureType<?> featureType() {
		return featureType;
	}
	
	public Set<FeatureType<?>> dependencies() {
		return dependencies;
	}
	
	@Nullable PlayerInit playerInit() {
		return playerInit;
	}
	
	@FunctionalInterface
	public interface Constructor<F> {
		F construct(FeatureConfiguration configuration);
	}
	
	@FunctionalInterface
	public interface PlayerInit {
		void init(Player player, boolean firstInit);
	}
}
