package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.ReforgeType;
import net.swofty.commons.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.commons.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.commons.item.attribute.attributes.ItemAttributeSoulbound;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.components.*;
import net.swofty.types.generic.item.handlers.lore.LoreConfig;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        @Nullable ItemType type = item.getAttributeHandler().getPotentialType();
        ItemAttributeHandler handler = item.getAttributeHandler();

        Rarity rarity = handler.getRarity();
        boolean recombobulated = handler.isRecombobulated();
        ItemStatistics statistics = handler.getStatistics();

        if (recombobulated) rarity = rarity.upgrade();

        String displayName;
        if (handler.getPotentialType() != null) {
            displayName = handler.getPotentialType().getDisplayName();
        } else {
            Material material = stack.material();
            displayName = StringUtility.toNormalCase(material.key().value());
        }
        String displayRarity = rarity.getDisplay();

        if (item.hasComponent(LoreUpdateComponent.class)) {
            LoreUpdateComponent loreUpdateComponent = item.getComponent(LoreUpdateComponent.class);
            if (loreUpdateComponent.isAbsolute()) {
                LoreConfig loreConfig = loreUpdateComponent.getHandler();
                String forcedDisplayName = StringUtility.toNormalCase(item.getAttributeHandler().getTypeAsString());

                if (loreConfig.displayNameGenerator() != null) {
                    forcedDisplayName = loreConfig.displayNameGenerator().apply(item, player);
                }
                if (loreConfig.loreGenerator() != null) {
                    loreConfig.loreGenerator().apply(item, player).forEach(line -> addLoreLine("§7" + line));
                }

                this.stack = stack
                        .with(ItemComponent.LORE, loreLines)
                        .with(ItemComponent.CUSTOM_NAME, Component.text(forcedDisplayName).decoration(TextDecoration.ITALIC, false));
                return;
            }
        }

        if (item.hasComponent(ExtraUnderNameComponent.class)) {
            ExtraUnderNameComponent underNameDisplay = item.getComponent(ExtraUnderNameComponent.class);
            underNameDisplay.getDisplays().forEach(line -> addLoreLine("§8" + line));

            if (type != null && CollectionCategories.getCategory(type) != null) {
                addLoreLine("§8Collection Item");
            }

            addLoreLine(null);
        } else {
            if (type != null && CollectionCategories.getCategory(type) != null) {
                addLoreLine("§8Collection Item");
                addLoreLine(null);
            }
        }

        // Handle Item Statistics
        if (handler.isMiningTool()) {
            addLoreLine("§8Breaking Power " + handler.getBreakingPower());
            addLoreLine(null);
        }

        List<ItemStatistic> itemStatistics = new ArrayList<>(List.of(ItemStatistic.DAMAGE, ItemStatistic.STRENGTH, ItemStatistic.CRIT_CHANCE, ItemStatistic.CRIT_DAMAGE,
                ItemStatistic.SEA_CREATURE_CHANCE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.HEALTH, ItemStatistic.DEFENSE,
                ItemStatistic.SPEED, ItemStatistic.INTELLIGENCE, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK, ItemStatistic.TRUE_DEFENSE, ItemStatistic.HEALTH_REGEN,
                ItemStatistic.MENDING, ItemStatistic.VITALITY, ItemStatistic.FEROCITY, ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE,
                ItemStatistic.FARMING_FORTUNE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.COLD_RESISTANCE,ItemStatistic.PRISTINE,
                ItemStatistic.SWING_RANGE));

        boolean addNextLine = false;
        for (ItemStatistic itemStatistic : itemStatistics) {
            boolean x = addPossiblePropertyInt(itemStatistic, statistics.getOverall(itemStatistic), handler.getReforge(), rarity);
            if (x) {
                addNextLine = true;
            }
        }

        if (item.hasComponent(ShortBowComponent.class)) {
            ShortBowComponent shortBowComponent = item.getComponent(ShortBowComponent.class);
            addLoreLine("§7Shot Cooldown: §a" + shortBowComponent.getCooldown() + "s");
            addNextLine = true;
        }

        // Handle Gemstone lore
        if (item.hasComponent(GemstoneComponent.class)) {
            GemstoneComponent gemstoneComponent = item.getComponent(GemstoneComponent.class);
            ItemAttributeGemData.GemData gemData = handler.getGemData();
            StringBuilder gemstoneLore = new StringBuilder(" ");

            int index = -1;
            for (GemstoneComponent.GemstoneSlot entry : gemstoneComponent.getSlots()) {
                index++;
                Gemstone.Slots gemstone = entry.slot();

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

                SkyBlockItem gemstoneImpl = new SkyBlockItem(filledWith);
                GemstoneImplComponent gemstoneImplComponent = gemstoneImpl.getComponent(GemstoneImplComponent.class);
                GemRarity gemRarity = gemstoneImplComponent.getGemRarity();
                Gemstone gemstoneEnum = gemstoneImplComponent.getGemstone();
                Gemstone.Slots gemstoneSlot = Gemstone.Slots.getFromGemstone(gemstoneEnum);

                gemstoneLore.append(gemRarity.bracketColor + "[" + gemstoneSlot.symbol + gemRarity.bracketColor + "] ");
            }

            if (!gemstoneLore.toString().trim().isEmpty()) {
                addLoreLine(gemstoneLore.toString());
                addNextLine = true;
            }
        }

        if (addNextLine) addLoreLine(null);

        // Handle Item Enchantments
        if (item.hasComponent(EnchantableComponent.class)) {
            EnchantableComponent enchantable = item.getComponent(EnchantableComponent.class);
            if (enchantable.showEnchantLores()) {
                long enchantmentCount = handler.getEnchantments().toList().size();
                if (enchantmentCount < 4) {
                    handler.getEnchantments().forEach((enchantment) -> {
                        addLoreLine("§9" + enchantment.type().getName() +
                                " " + StringUtility.getAsRomanNumeral(enchantment.level()));
                        if (player != null)
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
            SkyBlockItem runeItem = new SkyBlockItem(runeData.getRuneType());
            RuneComponent runeComponent = runeItem.getComponent(RuneComponent.class);

            addLoreLine(runeComponent.getDisplayName(runeData.getRuneType(), runeData.getLevel()));
            addLoreLine(null);
        }

        // Handle Custom Item Lore
        if (item.hasComponent(LoreUpdateComponent.class)) {
            LoreUpdateComponent loreUpdateComponent = item.getComponent(LoreUpdateComponent.class);
            if (loreUpdateComponent.getHandler() == null)
                throw new RuntimeException("Lore update handler is null for " + item.getAttributeHandler().getTypeAsString());

            if (loreUpdateComponent.getHandler().loreGenerator() != null) {
                loreUpdateComponent.getHandler().loreGenerator().apply(item, player).forEach(line -> addLoreLine("§7" + line));
                addLoreLine(null);
            }
        }

        if (item.getConfigLore() != null && !item.getConfigLore().isEmpty()) {
            item.getConfigLore().forEach(line -> addLoreLine("§7" + line));
            addLoreLine(null);
        }

        // Handle Custom Item Ability
        if (item.hasComponent(AbilityComponent.class)) {
            AbilityComponent abilityComponent = item.getComponent(AbilityComponent.class);

            abilityComponent.getAbilities().forEach(ability -> {
                addLoreLine("§6Ability: " + ability.getName() + "  §e§l" +
                        ability.getActivation().getDisplay());
                for (String line : StringUtility.splitByWordAndLength(ability.getDescription(), 34)) addLoreLine("§7" + line);

                String costDisplay = ability.getCost().getLoreDisplay();
                if (costDisplay != null) addLoreLine(costDisplay);

                if (ability.getCooldownTicks() > 20) {
                    addLoreLine("§8Cooldown: §a" + StringUtility.decimalify((double) ability.getCooldownTicks() / 20, 1) + "s");
                }

                addLoreLine(null);
            });
        }

        // Handle full set abilities
        if (ArmorSetRegistry.getArmorSet(handler.getPotentialType()) != null) {
            ArmorSet armorSet = ArmorSetRegistry.getArmorSet(handler.getPotentialType()).getClazz().getDeclaredConstructor().newInstance();

            int wearingAmount = 0;
            if (player != null && player.isWearingItem(item)) {
                for (SkyBlockItem armorItem : player.getArmor()) {
                    if (armorItem == null) continue;
                    ArmorSetRegistry armorSetRegistry = ArmorSetRegistry.getArmorSet(armorItem.getAttributeHandler().getPotentialType());
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

        if (item.hasComponent(RightClickRecipeComponent.class)) {
            addLoreLine("§eRight-click to view recipes!");
            addLoreLine(null);
        }

        if (item.hasComponent(ExtraRarityComponent.class)) {
            ExtraRarityComponent extraRarityComponent = item.getComponent(ExtraRarityComponent.class);
            displayRarity = displayRarity + " " + extraRarityComponent.getExtraRarityDisplay();
        }

        if (item.hasComponent(ReforgableComponent.class)) {
            addLoreLine("§8This item can be reforged!");
            if (handler.getReforge() != null) displayName = handler.getReforge().prefix() + " " + displayName;
        }

        ItemAttributeSoulbound.SoulBoundData bound = handler.getSoulBoundData();
        if (bound != null) addLoreLine("§8* " + (bound.isCoopAllowed() ? "Co-op " : "") + "Soulbound *");

        if (item.hasComponent(ArrowComponent.class)) {
            addLoreLine("§8Stats added when shot!");
        }

        if (item.hasComponent(NotFinishedYetComponent.class)) {
            addLoreLine("§c§lITEM IS NOT FINISHED!");
            addLoreLine(null);
        }

        if (recombobulated) displayRarity = rarity.getColor() + "&kL " + displayRarity + " &kL";

        displayName = rarity.getColor() + displayName;
        addLoreLine(displayRarity);
        this.stack = stack.with(ItemComponent.LORE, loreLines)
                .withAmount(item.getAmount())
                .with(ItemComponent.CUSTOM_NAME, Component.text(displayName)
                        .decoration(TextDecoration.ITALIC, false));
    }

    private boolean addPossiblePropertyInt(ItemStatistic statistic, double overallValue,
                                           ReforgeType.Reforge reforge, Rarity rarity) {
        SkyBlockItem item = new SkyBlockItem(stack);
        double reforgeValue = 0;
        double gemstoneValue = Gemstone.getExtraStatisticFromGemstone(statistic, item);
        if (reforge != null) {
            reforgeValue = reforge.getAfterCalculation(ItemStatistics.empty(), rarity.ordinal() + 1).getOverall(statistic);
            overallValue += reforgeValue;
        }
        overallValue += gemstoneValue;

        double hpbValue = 0;
        ItemAttributeHotPotatoBookData.HotPotatoBookData hotPotatoBookData = item.getAttributeHandler().getHotPotatoBookData();
        if (hotPotatoBookData.hasAppliedItem()) {
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

