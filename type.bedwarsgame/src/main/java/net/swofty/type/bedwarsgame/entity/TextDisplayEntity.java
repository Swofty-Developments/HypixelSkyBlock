package net.swofty.type.bedwarsgame.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;

public class TextDisplayEntity extends EntityCreature {

	public TextDisplayEntity(Component text) {
		super(EntityType.TEXT_DISPLAY);
		setNoGravity(true);
		setGlowing(true);
		editEntityMeta(TextDisplayMeta.class, (meta) -> {
			meta.setText(text);
			meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
		});
	}

	public void setText(Component text) {
		editEntityMeta(TextDisplayMeta.class, (meta) -> {
			meta.setText(text);
		});
	}

	public void setTranslation(Pos translation) {
		editEntityMeta(TextDisplayMeta.class, (meta) -> {
			meta.setTranslation(translation);
		});
	}

	public void setConstraints(AbstractDisplayMeta.BillboardConstraints constraints) {
		editEntityMeta(TextDisplayMeta.class, (meta) -> {
			meta.setBillboardRenderConstraints(constraints);
		});
	}

}
