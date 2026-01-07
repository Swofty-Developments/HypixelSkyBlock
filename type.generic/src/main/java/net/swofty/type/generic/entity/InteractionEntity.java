package net.swofty.type.generic.entity;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.InteractionMeta;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.BiConsumer;

public class InteractionEntity extends LivingEntity {

	public final BiConsumer<HypixelPlayer, PlayerEntityInteractEvent> onClick;

	public InteractionEntity(float width, float height, BiConsumer<HypixelPlayer, PlayerEntityInteractEvent> onClick) {
		super(EntityType.INTERACTION);
		this.onClick = onClick;

        setAutoViewable(true);
		editEntityMeta(InteractionMeta.class, meta -> {
			meta.setHeight(height);
			meta.setWidth(width);
			meta.setHasNoGravity(true);
			meta.setResponse(true);
		});
	}
}
