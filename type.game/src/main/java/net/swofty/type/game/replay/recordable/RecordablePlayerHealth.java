package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordablePlayerHealth extends AbstractRecordable {
    private int entityId;
    private float health;
    private float maxHealth;

    public RecordablePlayerHealth(int entityId, float health, float maxHealth) {
        this.entityId = entityId;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.PLAYER_HEALTH;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeHalfFloat(health);
        writer.writeHalfFloat(maxHealth);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        health = reader.readHalfFloat();
        maxHealth = reader.readHalfFloat();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public boolean isEntityState() {
        return true;
    }

    @Override
    public int estimatedSize() {
        return 2 + 2 + 2; // 6 bytes
    }
}
