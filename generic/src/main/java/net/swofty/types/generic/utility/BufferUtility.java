package net.swofty.types.generic.utility;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BufferUtility {
    public static int getUnsignedShort(ByteBuffer buffer) {
        byte[] bytes = new byte[2];
        buffer.get(bytes);

        int sum = 0;
        int i = 0;
        for (byte b : bytes) {
            sum += Byte.toUnsignedInt(b) << (i++ * 8);
        }

        return sum;
    }

    public static String getNBSString(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.getInt()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
