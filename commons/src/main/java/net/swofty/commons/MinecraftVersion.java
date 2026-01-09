package net.swofty.commons;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum MinecraftVersion {
    MINECRAFT_1_7_2(4, new String[]{"1.7.2", "1.7.3", "1.7.4", "1.7.5"}),
    MINECRAFT_1_7_6(5, new String[]{"1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10"}),
    MINECRAFT_1_8(47, new String[]{"1.8", "1.8.1", "1.8.2", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8", "1.8.9"}),
    MINECRAFT_1_9(107, new String[]{"1.9"}),
    MINECRAFT_1_9_1(108, new String[]{"1.9.1"}),
    MINECRAFT_1_9_2(109, new String[]{"1.9.2"}),
    MINECRAFT_1_9_4(110, new String[]{"1.9.3", "1.9.4"}),
    MINECRAFT_1_10(210, new String[]{"1.10", "1.10.1", "1.10.2"}),
    MINECRAFT_1_11(315, new String[]{"1.11"}),
    MINECRAFT_1_11_1(316, new String[]{"1.11.1", "1.11.2"}),
    MINECRAFT_1_12(335, new String[]{"1.12"}),
    MINECRAFT_1_12_1(338, new String[]{"1.12.1"}),
    MINECRAFT_1_12_2(340, new String[]{"1.12.2"}),
    MINECRAFT_1_13(393, new String[]{"1.13"}),
    MINECRAFT_1_13_1(401, new String[]{"1.13.1"}),
    MINECRAFT_1_13_2(404, new String[]{"1.13.2"}),
    MINECRAFT_1_14(477, new String[]{"1.14"}),
    MINECRAFT_1_14_1(480, new String[]{"1.14.1"}),
    MINECRAFT_1_14_2(485, new String[]{"1.14.2"}),
    MINECRAFT_1_14_3(490, new String[]{"1.14.3"}),
    MINECRAFT_1_14_4(498, new String[]{"1.14.4"}),
    MINECRAFT_1_15(573, new String[]{"1.15"}),
    MINECRAFT_1_15_1(575, new String[]{"1.15.1"}),
    MINECRAFT_1_15_2(578, new String[]{"1.15.2"}),
    MINECRAFT_1_16(735, new String[]{"1.16"}),
    MINECRAFT_1_16_1(736, new String[]{"1.16.1"}),
    MINECRAFT_1_16_2(751, new String[]{"1.16.2"}),
    MINECRAFT_1_16_3(753, new String[]{"1.16.3"}),
    MINECRAFT_1_16_4(754, new String[]{"1.16.4", "1.16.5"}),
    MINECRAFT_1_17(755, new String[]{"1.17"}),
    MINECRAFT_1_17_1(756, new String[]{"1.17.1"}),
    MINECRAFT_1_18(757, new String[]{"1.18", "1.18.1"}),
    MINECRAFT_1_18_2(758, new String[]{"1.18.2"}),
    MINECRAFT_1_19(759, new String[]{"1.19"}),
    MINECRAFT_1_19_1(760, new String[]{"1.19.1", "1.19.2"}),
    MINECRAFT_1_19_3(761, new String[]{"1.19.3"}),
    MINECRAFT_1_19_4(762, new String[]{"1.19.4"}),
    MINECRAFT_1_20(763, new String[]{"1.20", "1.20.1"}),
    MINECRAFT_1_20_2(764, new String[]{"1.20.2"}),
    MINECRAFT_1_20_3(765, new String[]{"1.20.3", "1.20.4"}),
    MINECRAFT_1_20_5(766, new String[]{"1.20.5", "1.20.6"}),
    MINECRAFT_1_21(767, new String[]{"1.21", "1.21.1"}),
    MINECRAFT_1_21_2(768, new String[]{"1.21.2", "1.21.3"}),
    MINECRAFT_1_21_4(769, new String[]{"1.21.4"}),
    MINECRAFT_1_21_5(770, new String[]{"1.21.5"}),
    MINECRAFT_1_21_6(771, new String[]{"1.21.6"}),
    MINECRAFT_1_21_7(772, new String[]{"1.21.7"}),
    MINECRAFT_1_21_8(772, new String[]{"1.21.8"}),
    MINECRAFT_1_21_9(773, new String[]{"1.21.9"}),
    MINECRAFT_1_21_10(773, new String[]{"1.21.10"}),
    MINECRAFT_1_21_11(774, new String[]{"1.21.11"}),
    ;

    private final int protocol;
    private final String[] versions;

    MinecraftVersion(int protocol, String[] versions) {
        this.protocol = protocol;
        this.versions = versions;
    }

    public static MinecraftVersion byProtocolId(int protocolID) {
        return Arrays.stream(MinecraftVersion.values())
                .filter(version -> version.protocol == protocolID)
                .findFirst()
                .orElse(null);
    }

}
