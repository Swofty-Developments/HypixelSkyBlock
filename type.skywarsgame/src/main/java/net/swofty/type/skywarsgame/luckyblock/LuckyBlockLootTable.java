package net.swofty.type.skywarsgame.luckyblock;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.effects.LuckyBlockEnvironmentEffect;

import java.util.Random;

/**
 * Loot table for Lucky Blocks in SkyWars.
 * Categories:
 * - Good items (35%): weapons, armor, golden apples, ender pearls
 * - Neutral items (20%): blocks, fishing rod, eggs, snowballs
 * - Potions (20%): speed, strength, jump boost, invisibility
 * - Bad luck (15%): spawn mobs, poison, wither, lightning
 * - Rare/Ultra rare (10%): full armor sets, enchanted weapons, totem
 */
public class LuckyBlockLootTable {
    private static final Random RANDOM = new Random();

    // Timing constants
    private static final int TICKS_PER_SECOND = 20;

    // Reward category percentages
    private static final int GOOD_ITEMS_CHANCE = 35;
    private static final int NEUTRAL_ITEMS_CHANCE = 55;  // 35 + 20
    private static final int POTIONS_CHANCE = 75;        // 55 + 20
    private static final int BAD_LUCK_CHANCE = 90;       // 75 + 15

    /**
     * Convert seconds to game ticks.
     */
    private static int secondsToTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public enum RewardType {
        ITEMS,
        POTION,
        SPAWN_MOB,
        BAD_EFFECT,
        EXPLOSION,
        NOTHING,
        OP_RULE,
        ENVIRONMENT_EFFECT
    }

    public record LuckyBlockReward(
            RewardType type,
            ItemStack[] items,
            Potion potion,
            EntityType mobType,
            int mobCount,
            Component message,
            boolean isGood,
            LuckyBlockEnvironmentEffect environmentEffect
    ) {
        public static LuckyBlockReward items(ItemStack... items) {
            return new LuckyBlockReward(RewardType.ITEMS, items, null, null, 0,
                    Component.text("Lucky!", NamedTextColor.GOLD), true, null);
        }

        public static LuckyBlockReward itemsWithMessage(Component message, ItemStack... items) {
            return new LuckyBlockReward(RewardType.ITEMS, items, null, null, 0, message, true, null);
        }

        public static LuckyBlockReward potion(Potion potion, String effectName) {
            return new LuckyBlockReward(RewardType.POTION, null, potion, null, 0,
                    Component.text("You got " + effectName + "!", NamedTextColor.GREEN), true, null);
        }

        public static LuckyBlockReward badPotion(Potion potion, String effectName) {
            return new LuckyBlockReward(RewardType.BAD_EFFECT, null, potion, null, 0,
                    Component.text("Unlucky! " + effectName + "!", NamedTextColor.RED), false, null);
        }

        public static LuckyBlockReward spawnMob(EntityType mobType, int count) {
            return new LuckyBlockReward(RewardType.SPAWN_MOB, null, null, mobType, count,
                    Component.text("Unlucky! Enemies spawned!", NamedTextColor.RED), false, null);
        }

        public static LuckyBlockReward explosion() {
            return new LuckyBlockReward(RewardType.EXPLOSION, null, null, null, 0,
                    Component.text("BOOM!", NamedTextColor.DARK_RED), false, null);
        }

        public static LuckyBlockReward nothing() {
            return new LuckyBlockReward(RewardType.NOTHING, null, null, null, 0,
                    Component.text("Nothing happened...", NamedTextColor.GRAY), true, null);
        }

        public static LuckyBlockReward opRule() {
            return new LuckyBlockReward(RewardType.OP_RULE, null, null, null, 0,
                    Component.text("OP RULE ACTIVATED!", NamedTextColor.AQUA), true, null);
        }

        public static LuckyBlockReward environmentEffect(LuckyBlockEnvironmentEffect effect) {
            return new LuckyBlockReward(RewardType.ENVIRONMENT_EFFECT, null, null, null, 0,
                    effect.getMessageComponent(), effect.isGood(), effect);
        }

        public static LuckyBlockReward randomGoodEnvironmentEffect() {
            return environmentEffect(LuckyBlockEnvironmentEffect.randomGood());
        }

        public static LuckyBlockReward randomBadEnvironmentEffect() {
            return environmentEffect(LuckyBlockEnvironmentEffect.randomBad());
        }
    }

    /**
     * Generate a random reward from breaking a lucky block (legacy - uses default loot).
     */
    public static LuckyBlockReward generateReward() {
        return generateReward(LuckyBlockType.WILD);
    }

    /**
     * Generate a random reward based on the lucky block type.
     */
    public static LuckyBlockReward generateReward(LuckyBlockType type) {
        return switch (type) {
            case GUARDIAN -> generateGuardianReward();
            case WEAPONRY -> generateWeaponryReward();
            case WILD -> generateWildReward();
            case CRAZY -> generateCrazyReward();
            case INSANE -> generateInsaneReward();
            case OP_RULE -> generateOPRuleReward();
        };
    }

    /**
     * Guardian Lucky Block - Defense items (armor, health, power-ups).
     */
    private static LuckyBlockReward generateGuardianReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < 40) {
            // Armor pieces
            return generateArmorReward();
        } else if (roll < 70) {
            // Health items
            return generateHealthReward();
        } else if (roll < 90) {
            // Defensive potions
            return generateDefensivePotion();
        } else {
            // Rare defensive items
            return generateRareDefensiveItems();
        }
    }

    /**
     * Weaponry Lucky Block - Weapons for melee and ranged attacks.
     */
    private static LuckyBlockReward generateWeaponryReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < 50) {
            // Melee weapons
            return generateMeleeWeapon();
        } else if (roll < 80) {
            // Ranged weapons
            return generateRangedWeapon();
        } else {
            // Rare weapons
            return generateRareWeapons();
        }
    }

    /**
     * Wild Lucky Block - Mix of good and bad effects.
     */
    private static LuckyBlockReward generateWildReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < GOOD_ITEMS_CHANCE) {
            return generateGoodItems();
        } else if (roll < NEUTRAL_ITEMS_CHANCE) {
            return generateNeutralItems();
        } else if (roll < POTIONS_CHANCE) {
            return generatePotion();
        } else if (roll < BAD_LUCK_CHANCE) {
            return generateBadLuck();
        } else {
            return generateRareItems();
        }
    }

    /**
     * Crazy Lucky Block - More chaotic effects than Wild.
     */
    private static LuckyBlockReward generateCrazyReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < 25) {
            return generateGoodItems();
        } else if (roll < 40) {
            return generatePotion();
        } else if (roll < 60) {
            return generateBadLuck();
        } else if (roll < 80) {
            return generateCrazyEffect();
        } else {
            return generateRareItems();
        }
    }

    /**
     * Insane Lucky Block - The most unpredictable effects.
     */
    private static LuckyBlockReward generateInsaneReward() {
        int roll = RANDOM.nextInt(100);

        if (roll < 20) {
            return generateRareItems();
        } else if (roll < 40) {
            return generateBadLuck();
        } else if (roll < 60) {
            return generateCrazyEffect();
        } else if (roll < 80) {
            return generateInsaneEffect();
        } else {
            return generateLegendaryReward();
        }
    }

    /**
     * OP Rule Lucky Block - Activates a game-wide rule.
     * The actual rule activation is handled by OPRuleManager in LuckyBlock.applyReward().
     */
    private static LuckyBlockReward generateOPRuleReward() {
        return LuckyBlockReward.opRule();
    }

    // === Guardian-specific rewards ===

    private static LuckyBlockReward generateArmorReward() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 30) {
            // Iron armor piece
            return switch (RANDOM.nextInt(4)) {
                case 0 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_HELMET));
                case 1 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_CHESTPLATE));
                case 2 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_LEGGINGS));
                default -> LuckyBlockReward.items(ItemStack.of(Material.IRON_BOOTS));
            };
        } else if (subRoll < 50) {
            // Diamond armor piece
            return switch (RANDOM.nextInt(4)) {
                case 0 -> LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_HELMET));
                case 1 -> LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_CHESTPLATE));
                case 2 -> LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_LEGGINGS));
                default -> LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_BOOTS));
            };
        } else if (subRoll < 70) {
            // Gold armor (El Dorado set)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("El Dorado Armor!", NamedTextColor.GOLD),
                    ItemStack.of(Material.GOLDEN_HELMET),
                    ItemStack.of(Material.GOLDEN_CHESTPLATE)
            );
        } else {
            // Leather armor (could be special types later)
            return switch (RANDOM.nextInt(4)) {
                case 0 -> LuckyBlockReward.items(ItemStack.of(Material.LEATHER_HELMET));
                case 1 -> LuckyBlockReward.items(ItemStack.of(Material.LEATHER_CHESTPLATE));
                case 2 -> LuckyBlockReward.items(ItemStack.of(Material.LEATHER_LEGGINGS));
                default -> LuckyBlockReward.items(ItemStack.of(Material.LEATHER_BOOTS));
            };
        }
    }

    private static LuckyBlockReward generateHealthReward() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 40) {
            // Golden apples
            int count = 1 + RANDOM.nextInt(3);
            return LuckyBlockReward.items(ItemStack.of(Material.GOLDEN_APPLE, count));
        } else if (subRoll < 60) {
            // Absorption effect
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.ABSORPTION, (byte) 1, secondsToTicks(60)),
                    "Absorption Hearts"
            );
        } else if (subRoll < 80) {
            // Regeneration effect
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.REGENERATION, (byte) 1, secondsToTicks(30)),
                    "Regeneration II"
            );
        } else {
            // Enchanted golden apple (rare)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("1-up Mushroom!", NamedTextColor.GOLD),
                    ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE)
            );
        }
    }

    private static LuckyBlockReward generateDefensivePotion() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 35) {
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.RESISTANCE, (byte) 0, secondsToTicks(45)),
                    "Resistance"
            );
        } else if (subRoll < 60) {
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, secondsToTicks(60)),
                    "Fire Resistance"
            );
        } else if (subRoll < 85) {
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.HEALTH_BOOST, (byte) 1, secondsToTicks(120)),
                    "+4 Hearts"
            );
        } else {
            // Super Star - brief immunity
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.RESISTANCE, (byte) 4, secondsToTicks(15)),
                    "Super Star!"
            );
        }
    }

    private static LuckyBlockReward generateRareDefensiveItems() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 40) {
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Full Iron Armor!", NamedTextColor.WHITE),
                    ItemStack.of(Material.IRON_HELMET),
                    ItemStack.of(Material.IRON_CHESTPLATE),
                    ItemStack.of(Material.IRON_LEGGINGS),
                    ItemStack.of(Material.IRON_BOOTS)
            );
        } else if (subRoll < 70) {
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Diamond Armor Set!", NamedTextColor.AQUA),
                    ItemStack.of(Material.DIAMOND_HELMET),
                    ItemStack.of(Material.DIAMOND_CHESTPLATE),
                    ItemStack.of(Material.DIAMOND_LEGGINGS),
                    ItemStack.of(Material.DIAMOND_BOOTS)
            );
        } else {
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Totem of Undying!", NamedTextColor.GOLD),
                    ItemStack.of(Material.TOTEM_OF_UNDYING)
            );
        }
    }

    // === Weaponry-specific rewards ===

    private static LuckyBlockReward generateMeleeWeapon() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 30) {
            return LuckyBlockReward.items(ItemStack.of(Material.IRON_SWORD));
        } else if (subRoll < 50) {
            return LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_SWORD));
        } else if (subRoll < 65) {
            return LuckyBlockReward.items(ItemStack.of(Material.IRON_AXE));
        } else if (subRoll < 80) {
            return LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_AXE));
        } else if (subRoll < 90) {
            // The Stick (knockback)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("The Stick!", NamedTextColor.YELLOW),
                    ItemStack.of(Material.STICK)
            );
        } else {
            // Golden sword (El Dorado)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Sword of El Dorado!", NamedTextColor.GOLD),
                    ItemStack.of(Material.GOLDEN_SWORD)
            );
        }
    }

    private static LuckyBlockReward generateRangedWeapon() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 40) {
            int arrows = 16 + RANDOM.nextInt(16);
            return LuckyBlockReward.items(
                    ItemStack.of(Material.BOW),
                    ItemStack.of(Material.ARROW, arrows)
            );
        } else if (subRoll < 60) {
            // Crossbow
            int arrows = 8 + RANDOM.nextInt(8);
            return LuckyBlockReward.items(
                    ItemStack.of(Material.CROSSBOW),
                    ItemStack.of(Material.ARROW, arrows)
            );
        } else if (subRoll < 80) {
            // Ender pearls
            int count = 2 + RANDOM.nextInt(3);
            return LuckyBlockReward.items(ItemStack.of(Material.ENDER_PEARL, count));
        } else {
            // Snowballs (paintballs)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Paintballs!", NamedTextColor.WHITE),
                    ItemStack.of(Material.SNOWBALL, 64)
            );
        }
    }

    private static LuckyBlockReward generateRareWeapons() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 40) {
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Netherite Sword!", NamedTextColor.DARK_RED),
                    ItemStack.of(Material.NETHERITE_SWORD)
            );
        } else if (subRoll < 70) {
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Trident!", NamedTextColor.AQUA),
                    ItemStack.of(Material.TRIDENT)
            );
        } else {
            // Wither blast materials (blaze rods)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Wither Blast Rod!", NamedTextColor.DARK_GRAY),
                    ItemStack.of(Material.BLAZE_ROD, 5)
            );
        }
    }

    // === Crazy/Insane effects ===

    private static LuckyBlockReward generateCrazyEffect() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 15) {
            // Spawn wolves
            return LuckyBlockReward.spawnMob(EntityType.WOLF, 3);
        } else if (subRoll < 30) {
            // Random teleport environment effect
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.RANDOM_TELEPORT);
        } else if (subRoll < 45) {
            // Launch pad
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.LAUNCH_PAD);
        } else if (subRoll < 55) {
            // Bridge eggs (eggs)
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Bridge Eggs!", NamedTextColor.YELLOW),
                    ItemStack.of(Material.EGG, 4)
            );
        } else if (subRoll < 70) {
            // Cobweb trap
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.COBWEB_TRAP);
        } else if (subRoll < 85) {
            // Fireball items
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Fireballs!", NamedTextColor.RED),
                    ItemStack.of(Material.FIRE_CHARGE, 8)
            );
        } else {
            // Protective wall (good effect)
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.PROTECTIVE_WALL);
        }
    }

    private static LuckyBlockReward generateInsaneEffect() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 15) {
            // Big slime
            return LuckyBlockReward.spawnMob(EntityType.SLIME, 1);
        } else if (subRoll < 25) {
            // Wither skeleton spawn
            return LuckyBlockReward.spawnMob(EntityType.WITHER_SKELETON, 2);
        } else if (subRoll < 35) {
            // Explosion
            return LuckyBlockReward.explosion();
        } else if (subRoll < 45) {
            // Lightning strike
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.LIGHTNING_STRIKE);
        } else if (subRoll < 55) {
            // TNT surround
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.TNT_SURROUND);
        } else if (subRoll < 65) {
            // Lava pit
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.LAVA_PIT);
        } else if (subRoll < 75) {
            // Arrow rain
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.ARROW_RAIN);
        } else if (subRoll < 85) {
            // Anvil rain
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.ANVIL_RAIN);
        } else if (subRoll < 92) {
            // Curse
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.CURSE);
        } else {
            // Blessing (rare good outcome in insane)
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.BLESSING);
        }
    }

    private static LuckyBlockReward generateLegendaryReward() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 30) {
            // Full netherite
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("LEGENDARY! Full Netherite!", NamedTextColor.DARK_PURPLE),
                    ItemStack.of(Material.NETHERITE_HELMET),
                    ItemStack.of(Material.NETHERITE_CHESTPLATE),
                    ItemStack.of(Material.NETHERITE_LEGGINGS),
                    ItemStack.of(Material.NETHERITE_BOOTS),
                    ItemStack.of(Material.NETHERITE_SWORD)
            );
        } else if (subRoll < 60) {
            // Elytra + fireworks
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("LEGENDARY! Elytra!", NamedTextColor.LIGHT_PURPLE),
                    ItemStack.of(Material.ELYTRA),
                    ItemStack.of(Material.FIREWORK_ROCKET, 16)
            );
        } else if (subRoll < 80) {
            // Stack of golden apples
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("Golden Apples x32!", NamedTextColor.GOLD),
                    ItemStack.of(Material.GOLDEN_APPLE, 32)
            );
        } else {
            // Multiple enchanted golden apples
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("ULTRA RARE! Enchanted Golden Apples!", NamedTextColor.GOLD),
                    ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE, 3)
            );
        }
    }

    private static LuckyBlockReward generateGoodItems() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 25) {
            // Weapons
            return switch (RANDOM.nextInt(4)) {
                case 0 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_SWORD));
                case 1 -> LuckyBlockReward.items(ItemStack.of(Material.DIAMOND_SWORD));
                case 2 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_AXE));
                default -> LuckyBlockReward.items(ItemStack.of(Material.BOW), ItemStack.of(Material.ARROW, 16));
            };
        } else if (subRoll < 50) {
            // Armor pieces
            return switch (RANDOM.nextInt(4)) {
                case 0 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_HELMET));
                case 1 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_CHESTPLATE));
                case 2 -> LuckyBlockReward.items(ItemStack.of(Material.IRON_LEGGINGS));
                default -> LuckyBlockReward.items(ItemStack.of(Material.IRON_BOOTS));
            };
        } else if (subRoll < 70) {
            // Golden apples
            int count = 1 + RANDOM.nextInt(2);
            return LuckyBlockReward.items(ItemStack.of(Material.GOLDEN_APPLE, count));
        } else if (subRoll < 85) {
            // Ender pearls
            int count = 1 + RANDOM.nextInt(3);
            return LuckyBlockReward.items(ItemStack.of(Material.ENDER_PEARL, count));
        } else {
            // Arrows
            int count = 8 + RANDOM.nextInt(8);
            return LuckyBlockReward.items(ItemStack.of(Material.ARROW, count));
        }
    }

    private static LuckyBlockReward generateNeutralItems() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 30) {
            // Building blocks
            int count = 32 + RANDOM.nextInt(33);
            return LuckyBlockReward.items(ItemStack.of(Material.OAK_PLANKS, count));
        } else if (subRoll < 50) {
            // Fishing rod
            return LuckyBlockReward.items(ItemStack.of(Material.FISHING_ROD));
        } else if (subRoll < 70) {
            // Eggs
            int count = 8 + RANDOM.nextInt(8);
            return LuckyBlockReward.items(ItemStack.of(Material.EGG, count));
        } else if (subRoll < 85) {
            // Snowballs
            int count = 8 + RANDOM.nextInt(8);
            return LuckyBlockReward.items(ItemStack.of(Material.SNOWBALL, count));
        } else {
            // Cobwebs
            int count = 2 + RANDOM.nextInt(3);
            return LuckyBlockReward.items(ItemStack.of(Material.COBWEB, count));
        }
    }

    private static LuckyBlockReward generatePotion() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 30) {
            // Speed
            int duration = 30 + RANDOM.nextInt(31);
            int amplifier = RANDOM.nextInt(2);
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.SPEED, (byte) amplifier, secondsToTicks(duration)),
                    "Speed " + (amplifier + 1));
        } else if (subRoll < 55) {
            // Strength
            int duration = 20 + RANDOM.nextInt(21);
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.STRENGTH, (byte) 0, secondsToTicks(duration)),
                    "Strength");
        } else if (subRoll < 75) {
            // Jump boost
            int duration = 30 + RANDOM.nextInt(31);
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.JUMP_BOOST, (byte) 1, secondsToTicks(duration)),
                    "Jump Boost II");
        } else if (subRoll < 90) {
            // Invisibility
            int duration = 15 + RANDOM.nextInt(16);
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.INVISIBILITY, (byte) 0, secondsToTicks(duration)),
                    "Invisibility");
        } else {
            // Regeneration
            int duration = 10 + RANDOM.nextInt(11);
            return LuckyBlockReward.potion(
                    new Potion(PotionEffect.REGENERATION, (byte) 1, secondsToTicks(duration)),
                    "Regeneration II");
        }
    }

    private static LuckyBlockReward generateBadLuck() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 20) {
            // Spawn zombie
            int count = 1 + RANDOM.nextInt(2);
            return LuckyBlockReward.spawnMob(EntityType.ZOMBIE, count);
        } else if (subRoll < 35) {
            // Spawn skeleton
            return LuckyBlockReward.spawnMob(EntityType.SKELETON, 1);
        } else if (subRoll < 45) {
            // Spawn silverfish
            int count = 2 + RANDOM.nextInt(3);
            return LuckyBlockReward.spawnMob(EntityType.SILVERFISH, count);
        } else if (subRoll < 55) {
            // Poison effect
            int duration = 5 + RANDOM.nextInt(6);
            return LuckyBlockReward.badPotion(
                    new Potion(PotionEffect.POISON, (byte) 0, secondsToTicks(duration)),
                    "Poison");
        } else if (subRoll < 65) {
            // Wither effect (short)
            int duration = 3 + RANDOM.nextInt(3);
            return LuckyBlockReward.badPotion(
                    new Potion(PotionEffect.WITHER, (byte) 0, secondsToTicks(duration)),
                    "Wither");
        } else if (subRoll < 75) {
            // Cobweb trap
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.COBWEB_TRAP);
        } else if (subRoll < 85) {
            // Lightning strike
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.LIGHTNING_STRIKE);
        } else {
            // Explosion
            return LuckyBlockReward.explosion();
        }
    }

    private static LuckyBlockReward generateRareItems() {
        int subRoll = RANDOM.nextInt(100);

        if (subRoll < 25) {
            // Full iron armor set
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("LEGENDARY! Full Iron Armor!", NamedTextColor.GOLD),
                    ItemStack.of(Material.IRON_HELMET),
                    ItemStack.of(Material.IRON_CHESTPLATE),
                    ItemStack.of(Material.IRON_LEGGINGS),
                    ItemStack.of(Material.IRON_BOOTS)
            );
        } else if (subRoll < 45) {
            // Diamond armor pieces (2 random)
            Material[] diamondArmor = {Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
                    Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
            int idx1 = RANDOM.nextInt(4);
            int idx2 = (idx1 + 1 + RANDOM.nextInt(3)) % 4;
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("RARE! Diamond Armor!", NamedTextColor.AQUA),
                    ItemStack.of(diamondArmor[idx1]),
                    ItemStack.of(diamondArmor[idx2])
            );
        } else if (subRoll < 60) {
            // Netherite sword
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("ULTRA RARE! Netherite Sword!", NamedTextColor.DARK_PURPLE),
                    ItemStack.of(Material.NETHERITE_SWORD)
            );
        } else if (subRoll < 75) {
            // Enchanted Golden Apple
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("LEGENDARY! Enchanted Golden Apple!", NamedTextColor.GOLD),
                    ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE)
            );
        } else if (subRoll < 85) {
            // Totem of Undying
            return LuckyBlockReward.itemsWithMessage(
                    Component.text("ULTRA RARE! Totem of Undying!", NamedTextColor.DARK_PURPLE),
                    ItemStack.of(Material.TOTEM_OF_UNDYING)
            );
        } else if (subRoll < 93) {
            // Instant bridge
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.BRIDGE_BUILD);
        } else {
            // Lucky blessing
            return LuckyBlockReward.environmentEffect(LuckyBlockEnvironmentEffect.BLESSING);
        }
    }
}
