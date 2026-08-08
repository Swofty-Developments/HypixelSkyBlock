package net.swofty.commons;

import lombok.Getter;

@Getter
public enum MinecraftVersion {
    MINECRAFT_1_20_5(766, 41.0, 32),
    MINECRAFT_1_21(767, 48.0, 34),
    MINECRAFT_1_21_2(768, 57.0, 42),
    MINECRAFT_1_21_4(769, 61.0, 46),
    MINECRAFT_1_21_5(770, 71.0, 55),
    MINECRAFT_1_21_6(771, 80.0, 63),
    MINECRAFT_1_21_7(772, 81.0, 64),
    MINECRAFT_1_21_9(773, 88.0, 69),
    MINECRAFT_1_21_11(774, 94.1, 75),
    MINECRAFT_26_1(775, 101.1, 84),
    MINECRAFT_26_2(776, 107.1, 88);

    private final int protocolVersion;
    private final double dataPackVersion;
    private final int packVersion;

    MinecraftVersion(int protocolVersion, double dataPackVersion, int packVersion) {
        this.protocolVersion = protocolVersion;
        this.dataPackVersion = dataPackVersion;
        this.packVersion = packVersion;
    }

    public static MinecraftVersion byProtocol(int protocolVersion) {
        for (MinecraftVersion version : values()) {
            if (version.protocolVersion == protocolVersion) {
                return version;
            }
        }
        return null;
    }

    public static MinecraftVersion latest() {
        return MINECRAFT_26_2;
    }
}
