package gg.itzkatze.thehypixelrecreationmod.features.nbs;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class NbsWriter {
    public static final int VANILLA_INSTRUMENT_COUNT = 16;
    public static final int TEMPO = 20;

    private NbsWriter() {
    }

    public static void write(Path path, String songName, List<Note> notes, List<CustomInstrument> customInstruments)
            throws IOException {
        Files.createDirectories(path.getParent());

        List<Note> sortedNotes = new ArrayList<>(notes);
        sortedNotes.sort(Comparator.comparingInt(Note::tick).thenComparingInt(Note::layer));
        int layerCount = sortedNotes.stream().mapToInt(Note::layer).max().orElse(-1) + 1;
        int songLength = sortedNotes.stream().mapToInt(Note::tick).max().orElse(-1) + 1;

        try (OutputStream stream = Files.newOutputStream(path);
             DataOutputStream output = new DataOutputStream(new BufferedOutputStream(stream))) {
            writeHeader(output, songName, songLength, layerCount);
            writeNotes(output, sortedNotes);
            writeLayers(output, layerCount);
            writeCustomInstruments(output, customInstruments);
        }
    }

    private static void writeHeader(DataOutputStream output, String songName, int songLength, int layerCount)
            throws IOException {
        writeShort(output, 0);
        output.writeByte(5);
        output.writeByte(VANILLA_INSTRUMENT_COUNT);
        writeShort(output, songLength);
        writeShort(output, layerCount);
        writeString(output, songName);
        writeString(output, "");
        writeString(output, "");
        writeString(output, "Recorded from Minecraft server sounds by TheHypixelRecreationMod.");
        writeShort(output, TEMPO * 100);
        output.writeByte(0);
        output.writeByte(10);
        output.writeByte(4);
        writeInt(output, 0);
        writeInt(output, 0);
        writeInt(output, 0);
        writeInt(output, 0);
        writeInt(output, 0);
        writeString(output, "");
        output.writeByte(0);
        output.writeByte(0);
        writeShort(output, 0);
    }

    private static void writeNotes(DataOutputStream output, List<Note> notes) throws IOException {
        int previousTick = -1;
        int index = 0;
        while (index < notes.size()) {
            int tick = notes.get(index).tick();
            writeShort(output, tick - previousTick);
            previousTick = tick;

            int previousLayer = -1;
            while (index < notes.size() && notes.get(index).tick() == tick) {
                Note note = notes.get(index++);
                writeShort(output, note.layer() - previousLayer);
                previousLayer = note.layer();
                output.writeByte(note.instrument());
                output.writeByte(note.key());
                output.writeByte(note.velocity());
                output.writeByte(note.panning());
                writeShort(output, note.finePitch());
            }
            writeShort(output, 0);
        }
        writeShort(output, 0);
    }

    private static void writeLayers(DataOutputStream output, int layerCount) throws IOException {
        for (int layer = 0; layer < layerCount; layer++) {
            writeString(output, "Layer " + (layer + 1));
            output.writeByte(0);
            output.writeByte(100);
            output.writeByte(100);
        }
    }

    private static void writeCustomInstruments(DataOutputStream output, List<CustomInstrument> customInstruments)
            throws IOException {
        output.writeByte(customInstruments.size());
        for (CustomInstrument instrument : customInstruments) {
            writeString(output, instrument.name());
            writeString(output, instrument.soundFile());
            output.writeByte(instrument.soundKey());
            output.writeByte(1);
        }
    }

    private static void writeString(DataOutputStream output, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(output, bytes.length);
        output.write(bytes);
    }

    private static void writeShort(DataOutputStream output, int value) throws IOException {
        output.writeByte(value & 0xff);
        output.writeByte((value >>> 8) & 0xff);
    }

    private static void writeInt(DataOutputStream output, int value) throws IOException {
        output.writeByte(value & 0xff);
        output.writeByte((value >>> 8) & 0xff);
        output.writeByte((value >>> 16) & 0xff);
        output.writeByte((value >>> 24) & 0xff);
    }

    public record Note(int tick, int layer, int instrument, int key, int velocity, int panning, int finePitch) {
    }

    public record CustomInstrument(String name, String soundFile, int soundKey) {
    }
}
