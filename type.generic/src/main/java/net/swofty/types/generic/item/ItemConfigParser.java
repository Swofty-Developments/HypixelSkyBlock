package net.swofty.types.generic.item;

import net.minestom.server.color.Color;
import net.minestom.server.item.ItemAnimation;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.PotatoType;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.reforge.ReforgeType;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.components.*;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.handlers.pet.KatUpgrade;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.utility.RarityValue;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfigParser {
    public static ConfigurableSkyBlockItem parseItem(Map<String, Object> config) {
        String id = (String) config.get("id");
        // Clean up the ID
        id = id.replaceAll("[^a-zA-Z0-9_]", "");

        Material material = Material.values().stream().filter(loopedMaterial -> {
            return loopedMaterial.key().value().equalsIgnoreCase((String) config.get("material"));
        }).findFirst().orElse(Material.AIR);

        List<String> lore = (List<String>) config.get("lore");
        Map<String, Double> statistics = new HashMap<>();

        if (config.containsKey("default_statistics")) {
            // Convert all the objects to doubles, noting they may be integers
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) config.get("default_statistics")).entrySet()) {
                statistics.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
            }
        }

        ConfigurableSkyBlockItem item = new ConfigurableSkyBlockItem(id, material, lore, statistics);

        List<Map<String, Object>> components = (List<Map<String, Object>>) config.get("components");
        if (components == null) components = new ArrayList<>();
        for (Map<String, Object> componentConfig : components) {
            String componentId = (String) componentConfig.get("id");
            SkyBlockItemComponent component = parseComponent(id, componentId, componentConfig);
            if (component != null) {
                // Mark all components from YAML as explicit
                item.addComponent(component, true);
            }
        }

        item.register();
        return item;
    }

    private static @Nullable SkyBlockItemComponent parseComponent(String itemId, String id, Map<String, Object> config) {
        return switch (id.toUpperCase()) {
            case "ABILITY" -> {
                List<String> abilities = (List<String>) config.get("abilities");
                yield new AbilityComponent(abilities);
            }
            case "TALISMAN", "ACCESSORY" -> new AccessoryComponent();
            case "ANVIL_COMBINABLE" -> {
                String handlerId = (String) config.get("handler_id");
                yield new AnvilCombinableComponent(handlerId);
            }
            case "ARMOR" -> new ArmorComponent();
            case "ARROW" -> new ArrowComponent();
            case "AUCTION_CATEGORY" -> {
                String category = (String) config.get("category");
                yield new AuctionCategoryComponent(category);
            }
            case "AXE" -> new AxeComponent();
            case "BACKPACK" -> {
                int rows = (int) config.get("rows");
                String skullTexture = (String) config.get("skull-texture");
                yield new BackpackComponent(rows, skullTexture);
            }
            case "BOW" -> {
                String handlerId = (String) config.get("handler_id");
                boolean shouldBeArrow = (boolean) config.getOrDefault("should-be-arrow", true);
                yield new BowComponent(handlerId, shouldBeArrow);
            }
            case "CONSTANT_STATISTICS" -> new ConstantStatisticsComponent();
            case "DEFAULT_CRAFTABLE" -> {
                List<Map<String, Object>> recipes = (List<Map<String, Object>>) config.get("recipes");
                boolean defaultCraftable = true;
                if (config.containsKey("default-craftable")) {
                    defaultCraftable = (boolean) config.get("default-craftable");
                }
                CraftableComponent component = new CraftableComponent(recipes);
                component.setDefaultCraftable(defaultCraftable);
                yield component;
            }
            case "CUSTOM_DISPLAY_NAME" -> new CustomDisplayNameComponent((item) -> config.get("display_name").toString());
            case "DECORATION_HEAD" -> {
                String texture = (String) config.get("texture");
                yield new DecorationHeadComponent(texture);
            }
            case "DEFAULT_SOULBOUND" -> {
                boolean coopAllowed = (boolean) config.get("coop_allowed");
                yield new DefaultSoulboundComponent(coopAllowed);
            }
            case "DISABLE_ANIMATION" -> {
                List<ItemAnimation> animations = (List<ItemAnimation>) config.get("disabled_animations");
                yield new DisableAnimationComponent(animations);
            }
            case "DRILL" -> new DrillComponent();
            case "ENCHANTABLE" -> {
                List<String> groups = (List<String>) config.getOrDefault("enchant_groups", List.of());
                boolean showLores = (boolean) config.getOrDefault("show_lores", true);
                yield new EnchantableComponent(
                        groups.stream().map(EnchantItemGroups::valueOf).toList(),
                        showLores
                );
            }
            case "ENCHANTED" -> {
                if (config.containsKey("recipe_type") && config.containsKey("item_id")) {
                    SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.valueOf((String) config.get("recipe_type"));
                    String baseMaterial = (String) config.get("item_id");
                    yield new EnchantedComponent(type, itemId, baseMaterial);
                }
                yield new EnchantedComponent();
            }
            case "EXTRA_RARITY" -> {
                String display = (String) config.get("display");
                yield new ExtraRarityComponent(display);
            }
            case "DUNGEON_ITEM" -> new ExtraRarityComponent("DUNGEON ITEM");
            case "EXTRA_UNDER_NAME" -> {
                if (config.containsKey("displays")) {
                    List<String> displays = (List<String>) config.get("displays");
                    yield new ExtraUnderNameComponent(displays);
                } else {
                    String display = (String) config.get("display");
                    yield new ExtraUnderNameComponent(display);
                }
            }
            case "GEMSTONE" -> {
                List<String> gemstones = (List<String>) config.get("gemstones");
                yield new GemstoneComponent(gemstones);
            }
            case "GEMSTONE_IMPL" -> {
                GemRarity rarity = GemRarity.valueOf((String) config.get("rarity"));
                Gemstone gemstone = Gemstone.valueOf((String) config.get("gemstone"));
                String texture = (String) config.get("skull_texture");
                yield new GemstoneImplComponent(rarity, gemstone, texture);
            }
            case "HOT_POTATO" -> {
                String type = (String) config.get("potato_type");

                if (config.containsKey("appliable_items")) {
                    var appliableItems = (List<String>) config.get("appliable_items");
                    HashMap<ItemType, Integer> appliable = new HashMap<>();

                    for (var item : appliableItems) {
                        var split = item.split(":");

                        if (split.length != 2)
                            continue;

                        appliable.put(ItemType.valueOf(split[0]), Integer.parseInt(split[1]));
                    }

                    yield new HotPotatoableComponent(PotatoType.valueOf(type), appliable);
                }

                yield new HotPotatoableComponent(PotatoType.valueOf(type));
            }
            case "INTERACTABLE" -> {
                String handlerId = (String) config.get("handler_id");
                try {
                    yield new InteractableComponent(handlerId);
                } catch (Exception e) {
                    Logger.error("Failed to parse InteractableComponent for " + handlerId);
                    yield null;
                }
            }
            case "KAT" -> {
                int reducedDays = (int) config.get("reduced_days");
                yield new KatComponent(reducedDays);
            }
            case "LEATHER_COLOR" -> {
                String r = (String) config.get("r");
                String g = (String) config.get("g");
                String b = (String) config.get("b");

                Color color = new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
                yield new LeatherColorComponent(color);
            }
            case "MINION" -> {
                String minionType = (String) config.get("minion_type");
                String baseItem = (String) config.get("base_item");
                boolean isByDefaultCraftable = (boolean) config.get("default_craftable");

                List<String> ingredients = (List<String>) config.get("ingredients");
                List<MinionIngredient> ingredientsMap = new ArrayList<>();

                for (String ingredient : ingredients) {
                    String[] ingredientParts = ingredient.split(":");
                    ingredientsMap.add(new MinionIngredient(
                            ItemType.valueOf(ingredientParts[0]),
                            Integer.parseInt(ingredientParts[1])
                    ));
                }

                yield new MinionComponent(minionType,baseItem, isByDefaultCraftable, ingredientsMap);
            }
            case "MINION_FUEL" -> {
                double percentage = (double) config.get("fuel_percentage");
                long lastTime = (int) config.get("last_time_ms");
                yield new MinionFuelComponent(percentage, lastTime);
            }
            case "MINION_SHIPPING" -> {
                double percentage = (double) config.get("percentage");
                yield new MinionShippingComponent(percentage);
            }
            case "MINION_SKIN" -> {
                String skinName = (String) config.get("name");
                Map<String, Object> helmetConfig = (Map<String, Object>) config.get("helmet");
                Map<String, Object> chestplateConfig = (Map<String, Object>) config.get("chestplate");
                Map<String, Object> leggingsConfig = (Map<String, Object>) config.get("leggings");
                Map<String, Object> bootsConfig = (Map<String, Object>) config.get("boots");

                MinionSkinComponent.MinionArmorPiece helmet = helmetConfig != null ? MinionSkinComponent.MinionArmorPiece.fromConfig(helmetConfig) :
                        new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null);
                MinionSkinComponent.MinionArmorPiece chestplate = chestplateConfig != null ? MinionSkinComponent.MinionArmorPiece.fromConfig(chestplateConfig) :
                        new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null);
                MinionSkinComponent.MinionArmorPiece leggings = leggingsConfig != null ? MinionSkinComponent.MinionArmorPiece.fromConfig(leggingsConfig) :
                        new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null);
                MinionSkinComponent.MinionArmorPiece boots = bootsConfig != null ? MinionSkinComponent.MinionArmorPiece.fromConfig(bootsConfig) :
                        new MinionSkinComponent.MinionArmorPiece(Material.AIR, null, null);

                yield new MinionSkinComponent(skinName, helmet, chestplate, leggings, boots);
            }
            case "MINION_UPGRADE" -> {
                double speedIncrease = (double) config.get("speed_increase");
                yield new MinionUpgradeComponent(speedIncrease);
            }
            case "MUSEUM" -> {
                String category = (String) config.get("museum_category");
                yield new MuseumComponent(category);
            }
            case "NOT_FINISHED_YET" -> new NotFinishedYetComponent();
            case "LORE_UPDATE" -> {
                boolean isAbsolute = (boolean) config.getOrDefault("is_absolute", false);
                yield new LoreUpdateComponent(config.get("handler_id").toString(), isAbsolute);
            }
            case "PET_ITEM" -> new PetItemComponent();
            case "PICKAXE" -> new PickaxeComponent();
            case "PLACEABLE" -> {
                String blockType = (String) config.get("block_type");
                yield new PlaceableComponent(blockType);
            }
            case "PLACE_EVENT" -> {
                String handlerId = (String) config.get("handler_id");
                yield new PlaceEventComponent(handlerId);
            }
            case "POWER_STONE" -> new PowerStoneComponent();
            case "QUIVER_DISPLAY" -> {
                boolean shouldBeArrow = (boolean) config.get("should_be_arrow");
                yield new QuiverDisplayComponent(shouldBeArrow);
            }
            case "REFORGABLE" -> {
                String type = (String) config.get("reforge_type");
                yield new ReforgableComponent(ReforgeType.valueOf(type));
            }
            case "RIGHT_CLICK_RECIPE" -> {
                String recipeItem = (String) config.get("recipe_item");
                yield new RightClickRecipeComponent(recipeItem);
            }
            case "RUNEABLE" -> {
                String applicableTo = (String) config.get("applicable_to");
                yield new RuneableComponent(RuneableComponent.RuneApplicableTo.valueOf(applicableTo));
            }
            case "CUSTOM_DROP" -> {
                List<Map<String, Object>> rulesConfig = (List<Map<String, Object>>) config.get("rules");
                List<CustomDropComponent.DropRule> rules = new ArrayList<>();

                for (Map<String, Object> ruleConfig : rulesConfig) {
                    // Parse conditions
                    Map<String, Object> conditionsConfig = (Map<String, Object>) ruleConfig.get("conditions");
                    CustomDropComponent.DropConditions conditions = CustomDropComponent.parseDropConditions(conditionsConfig);

                    // Parse drops
                    List<Map<String, Object>> dropsConfig = (List<Map<String, Object>>) ruleConfig.get("drops");
                    List<CustomDropComponent.Drop> drops = new ArrayList<>();

                    for (Map<String, Object> dropConfig : dropsConfig) {
                        String itemName = (String) dropConfig.get("item");
                        ItemType itemType = ItemType.valueOf(itemName);
                        double chance = ((Number) dropConfig.get("chance")).doubleValue();
                        String amount = dropConfig.get("amount").toString();

                        drops.add(new CustomDropComponent.Drop(itemType, chance, amount));
                    }

                    rules.add(new CustomDropComponent.DropRule(conditions, drops));
                }

                yield new CustomDropComponent(rules);
            }
            case "RUNE" -> {
                int level = (int) config.get("required_level");
                String color = (String) config.get("color");
                String applicableTo = (String) config.get("applicable_to");
                String texture = (String) config.get("skull_texture");
                yield new RuneComponent(level, color, applicableTo, texture);
            }
            case "SACK" -> {
                List<String> items = (List<String>) config.get("valid_items");
                int capacity = (int) config.get("max_capacity");
                yield new SackComponent(items, capacity);
            }
            case "SELLABLE" -> {
                Object value = config.get("value");
                if (value instanceof Double) {
                    yield new SellableComponent((double) value);
                } else if (value instanceof Integer) {
                    yield new SellableComponent((int) value);
                }
                yield new SellableComponent(1);
            }
            case "SERVER_ORB" -> {
                String handlerId = (String) config.get("handler_id");
                List<String> blockStrings = ((List<String>) config.getOrDefault("valid_blocks", List.of())).stream().map(
                        String::toLowerCase
                ).toList();
                List<Material> materials = Material.values().stream()
                        .filter(material -> blockStrings.contains(material.key().value().toLowerCase()))
                        .toList();

                yield new ServerOrbComponent(handlerId, materials);
            }
            case "SHORT_BOW" -> {
                String handlerId = (String) config.get("handler_id");
                float cooldown = (float) config.get("cooldown");
                boolean shouldBeArrow = (boolean) config.getOrDefault("should-be-arrow", true);
                yield new ShortBowComponent(cooldown, handlerId, shouldBeArrow);
            }
            case "SHOVEL" -> new ShovelComponent();
            case "SKILLABLE_MINE" -> {
                String category = (String) config.get("category");
                double value = (double) config.get("mining_value");
                yield new SkillableMineComponent(category, value);
            }
            case "SKULL_HEAD" -> new SkullHeadComponent((item) -> config.get("texture").toString());
            case "STANDARD_ITEM" -> {
                String type = (String) config.get("standard_item_type");
                yield new StandardItemComponent(type);
            }
            case "CUSTOM_STATISTICS" -> {
                String handlerId = (String) config.get("handler_id");
                yield new CustomStatisticsComponent(handlerId);
            }
            case "TIERED_TALISMAN" -> {
                ItemType baseTier = ItemType.valueOf((String) config.get("base_tier"));
                int tier = (int) config.get("tier");
                yield new TieredTalismanComponent(baseTier, tier);
            }
            case "TRACKED_UNIQUE" -> new TrackedUniqueComponent();
            case "TRAVEL_SCROLL" -> {
                String scrollType = (String) config.get("scroll_type");
                yield new TravelScrollComponent(scrollType);
            }
            case "PET" -> {
                String petName = (String) config.get("pet_name");

                // Parse george price
                Map<String, Integer> georgePriceMap = (Map<String, Integer>) config.get("george_price");
                RarityValue<Integer> georgePrice = new RarityValue<>(
                        georgePriceMap.get("common"),
                        georgePriceMap.get("uncommon"),
                        georgePriceMap.get("rare"),
                        georgePriceMap.get("epic"),
                        georgePriceMap.get("legendary"),
                        georgePriceMap.get("rest")
                );

                // Parse kat upgrades if present
                RarityValue<KatUpgrade> katUpgrades = null;
                if (config.containsKey("kat_upgrades")) {
                    Map<String, Map<String, Object>> katUpgradeMap = (Map<String, Map<String, Object>>) config.get("kat_upgrades");
                    katUpgrades = new RarityValue<>(
                            parseKatUpgrade(katUpgradeMap.get("common")),
                            parseKatUpgrade(katUpgradeMap.get("uncommon")),
                            parseKatUpgrade(katUpgradeMap.get("rare")),
                            parseKatUpgrade(katUpgradeMap.get("epic")),
                            parseKatUpgrade(katUpgradeMap.get("legendary")),
                            parseKatUpgrade(katUpgradeMap.get("rest"))
                    );
                }

                // Parse base statistics
                Map<String, Double> baseStatsMap = (Map<String, Double>) config.get("base_statistics");
                ItemStatistics.Builder baseBuilder = ItemStatistics.builder();
                baseStatsMap.forEach((stat, value) ->
                        baseBuilder.withBase(ItemStatistic.valueOf(stat.toUpperCase()), value)
                );
                ItemStatistics baseStatistics = baseBuilder.build();

                // Parse per level statistics
                Map<String, Object> perLevelStatsMap = (Map<String, Object>) config.get("per_level_statistics");
                Map<Rarity, ItemStatistics> perLevelStatistics = new HashMap<>();
                for (Map.Entry<String, Object> entry : perLevelStatsMap.entrySet()) {
                    String rarity = entry.getKey();
                    ItemStatistics.Builder rarityBuilder = ItemStatistics.builder();
                    try {
                        Map<String, Double> rarityStatsMap = (Map<String, Double>) entry.getValue();
                        rarityBuilder = ItemStatistics.builder();
                        for (Map.Entry<String, Double> e : rarityStatsMap.entrySet()) {
                            String stat = e.getKey();
                            Double value = e.getValue();
                            rarityBuilder.withBase(ItemStatistic.valueOf(stat.toUpperCase()), value);
                        }
                    } catch (ClassCastException e) {
                        // Per level statistics is a map with an Integer, so we need to convert it to a double
                        Map<String, Integer> rarityStatsMap = (Map<String, Integer>) entry.getValue();
                        rarityBuilder = ItemStatistics.builder();
                        for (Map.Entry<String, Integer> mapEntry : rarityStatsMap.entrySet()) {
                            String stat = mapEntry.getKey();
                            Integer value = mapEntry.getValue();
                            rarityBuilder.withBase(ItemStatistic.valueOf(stat.toUpperCase()), Double.valueOf(value));
                        }
                    }
                    perLevelStatistics.put(Rarity.valueOf(rarity.toUpperCase()), rarityBuilder.build());
                }

                // Parse other fields
                Particle particleId = Particle.fromId((Integer) config.get("particle"));
                String skillCategory = (String) config.get("skill_category");
                String skullTexture = (String) config.get("skull_texture");
                String handlerId = (String) config.get("handler_id");

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
            default -> throw new IllegalArgumentException("Unknown component type: " + id);
        };
    }

    private static KatUpgrade parseKatUpgrade(Map<String, Object> config) {
        if (config == null) return null;

        Long time = ((Number) config.get("time")).longValue();
        Integer coins = (Integer) config.get("coins");

        if (config.containsKey("item")) {
            String item = (String) config.get("item");
            Integer amount = (Integer) config.get("amount");
            return KatUpgrade.WithItem(time, coins, ItemType.valueOf(item), amount);
        }

        return KatUpgrade.OnlyCoins(time, coins);
    }
}