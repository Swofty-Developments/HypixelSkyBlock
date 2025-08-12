package net.swofty.type.skyblockgeneric.gems;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.commons.ChatColor;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.GemstoneComponent;
import net.swofty.type.generic.item.components.GemstoneImplComponent;

import java.util.List;

@Getter
public enum Gemstone {
    RUBY(List.of(ItemType.ROUGH_RUBY_GEMSTONE, ItemType.FLAWED_RUBY_GEMSTONE, ItemType.FINE_RUBY_GEMSTONE, ItemType.FLAWLESS_RUBY_GEMSTONE, ItemType.PERFECT_RUBY_GEMSTONE), ChatColor.RED, ItemStatistic.HEALTH),
    AMETHYST(List.of(ItemType.ROUGH_AMETHYST_GEMSTONE, ItemType.FLAWED_AMETHYST_GEMSTONE, ItemType.FINE_AMETHYST_GEMSTONE, ItemType.FLAWLESS_AMETHYST_GEMSTONE, ItemType.PERFECT_AMETHYST_GEMSTONE), ChatColor.DARK_PURPLE, ItemStatistic.DEFENSE),
    JADE(List.of(ItemType.ROUGH_JADE_GEMSTONE, ItemType.FLAWED_JADE_GEMSTONE, ItemType.FINE_JADE_GEMSTONE, ItemType.FLAWLESS_JADE_GEMSTONE, ItemType.PERFECT_JADE_GEMSTONE), ChatColor.GREEN, null),
    SAPPHIRE(List.of(ItemType.ROUGH_SAPPHIRE_GEMSTONE, ItemType.FLAWED_SAPPHIRE_GEMSTONE, ItemType.FINE_SAPPHIRE_GEMSTONE, ItemType.FLAWLESS_SAPPHIRE_GEMSTONE, ItemType.PERFECT_SAPPHIRE_GEMSTONE), ChatColor.DARK_AQUA, ItemStatistic.INTELLIGENCE),
    AMBER(List.of(ItemType.ROUGH_AMBER_GEMSTONE, ItemType.FLAWED_AMBER_GEMSTONE, ItemType.FINE_AMBER_GEMSTONE, ItemType.FLAWLESS_AMBER_GEMSTONE, ItemType.PERFECT_AMBER_GEMSTONE), ChatColor.GOLD, ItemStatistic.MINING_SPEED),
    TOPAZ(List.of(ItemType.ROUGH_TOPAZ_GEMSTONE, ItemType.FLAWED_TOPAZ_GEMSTONE, ItemType.FINE_TOPAZ_GEMSTONE, ItemType.FLAWLESS_TOPAZ_GEMSTONE, ItemType.PERFECT_TOPAZ_GEMSTONE), ChatColor.YELLOW, null),
    JASPER(List.of(ItemType.ROUGH_JASPER_GEMSTONE, ItemType.FLAWED_JASPER_GEMSTONE, ItemType.FINE_JASPER_GEMSTONE, ItemType.FLAWLESS_JASPER_GEMSTONE, ItemType.PERFECT_JASPER_GEMSTONE), ChatColor.LIGHT_PURPLE, ItemStatistic.STRENGTH),
    ;

    public final List<ItemType> item;
    public final ItemStatistic correspondingStatistic;
    public final ChatColor color;

    Gemstone(List<ItemType> item, ChatColor color, ItemStatistic correspondingStatistic) {
        this.item = item;
        this.color = color;
        this.correspondingStatistic = correspondingStatistic;
    }

    @SneakyThrows
    public static Integer getExtraStatisticFromGemstone(ItemStatistic statistic, SkyBlockItem item) {
        ItemAttributeGemData.GemData gemData;
        if (item.hasComponent(GemstoneComponent.class))
            gemData = item.getAttributeHandler().getGemData();
        else return 0;

        int toReturn = 0;
        for (ItemAttributeGemData.GemData.GemSlots slot : gemData.slots) {
            ItemType gem = slot.filledWith;
            if (gem == null) continue;

            GemstoneImplComponent impl = new SkyBlockItem(gem).getComponent(GemstoneImplComponent.class);
            GemRarity gemRarity = impl.getGemRarity();
            Gemstone gemstone = impl.getGemstone();
            ItemStatistic correspondingStatistic = gemstone.getCorrespondingStatistic();

            if (correspondingStatistic != statistic) continue;
            toReturn += GemStats.getFromGemstoneAndRarity(gemstone, gemRarity)
                    .getFromRarity(item.getAttributeHandler().getRarity());
        }

        return toReturn;
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
