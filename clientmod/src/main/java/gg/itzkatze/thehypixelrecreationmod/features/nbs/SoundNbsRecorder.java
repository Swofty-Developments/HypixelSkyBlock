package gg.itzkatze.thehypixelrecreationmod.features.nbs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.RandomSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// format as defined in https://noteblock.studio/nbs
public final class SoundNbsRecorder {
    private static final long NBS_TICK_NANOS = 50_000_000L;
    private static final long SAME_BATCH_NANOS = 18_000_000L;
    private static final int MAX_CUSTOM_INSTRUMENTS = 240;
    private static final Map<String, Integer> VANILLA_INSTRUMENTS = createVanillaInstruments();
    private static final List<PendingNote> NOTES = new ArrayList<>();
    private static final Map<String, CustomSound> CUSTOM_SOUNDS = new LinkedHashMap<>();
    private static boolean active;
    private static long firstArrivalNanos;
    private static long previousArrivalNanos;
    private static long batchStartNanos;
    private static int previousTick;
    private static int ignoredSoundCount;

    private SoundNbsRecorder() {
    }

    public static void start() {
        if (active) {
            throw new IllegalStateException("An NBS recording is already active.");
        }

        NOTES.clear();
        CUSTOM_SOUNDS.clear();
        firstArrivalNanos = 0;
        previousArrivalNanos = 0;
        batchStartNanos = 0;
        previousTick = 0;
        ignoredSoundCount = 0;
        active = true;
    }

    public static StopResult stop(String requestedName) throws IOException {
        if (!active) {
            throw new IllegalStateException("No NBS recording is active.");
        }

        String name = sanitizeName(requestedName);
        active = false;
        try {
            Path baseDirectory = Minecraft.getInstance().gameDirectory.toPath().resolve("nbs-recordings");
            Path soundsDirectory = baseDirectory.resolve("Sounds");
            Files.createDirectories(soundsDirectory);

            List<NbsWriter.CustomInstrument> customInstruments = new ArrayList<>();
            for (CustomSound customSound : CUSTOM_SOUNDS.values()) {
                exportSound(customSound, soundsDirectory);
                customInstruments.add(new NbsWriter.CustomInstrument(customSound.name(), customSound.relativeFile(), 45));
            }

            List<NbsWriter.Note> notes = assignLayers();
            Path output = baseDirectory.resolve(name + ".nbs");
            NbsWriter.write(output, name, notes, customInstruments);
            return new StopResult(output, notes.size(), customInstruments.size(), ignoredSoundCount);
        } catch (IOException exception) {
            active = true;
            throw exception;
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static Status getStatus() {
        if (!active) {
            throw new IllegalStateException("No NBS recording is active.");
        }
        return new Status(NOTES.size(), CUSTOM_SOUNDS.size(), ignoredSoundCount);
    }

    public static void recordServerSound(Identifier eventId, float volume, float pitch, long seed) {
        if (!active) {
            return;
        }

        String eventPath = eventId.getPath().toLowerCase(Locale.ROOT);
        if (isIgnored(eventPath)) {
            ignoredSoundCount++;
            return;
        }

        int instrument = VANILLA_INSTRUMENTS.getOrDefault(eventPath, -1);
        if (instrument < 0) {
            instrument = customInstrument(eventId, seed);
            if (instrument < 0) {
                ignoredSoundCount++;
                return;
            }
        }

        long arrivalNanos = System.nanoTime();
        int tick = quantizeTick(arrivalNanos);
        Pitch nbsPitch = toNbsPitch(pitch);
        int velocity = Math.clamp(Math.round(volume * 100.0f), 0, 100);
        NOTES.add(new PendingNote(tick, instrument, nbsPitch.key(), velocity, 100, nbsPitch.finePitch()));
    }

    private static int quantizeTick(long arrivalNanos) {
        if (firstArrivalNanos == 0) {
            firstArrivalNanos = arrivalNanos;
            previousArrivalNanos = arrivalNanos;
            batchStartNanos = arrivalNanos;
            previousTick = 0;
            return 0;
        }

        if (arrivalNanos - previousArrivalNanos <= SAME_BATCH_NANOS
            && arrivalNanos - batchStartNanos <= NBS_TICK_NANOS / 2) {
            previousArrivalNanos = arrivalNanos;
            return previousTick;
        }

        previousArrivalNanos = arrivalNanos;
        batchStartNanos = arrivalNanos;
        previousTick = Math.max(previousTick, (int) Math.round((arrivalNanos - firstArrivalNanos) / (double) NBS_TICK_NANOS));
        return previousTick;
    }

    private static int customInstrument(Identifier eventId, long seed) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        WeighedSoundEvents event = soundManager.getSoundEvent(eventId);
        if (event == null) {
            return -1;
        }
        Sound sound = event.getSound(RandomSource.create(seed));
        if (sound == SoundManager.EMPTY_SOUND || sound == SoundManager.INTENTIONALLY_EMPTY_SOUND) {
            return -1;
        }

        String id = sound.getLocation().toString();
        CustomSound existing = CUSTOM_SOUNDS.get(id);
        if (existing != null) {
            return existing.instrument();
        }
        if (CUSTOM_SOUNDS.size() >= MAX_CUSTOM_INSTRUMENTS) {
            return -1;
        }

        Identifier location = sound.getLocation();
        String relativeFile = location.getNamespace() + "/" + location.getPath() + ".ogg";
        int instrument = NbsWriter.VANILLA_INSTRUMENT_COUNT + CUSTOM_SOUNDS.size();
        CUSTOM_SOUNDS.put(id, new CustomSound(instrument, id, relativeFile, sound.getPath()));
        return instrument;
    }

    private static List<NbsWriter.Note> assignLayers() {
        Map<Integer, Integer> nextLayerByTick = new LinkedHashMap<>();
        List<NbsWriter.Note> notes = new ArrayList<>(NOTES.size());
        for (PendingNote note : NOTES) {
            int layer = nextLayerByTick.getOrDefault(note.tick(), 0);
            nextLayerByTick.put(note.tick(), layer + 1);
            notes.add(new NbsWriter.Note(
                note.tick(), layer, note.instrument(), note.key(), note.velocity(), note.panning(), note.finePitch()
            ));
        }
        return notes;
    }

    // export these so you can listen to them easily in Note Block Studio
    private static void exportSound(CustomSound customSound, Path soundsDirectory) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(customSound.resourcePath());
        if (resource.isEmpty()) {
            return;
        }

        Path destination = soundsDirectory.resolve(customSound.relativeFile());
        try {
            Files.createDirectories(destination.getParent());
            try (InputStream input = resource.get().open()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {

        }
    }

    private static boolean isIgnored(String path) {
        final Set<String> FIRE_CRACKLES = Set.of(
            "block.fire.ambient",
            "block.campfire.crackle",
            "block.soul_sand.step"
        );

        return path.contains(".step")
            || path.startsWith("step.")
            || path.endsWith(".step")
            || FIRE_CRACKLES.contains(path)
            || path.contains("fire") && (path.contains("crackle") || path.contains("ambient"));
    }

    private static Pitch toNbsPitch(float minecraftPitch) {
        double exactKey = 45.0 + 12.0 * (Math.log(Math.max(minecraftPitch, 0.0001f)) / Math.log(2.0));
        int key = Math.clamp((int) Math.round(exactKey), 0, 87);
        int finePitch = Math.clamp((int) Math.round((exactKey - key) * 100.0), -1200, 1200);
        return new Pitch(key, finePitch);
    }

    private static String sanitizeName(String requestedName) {
        String sanitized = requestedName.trim().replaceAll("[^A-Za-z0-9._-]+", "_");
        sanitized = sanitized.replaceAll("^\\.+", "");
        if (sanitized.isBlank()) {
            throw new IllegalArgumentException("The recording name must contain a letter or number.");
        }
        return sanitized;
    }

    private static Map<String, Integer> createVanillaInstruments() {
        String[] names = {
            "harp", "bass", "basedrum", "snare", "hat", "guitar", "flute", "bell",
            "chime", "xylophone", "iron_xylophone", "cow_bell", "didgeridoo", "bit", "banjo", "pling"
        };
        Map<String, Integer> instruments = new LinkedHashMap<>();
        for (int index = 0; index < names.length; index++) {
            instruments.put("block.note_block." + names[index], index);
        }
        return instruments;
    }

    private record PendingNote(int tick, int instrument, int key, int velocity, int panning, int finePitch) {
    }

    private record Pitch(int key, int finePitch) {
    }

    private record CustomSound(int instrument, String name, String relativeFile, Identifier resourcePath) {
    }

    public record StopResult(Path path, int noteCount, int customInstrumentCount, int ignoredSoundCount) {
    }

    public record Status(int noteCount, int customInstrumentCount, int ignoredSoundCount) {
    }
}
