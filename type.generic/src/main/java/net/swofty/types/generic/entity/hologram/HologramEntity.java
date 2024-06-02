package net.swofty.types.generic.entity.hologram;

import lombok.Getter;
import net.kyori.adventure.text.Component;
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

        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();

        setInvisible(true);

        meta.setNotifyAboutChanges(false);
        meta.setCustomNameVisible(true);
        meta.setCustomName(Component.text(text));
        meta.setSmall(true);
        meta.setHasNoGravity(true);
        meta.setNotifyAboutChanges(true);
    }

    public void setText(String text) {
        text = text.replace("&", "§");
        this.text = text;

        ArmorStandMeta meta = (ArmorStandMeta) this.getEntityMeta();

        meta.setNotifyAboutChanges(false);
        meta.setCustomName(Component.text(text));
        meta.setNotifyAboutChanges(true);
    }

}
