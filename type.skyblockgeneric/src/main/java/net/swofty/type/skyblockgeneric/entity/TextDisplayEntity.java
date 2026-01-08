package net.swofty.type.skyblockgeneric.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;

import java.util.function.Consumer;

public class TextDisplayEntity extends LivingEntity {

	public TextDisplayEntity(Component text, Consumer<TextDisplayMeta> metaConsumer) {
		super(EntityType.TEXT_DISPLAY);

		editEntityMeta(TextDisplayMeta.class, meta -> {
			meta.setText(text);
			meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
			meta.setHasNoGravity(true);
			meta.setSeeThrough(true);
			metaConsumer.accept(meta);
		});
	}

}
