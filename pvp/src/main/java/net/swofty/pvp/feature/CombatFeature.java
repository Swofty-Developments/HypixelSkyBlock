package net.swofty.pvp.feature;

import net.swofty.pvp.feature.config.FeatureConfiguration;

/**
 * The base interface of all combat features. See the readme for a more sophisticated explanation.
 */
public interface CombatFeature {
	/**
	 * Signals to this feature that the {@link FeatureConfiguration} it's been given in the constructor is ready to use.
	 * Most features will use it to get instances of all their dependencies.
	 * <p>
	 * This has to exist because of complications with recursive dependencies.
	 */
	default void initDependencies() {}
}
