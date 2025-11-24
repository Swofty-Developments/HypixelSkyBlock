package net.swofty.type.skyblockgeneric.noteblock;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import net.swofty.type.generic.utility.BufferUtility;

import java.nio.ByteBuffer;

public class Note {

    private final byte instrument;
    private final byte key;
    private final byte volume;
    private Note(byte instrument, byte key, byte volume) {
        this.instrument = instrument;
        this.key = key;
        this.volume = volume;
    }

    private static final SoundEvent[] SOUNDS = new SoundEvent[] {
            SoundEvent.BLOCK_NOTE_BLOCK_HARP,
            SoundEvent.BLOCK_NOTE_BLOCK_BASS,
            SoundEvent.BLOCK_NOTE_BLOCK_BASEDRUM,
            SoundEvent.BLOCK_NOTE_BLOCK_SNARE,
            SoundEvent.BLOCK_NOTE_BLOCK_HAT,
            SoundEvent.BLOCK_NOTE_BLOCK_GUITAR,
            SoundEvent.BLOCK_NOTE_BLOCK_FLUTE,
            SoundEvent.BLOCK_NOTE_BLOCK_BELL,
            SoundEvent.BLOCK_NOTE_BLOCK_CHIME,
            SoundEvent.BLOCK_NOTE_BLOCK_XYLOPHONE,
            SoundEvent.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
            SoundEvent.BLOCK_NOTE_BLOCK_COW_BELL,
            SoundEvent.BLOCK_NOTE_BLOCK_DIDGERIDOO,
            SoundEvent.BLOCK_NOTE_BLOCK_BIT,
            SoundEvent.BLOCK_NOTE_BLOCK_BANJO,
            SoundEvent.BLOCK_NOTE_BLOCK_PLING
    };

    public static Note readNote(ByteBuffer buffer) {
        byte instrument = buffer.get();
        byte key = buffer.get();
        byte volume = buffer.get();
        buffer.get(); // pan (ignored)
        BufferUtility.getUnsignedShort(buffer); // pitch (ignored)
        return new Note(instrument, key, volume);
    }

    public Sound toSound(Sound.Source source) {
        return Sound.sound(
                SOUNDS[instrument],
                source,
                volume / 100f,
                (float) Math.pow(2f, (key - 45) / 12f)
        );
    }

}