package gg.itzkatze.thehypixelrecreationmod.features.worldexport;

import gg.itzkatze.thehypixelrecreationmod.utils.PolarConvert;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueOutput;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ChunkExportRecorder {
    private static final int CAPTURE_INTERVAL_TICKS = 20;

    private static final Map<Long, CompoundTag> RECORDED_CHUNKS = new LinkedHashMap<>();
    private static final Map<UUID, CapturedEntity> RECORDED_BLOCK_DISPLAYS = new LinkedHashMap<>();
    private static final Set<UUID> MOVING_ENTITIES = new HashSet<>();

    private static LoadedChunkExporter.SessionContext sessionContext;
    private static Instant startedAt;
    private static int ticksUntilCapture;
    private static CaptureMode captureMode;

    private ChunkExportRecorder() {
    }

    public static StartResult start() {
        return start(CaptureMode.CHUNKS);
    }

    public static StartResult start(CaptureMode mode) {
        if (isActive()) {
            throw new IllegalStateException("A chunk export session is already active.");
        }

        Minecraft client = Minecraft.getInstance();
        sessionContext = LoadedChunkExporter.captureCurrentContext(client);
        startedAt = Instant.now();
        RECORDED_CHUNKS.clear();
        RECORDED_BLOCK_DISPLAYS.clear();
        MOVING_ENTITIES.clear();
        captureMode = mode;
        ticksUntilCapture = CAPTURE_INTERVAL_TICKS;

        captureCurrentWorld(client.level, true);
        return new StartResult(sessionContext.dimension(), RECORDED_CHUNKS.size(), RECORDED_BLOCK_DISPLAYS.size(), mode);
    }

    public static StopResult stop(String sessionName) throws IOException {
        if (!isActive()) {
            throw new IllegalStateException("No chunk export session is active.");
        }

        Minecraft client = Minecraft.getInstance();
        captureCurrentWorld(client.level, false);

        LoadedChunkExporter.ExportResult exportResult = LoadedChunkExporter.writeRecordedChunks(
                sessionContext,
                sessionName,
                startedAt,
                Instant.now(),
                RECORDED_CHUNKS
        );
        PolarConvert.ConversionResult polarResult = PolarConvert.convertWorldFolderToPolar(
                exportResult.path(),
                exportResult.path().resolveSibling(exportResult.path().getFileName() + ".polar"),
                client.level,
                RECORDED_CHUNKS,
                RECORDED_BLOCK_DISPLAYS.values().stream().map(CapturedEntity::tag).toList()
        );

        String sanitizedName = LoadedChunkExporter.sanitizeSessionName(sessionName);
        clearSession();
        return new StopResult(
                sanitizedName,
                exportResult.path(),
                polarResult.path(),
                exportResult.chunkCount(),
                exportResult.sectionCount(),
                exportResult.blockEntityCount(),
                polarResult.customBiomeCount(),
                polarResult.blockDisplayCount()
        );
    }

    public static void tick() {
        if (!isActive()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        ClientLevel level = client.level;
        if (level == null || client.player == null) {
            return;
        }

        if (!sessionContext.matches(client, level)) {
            return;
        }

        if (--ticksUntilCapture > 0) {
            return;
        }

        ticksUntilCapture = CAPTURE_INTERVAL_TICKS;
        captureCurrentWorld(level, false);
    }

    public static boolean isActive() {
        return sessionContext != null;
    }

    public static Status getStatus() {
        if (!isActive()) {
            throw new IllegalStateException("No chunk export session is active.");
        }

        return new Status(sessionContext.dimension(), RECORDED_CHUNKS.size(), RECORDED_BLOCK_DISPLAYS.size(), MOVING_ENTITIES.size(), captureMode);
    }

    private static void captureCurrentWorld(ClientLevel level, boolean requireMatchingContext) {
        if (level == null) {
            return;
        }

        if (requireMatchingContext || sessionContext.matches(Minecraft.getInstance(), level)) {
            List<LoadedChunkExporter.CapturedChunk> snapshots = LoadedChunkExporter.captureLoadedChunks(level);
            for (LoadedChunkExporter.CapturedChunk snapshot : snapshots) {
                RECORDED_CHUNKS.put(snapshot.packedPos(), snapshot.chunkTag().copy());
            }
            if (captureMode == CaptureMode.BLOCK_DISPLAYS) {
                captureBlockDisplays(level);
            }
        }
    }

    private static void captureBlockDisplays(ClientLevel level) {
        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof Display.BlockDisplay blockDisplay)) {
                continue;
            }

            UUID uuid = blockDisplay.getUUID();
            if (MOVING_ENTITIES.contains(uuid)) {
                continue;
            }

            if (blockDisplay.getDeltaMovement().lengthSqr() > 0) {
                RECORDED_BLOCK_DISPLAYS.remove(uuid);
                MOVING_ENTITIES.add(uuid);
                continue;
            }

            EntityPosition position = new EntityPosition(blockDisplay.getX(), blockDisplay.getY(), blockDisplay.getZ());
            CapturedEntity previous = RECORDED_BLOCK_DISPLAYS.get(uuid);
            if (previous != null && !previous.position().equals(position)) {
                RECORDED_BLOCK_DISPLAYS.remove(uuid);
                MOVING_ENTITIES.add(uuid);
                continue;
            }

            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            if (blockDisplay.save(output)) {
                RECORDED_BLOCK_DISPLAYS.put(uuid, new CapturedEntity(position, output.buildResult()));
            }
        }
    }

    private static void clearSession() {
        RECORDED_CHUNKS.clear();
        RECORDED_BLOCK_DISPLAYS.clear();
        MOVING_ENTITIES.clear();
        sessionContext = null;
        startedAt = null;
        ticksUntilCapture = 0;
        captureMode = null;
    }

    public enum CaptureMode {
        CHUNKS,
        BLOCK_DISPLAYS
    }

    public record StartResult(String dimension, int initialChunkCount, int initialBlockDisplayCount, CaptureMode mode) {
    }

    public record StopResult(
            String sessionName,
            Path path,
            Path polarPath,
            int chunkCount,
            int sectionCount,
            int blockEntityCount,
            int customBiomeCount,
            int blockDisplayCount
    ) {
    }

    public record Status(String dimension, int chunkCount, int blockDisplayCount, int movingEntityCount, CaptureMode mode) {
    }

    private record CapturedEntity(EntityPosition position, CompoundTag tag) {
    }

    private record EntityPosition(double x, double y, double z) {
    }
}
