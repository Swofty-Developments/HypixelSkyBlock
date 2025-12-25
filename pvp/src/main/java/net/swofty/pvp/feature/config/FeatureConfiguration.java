package net.swofty.pvp.feature.config;

import net.swofty.pvp.feature.CombatFeature;
import net.swofty.pvp.feature.FeatureType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A (mutable) configuration for a feature.
 * It contains dependencies for the feature, accessible by their feature type.
 * See {@link FeatureConfiguration#get(FeatureType)}
 */
public class FeatureConfiguration {
	protected final Map<FeatureType<?>, CombatFeature> combatFeatures = new HashMap<>();
	
	public FeatureConfiguration() {}
	
	/**
	 * Adds the specified feature as the specified type to this configuration.
	 * This WILL overwrite any previous feature associated with the specified type.
	 * <p>
	 * This method is not generified to avoid internal complication.
	 * DO NOT add a feature of a different type than specified, this WILL break things.
	 *
	 * @param type the feature type to add the feature as
	 * @param feature the feature to add
	 * @return this configuration
	 */
	public FeatureConfiguration add(FeatureType<?> type, CombatFeature feature) {
		combatFeatures.put(type, feature);
		return this;
	}
	
	/**
	 * Gets the feature which has been associated with the specified type in this configuration.
	 * If there is none present, the default feature for this type will be returned.
	 *
	 * @param type the feature type
	 * @return the feature associated with the feature type, or the default feature for this type
	 * @param <T> the feature class
	 */
	@SuppressWarnings("unchecked")
	public <T extends CombatFeature> @NotNull T get(FeatureType<T> type) {
		return (T) combatFeatures.getOrDefault(type, type.defaultFeature());
	}
	
	/**
	 * Gets the feature which has been associated with the specified type in this configuration.
	 * Will yield null if there is no associated feature.
	 *
	 * @param type the feature type
	 * @return the feature associated with the feature type, or null if there is none present
	 * @param <T> the feature class
	 */
	@SuppressWarnings("unchecked")
	<T extends CombatFeature> @Nullable T getDirect(FeatureType<T> type) {
		return (T) combatFeatures.get(type);
	}
	
	public Collection<CombatFeature> listFeatures() {
		return combatFeatures.values();
	}
	
	public Set<FeatureType<?>> listTypes() {
		return combatFeatures.keySet();
	}
	
	public int size() {
		return combatFeatures.size();
	}
	
	void forEach(BiConsumer<FeatureType<?>, CombatFeature> consumer) {
		combatFeatures.forEach(consumer);
	}
	
	FeatureConfiguration overlay() {
		return new Overlay(this);
	}
	
	private static class Overlay extends FeatureConfiguration {
		private final FeatureConfiguration backing;
		
		public Overlay(FeatureConfiguration backing) {
			this.backing = backing;
		}
		
		@Override
		public <T extends CombatFeature> @NotNull T get(FeatureType<T> type) {
			if (super.combatFeatures.containsKey(type)) {
				return super.get(type);
			} else {
				return backing.get(type);
			}
		}
	}
	
	public static FeatureConfiguration of(FeatureType<?> type1, CombatFeature feature1) {
		return new FeatureConfiguration()
				.add(type1, feature1);
	}
	
	public static FeatureConfiguration of(FeatureType<?> type1, CombatFeature feature1,
	                                      FeatureType<?> type2, CombatFeature feature2) {
		return new FeatureConfiguration()
				.add(type1, feature1)
				.add(type2, feature2);
	}
	
	public static FeatureConfiguration of(FeatureType<?> type1, CombatFeature feature1,
	                                      FeatureType<?> type2, CombatFeature feature2,
	                                      FeatureType<?> type3, CombatFeature feature3) {
		return new FeatureConfiguration()
				.add(type1, feature1)
				.add(type2, feature2)
				.add(type3, feature3);
	}
	
	public static FeatureConfiguration of(FeatureType<?> type1, CombatFeature feature1,
	                                      FeatureType<?> type2, CombatFeature feature2,
	                                      FeatureType<?> type3, CombatFeature feature3,
	                                      FeatureType<?> type4, CombatFeature feature4) {
		return new FeatureConfiguration()
				.add(type1, feature1)
				.add(type2, feature2)
				.add(type3, feature3)
				.add(type4, feature4);
	}
}
