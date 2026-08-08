package net.swofty.type.skyblockgeneric.resourcepack;

final class HypixelPackFormatResolver {
    private HypixelPackFormatResolver() {
    }

    static int packFormatForProtocol(int protocolVersion) {
        if (protocolVersion > 776) {
            return Integer.MAX_VALUE;
        }
        if (protocolVersion >= 776) {
            return 88;
        }
        if (protocolVersion >= 775) {
            return 84;
        }
        if (protocolVersion >= 774) {
            return 75;
        }
        if (protocolVersion >= 773) {
            return 69;
        }
        if (protocolVersion >= 772) {
            return 64;
        }
        if (protocolVersion >= 771) {
            return 63;
        }
        if (protocolVersion >= 770) {
            return 55;
        }
        if (protocolVersion >= 769) {
            return 46;
        }
        if (protocolVersion >= 768) {
            return 42;
        }
        if (protocolVersion >= 766) {
            return 34;
        }
        return 0;
    }
}
