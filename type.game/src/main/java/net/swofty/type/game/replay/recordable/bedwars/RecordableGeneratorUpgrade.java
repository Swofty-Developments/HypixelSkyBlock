package net.swofty.type.game.replay.recordable.bedwars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.type.game.replay.recordable.AbstractRecordable;
import net.swofty.type.game.replay.recordable.RecordableType;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordableGeneratorUpgrade extends AbstractRecordable {
    private byte generatorType; // 0=diamond, 1=emerald
    private byte tier; // 1, 2, 3

    public RecordableGeneratorUpgrade(byte generatorType, byte tier) {
        this.generatorType = generatorType;
        this.tier = tier;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_EVENT_CONTINUE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeByte(generatorType);
        writer.writeByte(tier);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        generatorType = (byte) reader.readByte();
        tier = (byte) reader.readByte();
    }

}
