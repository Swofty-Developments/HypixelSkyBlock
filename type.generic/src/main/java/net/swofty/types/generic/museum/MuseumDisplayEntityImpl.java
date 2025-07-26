package net.swofty.types.generic.museum;

import lombok.Getter;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.item.SkyBlockItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class MuseumDisplayEntityImpl extends LivingEntity {
    private final MuseumDisplays display;
    private final int positionInMuseum;
    private final boolean empty;

    public MuseumDisplayEntityImpl(@NotNull EntityType entityType, @NotNull MuseumDisplays display, int position, boolean empty) {
        super(entityType);

        this.display = display;
        this.positionInMuseum = position;
        this.empty = empty;
    }
}
