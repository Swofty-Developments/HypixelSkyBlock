package net.swofty.types.generic.levels;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.levels.unlocks.CustomLevelUnlock;
import net.swofty.types.generic.levels.unlocks.SkyBlockLevelStatisticUnlock;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum SkyBlockLevelRequirement {
    LEVEL_0(0, false, List.of(), "§7", null, null),
    LEVEL_1(100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_2(200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_3(300, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_COMMUNITY_SHOP)
    ), "§7", null, null),
    LEVEL_4(400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_5(500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_GARDEN),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_WARDROBE)
    ), "§7", null, null),
    LEVEL_6(600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_7(700, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_8(800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_9(900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_10(1000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_11(1100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_12(1200, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_13(1300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_14(1400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_15(1500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_16(1600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_17(1700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_18(1800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_19(1900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_20(2000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_21(2100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_22(2200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_23(2300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_24(2400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_25(2500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_26(2600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_27(2700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_28(2800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_29(2900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_30(3000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_31(3100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_32(3200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_33(3300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_34(3400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_35(3500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§7", null, null),
    LEVEL_36(3600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_37(3700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_38(3800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_39(3900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_40(4000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", "White Level Prefix", Material.BONE_MEAL),
    LEVEL_41(4100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_42(4200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_43(4300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_44(4400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_45(4500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_46(4600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_47(4700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_48(4800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_49(4900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_50(5000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_51(5100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_52(5200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_53(5300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_54(5400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_55(5500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_56(5600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_57(5700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_58(5800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_59(5900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_60(6000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_61(6100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_62(6200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_63(6300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_64(6400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_65(6500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_66(6600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_67(6700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_68(6800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_69(6900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_70(7000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_71(7100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_72(7200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_73(7300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_74(7400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_75(7500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§f", null, null),
    LEVEL_76(7600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_77(7700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_78(7800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_79(7900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§f", null, null),
    LEVEL_80(8000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", "Yellow Level Prefix", Material.YELLOW_DYE),
    LEVEL_81(8100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_82(8200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_83(8300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_84(8400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_85(8500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_86(8600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_87(8700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_88(8800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_89(8900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_90(9000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_91(9100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_92(9200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_93(9300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_94(9400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_95(9500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_96(9600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_97(9700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_98(9800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_99(9900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_100(10000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_101(10100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_102(10200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_103(10300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_104(10400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_105(10500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_106(10600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_107(10700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_108(10800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_109(10900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_110(11000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_111(11100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_112(11200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_113(11300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_114(11400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_115(11500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§e", null, null),
    LEVEL_116(11600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_117(11700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_118(11800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_119(11900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§e", null, null),
    LEVEL_120(12000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", "Green Level Prefix", Material.LIME_DYE),
    LEVEL_121(12100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_122(12200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_123(12300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_124(12400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_125(12500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_126(12600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_127(12700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_128(12800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_129(12900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_130(13000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_131(13100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_132(13200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_133(13300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_134(13400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_135(13500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_136(13600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_137(13700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_138(13800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_139(13900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_140(14000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_141(14100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_142(14200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_143(14300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_144(14400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_145(14500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_146(14600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_147(14700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_148(14800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_149(14900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_150(15000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_151(15100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_152(15200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_153(15300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_154(15400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_155(15500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§a", null, null),
    LEVEL_156(15600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_157(15700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_158(15800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_159(15900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§a", null, null),
    LEVEL_160(16000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", "Dark Green Level Prefix", Material.GREEN_DYE),
    LEVEL_161(16100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_162(16200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_163(16300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_164(16400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_165(16500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_166(16600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_167(16700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_168(16800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_169(16900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_170(17000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_171(17100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_172(17200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_173(17300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_174(17400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_175(17500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_176(17600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_177(17700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_178(17800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_179(17900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_180(18000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_181(18100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_182(18200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_183(18300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_184(18400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_185(18500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_186(18600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_187(18700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_188(18800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_189(18900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_190(19000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_191(19100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_192(19200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_193(19300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_194(19400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_195(19500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§2", null, null),
    LEVEL_196(19600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_197(19700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_198(19800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_199(19900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§2", null, null),
    LEVEL_200(20000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", "Aqua Level Prefix", Material.LIGHT_BLUE_DYE),
    LEVEL_201(20100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_202(20200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_203(20300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_204(20400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_205(20500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_206(20600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_207(20700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_208(20800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_209(20900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_210(21000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_211(21100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_212(21200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_213(21300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_214(21400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_215(21500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_216(21600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_217(21700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_218(21800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_219(21900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_220(22000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_221(22100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_222(22200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_223(22300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_224(22400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_225(22500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_226(22600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_227(22700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_228(22800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_229(22900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_230(23000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_231(23100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_232(23200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_233(23300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_234(23400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_235(23500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§b", null, null),
    LEVEL_236(23600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_237(23700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_238(23800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_239(23900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§b", null, null),
    LEVEL_240(24000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", "Cyan Level Prefix", Material.CYAN_DYE),
    LEVEL_241(24100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_242(24200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_243(24300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_244(24400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_245(24500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_246(24600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_247(24700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_248(24800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_249(24900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_250(25000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_251(25100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_252(25200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_253(25300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_254(25400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_255(25500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_256(25600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_257(25700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_258(25800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_259(25900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_260(26000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_261(26100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_262(26200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_263(26300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_264(26400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_265(26500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_266(26600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_267(26700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_268(26800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_269(26900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_270(27000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_271(27100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_272(27200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_273(27300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_274(27400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_275(27500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§3", null, null),
    LEVEL_276(27600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_277(27700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_278(27800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_279(27900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§3", null, null),
    LEVEL_280(28000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", "Blue Level Prefix", Material.LAPIS_LAZULI),
    LEVEL_281(28100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_282(28200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_283(28300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_284(28400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_285(28500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_286(28600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_287(28700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_288(28800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_289(28900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_290(29000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_291(29100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_292(29200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_293(29300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_294(29400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_295(29500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_296(29600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_297(29700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_298(29800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_299(29900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_300(30000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_301(30100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_302(30200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_303(30300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_304(30400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_305(30500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_306(30600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_307(30700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_308(30800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_309(30900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_310(31000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_311(31100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_312(31200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_313(31300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_314(31400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_315(31500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§9", null, null),
    LEVEL_316(31600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_317(31700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_318(31800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_319(31900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§9", null, null),
    LEVEL_320(32000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", "Pink Level Prefix", Material.PINK_DYE),
    LEVEL_321(32100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_322(32200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_323(32300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_324(32400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_325(32500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_326(32600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_327(32700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_328(32800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_329(32900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_330(33000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_331(33100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_332(33200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_333(33300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_334(33400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_335(33500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_336(33600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_337(33700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_338(33800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_339(33900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_340(34000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_341(34100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_342(34200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_343(34300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_344(34400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_345(34500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_346(34600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_347(34700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_348(34800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_349(34900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_350(35000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_351(35100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_352(35200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_353(35300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_354(35400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_355(35500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§d", null, null),
    LEVEL_356(35600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_357(35700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_358(35800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_359(35900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§d", null, null),
    LEVEL_360(36000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", "Purple Level Prefix", Material.PURPLE_DYE),
    LEVEL_361(36100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_362(36200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_363(36300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_364(36400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_365(36500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_366(36600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_367(36700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_368(36800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_369(36900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_370(37000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_371(37100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_372(37200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_373(37300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_374(37400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_375(37500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_376(37600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_377(37700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_378(37800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_379(37900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_380(38000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_381(38100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_382(38200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_383(38300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_384(38400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_385(38500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_386(38600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_387(38700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_388(38800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_389(38900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_390(39000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_391(39100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_392(39200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_393(39300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_394(39400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_395(39500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§5", null, null),
    LEVEL_396(39600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_397(39700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_398(39800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_399(39900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§5", null, null),
    LEVEL_400(40000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", "Gold Level Prefix", Material.ORANGE_DYE),
    LEVEL_401(40100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_402(40200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_403(40300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_404(40400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_405(40500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_406(40600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_407(40700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_408(40800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_409(40900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_410(41000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_411(41100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_412(41200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_413(41300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_414(41400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_415(41500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_416(41600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_417(41700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_418(41800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_419(41900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_420(42000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_421(42100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_422(42200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_423(42300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_424(42400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_425(42500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_426(42600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_427(42700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_428(42800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_429(42900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_430(43000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_431(43100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_432(43200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_433(43300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_434(43400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_435(43500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§6", null, null),
    LEVEL_436(43600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_437(43700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_438(43800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_439(43900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§6", null, null),
    LEVEL_440(44000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", "Red Level Prefix", Material.RED_DYE),
    LEVEL_441(44100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_442(44200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_443(44300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_444(44400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_445(44500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_446(44600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_447(44700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_448(44800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_449(44900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_450(45000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_451(45100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_452(45200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_453(45300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_454(45400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_455(45500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_456(45600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_457(45700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_458(45800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_459(45900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_460(46000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_461(46100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_462(46200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_463(46300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_464(46400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_465(46500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_466(46600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_467(46700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_468(46800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_469(46900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_470(47000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_471(47100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_472(47200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_473(47300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_474(47400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_475(47500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§c", null, null),
    LEVEL_476(47600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_477(47700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_478(47800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_479(47900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§c", null, null),
    LEVEL_480(48000, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§4", "Dark Red Level Prefix", Material.REDSTONE),
    LEVEL_481(48100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_482(48200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_483(48300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_484(48400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_485(48500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§4", null, null),
    LEVEL_486(48600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_487(48700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_488(48800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_489(48900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_490(49000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§4", null, null),
    LEVEL_491(49100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_492(49200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_493(49300, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_494(49400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_495(49500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§4", null, null),
    LEVEL_496(49600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_497(49700, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_498(49800, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_499(49900, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§4", null, null),
    LEVEL_500(50000, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().withBase(
                    ItemStatistic.STRENGTH, 1D
            ).build())
    ), "§4", null, null),
    ;

    private final int experience;
    private final boolean isMilestone;
    private final List<SkyBlockLevelUnlock> unlocks;
    private final String prefix;
    private final String prefixDisplay;
    private final Material prefixItem;

    SkyBlockLevelRequirement(int experience, boolean isMilestone, List<SkyBlockLevelUnlock> unlocks, String prefix, String prefixDisplay, Material prefixItem) {
        this.experience = experience;
        this.isMilestone = isMilestone;
        this.unlocks = unlocks;
        this.prefix = prefix;
        this.prefixDisplay = prefixDisplay;
        this.prefixItem = prefixItem;
    }

    public int getCumulativeExperience() {
        int cumulative = 0;
        for (SkyBlockLevelRequirement requirement : values()) {
            cumulative += requirement.experience;
            if (requirement == this) {
                return cumulative;
            }
        }
        return 0;
    }

    public SkyBlockLevelRequirement getNextMilestoneLevel() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.isMilestone && requirement.ordinal() > ordinal()) {
                return requirement;
            }
        }
        return null;
    }

    public List<SkyBlockLevelStatisticUnlock> getStatisticUnlocks() {
        return unlocks.stream()
                .filter(unlock -> unlock instanceof SkyBlockLevelStatisticUnlock)
                .map(unlock -> (SkyBlockLevelStatisticUnlock) unlock)
                .toList();
    }

    public int asInt() {
        return ordinal();
    }

    public String getColor() {
        return "§7";
    }

    public @Nullable SkyBlockLevelRequirement getNextLevel() {
        return ordinal() + 1 < values().length ? values()[ordinal() + 1] : null;
    }

    @Override
    public String toString() {
        return String.valueOf(ordinal());
    }

    public Map<SkyBlockLevelRequirement, String> getPreviousPrefixChanges() {
        HashMap<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.ordinal() < ordinal() && !requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public Map.Entry<SkyBlockLevelRequirement, String> getNextPrefixChange() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.ordinal() > ordinal() && !requirement.prefix.equals(prefix)) {
                return Map.entry(requirement, requirement.prefix);
            }
        }
        return null;
    }

    public int getExperienceOfAllPreviousLevels() {
        return Arrays.stream(values())
                .filter(requirement -> requirement.ordinal() < ordinal())
                .mapToInt(SkyBlockLevelRequirement::getExperience)
                .sum();
    }

    public static Map<SkyBlockLevelRequirement, String> getAllPrefixChanges() {
        HashMap<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (!requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public static SkyBlockLevelRequirement getFromTotalXP(double xp) {
        SkyBlockLevelRequirement toReturn = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (xp < requirement.experience) {
                return toReturn;
            } else {
                toReturn = requirement;
            }
        }
        return toReturn;
    }
}