package net.swofty.commons.replay.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.UUID;

/**
 * Efficient binary reader for replay data.
 * Counterpart to ReplayDataWriter.
 */
public class ReplayDataReader implements AutoCloseable {
    private final DataInputStream in;

    public ReplayDataReader(byte[] data) {
        this.in = new DataInputStream(new ByteArrayInputStream(data));
    }

    public ReplayDataReader(InputStream inputStream) {
        this.in = new DataInputStream(inputStream);
    }

    /**
     * Reads a byte.
     */
    public int readByte() throws IOException {
        return in.readByte();
    }

    /**
     * Reads an unsigned byte.
     */
    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    /**
     * Reads a short (2 bytes).
     */
    public short readShort() throws IOException {
        return in.readShort();
    }

    /**
     * Reads an int (4 bytes).
     */
    public int readInt() throws IOException {
        return in.readInt();
    }

    /**
     * Reads a long (8 bytes).
     */
    public long readLong() throws IOException {
        return in.readLong();
    }

    /**
     * Reads a float (4 bytes).
     */
    public float readFloat() throws IOException {
        return in.readFloat();
    }

    /**
     * Reads a double (8 bytes).
     */
    public double readDouble() throws IOException {
        return in.readDouble();
    }

    /**
     * Reads a boolean (1 byte).
     */
    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    /**
     * Reads a string with length prefix.
     */
    public String readString() throws IOException {
        int length = readVarInt();
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Reads a UUID (16 bytes).
     */
    public UUID readUUID() throws IOException {
        long mostSig = in.readLong();
        long leastSig = in.readLong();
        return new UUID(mostSig, leastSig);
    }

    /**
     * Reads an unsigned VarInt (1-5 bytes).
     */
    public int readVarInt() throws IOException {
        int result = 0;
        int shift = 0;
        int b;
        do {
            b = in.readUnsignedByte();
            result |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }

    /**
     * Reads a signed VarInt using ZigZag encoding.
     */
    public int readSignedVarInt() throws IOException {
        int raw = readVarInt();
        // ZigZag decode: (raw >>> 1) ^ -(raw & 1)
        return (raw >>> 1) ^ -(raw & 1);
    }

    /**
     * Reads a VarLong (1-10 bytes).
     */
    public long readVarLong() throws IOException {
        long result = 0;
        int shift = 0;
        int b;
        do {
            b = in.readUnsignedByte();
            result |= (long) (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }

    /**
     * Reads a half-precision float (2 bytes).
     */
    public float readHalfFloat() throws IOException {
        return halfToFloat(in.readShort());
    }

    /**
     * Reads block coordinates using SignedVarInts.
     */
    public int[] readBlockCoords() throws IOException {
        int x = readSignedVarInt();
        int y = readSignedVarInt() - 80; // Undo Y offset
        int z = readSignedVarInt();
        return new int[]{x, y, z};
    }

    /**
     * Reads a location (x, y, z, yaw, pitch).
     */
    public double[] readLocation() throws IOException {
        int xWhole = readSignedVarInt();
        int yWhole = readSignedVarInt();
        int zWhole = readSignedVarInt();

        float xFrac = readHalfFloat();
        float yFrac = readHalfFloat();
        float zFrac = readHalfFloat();

        float[] look = readCompressedLook();

        return new double[]{
                xWhole + xFrac,
                yWhole + yFrac,
                zWhole + zFrac,
                look[0], // yaw
                look[1]  // pitch
        };
    }

    /**
     * Reads compressed yaw and pitch from 3 bytes.
     */
    public float[] readCompressedLook() throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();

        int packed = (b1 << 16) | (b2 << 8) | b3;
        int yawBits = (packed >> 10) & 0x3FF;
        int pitchBits = packed & 0x3FF;

        // Convert back from radians * 160 to degrees
        float yaw = (float) (yawBits / 160.0 * 180.0 / Math.PI);
        float pitch = (float) (pitchBits / 160.0 * 180.0 / Math.PI);

        return new float[]{yaw, pitch};
    }

    /**
     * Reads a byte array with length prefix.
     */
    public byte[] readBytes() throws IOException {
        int length = readVarInt();
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return bytes;
    }

    /**
     * Reads raw bytes.
     */
    public byte[] readRawBytes(int length) throws IOException {
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return bytes;
    }

    /**
     * Reads a BitSet from bytes.
     */
    public BitSet readBitSet(int numBits) throws IOException {
        int numBytes = (numBits + 7) / 8;
        byte[] bytes = new byte[numBytes];
        in.readFully(bytes);
        return BitSet.valueOf(bytes);
    }

    /**
     * Returns available bytes to read.
     */
    public int available() throws IOException {
        return in.available();
    }

    /**
     * Skips bytes.
     */
    public void skip(int n) throws IOException {
        in.skipBytes(n);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * Converts IEEE 754 half-precision to float.
     */
    private static float halfToFloat(short half) {
        int h = half & 0xFFFF;
        int sign = (h >>> 15) & 0x1;
        int exp = (h >>> 10) & 0x1F;
        int mantissa = h & 0x3FF;

        if (exp == 0) {
            if (mantissa == 0) {
                return sign == 0 ? 0.0f : -0.0f;
            }
            // Subnormal number
            float result = mantissa / 1024.0f * (float) Math.pow(2, -14);
            return sign == 0 ? result : -result;
        } else if (exp == 31) {
            if (mantissa == 0) {
                return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
            }
            return Float.NaN;
        }

        // Normal number
        float result = (1.0f + mantissa / 1024.0f) * (float) Math.pow(2, exp - 15);
        return sign == 0 ? result : -result;
    }
}
