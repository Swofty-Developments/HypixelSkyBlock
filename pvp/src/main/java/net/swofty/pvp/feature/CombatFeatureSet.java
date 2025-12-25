package net.swofty.pvp.feature;

import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityInstanceEvent;

/**
 * A container for multiple {@link CombatFeature}s. Use {@link CombatFeatureSet#createNode()} to get an event node.
 */
public class CombatFeatureSet extends FeatureConfiguration implements RegistrableFeature {
	private boolean initialized = false;
	
	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		for (CombatFeature feature : listFeatures()) {
			if (!(feature instanceof RegistrableFeature registrable)) continue;
			node.addChild(registrable.createNode());
		}
	}
	
	@Override
	public void initDependencies() {
		for (CombatFeature feature : listFeatures()) {
			feature.initDependencies();
		}
		initialized = true;
	}
	
	@Override
	public FeatureConfiguration add(FeatureType<?> type, CombatFeature feature) {
		if (initialized) throw new UnsupportedOperationException("Cannot add features after initialization");
		return super.add(type, feature);
	}
}
