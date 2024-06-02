package net.swofty.types.generic.noteblock;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.sound.Sound;
import net.swofty.commons.Songs;
import net.swofty.types.generic.utility.BufferUtility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SkyBlockSong {
    private final Songs song;

    private final byte version;
    private final byte instrumentCount;
    private final int length;
    private final int layerCount;
    private final String songName;
    private final String author;
    private final String originalAuthor;
    private final String description;
    private final double tps;
    private final byte timeSignature;
    private final boolean loop;
    private final byte maxLoopCount;
    private final int loopStart;

    private final Map<Integer, List<Sound>> ticks;

    @SneakyThrows
    public SkyBlockSong(Songs song) {
        this.song = song;
        ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(song.getPath())).order(ByteOrder.LITTLE_ENDIAN);

        BufferUtility.getUnsignedShort(buffer); // first 2 bytes are always nothing in new versions of NBS files
        this.version = buffer.get();
        this.instrumentCount = buffer.get();
        this.length = BufferUtility.getUnsignedShort(buffer);
        this.layerCount = BufferUtility.getUnsignedShort(buffer);
        this.songName = BufferUtility.getNBSString(buffer);
        this.author = BufferUtility.getNBSString(buffer);
        this.originalAuthor = BufferUtility.getNBSString(buffer);
        this.description = BufferUtility.getNBSString(buffer);
        this.tps = BufferUtility.getUnsignedShort(buffer) / 100.0;
        buffer.get(); // auto saving
        buffer.get(); // auto saving duration
        this.timeSignature = buffer.get();
        buffer.getInt(); // minutes spent
        buffer.getInt(); // left clicks
        buffer.getInt(); // right clicks
        buffer.getInt(); // note blocks added
        buffer.getInt(); // note blocks removed
        BufferUtility.getNBSString(buffer); // midi file name
        this.loop = buffer.get() == 1;
        this.maxLoopCount = buffer.get();
        this.loopStart = BufferUtility.getUnsignedShort(buffer);

        this.ticks = getTicks(buffer);
    }

    private Map<Integer, List<Sound>> getTicks(ByteBuffer buffer) {
        Map<Integer, List<Sound>> ticks = new HashMap<>(length);

        int i = 0;

        while (true) {
            int jumps = BufferUtility.getUnsignedShort(buffer);
            if (jumps == 0) break;
            i += jumps;

            List<Sound> tick = getNotes(buffer);
            ticks.put(i, tick);
        }

        return ticks;
    }

    private @Nullable List<Sound> getNotes(ByteBuffer buffer) {
        List<Sound> notes = new ArrayList<>();

        while (true) {
            int jumps = BufferUtility.getUnsignedShort(buffer);
            if (jumps == 0) break;

            notes.add(Note.readNote(buffer).toSound(Sound.Source.RECORD));
        }

        if (notes.isEmpty()) return null;
        return notes;
    }
}
