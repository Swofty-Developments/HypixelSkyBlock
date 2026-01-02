package net.swofty.type.skyblockgeneric.gems;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.commons.ChatColor;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.GemstoneComponent;
import net.swofty.type.skyblockgeneric.item.components.GemstoneImplComponent;

import java.util.List;

@Getter
public enum Gemstone {
    RUBY(List.of(ItemType.ROUGH_RUBY_GEM, ItemType.FLAWED_RUBY_GEM, ItemType.FINE_RUBY_GEM, ItemType.FLAWLESS_RUBY_GEM, ItemType.PERFECT_RUBY_GEM), ChatColor.RED, ItemStatistic.HEALTH),
    AMETHYST(List.of(ItemType.ROUGH_AMETHYST_GEM, ItemType.FLAWED_AMETHYST_GEM, ItemType.FINE_AMETHYST_GEM, ItemType.FLAWLESS_AMETHYST_GEM, ItemType.PERFECT_AMETHYST_GEM), ChatColor.DARK_PURPLE, ItemStatistic.DEFENSE),
    JADE(List.of(ItemType.ROUGH_JADE_GEM, ItemType.FLAWED_JADE_GEM, ItemType.FINE_JADE_GEM, ItemType.FLAWLESS_JADE_GEM, ItemType.PERFECT_JADE_GEM), ChatColor.GREEN, null),
    SAPPHIRE(List.of(ItemType.ROUGH_SAPPHIRE_GEM, ItemType.FLAWED_SAPPHIRE_GEM, ItemType.FINE_SAPPHIRE_GEM, ItemType.FLAWLESS_SAPPHIRE_GEM, ItemType.PERFECT_SAPPHIRE_GEM), ChatColor.DARK_AQUA, ItemStatistic.INTELLIGENCE),
    AMBER(List.of(ItemType.ROUGH_AMBER_GEM, ItemType.FLAWED_AMBER_GEM, ItemType.FINE_AMBER_GEM, ItemType.FLAWLESS_AMBER_GEM, ItemType.PERFECT_AMBER_GEM), ChatColor.GOLD, ItemStatistic.MINING_SPEED),
    TOPAZ(List.of(ItemType.ROUGH_TOPAZ_GEM, ItemType.FLAWED_TOPAZ_GEM, ItemType.FINE_TOPAZ_GEM, ItemType.FLAWLESS_TOPAZ_GEM, ItemType.PERFECT_TOPAZ_GEM), ChatColor.YELLOW, null),
    JASPER(List.of(ItemType.ROUGH_JASPER_GEM, ItemType.FLAWED_JASPER_GEM, ItemType.FINE_JASPER_GEM, ItemType.FLAWLESS_JASPER_GEM, ItemType.PERFECT_JASPER_GEM), ChatColor.LIGHT_PURPLE, ItemStatistic.STRENGTH),
    OPAL(List.of(ItemType.ROUGH_OPAL_GEM, ItemType.FLAWED_OPAL_GEM, ItemType.FINE_OPAL_GEM, ItemType.FLAWLESS_OPAL_GEM, ItemType.PERFECT_OPAL_GEM), ChatColor.WHITE, ItemStatistic.TRUE_DEFENSE),
    AQUAMARINE(List.of(ItemType.ROUGH_AQUAMARINE_GEM, ItemType.FLAWED_AQUAMARINE_GEM, ItemType.FINE_AQUAMARINE_GEM, ItemType.FLAWLESS_AQUAMARINE_GEM, ItemType.PERFECT_AQUAMARINE_GEM), ChatColor.AQUA, ItemStatistic.FISHING_SPEED),
    CITRINE(List.of(ItemType.ROUGH_CITRINE_GEM, ItemType.FLAWED_CITRINE_GEM, ItemType.FINE_CITRINE_GEM, ItemType.FLAWLESS_CITRINE_GEM, ItemType.PERFECT_CITRINE_GEM), ChatColor.GOLD, ItemStatistic.FORAGING_FORTUNE),
    PERIDOT(List.of(ItemType.ROUGH_PERIDOT_GEM, ItemType.FLAWED_PERIDOT_GEM, ItemType.FINE_PERIDOT_GEM, ItemType.FLAWLESS_PERIDOT_GEM, ItemType.PERFECT_PERIDOT_GEM), ChatColor.GREEN, ItemStatistic.FARMING_FORTUNE),
    ONYX(List.of(ItemType.ROUGH_ONYX_GEM, ItemType.FLAWED_ONYX_GEM, ItemType.FINE_ONYX_GEM, ItemType.FLAWLESS_ONYX_GEM, ItemType.PERFECT_ONYX_GEM), ChatColor.BLACK, ItemStatistic.CRIT_DAMAGE),
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
