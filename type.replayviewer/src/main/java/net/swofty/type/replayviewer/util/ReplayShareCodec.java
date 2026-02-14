package net.swofty.type.replayviewer.util;

import net.minestom.server.coordinate.Pos;

public class ReplayShareCodec {

    private static final double REFERENCE_Y = 70.0;

    private static final int DX_MIN = -128;
    private static final int DX_MAX = 127;
    private static final int DZ_MIN = -128;
    private static final int DZ_MAX = 127;
    private static final int DY_MIN = -64;
    private static final int DY_MAX = 63;

    private static final int TICK_PRECISION = 10;
    private static final int MAX_TICK_BUCKET = 3;

    public static String encode(Pos pos, int tick,
                                double mapCenterX,
                                double mapCenterZ) {

        int dx = clamp((int) Math.round(pos.x() - mapCenterX), DX_MIN, DX_MAX);
        int dz = clamp((int) Math.round(pos.z() - mapCenterZ), DZ_MIN, DZ_MAX);
        int dy = clamp((int) Math.round(pos.y() - REFERENCE_Y), DY_MIN, DY_MAX);

        int yawBucket = quantizeYaw(pos.yaw());
        int pitchBucket = quantizePitch(pos.pitch());

        int tickBucket = clamp(tick / TICK_PRECISION, 0, MAX_TICK_BUCKET);

        int packed = 0;
        packed |= (dx + 128) << 24;
        packed |= (dz + 128) << 16;
        packed |= (dy + 64) << 9;
        packed |= yawBucket << 5;
        packed |= pitchBucket << 2;
        packed |= tickBucket;

        return "#" + String.format("%08x", packed);
    }

    public static ShareData decode(String code,
                                   double mapCenterX,
                                   double mapCenterZ) {

        if (code == null) return null;

        if (code.startsWith("#")) {
            code = code.substring(1);
        }

        if (code.length() != 8) return null;

        int packed;
        try {
            packed = (int) Long.parseLong(code, 16);
        } catch (Exception e) {
            return null;
        }

        int dx = ((packed >>> 24) & 0xFF) - 128;
        int dz = ((packed >>> 16) & 0xFF) - 128;
        int dy = ((packed >>> 9) & 0x7F) - 64;
        int yawBucket = (packed >>> 5) & 0x0F;
        int pitchBucket = (packed >>> 2) & 0x07;
        int tickBucket = packed & 0x03;

        double x = mapCenterX + dx;
        double y = REFERENCE_Y + dy;
        double z = mapCenterZ + dz;

        float yaw = dequantizeYaw(yawBucket);
        float pitch = dequantizePitch(pitchBucket);
        int tick = tickBucket * TICK_PRECISION;

        Pos pos = new Pos(x, y, z, yaw, pitch);
        return new ShareData(pos, tick);
    }

    private static int quantizeYaw(float yaw) {
        float normalized = yaw % 360f;
        if (normalized < 0) normalized += 360f;
        return (int) (normalized / 360f * 16f) & 0xF;
    }

    private static float dequantizeYaw(int bucket) {
        return (bucket + 0.5f) * (360f / 16f);
    }

    private static int quantizePitch(float pitch) {
        float clamped = Math.max(-90f, Math.min(90f, pitch));
        return (int) ((clamped + 90f) / 180f * 8f) & 0x7;
    }

    private static float dequantizePitch(int bucket) {
        return (bucket + 0.5f) * (180f / 8f) - 90f;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public record ShareData(Pos position, int tick) {}
}
