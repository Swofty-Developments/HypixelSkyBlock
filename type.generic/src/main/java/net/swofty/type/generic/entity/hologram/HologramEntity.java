package net.swofty.type.generic.entity.hologram;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;

@Getter
public class HologramEntity extends Entity {
    private String text;

    public HologramEntity(String text) {
        super(EntityType.ARMOR_STAND);

        text = text.replace("&", "§");
        this.text = text;

        setInvisible(true);
        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();
        meta.setNotifyAboutChanges(false);
        set(DataComponents.CUSTOM_NAME, Component.text(this.text));
        meta.setNotifyAboutChanges(true);

        editEntityMeta(ArmorStandMeta.class, m -> {
            m.setCustomNameVisible(true);
            m.setSmall(true);
            m.setHasNoGravity(true);
            m.setMarker(true);
        });
    }

    public void setText(String text) {
        text = text.replace("&", "§");
        this.text = text;

        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();

        meta.setNotifyAboutChanges(false);
        set(DataComponents.CUSTOM_NAME, Component.text(this.text));
        meta.setNotifyAboutChanges(true);
    }
}