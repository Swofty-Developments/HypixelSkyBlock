package net.swofty.type.skyblockgeneric.item;

import io.sentry.Sentry;
import net.minestom.server.color.Color;
import net.minestom.server.item.ItemAnimation;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.PotatoType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.gems.GemRarity;
import net.swofty.type.skyblockgeneric.gems.Gemstone;
import net.swofty.type.skyblockgeneric.item.components.*;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.item.handlers.pet.KatUpgrade;
import net.swofty.type.skyblockgeneric.minion.MinionIngredient;
import net.swofty.type.skyblockgeneric.utility.RarityValue;
import net.swofty.type.skyblockgeneric.utility.RecipeParser;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfigParser {
	public static ConfigurableSkyBlockItem parseItem(Map<String, Object> config) {
		SafeConfig safeConfig = SafeConfig.of(config);

		try {
			String id = safeConfig.getString("id", "");
			id = id.replaceAll("[^a-zA-Z0-9_]", "");

			String materialName = safeConfig.getString("material", "AIR");
			Material material = Material.values().stream()
					.filter(m -> m.key().value().equalsIgnoreCase(materialName))
					.findFirst()
					.orElse(Material.AIR);

			List<String> lore = safeConfig.getList("lore", String.class);
			Map<String, Double> statistics = new HashMap<>();

			if (safeConfig.containsKey("default_statistics")) {
				SafeConfig statsConfig = safeConfig.getNested("default_statistics");
				for (String key : statsConfig.getKeys()) {
					double value = statsConfig.getDouble(key, 0.0);
					statistics.put(key.toUpperCase(), value);
				}
			}

			ConfigurableSkyBlockItem item = new ConfigurableSkyBlockItem(id, material, lore, statistics);

			List<Map<String, Object>> components = safeConfig.getMapList("components");
			for (Map<String, Object> componentConfig : components) {
				SafeConfig componentSafeConfig = SafeConfig.of(componentConfig);
				String componentId = componentSafeConfig.getString("id");
				if (componentId != null) {
					SkyBlockItemComponent component = parseComponent(id, componentId, componentConfig);
					if (component != null) {
						item.addComponent(component, true);
					}
				}
			}

			item.register();
			return item;

		} catch (SafeConfig.ConfigParseException e) {
			Logger.error("Failed to parse item: {}", e.getMessage());
			return null;
		} catch (Exception e) {
			Logger.error("Unexpected error parsing item: {}", e.getMessage());
			e.printStackTrace();
			Sentry.captureException(e);
			return null;
		}
	}

	private static @Nullable SkyBlockItemComponent parseComponent(String itemId, String id, Map<String, Object> config) {
		SafeConfig safeConfig = SafeConfig.of(config);

		try {
			return switch (id.toUpperCase()) {
				case "ABILITY" -> {
					List<String> abilities = safeConfig.getList("abilities", String.class);
					yield new AbilityComponent(abilities);
				}
				case "TALISMAN", "ACCESSORY" -> new AccessoryComponent();
				case "ANVIL_COMBINABLE" -> {
					String handlerId = safeConfig.getString("handler_id");
					yield new AnvilCombinableComponent(handlerId);
				}
				case "ARMOR" -> new ArmorComponent();
				case "ARROW" -> new ArrowComponent();
				case "AUCTION_CATEGORY" -> {
					String category = safeConfig.getString("category");
					yield new AuctionCategoryComponent(category);
				}
				case "AXE" -> {
					int axeStrength = safeConfig.getInt("axe_strength", 1);
					yield new AxeComponent(axeStrength);
				}
				case "BACKPACK" -> {
					int rows = safeConfig.getInt("rows");
					String skullTexture = safeConfig.getString("skull-texture");
					yield new BackpackComponent(rows, skullTexture);
				}
				case "BOW" -> {
					String handlerId = safeConfig.getString("handler_id");
					boolean shouldBeArrow = safeConfig.getBoolean("should-be-arrow", true);
					yield new BowComponent(handlerId, shouldBeArrow);
				}
				case "CONSTANT_STATISTICS" -> new ConstantStatisticsComponent();
				case "DEFAULT_CRAFTABLE" -> {
					List<Map<String, Object>> recipes = safeConfig.getMapList("recipes");
					boolean defaultCraftable = safeConfig.getBoolean("default-craftable", true);
					CraftableComponent component = new CraftableComponent(recipes);
					component.setDefaultCraftable(defaultCraftable);
					yield component;
				}
				case "CUSTOM_DISPLAY_NAME" ->
						new CustomDisplayNameComponent((_) -> safeConfig.getString("display_name", ""));
				case "DECORATION_HEAD" -> {
					String texture = safeConfig.getString("texture");
					yield new DecorationHeadComponent(texture);
				}
				case "DEFAULT_SOULBOUND" -> {
					boolean coopAllowed = safeConfig.getBoolean("coop_allowed");
					yield new DefaultSoulboundComponent(coopAllowed);
				}
				case "DISABLE_ANIMATION" -> {
					List<ItemAnimation> animations = safeConfig.getList("disabled_animations", ItemAnimation.class);
					yield new DisableAnimationComponent(animations);
				}
				case "SOULFLOW" -> {
					int amount = safeConfig.getInt("amount");
					yield new SoulflowComponent(amount);
				}
				case "DRILL" -> new DrillComponent();
				case "ABIPHONE" -> {
					List<String> features = safeConfig.getList("features", String.class);
					List<AbiphoneComponent.AbiphoneFeature> abiphoneFeatures = new ArrayList<>();
					for (String feature : features) {
						abiphoneFeatures.add(AbiphoneComponent.AbiphoneFeature.valueOf(feature.toUpperCase().replace(" ", "_")));
					}
					int maxContacts = safeConfig.getInt("max_contacts", 7);
					int maxDiscs = safeConfig.getInt("max_discs", 0);
					yield new AbiphoneComponent(maxContacts, maxDiscs, abiphoneFeatures);
				}
				case "ENCHANTABLE" -> {
					List<String> groups = safeConfig.getList("enchant_groups", String.class);
					boolean showLores = safeConfig.getBoolean("show_lores", true);
					yield new EnchantableComponent(
							groups.stream().map(EnchantItemGroups::valueOf).toList(),
							showLores
					);
				}
				case "ENCHANTED" -> {
					if (safeConfig.containsKey("recipes")) {
						List<Map<String, Object>> recipeConfigs = safeConfig.getMapList("recipes");
						List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

						for (Map<String, Object> recipeConfig : recipeConfigs) {
							SkyBlockRecipe<?> recipe = RecipeParser.parseRecipe(recipeConfig);
							recipes.add(recipe);
						}

						yield new EnchantedComponent(recipes);
					}
					else if (safeConfig.containsKey("recipe_type") && safeConfig.containsKey("item_id")) {
						SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.valueOf(safeConfig.getString("recipe_type"));
						String baseMaterial = safeConfig.getString("item_id");
						yield new EnchantedComponent(type, itemId, baseMaterial);
					}
					else {
						yield new EnchantedComponent();
					}
				}
				case "EXTRA_RARITY" -> {
					String display = safeConfig.getString("display");
					yield new ExtraRarityComponent(display);
				}
				case "DUNGEON_ITEM" -> new ExtraRarityComponent("DUNGEON ITEM");
				case "EXTRA_UNDER_NAME" -> {
					if (safeConfig.containsKey("displays")) {
						List<String> displays = safeConfig.getList("displays", String.class);
						yield new ExtraUnderNameComponent(displays);
					} else {
						String display = safeConfig.getString("display");
						yield new ExtraUnderNameComponent(display);
					}
				}
				case "GEMSTONE" -> {
					List<Map<String, Object>> gemstoneEntries = safeConfig.getMapList("gemstone_slots");
					List<GemstoneComponent.GemstoneSlot> gemstoneSlots = parseGemstoneEntries(gemstoneEntries);
					yield new GemstoneComponent(gemstoneSlots);
				}
				case "GEMSTONE_IMPL" -> {
					GemRarity rarity = safeConfig.getEnum("rarity", GemRarity.class);
					Gemstone gemstone = safeConfig.getEnum("gemstone", Gemstone.class);
					String texture = safeConfig.getString("skull_texture");
					yield new GemstoneImplComponent(rarity, gemstone, texture);
				}
				case "HOE" -> new HoeComponent();
				case "HOT_POTATO" -> {
					String type = safeConfig.getString("potato_type");
					PotatoType potatoType = PotatoType.valueOf(type);

					if (safeConfig.containsKey("appliable_items")) {
						List<String> appliableItems = safeConfig.getList("appliable_items", String.class);
						HashMap<ItemType, Integer> appliable = new HashMap<>();

						for (String item : appliableItems) {
							String[] split = item.split(":");

							if (split.length != 2)
								continue;

							appliable.put(ItemType.valueOf(split[0]), Integer.parseInt(split[1]));
						}

						yield new HotPotatoableComponent(potatoType, appliable);
					}

					yield new HotPotatoableComponent(potatoType);
				}
				case "INTERACTABLE" -> {
					String handlerId = safeConfig.getString("handler_id");
					try {
						yield new InteractableComponent(handlerId);
					} catch (Exception e) {
						Logger.error("Failed to parse InteractableComponent for " + handlerId);
						yield null;
					}
				}
				case "KAT" -> {
					int reducedDays = safeConfig.getInt("reduced_days");
					yield new KatComponent(reducedDays);
				}
				case "LEATHER_COLOR" -> {
					String r = safeConfig.getString("r");
					String g = safeConfig.getString("g");
					String b = safeConfig.getString("b");

					if (r == null || g == null || b == null) {
						yield null;
					}

					Color color = new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
					yield new LeatherColorComponent(color);
				}
				case "MINION" -> {
					String minionType = safeConfig.getString("minion_type");
					String baseItem = safeConfig.getString("base_item");
					boolean isByDefaultCraftable = safeConfig.getBoolean("default_craftable");

					List<String> ingredients = safeConfig.getList("ingredients", String.class);
					List<MinionIngredient> ingredientsMap = new ArrayList<>();

					for (String ingredient : ingredients) {
						String[] ingredientParts = ingredient.split(":");
						ingredientsMap.add(new MinionIngredient(
								ItemType.valueOf(ingredientParts[0]),
								Integer.parseInt(ingredientParts[1])
						));
					}

					yield new MinionComponent(minionType, baseItem, isByDefaultCraftable, ingredientsMap);
				}
				case "MINION_FUEL" -> {
					double percentage = safeConfig.getDouble("fuel_percentage");
					long lastTime = safeConfig.getInt("last_time_ms");
					yield new MinionFuelComponent(percentage, lastTime);
				}
				case "MINION_SHIPPING" -> {
					double percentage = safeConfig.getDouble("percentage");
					yield new MinionShippingComponent(percentage);
				}
				case "MINION_SKIN" -> {
					String skinName = safeConfig.getString("name");
					SafeConfig helmetConfig = safeConfig.getNested("helmet");
					SafeConfig chestplateConfig = safeConfig.getNested("chestplate");
					SafeConfig leggingsConfig = safeConfig.getNested("leggings");
					SafeConfig bootsConfig = safeConfig.getNested("boots");

					MinionSkinComponent.MinionArmorPiece helmet = helmetConfig.getKeys().isEmpty() ?
							new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null) :
							MinionSkinComponent.MinionArmorPiece.fromConfig(helmetConfig.config);
					MinionSkinComponent.MinionArmorPiece chestplate = chestplateConfig.getKeys().isEmpty() ?
							new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null) :
							MinionSkinComponent.MinionArmorPiece.fromConfig(chestplateConfig.config);
					MinionSkinComponent.MinionArmorPiece leggings = leggingsConfig.getKeys().isEmpty() ?
							new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null) :
							MinionSkinComponent.MinionArmorPiece.fromConfig(leggingsConfig.config);
					MinionSkinComponent.MinionArmorPiece boots = bootsConfig.getKeys().isEmpty() ?
							new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null) :
							MinionSkinComponent.MinionArmorPiece.fromConfig(bootsConfig.config);

					yield new MinionSkinComponent(skinName, helmet, chestplate, leggings, boots);
				}
				case "MINION_UPGRADE" -> {
					double speedIncrease = safeConfig.getDouble("speed_increase");
					yield new MinionUpgradeComponent(speedIncrease);
				}
				case "MUSEUM" -> {
					String category = safeConfig.getString("museum_category", null);
					String gameStage = safeConfig.getString("game_stage", null);
					int donationXp = safeConfig.getInt("donation_xp", 0);

					Map<String, String> parent = null;
					if (safeConfig.containsKey("parent")) {
						Map<String, Object> parentMap = safeConfig.getMap("parent");
						if (!parentMap.isEmpty()) {
							parent = new java.util.LinkedHashMap<>();
							for (Map.Entry<String, Object> entry : parentMap.entrySet()) {
								parent.put(entry.getKey(), entry.getValue().toString());
							}
						}
					}

					List<String> mappedItemIds = null;
					if (safeConfig.containsKey("mapped_item_ids")) {
						mappedItemIds = safeConfig.getList("mapped_item_ids", String.class);
					}

					yield new MuseumComponent(category, gameStage, donationXp, parent, mappedItemIds);
				}
				case "NOT_FINISHED_YET" -> new NotFinishedYetComponent();
				case "NEW_YEAR_CAKE" -> new NewYearCakeComponent();
				case "LORE_UPDATE" -> {
					boolean isAbsolute = safeConfig.getBoolean("is_absolute", false);
					String handlerId = safeConfig.getString("handler_id", "");
					yield new LoreUpdateComponent(handlerId, isAbsolute);
				}
				case "PET_ITEM" -> new PetItemComponent();
				case "PICKAXE" -> new PickaxeComponent();
				case "PLACEABLE" -> {
					String blockType = safeConfig.getString("block_type");
					yield new PlaceableComponent(blockType);
				}
				case "PLACE_EVENT" -> {
					String handlerId = safeConfig.getString("handler_id");
					yield new PlaceEventComponent(handlerId);
				}
				case "POWER_STONE" -> new PowerStoneComponent();
				case "QUIVER_DISPLAY" -> {
					boolean shouldBeArrow = safeConfig.getBoolean("should_be_arrow");
					yield new QuiverDisplayComponent(shouldBeArrow);
				}
				case "REFORGABLE" -> {
					String type = safeConfig.getString("reforge_type");
					yield new ReforgableComponent(ReforgeType.valueOf(type));
				}
				case "RIGHT_CLICK_RECIPE" -> {
					String recipeItem = safeConfig.getString("recipe_item");
					yield new RightClickRecipeComponent(recipeItem);
				}
				case "RUNEABLE" -> {
					String applicableTo = safeConfig.getString("applicable_to");
					yield new RuneableComponent(RuneableComponent.RuneApplicableTo.valueOf(applicableTo));
				}
				case "CUSTOM_DROP" -> {
					List<Map<String, Object>> rulesConfig = safeConfig.getMapList("rules");
					List<CustomDropComponent.DropRule> rules = new ArrayList<>();

					for (Map<String, Object> ruleConfig : rulesConfig) {
						SafeConfig ruleSafeConfig = SafeConfig.of(ruleConfig);

						Map<String, Object> conditionsConfig = ruleSafeConfig.getMap("conditions");
						CustomDropComponent.DropConditions conditions = !conditionsConfig.isEmpty()
								? CustomDropComponent.parseDropConditions(conditionsConfig)
								: null;

						List<Map<String, Object>> dropsConfig = ruleSafeConfig.getMapList("drops");
						List<CustomDropComponent.Drop> drops = new ArrayList<>();

						for (Map<String, Object> dropConfig : dropsConfig) {
							Object itemObj = dropConfig.get("item");
							String itemName;
							if (itemObj instanceof String) {
								itemName = (String) itemObj;
							} else {
								itemName = itemObj.toString();
							}

							ItemType itemType = ItemType.valueOf(itemName.toUpperCase().replace(" ", "_"));

							Object chanceObj = dropConfig.get("chance");
							double chance;
							if (chanceObj instanceof Number) {
								chance = ((Number) chanceObj).doubleValue();
							} else if (chanceObj instanceof String) {
								chance = Double.parseDouble((String) chanceObj);
							} else {
								chance = 1.0;
							}

							Object amountObj = dropConfig.get("amount");
							String amount;
							if (amountObj instanceof String) {
								amount = (String) amountObj;
							} else if (amountObj instanceof Number) {
								amount = amountObj.toString();
							} else {
								amount = "1";
							}

							drops.add(new CustomDropComponent.Drop(itemType, chance, amount));
						}

						rules.add(new CustomDropComponent.DropRule(conditions, drops));
					}

					yield new CustomDropComponent(rules);
				}
				case "RUNE" -> {
					int level = safeConfig.getInt("required_level");
					String color = safeConfig.getString("color");
					String applicableTo = safeConfig.getString("applicable_to");
					String texture = safeConfig.getString("skull_texture");
					yield new RuneComponent(level, color, applicableTo, texture);
				}
				case "SACK" -> {
					List<String> items = safeConfig.getList("valid_items", String.class);
					int capacity = safeConfig.getInt("max_capacity");
					yield new SackComponent(items, capacity);
				}
				case "SELLABLE" -> {
					double value = safeConfig.getDouble("value", 1.0);
					yield new SellableComponent(value);
				}
				case "SERVER_ORB" -> {
					String handlerId = safeConfig.getString("handler_id");
					List<String> blockStrings = safeConfig.getList("valid_blocks", String.class);
					List<Material> materials = Material.values().stream()
							.filter(material -> blockStrings.contains(material.key().value().toLowerCase()))
							.toList();

					yield new ServerOrbComponent(handlerId, materials);
				}
				case "SHORT_BOW" -> {
					String handlerId = safeConfig.getString("handler_id");
					float cooldown = (float) safeConfig.getDouble("cooldown");
					boolean shouldBeArrow = safeConfig.getBoolean("should-be-arrow", true);
					yield new ShortBowComponent(cooldown, handlerId, shouldBeArrow);
				}
				case "SHOVEL" -> new ShovelComponent();
				case "SKILLABLE_MINE" -> {
					String category = safeConfig.getString("category");
					double value = safeConfig.getDouble("mining_value");
					yield new SkillableMineComponent(category, value);
				}
				case "SKULL_HEAD" -> new SkullHeadComponent((item) -> safeConfig.getString("texture", ""));
				case "STANDARD_ITEM" -> {
					String type = safeConfig.getString("standard_item_type");
					yield new StandardItemComponent(type);
				}
				case "CUSTOM_STATISTICS" -> {
					String handlerId = safeConfig.getString("handler_id");
					yield new CustomStatisticsComponent(handlerId);
				}
				case "TRACKED_UNIQUE" -> new TrackedUniqueComponent();
				case "BREWING_INGREDIENT" -> {
					int brewingTime = safeConfig.getInt("brewing_time_seconds", 20);
					String effect = safeConfig.getString("effect", "SPEED");
					int duration = safeConfig.getInt("effect_duration", 180);
					int amplifier = safeConfig.getInt("effect_amplifier", 0);
					int alchemyXp = safeConfig.getInt("alchemy_xp", 0);
					yield new BrewingIngredientComponent(brewingTime, effect, duration, amplifier, alchemyXp);
				}
				case "POTION_DATA" -> {
					String effect = safeConfig.getString("effect", "SPEED");
					int level = safeConfig.getInt("level", 1);
					int duration = safeConfig.getInt("base_duration", 180);
					boolean splash = safeConfig.getBoolean("splash", false);
					boolean extended = safeConfig.getBoolean("extended", false);
					yield new PotionDataComponent(effect, level, duration, splash, extended);
				}
				case "TRAVEL_SCROLL" -> {
					String scrollType = safeConfig.getString("scroll_type");
					yield new TravelScrollComponent(scrollType);
				}
				case "PET" -> {
					String petName = safeConfig.getString("pet_name");

					// Parse george price
					SafeConfig georgePriceConfig = safeConfig.getNested("george_price");
					RarityValue<Integer> georgePrice = new RarityValue<>(
							georgePriceConfig.getInt("common"),
							georgePriceConfig.getInt("uncommon"),
							georgePriceConfig.getInt("rare"),
							georgePriceConfig.getInt("epic"),
							georgePriceConfig.getInt("legendary"),
							georgePriceConfig.getInt("rest")
					);

					// Parse kat upgrades if present
					RarityValue<KatUpgrade> katUpgrades = null;
					if (safeConfig.containsKey("kat_upgrades")) {
						SafeConfig katUpgradeConfig = safeConfig.getNested("kat_upgrades");
						katUpgrades = new RarityValue<>(
								parseKatUpgrade(katUpgradeConfig.getNested("common").config),
								parseKatUpgrade(katUpgradeConfig.getNested("uncommon").config),
								parseKatUpgrade(katUpgradeConfig.getNested("rare").config),
								parseKatUpgrade(katUpgradeConfig.getNested("epic").config),
								parseKatUpgrade(katUpgradeConfig.getNested("legendary").config),
								parseKatUpgrade(katUpgradeConfig.getNested("rest").config)
						);
					}

					// Parse base statistics
					SafeConfig baseStatsConfig = safeConfig.getNested("base_statistics");
					ItemStatistics.Builder baseBuilder = ItemStatistics.builder();
					for (String statKey : baseStatsConfig.getKeys()) {
						double value = baseStatsConfig.getDouble(statKey);
						baseBuilder.withBase(ItemStatistic.valueOf(statKey.toUpperCase()), value);
					}
					ItemStatistics baseStatistics = baseBuilder.build();

					// Parse per level statistics
					SafeConfig perLevelStatsConfig = safeConfig.getNested("per_level_statistics");
					Map<Rarity, ItemStatistics> perLevelStatistics = new HashMap<>();
					for (String rarityKey : perLevelStatsConfig.getKeys()) {
						SafeConfig rarityStatsConfig = perLevelStatsConfig.getNested(rarityKey);
						ItemStatistics.Builder rarityBuilder = ItemStatistics.builder();

						for (String statKey : rarityStatsConfig.getKeys()) {
							double value = rarityStatsConfig.getDouble(statKey);
							rarityBuilder.withBase(ItemStatistic.valueOf(statKey.toUpperCase()), value);
						}

						perLevelStatistics.put(Rarity.valueOf(rarityKey.toUpperCase()), rarityBuilder.build());
					}

					// Parse other fields
					int particleIdValue = safeConfig.getInt("particle");
					Particle particleId = Particle.fromId(particleIdValue);
					String skillCategory = safeConfig.getString("skill_category");
					String skullTexture = safeConfig.getString("skull_texture");
					String handlerId = safeConfig.getString("handler_id");

					yield new PetComponent(
							petName,
							georgePrice,
							katUpgrades,
							baseStatistics,
							perLevelStatistics,
							particleId,
							skillCategory,
							skullTexture,
							handlerId
					);
				}
				case "UPGRADES" -> {
					List<Map<String, Object>> levelsConfig = safeConfig.getMapList("levels");
					List<UpgradesComponent.UpgradeLevel> levels = new ArrayList<>();

					for (Map<String, Object> levelConfig : levelsConfig) {
						SafeConfig levelSafeConfig = SafeConfig.of(levelConfig);

						int level = levelSafeConfig.getInt("level");
						List<Map<String, Object>> requirementsConfig = levelSafeConfig.getMapList("requirements");
						List<UpgradesComponent.UpgradeRequirement> requirements = new ArrayList<>();

						for (Map<String, Object> reqConfig : requirementsConfig) {
							SafeConfig reqSafeConfig = SafeConfig.of(reqConfig);

							String typeStr = reqSafeConfig.getString("type");
							UpgradesComponent.UpgradeRequirement.RequirementType type =
									UpgradesComponent.UpgradeRequirement.RequirementType.valueOf(typeStr.toUpperCase());

							Object requirementData;

							switch (type) {
								case ESSENCE -> {
									String essence = reqSafeConfig.getString("essence");
									int amount = reqSafeConfig.getInt("amount");
									requirementData = new UpgradesComponent.UpgradeRequirement.EssenceRequirement(essence, amount);
								}
								case ITEM -> {
									String itemName = reqSafeConfig.getString("item");
									ItemType itemType = ItemType.valueOf(itemName);
									int amount = reqSafeConfig.getInt("amount");
									requirementData = new UpgradesComponent.UpgradeRequirement.ItemRequirement(itemType, amount);
								}
								default -> throw new IllegalArgumentException("Unknown requirement type: " + type);
							}

							requirements.add(new UpgradesComponent.UpgradeRequirement(type, requirementData));
						}

						levels.add(new UpgradesComponent.UpgradeLevel(level, requirements));
					}

					yield new UpgradesComponent(levels);
				}
				default -> throw new IllegalArgumentException("Unknown component type: " + id);
			};
		} catch (SafeConfig.ConfigParseException e) {
			Logger.error("Failed to parse component {} for item {}: {}", id, itemId, e.getMessage());
			return null;
		} catch (Exception e) {
			Logger.error("Unexpected error parsing component {} for item {}: {}", id, itemId, e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static KatUpgrade parseKatUpgrade(Map<String, Object> config) {
		if (config == null || config.isEmpty()) return null;

		SafeConfig safeConfig = SafeConfig.of(config);
		Long time = safeConfig.getLong("time");
		Integer coins = safeConfig.getInt("coins");

		if (safeConfig.containsKey("item")) {
			String item = safeConfig.getString("item");
			Integer amount = safeConfig.getInt("amount");
			return KatUpgrade.WithItem(time, coins, ItemType.valueOf(item), amount);
		}

		return KatUpgrade.OnlyCoins(time, coins);
	}

	private static List<GemstoneComponent.GemstoneSlot> parseGemstoneEntries(List<Map<String, Object>> gemstoneEntries) {
		List<GemstoneComponent.GemstoneSlot> slots = new ArrayList<>();

		for (Map<String, Object> entry : gemstoneEntries) {
			SafeConfig entryConfig = SafeConfig.of(entry);

			String gemstoneName = entryConfig.getString("gemstone");
			Gemstone.Slots slot;
			try {
				slot = Gemstone.Slots.valueOf(gemstoneName);
			} catch (IllegalArgumentException e) {
				slot = Gemstone.Slots.UNIVERSAL;
			}

			int coins = entryConfig.getInt("coins", 50000);
			List<GemstoneComponent.ItemRequirement> itemRequirements = new ArrayList<>();

			if (entryConfig.containsKey("items")) {
				List<String> items = entryConfig.getList("items", String.class);
				for (String item : items) {
					String[] itemParts = item.split(":");
					if (itemParts.length == 2) {
						ItemType itemId = ItemType.valueOf(itemParts[0]);
						int amount = Integer.parseInt(itemParts[1]);
						itemRequirements.add(new GemstoneComponent.ItemRequirement(itemId, amount));
					}
				}
			}

			slots.add(new GemstoneComponent.GemstoneSlot(slot, coins, itemRequirements));
		}

		return slots;
	}
}