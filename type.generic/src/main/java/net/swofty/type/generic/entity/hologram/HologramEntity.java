package net.swofty.type.generic.entity.hologram;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;

@Getter
public class HologramEntity extends Entity {
    private Component text;

    public HologramEntity(Component text) {
        super(EntityType.ARMOR_STAND);
        this.text = text;

        setInvisible(true);
        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();
        meta.setNotifyAboutChanges(false);
        set(DataComponents.CUSTOM_NAME, text);
        meta.setNotifyAboutChanges(true);

        editEntityMeta(ArmorStandMeta.class, m -> {
            m.setCustomNameVisible(true);
            m.setSmall(true);
            m.setHasNoGravity(true);
            m.setMarker(true);
        });
    }

    public HologramEntity(String text) {
        this(Component.text(text.replace("&", "§")));
    }

    public void setText(Component text) {
        this.text = text;

        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();

        meta.setNotifyAboutChanges(false);
        set(DataComponents.CUSTOM_NAME, text);
        meta.setNotifyAboutChanges(true);
    }

    public void setText(String text) {
        setText(Component.text(text.replace("&", "§")));
    }
}