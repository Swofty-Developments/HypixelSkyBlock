package net.swofty.types.generic.museum;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

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
