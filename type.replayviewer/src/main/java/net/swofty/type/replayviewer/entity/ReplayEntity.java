package net.swofty.type.replayviewer.entity;

import lombok.Getter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class ReplayEntity extends Entity {
    private final int recordedEntityId;
    private final UUID recordedUuid;

    public ReplayEntity(@NotNull EntityType entityType, int recordedEntityId, UUID recordedUuid) {
        super(entityType);
        this.recordedEntityId = recordedEntityId;
        this.recordedUuid = recordedUuid;
    }

}
