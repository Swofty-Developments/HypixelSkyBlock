package gg.itzkatze.thehypixelrecreationmod.features.worldexport;

import gg.itzkatze.thehypixelrecreationmod.utils.PolarConvert;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ChunkExportRecorder {
    private static final int CAPTURE_INTERVAL_TICKS = 20;

    private static final Map<Long, CompoundTag> RECORDED_CHUNKS = new LinkedHashMap<>();

    private static LoadedChunkExporter.SessionContext sessionContext;
    private static Instant startedAt;
    private static int ticksUntilCapture;

    private ChunkExportRecorder() {
    }

    public static StartResult start() {
        if (isActive()) {
            throw new IllegalStateException("A chunk export session is already active.");
        }

        Minecraft client = Minecraft.getInstance();
        sessionContext = LoadedChunkExporter.captureCurrentContext(client);
        startedAt = Instant.now();
        RECORDED_CHUNKS.clear();
        ticksUntilCapture = CAPTURE_INTERVAL_TICKS;

        captureCurrentWorld(client.level, true);
        return new StartResult(sessionContext.dimension(), RECORDED_CHUNKS.size());
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
                RECORDED_CHUNKS
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
                polarResult.customBiomeCount()
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

        return new Status(sessionContext.dimension(), RECORDED_CHUNKS.size());
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
        }
    }

    private static void clearSession() {
        RECORDED_CHUNKS.clear();
        sessionContext = null;
        startedAt = null;
        ticksUntilCapture = 0;
    }

    public record StartResult(String dimension, int initialChunkCount) {
    }

    public record StopResult(
            String sessionName,
            Path path,
            Path polarPath,
            int chunkCount,
            int sectionCount,
            int blockEntityCount,
            int customBiomeCount
    ) {
    }

    public record Status(String dimension, int chunkCount) {
    }
}
