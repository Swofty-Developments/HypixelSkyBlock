package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.attribute.ItemAttributeHandler;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeSoulbound;
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
import java.util.Map;
import java.util.stream.Collectors;

public class ItemLore {
    private final ArrayList<Component> loreLines = new ArrayList<>();

    @Getter
    private ItemStack stack;

    public ItemLore(ItemStack stack) {
        this.stack = stack;
    }

    public static String getBaseName(ItemStack stack) {
        return StringUtility.toNormalCase(new SkyBlockItem(stack).getAttributeHandler().getItemType());
    }

    @SneakyThrows
    public void updateLore(@Nullable SkyBlockPlayer player) {
        SkyBlockItem item = new SkyBlockItem(stack);
        ItemAttributeHandler handler = item.getAttributeHandler();

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
            displayName = handler.getItemTypeAsType().getDisplayName(item);

            if (skyBlockItem.getAbsoluteLore(player, item) != null) {
                skyBlockItem.getAbsoluteLore(player, item).forEach(line -> addLoreLine("§7" + line));
                this.stack = stack.withLore(loreLines)
                        .withDisplayName(Component.text(skyBlockItem.getAbsoluteName(player, item))
                                .decoration(TextDecoration.ITALIC, false));
                return;
            }

            if (item.getGenericInstance() instanceof ExtraUnderNameDisplays underNameDisplay) {
                underNameDisplay.getExtraUnderNameDisplays().forEach(line -> addLoreLine("§8" + line));
                addLoreLine(null);
            }

            // Handle Item Statistics
            if (handler.isMiningTool()) {
                addLoreLine("§8Breaking Power " + handler.getBreakingPower());
                addLoreLine(null);
            }

            boolean health = addPossiblePropertyInt(ItemStatistic.HEALTH, statistics.getOverall(ItemStatistic.HEALTH),
                    handler.getReforge(), rarity
            );
            boolean damage = addPossiblePropertyInt(ItemStatistic.DAMAGE, statistics.getOverall(ItemStatistic.DAMAGE),
                    handler.getReforge(), rarity
            );
            boolean defence = addPossiblePropertyInt(ItemStatistic.DEFENSE, statistics.getOverall(ItemStatistic.DEFENSE),
                    handler.getReforge(), rarity
            );
            boolean strength = addPossiblePropertyInt(ItemStatistic.STRENGTH, statistics.getOverall(ItemStatistic.STRENGTH),
                    handler.getReforge(), rarity
            );
            boolean intelligence = addPossiblePropertyInt(ItemStatistic.INTELLIGENCE, statistics.getOverall(ItemStatistic.INTELLIGENCE),
                    handler.getReforge(), rarity
            );
            boolean miningSpeed = addPossiblePropertyInt(ItemStatistic.MINING_SPEED, statistics.getOverall(ItemStatistic.MINING_SPEED),
                    handler.getReforge(), rarity
            );
            boolean speed = addPossiblePropertyInt(ItemStatistic.SPEED, statistics.getOverall(ItemStatistic.SPEED),
                    handler.getReforge(), rarity
            );

            if (item.getGenericInstance() instanceof ShortBowImpl) {
                addLoreLine("§7Shot Cooldown: §a" + ((ShortBowImpl) item.getGenericInstance()).getCooldown() + "s");
            }

            // Handle Gemstone lore
            if (item.getGenericInstance() instanceof GemstoneItem gemstoneItem) {
                ItemAttributeGemData.GemData gemData = handler.getGemData();
                StringBuilder gemstoneLore = new StringBuilder(" ");

                int index = -1;
                for (GemstoneItem.GemstoneItemSlot entry : gemstoneItem.getGemstoneSlots()) {
                    index++;
                    Gemstone.Slots gemstone = entry.slot;

                    if (!gemData.hasGem(index)) {
                        gemstoneLore.append("§8[" + gemstone.symbol + "] ");
                        continue;
                    }

                    ItemAttributeGemData.GemData.GemSlots gemSlot = gemData.getGem(index);
                    ItemType filledWith = gemSlot.filledWith;

                    if (filledWith == null) {
                        gemstoneLore.append("§7[" + gemstone.symbol + "] ");
                        continue;
                    }

                    GemstoneImpl gemstoneImpl = (GemstoneImpl) filledWith.clazz.getDeclaredConstructor().newInstance();
                    GemRarity gemRarity = gemstoneImpl.getAssociatedGemRarity();
                    Gemstone gemstoneEnum = gemstoneImpl.getAssociatedGemstone();
                    Gemstone.Slots gemstoneSlot = Gemstone.Slots.getFromGemstone(gemstoneEnum);

                    gemstoneLore.append(gemRarity.bracketColor + "[" + gemstoneSlot.symbol + gemRarity.bracketColor + "] ");
                }

                if (!gemstoneLore.toString().trim().isEmpty()) {addLoreLine(gemstoneLore.toString());}
            }

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
                                    enchantment.type().getDescription(enchantment.level(), player),
                                    34
                            ).forEach(string -> addLoreLine("§7" + string));
                        });

                    } else {
                        String enchantmentNames = handler.getEnchantments().toList().stream().map(enchantment1 ->
                                        "§9" + enchantment1.type().getName() + " " + StringUtility
                                                .getAsRomanNumeral(enchantment1.level()))
                                .collect(Collectors.joining(", "));
                        StringUtility.splitByWordAndLength(enchantmentNames, 34).forEach(this::addLoreLine);
                    }

                    if (enchantmentCount != 0) addLoreLine(null);
                }
            }

            ItemAttributeRuneInfusedWith.RuneData runeData = handler.getRuneData();
            if (runeData != null && runeData.hasRune()) {
                RuneItem runeItem = runeData.getAsRuneItem();
                addLoreLine(runeItem.getDisplayName(runeData.getRuneType(), runeData.getLevel()));
                addLoreLine(null);
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
                for (String line : StringUtility.splitByWordAndLength(ability.getAbilityDescription(), 34)) addLoreLine("§7" + line);
                if (ability.getManaCost() > 0) addLoreLine("§8Mana Cost: §3" + ability.getManaCost());
                if (ability.getAbilityCooldownTicks() > 20) {
                    addLoreLine("§8Cooldown: §a" + StringUtility.decimalify((double) ability.getAbilityCooldownTicks() / 20, 1) + "s");
                }

                addLoreLine(null);
            }

            // Handle full set abilities
            if (ArmorSetRegistry.getArmorSet(handler.getItemTypeAsType()) != null) {
                ArmorSet armorSet = ArmorSetRegistry.getArmorSet(handler.getItemTypeAsType()).getClazz().getDeclaredConstructor().newInstance();

                int wearingAmount = 0;
                if (player != null && player.isWearingItem(item)) {
                    for (SkyBlockItem armorItem : player.getArmor()) {
                        if (armorItem == null) continue;
                        ArmorSetRegistry armorSetRegistry = ArmorSetRegistry.getArmorSet(armorItem.getAttributeHandler().getItemTypeAsType());
                        if (armorSetRegistry == null) continue;
                        if (armorSetRegistry.getClazz() == armorSet.getClass()) {
                            wearingAmount++;
                        }
                    }
                }
                int totalPieces = ArmorSetRegistry.getPieceCount(ArmorSetRegistry.getArmorSet(armorSet.getClass()));
                addLoreLine("§6Full Set Bonus: " + armorSet.getName() + " (" + wearingAmount + "/" + totalPieces + ")");
                armorSet.getDescription().forEach(line -> addLoreLine("§7" + line));
                addLoreLine(null);
            }

            if (item.getGenericInstance() instanceof RightClickRecipe) {
                addLoreLine("§eRight-click to view recipes!");
                addLoreLine(null);
            }

            if (item.getGenericInstance() instanceof ExtraRarityDisplay) {
                displayRarity = displayRarity + ((ExtraRarityDisplay) item.getGenericInstance()).getExtraRarityDisplay();
            }

            if (item.getGenericInstance() instanceof Reforgable) {
                addLoreLine("§8This item can be reforged!");
                if (handler.getReforge() != null) displayName = handler.getReforge().prefix() + " " + displayName;
            }

            if (item.getGenericInstance() instanceof ArrowImpl) {
                addLoreLine("§8Stats added when shot!");
            }

            ItemAttributeSoulbound.SoulBoundData bound = handler.getSoulBoundData();
            if (bound != null) addLoreLine("§8* " + (bound.isCoopAllowed() ? "Co-op " : "") + "Soulbound *");

            if (item.getGenericInstance() instanceof ArrowImpl) {
                addLoreLine("§8Stats added when shot!");
            }

            if (item.getGenericInstance() instanceof NotFinishedYet) {
                addLoreLine("§c§lITEM IS NOT FINISHED!");
                addLoreLine(null);
            }
        }

        if (recombobulated) displayRarity = rarity.getColor() + "&kL " + displayRarity + " &kL";

        displayName = rarity.getColor() + displayName;
        addLoreLine(displayRarity);
        this.stack = stack.withLore(loreLines)
                .withDisplayName(Component.text(displayName)
                        .decoration(TextDecoration.ITALIC, false));
    }

    private boolean addPossiblePropertyInt(ItemStatistic statistic, double overallValue,
            ReforgeType.Reforge reforge, Rarity rarity) {
        SkyBlockItem item = new SkyBlockItem(stack);
        double reforgeValue = 0;
        double gemstoneValue = Gemstone.getExtraStatisticFromGemstone(statistic, item);
        if (reforge != null) {
            overallValue += reforge.getAfterCalculation(ItemStatistics.empty(), rarity.ordinal() + 1)
                    .getOverall(statistic);
            reforgeValue = reforge.getAfterCalculation(ItemStatistics.empty(), rarity.ordinal() + 1)
                    .getOverall(statistic) - overallValue;
        }
        overallValue += gemstoneValue;

        double hpbValue = 0;
        ItemAttributeHotPotatoBookData.HotPotatoBookData hotPotatoBookData = item.getAttributeHandler().getHotPotatoBookData();
        if (hotPotatoBookData.hasPotatoBook()) {
            for (Map.Entry<ItemStatistic, Double> entry : hotPotatoBookData.getPotatoType().stats.entrySet()) {
                ItemStatistic stat = entry.getKey();
                Double value = entry.getValue();
                if (stat == statistic) hpbValue += value;
            }
        }
        overallValue += hpbValue;

        if (overallValue == 0) return false;

        String color = statistic.getLoreColor();
        String prefix = statistic.getIsPercentage() ? "" : "+";
        String suffix = statistic.getIsPercentage() ? "%" : "";
        String line = "§7" + StringUtility.toNormalCase(statistic.getDisplayName()) + ": " +
                color + prefix + Math.round(overallValue) + suffix;

        if (hpbValue != 0) line += " §e(" + (Math.round(hpbValue) >= 1 ? "+" : "") + Math.round(hpbValue) + ")";
        if (reforgeValue != 0) line += " §9(" + (Math.round(reforgeValue) > 0 ? "+" : "") + Math.round(reforgeValue) + ")";
        if (gemstoneValue != 0) line += " §d(" + (Math.round(gemstoneValue) >= 1 ? "+" : "") + Math.round(gemstoneValue) + ")";

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

