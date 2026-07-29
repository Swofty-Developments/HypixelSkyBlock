package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;

import java.util.List;
import java.util.Map;

public final class StatisticArrow {
    private static final String RED = "e44736f86be74deae5886a323df59d995aa39bea76c17b45baf832f4448c021c";
    private static final Map<String, String> TEXTURES = Map.ofEntries(
        Map.entry("§c", RED),
        Map.entry("§a", "8cd690ae9d4f09745fb9a55579df72b7a0aebc9653aa42ed490c6d036580f4ca"),
        Map.entry("§b", "9b02d8d0645d2f6e0caa8c3fa4facde0ecf8d5c4c92511be69577a12ad9ebe88"),
        Map.entry("§e", "17fa3bae5d8a844594c98ff87791a7c0d1b9e1370c21b6b04354e3ecf5b6a3a5"),
        Map.entry("§f", "33f4b333f1c6ff8d9a13747dfc5a047c77c079ab6480f9ef64d5c85ec740fce4"),
        Map.entry("§4", "580b4c9a1f7976da0d09b8394bb19f34257a5acd82906aaeba8ab020f825acbf"),
        Map.entry("§9", "add45dceae3989edff0f93c22da51884370ddf6096aa708a054c0515d62bf675")
    );

    public static ItemStack.Builder create(ItemStatistic statistic) {
        return ItemStackCreator.getUsingGUIMaterial(statistic.getDisplayColor() + "➭",
            new GUIMaterial(TEXTURES.getOrDefault(statistic.getDisplayColor(), RED)), 1, List.of());
    }
}
