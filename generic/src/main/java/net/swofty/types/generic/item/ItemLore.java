package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.attribute.AttributeHandler;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ItemLore {
    private final ArrayList<Component> loreLines = new ArrayList<>();

    @Getter
    private ItemStack stack;

    public ItemLore(ItemStack stack) {
        this.stack = stack;
    }

    @SneakyThrows
    public void updateLore(@Nullable SkyBlockPlayer player) {
        SkyBlockItem item = new SkyBlockItem(stack);
        AttributeHandler handler = item.getAttributeHandler();

        Rarity rarity = handler.getRarity();
        String type = handler.getItemType();
        boolean recombobulated = handler.isRecombobulated();
        ItemStatistics statistics = handler.getStatistics();
        Class<?> clazz = item.clazz;

        if (recombobulated) rarity = rarity.upgrade();

        String displayName = StringUtility.toNormalCase(type);
        String displayRarity = rarity.getDisplay();

        if (clazz != null) {
            CustomSkyBlockItem skyBlockItem = (CustomSkyBlockItem) item.getGenericInstance();

            if (skyBlockItem.getAbsoluteLore(player, item) != null) {
                skyBlockItem.getAbsoluteLore(player, item).forEach(line -> addLoreLine("§7" + line));
                this.stack = stack.withLore(loreLines)
                        .withDisplayName(Component.text(skyBlockItem.getAbsoluteName(player, item))
                                .decoration(TextDecoration.ITALIC, false));
                return;
            }

            // Handle Item Statistics
            if (handler.isMiningTool()) {
                addLoreLine("§8Breaking Power " + handler.getBreakingPower());
                addLoreLine(null);
            }

            boolean damage = addPossiblePropertyInt(ItemStatistic.DAMAGE, statistics.get(ItemStatistic.DAMAGE),
                    handler.getReforge(), rarity);
            boolean defence = addPossiblePropertyInt(ItemStatistic.DEFENSE, statistics.get(ItemStatistic.DEFENSE),
                    handler.getReforge(), rarity);
            boolean health = addPossiblePropertyInt(ItemStatistic.HEALTH, statistics.get(ItemStatistic.HEALTH),
                    handler.getReforge(), rarity);
            boolean strength = addPossiblePropertyInt(ItemStatistic.STRENGTH, statistics.get(ItemStatistic.STRENGTH),
                    handler.getReforge(), rarity);
            boolean intelligence = addPossiblePropertyInt(ItemStatistic.INTELLIGENCE, statistics.get(ItemStatistic.INTELLIGENCE),
                    handler.getReforge(), rarity);
            boolean miningSpeed = addPossiblePropertyInt(ItemStatistic.MINING_SPEED, statistics.get(ItemStatistic.MINING_SPEED),
                    handler.getReforge(), rarity);
            boolean speed = addPossiblePropertyInt(ItemStatistic.SPEED, statistics.get(ItemStatistic.SPEED),
                    handler.getReforge(), rarity);

            if (damage || defence || health || strength || intelligence || miningSpeed || speed) addLoreLine(null);

            // Handle Item Enchantments
            if (item.getGenericInstance() instanceof Enchantable enchantable) {
                if (enchantable.showEnchantLores()) {
                    long enchantmentCount = handler.getEnchantments().toList().size();
                    if (enchantmentCount < 4) {
                        handler.getEnchantments().forEach((enchantment) -> {
                            addLoreLine("§9" + enchantment.type().getName() +
                                    " " + StringUtility.getAsRomanNumeral(enchantment.level()));
                            StringUtility.splitByWordAndLength(
                                    "§7" + enchantment.type().getDescription(enchantment.level()),
                                    34, " ").forEach(this::addLoreLine);
                        });

                    } else {
                        String enchantmentNames = handler.getEnchantments().toList().stream().map(enchantment1 ->
                                        "§9" + enchantment1.type().getName() + " " + StringUtility
                                                .getAsRomanNumeral(enchantment1.level()))
                                .collect(Collectors.joining(", "));
                        StringUtility.splitByWordAndLength(enchantmentNames, 34, ",").forEach(this::addLoreLine);
                    }

                    if (enchantmentCount != 0) addLoreLine(null);
                }
            }

            // Handle Custom Item Lore
            if (skyBlockItem.getLore(player, item) != null) {
                skyBlockItem.getLore(player, item).forEach(line -> addLoreLine("§7" + line));
                addLoreLine(null);
            }

            // Handle Custom Item Ability
            if (item.getGenericInstance() instanceof CustomSkyBlockAbility ability) {
                addLoreLine("§6Ability: " + ability.getAbilityName() + "  §e§l" +
                        ability.getAbilityActivation().getDisplay());
                for (String line : StringUtility.splitByWordAndLength(ability.getAbilityDescription(), 34, "\\s"))
                    addLoreLine("§7" + line);
                if (ability.getManaCost() > 0)
                    addLoreLine("§8Mana Cost: §3" + ability.getManaCost());
                if (ability.getAbilityCooldownTicks() > 20)
                    addLoreLine("§8Cooldown: §a" + StringUtility.commaify((double) ability.getAbilityCooldownTicks() / 20) + "s");

                addLoreLine(null);
            }

            // Handle full set abilities
            if (ArmorSetRegistry.getArmorSet(handler.getItemTypeAsType()) != null) {
                ArmorSet armorSet = ArmorSetRegistry.getArmorSet(handler.getItemTypeAsType()).getClazz().getDeclaredConstructor().newInstance();

                int wearingAmount = 0;
                if (player != null && player.isWearingItem(item)) {
                    for (SkyBlockItem armorItem : player.getArmor()) {
                        if (armorItem == null) continue;
                        if (ArmorSetRegistry.getArmorSet(armorItem.getAttributeHandler().getItemTypeAsType()) == null)
                            continue;
                        if (ArmorSetRegistry.getArmorSet(armorItem.getAttributeHandler().getItemTypeAsType()).getClazz() == armorSet.getClass()) {
                            wearingAmount++;
                        }
                    }
                }

                addLoreLine("§6Full Set Bonus: " + armorSet.getName() + " (" + wearingAmount + "/4)");
                for (String line : StringUtility.splitByWordAndLength(armorSet.getDescription(), 36, "\\s"))
                    addLoreLine("§7" + line);
                addLoreLine(null);
            }

            if (item.getGenericInstance() instanceof ExtraRarityDisplay)
                displayRarity = displayRarity + ((ExtraRarityDisplay) item.getGenericInstance()).getExtraRarityDisplay();

            if (item.getGenericInstance() instanceof Reforgable) {
                addLoreLine("§8This item can be reforged!");
                if (handler.getReforge() != null)
                    displayName = handler.getReforge().prefix() + " " + displayName;
            }
        }

        if (recombobulated)
            displayRarity = rarity.getColor() + "&kL " + displayRarity + " &kL";

        displayName = rarity.getColor() + displayName;
        addLoreLine(displayRarity);
        this.stack = stack.withLore(loreLines)
                .withDisplayName(Component.text(displayName)
                        .decoration(TextDecoration.ITALIC, false));
    }

    public static String getBaseName(ItemStack stack) {
        return StringUtility.toNormalCase(new SkyBlockItem(stack).getAttributeHandler().getItemType());
    }

    private boolean addPossiblePropertyInt(ItemStatistic statistic, double overallValue,
                                           ReforgeType.Reforge reforge, Rarity rarity) {
        double reforgeValue = 0;
        if (reforge != null) {
            overallValue += reforge.getBonusCalculation(statistic, rarity.ordinal() + 1);
            reforgeValue = reforge.getBonusCalculation(statistic, rarity.ordinal() + 1);
        }

        if (overallValue == 0) return false;

        String color = statistic.isRed() ? "&c" : "&a";
        String line = "§7" + StringUtility.toNormalCase(statistic.getDisplayName()) + ": " +
                color + statistic.getPrefix() + overallValue + statistic.getSuffix() + " ";

        if (reforgeValue != 0)
            line += "§9(" + (reforgeValue > 0 ? "+" : "") + reforgeValue + ")";

        addLoreLine(line);
        return true;
    }

    private void addLoreLine(String line) {
        if (line == null) {
            loreLines.add(Component.empty());
            return;
        }

        loreLines.add(Component.text("§r" + line.replace("&", "§"))
                .decorations(Collections.singleton(TextDecoration.ITALIC), false));
    }
}

