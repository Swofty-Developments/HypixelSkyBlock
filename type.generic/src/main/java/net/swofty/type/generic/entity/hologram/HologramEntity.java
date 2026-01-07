package net.swofty.type.generic.entity.hologram;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Getter
public class HologramEntity extends Entity {
    private String text;

    public HologramEntity(String text) {
        super(EntityType.TEXT_DISPLAY);

        text = text.replace("&", "§");
        this.text = text;

        TextDisplayMeta meta = (TextDisplayMeta) this.getEntityMeta();

        setInvisible(true);

        meta.setNotifyAboutChanges(false);
        meta.setCustomNameVisible(false);
        meta.setText(Component.text(text));
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        meta.setHasNoGravity(true);
        meta.setNotifyAboutChanges(true);
    }

    public void setText(String text) {
        text = text.replace("&", "§");
        this.text = text;

        TextDisplayMeta meta = (TextDisplayMeta) this.getEntityMeta();

        meta.setNotifyAboutChanges(false);
        meta.setText(Component.text(text));
        meta.setNotifyAboutChanges(true);
    }

    /**
     * Required because we changed to TextDisplayEntities late, which have a different eye height.
     */
    @Override
    public @NotNull CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos pos) {
        pos = pos.add(0, 1.3, 0);
        return super.setInstance(instance, pos);
    }
}
