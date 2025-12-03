package net.swofty.type.skyblockgeneric.item.handlers.lore;

import net.swofty.commons.StringUtility;
import net.swofty.commons.item.PotatoType;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.*;
import java.util.stream.Collectors;

public class LoreRegistry {
    private static final Map<String, LoreConfig> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("SANDBOX_ITEM", new LoreConfig(
                (item, player) -> {
                    List<String> lore = item.getAttributeHandler().getSandboxData().getLore();
                    List<String> loreToDisplay = new ArrayList<>();

                    if (item.getAttributeHandler().getSandboxData().isShowLoreLinesToggle()) {
                        // Add the line number to the start of every line
                        for (int i = 0; i < lore.size(); i++) {
                            loreToDisplay.add("§c" + (i + 1) + ". §7" + lore.get(i));
                        }
                    } else {
                        loreToDisplay.addAll(lore);
                    }

                    // Replace all & with §
                    loreToDisplay.replaceAll(s -> s.replace("&", "§"));

                    return loreToDisplay;
                },
                (item, player) -> item.getAttributeHandler().getSandboxData().getDisplayName().replace("&", "§")
        ));
        register("ENCHANTED_BOOK", new LoreConfig(
                (item, player) -> {
                    List<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments().toList();
                    ArrayList<String> lore = new ArrayList<>();

                    enchantments.forEach(enchantment -> {
                        lore.add("§9" + StringUtility.toNormalCase(enchantment.type().name()) + " " +
                                StringUtility.getAsRomanNumeral(enchantment.level()));
                        StringUtility.splitByWordAndLength(enchantment.type().getDescription(enchantment.level(), player), 30)
                                .forEach(line -> lore.add("§7" + line));
                    });

                    lore.add(" ");
                    lore.add("§7Apply Cost: §3" + enchantments.stream()
                            .mapToInt(enchant -> enchant.type().getApplyCost(enchant.level(), player))
                            .sum() + " Exp Levels");
                    lore.add(" ");

                    Set<String> sourceTypes = enchantments.stream()
                            .flatMap(enchantment -> enchantment.type().getEnch().getGroups().stream())
                            .map(EnchantItemGroups::getDisplayName)
                            .collect(Collectors.toSet());

                    lore.add("§7Applicable on: §9" + String.join("§7, §9", sourceTypes));
                    lore.add("§7Use this on an item in an Anvil to");
                    lore.add("§7apply it!");

                    return lore;
                }, null));
        register("SKYBLOCK_MENU_LORE", new LoreConfig((item, player) -> Arrays.asList(
                "§7View all of your SkyBlock progress,",
                "§7including your Skills, Collections,",
                "§7Recipes, and more!",
                "§e ",
                "§eClick to open!"
        ), (item, player) -> "§aSkyBlock Menu §7(Click)"));
        register("HOT_POTATO_BOOK", new LoreConfig((item, player) -> PotatoType.allLores(), null));
    }

    public static void register(String id, LoreConfig handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static LoreConfig getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}