package net.swofty.pvp.feature;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;

/**
 * A {@link CombatFeature} which listens for events and likewise needs to register its listeners to an event node.
 * <p>
 * See {@link RegistrableFeature#createNode()}
 */
public interface RegistrableFeature extends CombatFeature {
	EventFilter<EntityInstanceEvent, Entity> ENTITY_INSTANCE_FILTER = EventFilter
			.from(EntityInstanceEvent.class, Entity.class, EntityEvent::getEntity);
	
	/**
	 * Returns the priority that an event node created by this feature will have.
	 *
	 * @return the priority
	 */
	default int getPriority() {
		return 0;
	}
	
	/**
	 * Initializes this feature by adding its listeners to the given event node.
	 *
	 * @param node the event node to add the listeners to
	 */
	void init(EventNode<EntityInstanceEvent> node);
	
	/**
	 * Creates an event node with all the listeners of this feature attached.
	 * This event node can on its turn be added to another node (e.g. the global one) to get the listeners working.
	 *
	 * @return the event node
	 */
	default EventNode<EntityInstanceEvent> createNode() {
		var node = EventNode.type(getClass().getTypeName(), ENTITY_INSTANCE_FILTER);
		node.setPriority(getPriority());
		init(node);
		return node;
	}
}
