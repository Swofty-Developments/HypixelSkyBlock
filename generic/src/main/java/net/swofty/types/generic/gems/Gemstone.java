package net.swofty.types.generic.gems;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.GemstoneItem;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.fine.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.rough.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.ChatColor;

import java.util.List;

@Getter
public enum Gemstone {
    RUBY(List.of(RoughRuby.class, FlawedRuby.class, FineRuby.class, FlawlessRuby.class, PerfectRuby.class), ChatColor.RED, ItemStatistic.HEALTH),
    AMETHYST(List.of(RoughAmethyst.class, FlawedAmethyst.class, FineAmethyst.class, FlawlessAmethyst.class, PerfectAmethyst.class), ChatColor.DARK_PURPLE, ItemStatistic.DEFENSE),
    JADE(List.of(RoughJade.class, FlawedJade.class, FineJade.class, FlawlessJade.class, PerfectJade.class), ChatColor.GREEN, null),
    SAPPHIRE(List.of(RoughSapphire.class, FlawedSapphire.class, FineSapphire.class, FlawlessSapphire.class, PerfectSapphire.class), ChatColor.DARK_AQUA, ItemStatistic.INTELLIGENCE),
    AMBER(List.of(RoughAmber.class, FlawedAmber.class, FineAmber.class, FlawlessAmber.class, PerfectAmber.class), ChatColor.GOLD, ItemStatistic.MINING_SPEED),
    TOPAZ(List.of(RoughTopaz.class, FlawedTopaz.class, FineTopaz.class, FlawlessTopaz.class, PerfectTopaz.class), ChatColor.YELLOW, null),
    JASPER(List.of(RoughJasper.class, FlawedJasper.class, FineJasper.class, FlawlessJasper.class, PerfectJasper.class), ChatColor.LIGHT_PURPLE, ItemStatistic.STRENGTH),
    ;

    public final List<Class<?>> clazz;
    public final ItemStatistic correspondingStatistic;
    public final ChatColor color;

    Gemstone(List<Class<?>> clazz, ChatColor color, ItemStatistic correspondingStatistic) {
        this.clazz = clazz;
        this.color = color;
        this.correspondingStatistic = correspondingStatistic;
    }

    @SneakyThrows
    public static Integer getExtraStatisticFromGemstone(ItemStatistic statistic, SkyBlockItem item) {
        ItemAttributeGemData.GemData gemData;
        if (item.getGenericInstance() instanceof GemstoneItem asGemstone)
            gemData = item.getAttributeHandler().getGemData();
        else return 0;

        int toReturn = 0;
        for (ItemAttributeGemData.GemData.GemSlots slot : gemData.slots) {
            ItemType gem = slot.filledWith;
            if (gem == null) continue;

            GemstoneImpl impl = ((GemstoneImpl) gem.clazz.getDeclaredConstructor().newInstance());
            GemRarity gemRarity = impl.getAssociatedGemRarity();
            Gemstone gemstone = impl.getAssociatedGemstone();
            ItemStatistic correspondingStatistic = gemstone.getCorrespondingStatistic();

            if (correspondingStatistic != statistic) continue;
            toReturn += GemStats.getFromGemstoneAndRarity(gemstone, gemRarity)
                    .getFromRarity(item.getAttributeHandler().getRarity());
        }

        return toReturn;
    }

    public static Gemstone getGemFromClazz(Class<?> clazz) {
        for (Gemstone gemstone : Gemstone.values()) {
            if (gemstone.clazz.contains(clazz)) {
                return gemstone;
            }
        }
        return null;
    }

    public enum Slots {
        //NORMAL
        RUBY("Ruby", ChatColor.RED, "❤", (short) 14, List.of(Gemstone.RUBY)),
        AMETHYST("Amethyst", ChatColor.DARK_PURPLE, "❈", (short) 10, List.of(Gemstone.AMETHYST)),
        JADE("Jade", ChatColor.GREEN, "☘", (short) 13, List.of(Gemstone.JADE)),
        SAPPHIRE("Sapphire", ChatColor.GREEN, "✎", (short) 5, List.of(Gemstone.SAPPHIRE)),
        AMBER("Amber", ChatColor.GOLD, "⸕", (short) 1, List.of(Gemstone.AMBER)),
        TOPAZ("Topaz", ChatColor.YELLOW, "✧", (short) 4, List.of(Gemstone.TOPAZ)),
        JASPER("Jasper", ChatColor.LIGHT_PURPLE, "❁", (short) 2, List.of(Gemstone.JASPER)),

        //SPECIAL
        COMBAT("Combat", ChatColor.RED, "⚔", (short) 14, List.of(Gemstone.RUBY, Gemstone.AMETHYST, Gemstone.SAPPHIRE, Gemstone.JASPER)),
        OFFENSIVE("Offensive", ChatColor.BLUE, "☠", (short) 11, List.of(Gemstone.SAPPHIRE, Gemstone.JASPER)),
        DEFENSIVE("Defensive", ChatColor.GREEN, "☤", (short) 13, List.of(Gemstone.RUBY, Gemstone.AMETHYST)),
        MINING("Mining", ChatColor.GREEN, "✦", (short) 5, List.of(Gemstone.JADE, Gemstone.AMBER, Gemstone.TOPAZ)),
        UNIVERSAL("Universal", ChatColor.WHITE, "❂", (short) 0, List.of(Gemstone.values())),
        ;

        @Getter
        public final String name;
        @Getter
        public final ChatColor color;
        @Getter
        public final String symbol;
        @Getter
        public final short paneColour;
        @Getter
        public final List<Gemstone> validGemstones;

        Slots(String name, ChatColor color, String symbol, short paneColour, List<Gemstone> validGemstones) {
            this.name = name;
            this.symbol = symbol;
            this.color = color;
            this.paneColour = paneColour;
            this.validGemstones = validGemstones;
        }

        public static Slots getFromGemstone(Gemstone gemstone) {
            for (Slots slots : Slots.values()) {
                if (slots.getValidGemstones().contains(gemstone) && slots.getValidGemstones().size() == 1) {
                    return slots;
                }
            };
            return null;
        }
    }
}
