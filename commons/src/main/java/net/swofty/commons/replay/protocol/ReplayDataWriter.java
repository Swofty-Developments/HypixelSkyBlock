package net.swofty.commons.replay.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Efficient binary writer for replay data.
 * Supports VarInts, SignedVarInts, half-precision floats, and other optimized formats.
 */
public class ReplayDataWriter implements AutoCloseable {
    private final DataOutputStream out;
    private final ByteArrayOutputStream buffer;

    public ReplayDataWriter() {
        this.buffer = new ByteArrayOutputStream(4096);
        this.out = new DataOutputStream(buffer);
    }

    public ReplayDataWriter(OutputStream outputStream) {
        this.buffer = null;
        this.out = new DataOutputStream(outputStream);
    }

    /**
     * Writes a byte.
     */
    public void writeByte(int value) throws IOException {
        out.writeByte(value);
    }

    /**
     * Writes a short (2 bytes).
     */
    public void writeShort(int value) throws IOException {
        out.writeShort(value);
    }

    /**
     * Writes an int (4 bytes).
     */
    public void writeInt(int value) throws IOException {
        out.writeInt(value);
    }

    /**
     * Writes a long (8 bytes).
     */
    public void writeLong(long value) throws IOException {
        out.writeLong(value);
    }

    /**
     * Writes a float (4 bytes).
     */
    public void writeFloat(float value) throws IOException {
        out.writeFloat(value);
    }

    /**
     * Writes a double (8 bytes).
     */
    public void writeDouble(double value) throws IOException {
        out.writeDouble(value);
    }

    /**
     * Writes a boolean (1 byte).
     */
    public void writeBoolean(boolean value) throws IOException {
        out.writeBoolean(value);
    }

    /**
     * Writes a string with length prefix.
     */
    public void writeString(String value) throws IOException {
        byte[] bytes = value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        out.write(bytes);
    }

    /**
     * Writes a UUID (16 bytes).
     */
    public void writeUUID(java.util.UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Writes an unsigned VarInt (1-5 bytes).
     * More efficient for small positive numbers.
     */
    public void writeVarInt(int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    /**
     * Writes a signed VarInt using ZigZag encoding.
     * Efficient for small values (both positive and negative).
     * Values -64 to 63 fit in 1 byte.
     * Values -8192 to 8191 fit in 2 bytes.
     */
    public void writeSignedVarInt(int value) throws IOException {
        // ZigZag encoding: (value << 1) ^ (value >> 31)
        writeVarInt((value << 1) ^ (value >> 31));
    }

    /**
     * Writes a VarLong (1-10 bytes).
     */
    public void writeVarLong(long value) throws IOException {
        while ((value & ~0x7FL) != 0) {
            out.writeByte((int) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        out.writeByte((int) value);
    }

    /**
     * Writes a half-precision float (2 bytes).
     * Reduced precision but half the storage.
     */
    public void writeHalfFloat(float value) throws IOException {
        out.writeShort(floatToHalf(value));
    }

    /**
     * Writes block coordinates using SignedVarInts.
     * Assumes coordinates are relative to map center.
     */
    public void writeBlockCoords(int x, int y, int z) throws IOException {
        writeSignedVarInt(x);
        writeSignedVarInt(y + 80); // Offset Y to be closer to 0
        writeSignedVarInt(z);
    }

    /**
     * Writes a location (x, y, z, yaw, pitch) efficiently.
     * Uses half-precision for fractional parts and compressed yaw/pitch.
     */
    public void writeLocation(double x, double y, double z, float yaw, float pitch) throws IOException {
        // Store whole part as SignedVarInt
        writeSignedVarInt((int) x);
        writeSignedVarInt((int) y);
        writeSignedVarInt((int) z);

        // Store fractional parts as half-precision
        writeHalfFloat((float) (x - (int) x));
        writeHalfFloat((float) (y - (int) y));
        writeHalfFloat((float) (z - (int) z));

        // Write yaw/pitch compressed into 3 bytes
        writeCompressedLook(yaw, pitch);
    }

    /**
     * Writes yaw and pitch compressed into 3 bytes.
     * 10 bits for yaw, 10 bits for pitch.
     */
    public void writeCompressedLook(float yaw, float pitch) throws IOException {
        // Normalize to [0, 360)
        yaw = ((yaw % 360) + 360) % 360;
        pitch = ((pitch % 360) + 360) % 360;

        // Convert to radians, multiply by factor, take whole part
        // Factor 160 fits in 10 bits (0-1023) for [0, 2π]
        int yawBits = (int) (yaw * Math.PI / 180.0 * 160) & 0x3FF;
        int pitchBits = (int) (pitch * Math.PI / 180.0 * 160) & 0x3FF;

        // Pack into 3 bytes (20 bits used, 4 bits unused)
        int packed = (yawBits << 10) | pitchBits;
        out.writeByte((packed >> 16) & 0xFF);
        out.writeByte((packed >> 8) & 0xFF);
        out.writeByte(packed & 0xFF);
    }

    /**
     * Writes a byte array with length prefix.
     */
    public void writeBytes(byte[] bytes) throws IOException {
        writeVarInt(bytes.length);
        out.write(bytes);
    }

    /**
     * Writes raw bytes without length prefix.
     */
    public void writeRawBytes(byte[] bytes) throws IOException {
        out.write(bytes);
    }

    /**
     * Writes a BitSet as bytes.
     */
    public void writeBitSet(java.util.BitSet bitSet, int numBits) throws IOException {
        int numBytes = (numBits + 7) / 8;
        byte[] bytes = bitSet.toByteArray();
        for (int i = 0; i < numBytes; i++) {
            out.writeByte(i < bytes.length ? bytes[i] : 0);
        }
    }

    /**
     * Gets the written data as a byte array.
     */
    public byte[] toByteArray() {
        if (buffer == null) {
            throw new IllegalStateException("Cannot get byte array when writing to external stream");
        }
        return buffer.toByteArray();
    }

    /**
     * Resets the internal buffer.
     */
    public void reset() {
        if (buffer != null) {
            buffer.reset();
        }
    }

    /**
     * Gets the current size of written data.
     */
    public int size() {
        return buffer != null ? buffer.size() : -1;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    /**
     * Converts a float to IEEE 754 half-precision format.
     */
    private static short floatToHalf(float value) {
        int bits = Float.floatToIntBits(value);
        int sign = (bits >>> 16) & 0x8000;
        int val = (bits & 0x7FFFFFFF) + 0x1000;

        if (val >= 0x47800000) {
            if ((bits & 0x7FFFFFFF) >= 0x47800000) {
                if (val < 0x7F800000) {
                    return (short) (sign | 0x7C00);
                }
                return (short) (sign | 0x7C00 | ((bits & 0x007FFFFF) >>> 13));
            }
            return (short) (sign | 0x7BFF);
        }

        if (val >= 0x38800000) {
            return (short) (sign | ((val - 0x38000000) >>> 13));
        }

        if (val < 0x33000000) {
            return (short) sign;
        }

        val = (bits & 0x7FFFFFFF) >>> 23;
        return (short) (sign | (((bits & 0x007FFFFF) | 0x00800000) + (0x00800000 >>> (val - 102)) >>> (126 - val)));
    }
}
