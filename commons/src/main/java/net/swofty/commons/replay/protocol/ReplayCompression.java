package net.swofty.commons.replay.protocol;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReplayCompression {
    private static final int LEVEL = 3;

    public static byte[] compress(byte[] data) {
        return Zstd.compress(data, LEVEL);
    }

    public static byte[] decompress(byte[] compressedData) throws IOException {
        long size = Zstd.getFrameContentSize(compressedData);
        if (size <= 0 || size > Integer.MAX_VALUE) {
            throw new IOException("Invalid zstd frame content size: " + size);
        }

        try {
            return Zstd.decompress(compressedData, (int) size);
        } catch (ZstdException e) {
            throw new IOException("Failed to decompress replay zstd payload", e);
        }
    }
}
