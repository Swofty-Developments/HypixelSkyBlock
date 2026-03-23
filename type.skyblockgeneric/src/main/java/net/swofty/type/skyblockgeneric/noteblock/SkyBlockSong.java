package net.swofty.type.skyblockgeneric.noteblock;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import net.swofty.commons.Songs;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
public class SkyBlockSong {
    private static final int F_SHARP_4_KEY = 45;
    private static final int CENTER_PANNING = 100;
    private static final String TEMPO_CHANGER_CUSTOM_INSTRUMENT_NAME = "Tempo Changer";

    private static final SoundEvent[] VANILLA_INSTRUMENTS = {SoundEvent.BLOCK_NOTE_BLOCK_HARP, SoundEvent.BLOCK_NOTE_BLOCK_BASS, SoundEvent.BLOCK_NOTE_BLOCK_BASEDRUM, SoundEvent.BLOCK_NOTE_BLOCK_SNARE, SoundEvent.BLOCK_NOTE_BLOCK_HAT, SoundEvent.BLOCK_NOTE_BLOCK_GUITAR, SoundEvent.BLOCK_NOTE_BLOCK_FLUTE, SoundEvent.BLOCK_NOTE_BLOCK_BELL, SoundEvent.BLOCK_NOTE_BLOCK_CHIME, SoundEvent.BLOCK_NOTE_BLOCK_XYLOPHONE, SoundEvent.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, SoundEvent.BLOCK_NOTE_BLOCK_COW_BELL, SoundEvent.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundEvent.BLOCK_NOTE_BLOCK_BIT, SoundEvent.BLOCK_NOTE_BLOCK_BANJO, SoundEvent.BLOCK_NOTE_BLOCK_PLING, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_CREEPER, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_PIGLIN, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_SKELETON, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON, SoundEvent.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE,};

    private static final Map<String, SoundEvent> CUSTOM_SOUND_MAP = Map.ofEntries(Map.entry("minecraft/random/levelup.ogg", SoundEvent.ENTITY_PLAYER_LEVELUP), Map.entry("minecraft/block/bell/bell_use01.ogg", SoundEvent.BLOCK_BELL_USE));

    private final Songs song;
    private final int length;
    private final float tps;
    private final boolean loop;
    private final int loopStartTick;
    private final NavigableMap<Integer, Float> tempoEvents;
    private final Map<Integer, Sound[]> ticks;

    public SkyBlockSong(Songs song) {
        this.song = song;
        ByteBuffer buf;
        try {
            buf = ByteBuffer.wrap(Files.readAllBytes(song.getPath())).order(ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new SongLoadException("Failed to load song " + song.name() + ": " + e.getMessage());
        }

        short firstShort = buf.getShort();
        int version;
        int vanillaInstrumentCount;
        int length;

        if (firstShort == 0) {
            version = buf.get() & 0xFF;
            vanillaInstrumentCount = buf.get() & 0xFF;
            length = version >= 3 ? buf.getShort() : -1;
        } else {
            version = 0;
            vanillaInstrumentCount = 10;
            length = firstShort;
        }

        if (version > 5) throw new SongLoadException("Unsupported NBS version: " + version);

        int layerCount = Short.toUnsignedInt(buf.getShort());
        readString(buf);
        readString(buf);
        readString(buf);
        readString(buf);
        this.tps = Short.toUnsignedInt(buf.getShort()) / 100f;
        buf.get();
        buf.get();
        buf.get();
        buf.position(buf.position() + 20);
        readString(buf);

        if (version >= 4) {
            this.loop = buf.get() == 1;
            buf.get();
            this.loopStartTick = Short.toUnsignedInt(buf.getShort());
        } else {
            this.loop = false;
            this.loopStartTick = 0;
        }

        Map<Integer, Map<Integer, RawNote>> layers = new HashMap<>();
        int tick = -1;
        int maxTick = 0;
        while (true) {
            int jumpTicks = Short.toUnsignedInt(buf.getShort());
            if (jumpTicks == 0) break;
            tick += jumpTicks;
            if (tick > maxTick) maxTick = tick;

            int layer = -1;
            while (true) {
                int jumpLayers = Short.toUnsignedInt(buf.getShort());
                if (jumpLayers == 0) break;
                layer += jumpLayers;

                int instrument = buf.get() & 0xFF;
                int key = buf.get() & 0xFF;
                int velocity = 100;
                int panning = CENTER_PANNING;
                short pitch = 0;

                if (version >= 4) {
                    velocity = buf.get() & 0xFF;
                    panning = buf.get() & 0xFF;
                    pitch = buf.getShort();
                }

                layers.computeIfAbsent(layer, _ -> new HashMap<>()).put(tick, new RawNote(instrument, key, velocity, panning, pitch));
            }
        }

        this.length = length >= 0 ? length : maxTick;

        LayerMeta[] layerMeta = new LayerMeta[layerCount];
        if (buf.hasRemaining()) {
            for (int i = 0; i < layerCount; i++) {
                readString(buf);
                LayerStatus status = LayerStatus.NONE;
                if (version >= 4) {
                    status = switch (buf.get() & 0xFF) {
                        case 1 -> LayerStatus.LOCKED;
                        case 2 -> LayerStatus.SOLO;
                        default -> LayerStatus.NONE;
                    };
                }
                int vol = buf.get() & 0xFF;
                int pan = version >= 2 ? (buf.get() & 0xFF) : CENTER_PANNING;
                layerMeta[i] = new LayerMeta(vol, pan, status);
            }
        }

        List<CustomInstrument> customInstruments = new ArrayList<>();
        if (buf.hasRemaining()) {
            int count = buf.get() & 0xFF;
            for (int i = 0; i < count; i++) {
                String name = readString(buf);
                String soundPath = readString(buf);
                int instrumentPitch = buf.get() & 0xFF;
                buf.get();
                customInstruments.add(new CustomInstrument(name, soundPath, instrumentPitch));
            }
        }

        boolean hasSoloLayers = false;
        for (LayerMeta meta : layerMeta) {
            if (meta != null && meta.status == LayerStatus.SOLO) {
                hasSoloLayers = true;
                break;
            }
        }

        Map<Integer, List<Sound>> tickSounds = HashMap.newHashMap(maxTick);
        TreeMap<Integer, Float> tempoEvents = new TreeMap<>();
        tempoEvents.put(0, Math.max(this.tps, 0.01f));
        for (var layerEntry : layers.entrySet()) {
            int layerIdx = layerEntry.getKey();
            LayerMeta meta = layerIdx < layerMeta.length && layerMeta[layerIdx] != null ? layerMeta[layerIdx] : new LayerMeta(100, CENTER_PANNING, LayerStatus.NONE);

            boolean muted = meta.status == LayerStatus.LOCKED || (hasSoloLayers && meta.status != LayerStatus.SOLO);

            float layerVolFactor = Math.min(meta.volume / 100f, 1f);

            for (var noteEntry : layerEntry.getValue().entrySet()) {
                BakedNote bakedNote = bakeNote(noteEntry.getValue(), layerVolFactor, vanillaInstrumentCount, customInstruments);
                Float tempoChange = bakedNote.tempoChange();
                if (tempoChange != null && tempoChange > 0f) {
                    tempoEvents.put(noteEntry.getKey(), tempoChange);
                }

                Sound sound = bakedNote.sound();
                if (sound != null && !muted) {
                    tickSounds.computeIfAbsent(noteEntry.getKey(), _ -> new ArrayList<>()).add(sound);
                }
            }
        }

        Map<Integer, Sound[]> bakedTicks = HashMap.newHashMap(tickSounds.size());
        tickSounds.forEach((t, sounds) -> bakedTicks.put(t, sounds.toArray(Sound[]::new)));
        this.tempoEvents = Collections.unmodifiableNavigableMap(tempoEvents);
        this.ticks = Collections.unmodifiableMap(bakedTicks);
    }

    private static BakedNote bakeNote(RawNote note, float layerVolFactor, int vanillaInstrumentCount, List<CustomInstrument> customInstruments) {
        float effectiveKey = Math.clamp(note.key, 0, 87) + note.pitch / 100f;
        SoundEvent soundEvent = null;
        Key customKey = null;

        if (note.instrument < vanillaInstrumentCount) {
            soundEvent = note.instrument < VANILLA_INSTRUMENTS.length ? VANILLA_INSTRUMENTS[note.instrument] : VANILLA_INSTRUMENTS[0];
        } else {
            int customIdx = note.instrument - vanillaInstrumentCount;
            if (customIdx >= 0 && customIdx < customInstruments.size()) {
                CustomInstrument ci = customInstruments.get(customIdx);

                if (TEMPO_CHANGER_CUSTOM_INSTRUMENT_NAME.equals(ci.name)) {
                    return new BakedNote(null, Math.abs(note.pitch / 15f));
                }

                effectiveKey += ci.pitch - F_SHARP_4_KEY;

                if (ci.soundPath != null && !ci.soundPath.isBlank()) {
                    SoundEvent mapped = CUSTOM_SOUND_MAP.get(ci.soundPath);
                    if (mapped != null) {
                        soundEvent = mapped;
                    } else if (!ci.soundPath.contains("/") && !ci.soundPath.contains(".ogg")) {
                        String keyStr = ci.soundPath.contains(":") ? ci.soundPath : "minecraft:" + ci.soundPath;
                        customKey = Key.key(keyStr);
                    } else {
                        Logger.warn("Unmapped custom sound '{}', skipping", ci.soundPath);
                        return new BakedNote(null, null);
                    }
                } else {
                    soundEvent = VANILLA_INSTRUMENTS[0];
                }
            } else {
                soundEvent = VANILLA_INSTRUMENTS[0];
            }
        }

        float pitch = (float) Math.pow(2.0, (effectiveKey - F_SHARP_4_KEY) / 12.0);
        float volume = Math.clamp(layerVolFactor * (note.velocity / 100f), 0f, 1f);

        Sound sound = customKey != null ? Sound.sound(customKey, Sound.Source.RECORD, volume, pitch) : Sound.sound(soundEvent, Sound.Source.RECORD, volume, pitch);
        return new BakedNote(sound, null);
    }

    private static String readString(ByteBuffer buf) {
        int len = buf.getInt();
        if (len <= 0) return "";
        byte[] bytes = new byte[len];
        buf.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private record RawNote(int instrument, int key, int velocity, int panning, short pitch) {
    }

    private record LayerMeta(int volume, int panning, LayerStatus status) {
    }

    private record CustomInstrument(String name, String soundPath, int pitch) {
    }

    private record BakedNote(Sound sound, Float tempoChange) {
    }

    private enum LayerStatus {NONE, LOCKED, SOLO}
}