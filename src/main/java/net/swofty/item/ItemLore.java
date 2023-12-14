package net.swofty.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.swofty.Utility;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.ItemStatistic;
import net.swofty.item.impl.ItemStatistics;
import net.swofty.user.PlayerStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemLore {
    private ArrayList<Component> loreLines = new ArrayList<>();

    @Getter
    private ItemStack stack;

    public ItemLore(ItemStack stack) {
        this.stack = stack;
    }

    @SneakyThrows
    public void updateLore() {
        SkyBlockItem item = new SkyBlockItem(stack);
        AttributeHandler handler = item.getAttributeHandler();

        Rarity rarity = handler.getRarity();
        String type = handler.getItemType();
        boolean recombobulated = handler.isRecombobulated();
        ItemStatistics statistics = handler.getStatistics();
        Class<?> clazz = item.clazz;

        if (recombobulated) {
            rarity = rarity.upgrade();
        }

        if (clazz != null) {
            // Handle Item Statistics
            boolean damage = addPossiblePropertyInt(ItemStatistic.DAMAGE, statistics.get(ItemStatistic.DAMAGE));
            boolean defence = addPossiblePropertyInt(ItemStatistic.DEFENSE, statistics.get(ItemStatistic.DEFENSE));
            boolean health = addPossiblePropertyInt(ItemStatistic.HEALTH, statistics.get(ItemStatistic.HEALTH));
            boolean strength = addPossiblePropertyInt(ItemStatistic.STRENGTH, statistics.get(ItemStatistic.STRENGTH));
            boolean intelligence = addPossiblePropertyInt(ItemStatistic.INTELLIGENCE, statistics.get(ItemStatistic.INTELLIGENCE));
            if (damage || defence || health || strength || intelligence) addLoreLine(null);

            // Handle Custom Item Lore
            CustomSkyBlockItem skyBlockItem = ((CustomSkyBlockItem) item.clazz.newInstance());
            if (skyBlockItem.getLore() != null) {
                skyBlockItem.getLore().forEach(line -> addLoreLine("§7" + line));
                addLoreLine(null);
            }
        }

        if (recombobulated) {
            addLoreLine(rarity.getColor() + "&kL " + rarity.getDisplay() + " &kL");
        } else {
            addLoreLine(rarity.getDisplay());
        }

        this.stack = stack.withLore(loreLines)
                .withDisplayName(Component.text(rarity.getColor() + Utility.toNormalCase(type))
                        .decoration(TextDecoration.ITALIC, false));
    }

    private boolean addPossiblePropertyInt(ItemStatistic statistic, int baseValue) {
        if (baseValue == 0) return false;

        String color = statistic.isRed() ? "&c" : "&a";
        addLoreLine("§7" + Utility.toNormalCase(statistic.getDisplayName()) + ": " +
                color + statistic.getPrefix() + baseValue + statistic.getSuffix());

        return true;
    }

    private void addLoreLine(String line) {
        if (line == null) {
            loreLines.add(Component.empty());
            return;
        }
        line = line.replace("&", "§");
        loreLines.add(Component.text("§r" + line)
                .decorations(Collections.singleton(TextDecoration.ITALIC), false));
    }
}
