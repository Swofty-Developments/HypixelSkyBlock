package net.swofty.pvp.feature.config;

import net.swofty.pvp.feature.CombatFeature;
import net.swofty.pvp.feature.CombatFeatureSet;
import net.swofty.pvp.feature.CombatFeatures;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.provider.DifficultyProvider;
import net.swofty.pvp.utils.CombatVersion;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A configuration of combat features which can be used to easily resolve dependencies to other combat features.
 * <p>
 * Whereas {@link CombatFeatureSet} contains the full set of feature instances with resolved dependencies,
 * this class can be used to add {@link DefinedFeature} instances (features which haven't yet been instantiated).
 * Vanilla versions of these defined features can be found in {@link CombatFeatures}.
 * <p>
 * This class contains methods to easily configure the combat version and difficulty provider,
 * see {@link CombatConfiguration#version(CombatVersion)} and {@link CombatConfiguration#difficulty(DifficultyProvider)}.
 * <p>
 * When calling {@link CombatConfiguration#build()}, it resolves all the dependencies
 * and turns this configuration into a {@link CombatFeatureSet}.
 */
public class CombatConfiguration {
	private final Map<FeatureType<?>, ConstructableFeature> features = new HashMap<>();
	
	public final CombatConfiguration addAll(Collection<DefinedFeature<?>> constructors) {
		for (DefinedFeature<?> constructor : constructors) {
			add(constructor);
		}
		return this;
	}
	
	public final CombatConfiguration addAll(DefinedFeature<?>... constructors) {
		for (DefinedFeature<?> constructor : constructors) {
			add(constructor);
		}
		return this;
	}
	
	public CombatConfiguration version(CombatVersion version) {
		return add(FeatureType.VERSION, version);
	}
	
	/**
	 * @deprecated use {@link #version(CombatVersion)} instead
	 */
	@Deprecated
	public CombatConfiguration legacy(boolean legacy) {
		return version(CombatVersion.fromLegacy(legacy));
	}
	
	public CombatConfiguration difficulty(DifficultyProvider difficulty) {
		return add(FeatureType.DIFFICULTY, difficulty);
	}
	
	/**
	 * Adds a feature to the configuration. This will overwrite any existing feature of this type.
	 *
	 * @param feature the feature to add
	 * @return this
	 */
	public CombatConfiguration add(ConstructableFeature feature) {
		features.put(feature.type, feature);
		return this;
	}
	
	/**
	 * Adds a feature to the configuration. This will overwrite any existing feature of this type.
	 *
	 * @param type the type of the feature
	 * @param feature the feature to add
	 * @return this
	 */
	public CombatConfiguration add(FeatureType<?> type, CombatFeature feature) {
		return add(wrap(type, feature));
	}
	
	/**
	 * Adds a feature to the configuration. This will overwrite any existing feature of this type.
	 * Any dependencies the feature might have will first be looked up inside the override configuration.
	 *
	 * @param constructor the defined feature
	 * @param override the override configuration
	 * @return this
	 */
	public CombatConfiguration add(DefinedFeature<?> constructor, FeatureConfiguration override) {
		return add(wrap(constructor, override));
	}
	
	/**
	 * Adds a feature to the configuration. This will overwrite any existing feature of this type.
	 * Any dependencies the feature might have will first be looked up inside the override configuration.
	 *
	 * @param constructor the type of the feature
	 * @param override list of features to override the configuration of the base feature
	 * @return this
	 */
	public CombatConfiguration add(DefinedFeature<?> constructor, DefinedFeature<?>... override) {
		return add(wrap(constructor, override));
	}
	
	public CombatConfiguration remove(FeatureType<?> type) {
		features.remove(type);
		return this;
	}
	
	public static ConstructableFeature wrap(FeatureType<?> type, CombatFeature feature) {
		return new ConstructedFeature(type, feature);
	}
	
	public static ConstructableFeature wrap(DefinedFeature<?> constructor, FeatureConfiguration override) {
		Set<ConstructableFeature> overrideSet = new HashSet<>();
		override.forEach((k, v) -> overrideSet.add(wrap(k, v)));
		return wrap(constructor, overrideSet);
	}
	
	public static ConstructableFeature wrap(DefinedFeature<?> constructor, DefinedFeature<?>... override) {
		Set<ConstructableFeature> overrideSet = new HashSet<>();
		for (DefinedFeature<?> overrideFeature : override) {
			overrideSet.add(wrap(overrideFeature));
		}
		
		return wrap(constructor, overrideSet);
	}
	
	public static ConstructableFeature wrap(DefinedFeature<?> constructor, Set<ConstructableFeature> override) {
		Map<FeatureType<?>, ConstructableFeature> overrideMap = new HashMap<>();
		
		for (ConstructableFeature overrideFeature : override) {
			overrideMap.put(overrideFeature.type, overrideFeature);
			
			if (!constructor.dependencies().contains(overrideFeature.type))
				throw new RuntimeException("Feature " + constructor.featureType().name()
						+ " does not require a " + overrideFeature.type.name() + " feature");
		}
		
		return new LazyFeatureInit(constructor, overrideMap);
	}
	
	/**
	 * Resolves all the dependencies and turns this configuration into a {@link CombatFeatureSet}.
	 *
	 * @return the combat feature set
	 */
	public CombatFeatureSet build() {
		CombatFeatureSet result = new CombatFeatureSet();
		
		//List<ConstructableFeature> buildOrder = getBuildOrder();
		
		for (ConstructableFeature feature : features.values()) {
			CombatFeature currentResult = feature.construct(result);
			result.add(feature.type, currentResult);
		}
		
		result.initDependencies();
		
		return result;
	}
	
	private @Nullable ConstructableFeature getFeatureOf(FeatureType<?> type) {
		return features.get(type);
	}
	
	public sealed abstract static class ConstructableFeature {
		private final FeatureType<?> type;
		
		public ConstructableFeature(FeatureType<?> type) {
			this.type = type;
		}
		
		abstract CombatFeature construct(FeatureConfiguration configuration);
	}
	
	private static final class ConstructedFeature extends ConstructableFeature {
		private final CombatFeature feature;
		
		private ConstructedFeature(FeatureType<?> type, CombatFeature feature) {
			super(type);
			this.feature = feature;
		}
		
		@Override
		CombatFeature construct(FeatureConfiguration configuration) {
			return feature;
		}
	}
	
	private static final class LazyFeatureInit extends ConstructableFeature {
		private final DefinedFeature<?> constructor;
		private final Map<FeatureType<?>, ConstructableFeature> override;
		
		public LazyFeatureInit(DefinedFeature<?> constructor, Map<FeatureType<?>, ConstructableFeature> override) {
			super(constructor.featureType());
			this.constructor = constructor;
			this.override = override;
		}
		
		public @Nullable ConstructableFeature getOverrideOf(FeatureType<?> featureType) {
			return override.get(featureType);
		}
		
		@Override
		CombatFeature construct(FeatureConfiguration configuration) {
			FeatureConfiguration local = configuration.overlay();
			override.forEach((k, v) -> local.add(k, v.construct(configuration)));
			
			return constructor.construct(local);
		}
	}
}
