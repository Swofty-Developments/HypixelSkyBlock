package net.swofty.type.skyblockgeneric.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ChatColor;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeSoulbound;
import net.swofty.commons.skyblock.item.reforge.Reforge;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePotionData;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.gems.GemRarity;
import net.swofty.type.skyblockgeneric.gems.Gemstone;
import net.swofty.type.skyblockgeneric.item.components.*;
import net.swofty.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import net.swofty.type.skyblockgeneric.item.set.impl.ArmorSet;
import net.swofty.type.skyblockgeneric.potion.PotionEffectType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
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
		if (item.hasComponent(CustomDisplayNameComponent.class)) {
			CustomDisplayNameComponent customDisplayName = item.getComponent(CustomDisplayNameComponent.class);
			displayName = customDisplayName.getDisplayName(item);
		} else {
			// Check for potion data FIRST to generate proper potion display name
			// This must be checked before getPotentialType() since POTION is a valid ItemType
			// but we want potions with effect data to show "Speed Potion I" instead of "Water Bottle"
			ItemAttributePotionData.PotionData potionDataForName = handler.getPotionData();
			if (potionDataForName != null && potionDataForName.getEffectType() != null
					&& !potionDataForName.getEffectType().equals("WATER")) {
				PotionEffectType effectForName = PotionEffectType.fromName(potionDataForName.getEffectType());
				if (effectForName != null) {
					String effectDisplay = effectForName.getLevelDisplay(potionDataForName.getLevel());
					if (potionDataForName.isSplash()) {
						displayName = effectDisplay + " Splash Potion";
					} else {
						displayName = effectDisplay + " Potion";
					}
				} else if (handler.getPotentialType() != null) {
					displayName = handler.getPotentialType().getDisplayName();
				} else {
					Material material = stack.material();
					displayName = StringUtility.toNormalCase(material.key().value());
				}
			} else if (handler.getPotentialType() != null) {
				displayName = handler.getPotentialType().getDisplayName();
			} else {
				Material material = stack.material();
				displayName = StringUtility.toNormalCase(material.key().value());
			}
		}
		String displayRarity = rarity.getDisplay();

		if (item.hasComponent(LoreUpdateComponent.class)) {
			LoreUpdateComponent loreUpdateComponent = item.getComponent(LoreUpdateComponent.class);
			if (loreUpdateComponent.isAbsolute()) {
				LoreConfig loreConfig = loreUpdateComponent.getHandler();
				String forcedDisplayName;
				if (item.hasComponent(CustomDisplayNameComponent.class)) {
					CustomDisplayNameComponent customDisplayName = item.getComponent(CustomDisplayNameComponent.class);
					forcedDisplayName = customDisplayName.getDisplayName(item);
				} else {
					forcedDisplayName = StringUtility.toNormalCase(item.getAttributeHandler().getTypeAsString());
				}

				if (loreConfig.displayNameGenerator() != null) {
					forcedDisplayName = loreConfig.displayNameGenerator().apply(item, player);
				}
				if (loreConfig.loreGenerator() != null) {
					loreConfig.loreGenerator().apply(item, player).forEach(line -> addLoreLine("§7" + line));
				}

				this.stack = stack
						.with(DataComponents.LORE, loreLines)
						.with(DataComponents.CUSTOM_NAME, Component.text(forcedDisplayName).decoration(TextDecoration.ITALIC, false));
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

		List<ItemStatistic> itemStatistics = new ArrayList<>(List.of(ItemStatistic.DAMAGE, ItemStatistic.STRENGTH, ItemStatistic.CRITICAL_CHANCE, ItemStatistic.CRITICAL_DAMAGE,
				ItemStatistic.SEA_CREATURE_CHANCE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.HEALTH, ItemStatistic.DEFENSE,
				ItemStatistic.SPEED, ItemStatistic.INTELLIGENCE, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK, ItemStatistic.TRUE_DEFENSE, ItemStatistic.HEALTH_REGENERATION,
				ItemStatistic.MENDING, ItemStatistic.VITALITY, ItemStatistic.FEROCITY, ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE,
				ItemStatistic.FARMING_FORTUNE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.COLD_RESISTANCE, ItemStatistic.PRISTINE,
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

				if (gemData.getGem(index) == null || !gemData.getGem(index).isUnlocked()) {
					gemstoneLore.append(ChatColor.DARK_GRAY).append("[").append(gemstone.symbol).append("] ");
					continue;
				}

				ItemAttributeGemData.GemData.GemSlots gemSlot = gemData.getGem(index);
				ItemType filledWith = gemSlot.filledWith;

				if (filledWith == null) {
					gemstoneLore.append(ChatColor.DARK_GRAY).append("[").append(ChatColor.GRAY).append(gemstone.symbol).append(ChatColor.DARK_GRAY).append("]");
					continue;
				}

				SkyBlockItem gemstoneImpl = new SkyBlockItem(filledWith);
				GemstoneImplComponent gemstoneImplComponent = gemstoneImpl.getComponent(GemstoneImplComponent.class);
				GemRarity gemRarity = gemstoneImplComponent.getGemRarity();
				Gemstone gemstoneEnum = gemstoneImplComponent.getGemstone();
				Gemstone.Slots gemstoneSlot = Gemstone.Slots.getFromGemstone(gemstoneEnum);

				gemstoneLore.append(gemRarity.bracketColor).append("[").append(gemstoneEnum.color).append(gemstoneSlot.symbol).append(gemRarity.bracketColor).append("] ");
			}

			if (!gemstoneLore.toString().trim().isEmpty()) {
				addLoreLine(gemstoneLore.toString());
				addNextLine = true;
			}
		}

		if (addNextLine) addLoreLine(null);

		// Handle Potion lore
		ItemAttributePotionData.PotionData potionData = handler.getPotionData();
		if (potionData != null) {
			PotionEffectType effectType = PotionEffectType.fromName(potionData.getEffectType());
			if (effectType != null && effectType.getCategory() != null) {
				// Format duration string
				String durationStr = "";
				if (potionData.getBaseDurationSeconds() > 0) {
					int totalSeconds = potionData.getBaseDurationSeconds();
					int minutes = totalSeconds / 60;
					int seconds = totalSeconds % 60;
					durationStr = " (" + minutes + ":" + String.format("%02d", seconds) + ")";
				}

				// Display effect name with level and duration: "Speed I (3:00)"
				String effectColor = effectType.getColor();
				String effectDisplay = effectType.getLevelDisplay(potionData.getLevel());
				addLoreLine(effectColor + effectDisplay + durationStr);

				// Display level-specific description: "Increases Strength by 20."
				String description = effectType.getDescription(potionData.getLevel());
				if (!description.isEmpty()) {
					addLoreLine("§7" + description);
				}

				addLoreLine(null);
			}
		}

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

		// Handle Custom Item Lore (BEFORE_ABILITY location)
		if (item.hasComponent(LoreUpdateComponent.class)) {
			LoreUpdateComponent loreUpdateComponent = item.getComponent(LoreUpdateComponent.class);
			if (loreUpdateComponent.getHandler() == null)
				throw new RuntimeException("Lore update handler is null for " + item.getAttributeHandler().getTypeAsString());

			LoreConfig loreConfig = loreUpdateComponent.getHandler();
			if (loreConfig.loreGenerator() != null && loreConfig.location() == LoreConfig.LoreConfigLocation.BEFORE_ABILITY) {
				loreConfig.loreGenerator().apply(item, player).forEach(line -> addLoreLine("§7" + line));
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
				for (String line : StringUtility.splitByWordAndLength(ability.getDescription().apply(player, item), 40))
					addLoreLine("§7" + line);

				String costDisplay = ability.getCost().getLoreDisplay();
				if (costDisplay != null) addLoreLine(costDisplay);

				if (ability.getCooldownTicks() > 20) {
					addLoreLine("§8Cooldown: §a" + StringUtility.decimalify((double) ability.getCooldownTicks() / 20, 1) + "s");
				}

				addLoreLine(null);
			});
		}

		// Handle Custom Item Lore (AFTER_ABILITY location)
		if (item.hasComponent(LoreUpdateComponent.class)) {
			LoreUpdateComponent loreUpdateComponent = item.getComponent(LoreUpdateComponent.class);
			LoreConfig loreConfig = loreUpdateComponent.getHandler();
			if (loreConfig != null && loreConfig.loreGenerator() != null && loreConfig.location() == LoreConfig.LoreConfigLocation.AFTER_ABILITY) {
				loreConfig.loreGenerator().apply(item, player).forEach(line -> addLoreLine("§7" + line));
				addLoreLine(null);
			}
		}

		// Handle full set abilities
		if (ArmorSetRegistry.getArmorSet(handler.getPotentialType()) != null && ArmorSetRegistry.getArmorSet(handler.getPotentialType()).getClazz() != null) {
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
			if (handler.getReforge() != null) displayName = handler.getReforge().getPrefix() + " " + displayName;
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
		this.stack = stack.with(DataComponents.LORE, loreLines)
				.withAmount(item.getAmount())
				.with(DataComponents.CUSTOM_NAME, Component.text(displayName)
						.decoration(TextDecoration.ITALIC, false));
	}

	private boolean addPossiblePropertyInt(ItemStatistic statistic, double overallValue,
										   Reforge reforge, Rarity rarity) {
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
		if (reforgeValue != 0)
			line += " §9(" + (Math.round(reforgeValue) > 0 ? "+" : "") + Math.round(reforgeValue) + ")";
		if (gemstoneValue != 0)
			line += " §d(" + (Math.round(gemstoneValue) >= 1 ? "+" : "") + Math.round(gemstoneValue) + ")";

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

