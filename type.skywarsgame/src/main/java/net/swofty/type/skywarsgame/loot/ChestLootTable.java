package net.swofty.type.skywarsgame.loot;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.type.skywarsgame.luckyblock.LuckyBlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestLootTable {
    private static final Random RANDOM = new Random();

    public static List<ItemStack> generateLoot(LootTier tier, boolean insane) {
        return generateLoot(tier, insane, null);
    }

    public static List<ItemStack> generateLoot(LootTier tier, boolean insane, SkywarsGameType gameType) {
        List<ItemStack> loot = new ArrayList<>();

        int itemCount = switch (tier) {
            case ISLAND -> 3 + RANDOM.nextInt(3);
            case CENTER -> 5 + RANDOM.nextInt(4);
        };

        for (int i = 0; i < itemCount; i++) {
            ItemStack item = generateItem(tier, insane, gameType);
            if (item != null) {
                loot.add(item);
            }
        }

        if (gameType == SkywarsGameType.SOLO_LUCKY_BLOCK) {
            int luckyBlockCount = switch (tier) {
                case ISLAND -> 1 + RANDOM.nextInt(2);
                case CENTER -> 2 + RANDOM.nextInt(2);
            };
            for (int i = 0; i < luckyBlockCount; i++) {
                loot.add(tier == LootTier.ISLAND ? getIslandLuckyBlock() : getCenterLuckyBlock());
            }
        }

        return loot;
    }

    private static ItemStack generateItem(LootTier tier, boolean insane, SkywarsGameType gameType) {
        int roll = RANDOM.nextInt(100);

        return switch (tier) {
            case ISLAND -> generateIslandLoot(roll, insane, gameType);
            case CENTER -> generateCenterLoot(roll, insane, gameType);
        };
    }

    private static ItemStack generateIslandLoot(int roll, boolean insane, SkywarsGameType gameType) {
        if (roll < 20) {
            return insane ? getInsaneWeapon() : getNormalWeapon();
        } else if (roll < 40) {
            return insane ? getInsaneArmor() : getNormalArmor();
        } else if (roll < 60) {
            return ItemStack.of(Material.OAK_PLANKS, 16 + RANDOM.nextInt(17));
        } else if (roll < 75) {
            return getFood();
        } else if (roll < 85) {
            return RANDOM.nextBoolean() ?
                    ItemStack.of(Material.BOW) :
                    ItemStack.of(Material.ARROW, 4 + RANDOM.nextInt(5));
        } else if (roll < 95) {
            return getTool();
        } else {
            return insane ? getInsaneSpecial() : getNormalSpecial();
        }
    }

    private static ItemStack generateCenterLoot(int roll, boolean insane, SkywarsGameType gameType) {
        if (roll < 30) {
            return insane ? getInsaneCenterWeapon() : getCenterWeapon();
        } else if (roll < 55) {
            return insane ? getInsaneCenterArmor() : getCenterArmor();
        } else if (roll < 70) {
            return getEnchantedItem(insane);
        } else if (roll < 80) {
            return ItemStack.of(Material.GOLDEN_APPLE, 1 + RANDOM.nextInt(2));
        } else if (roll < 90) {
            return ItemStack.of(Material.ARROW, 8 + RANDOM.nextInt(9));
        } else {
            return insane ? getInsaneCenterSpecial() : getCenterSpecial();
        }
    }

    private static ItemStack getIslandLuckyBlock() {
        int roll = RANDOM.nextInt(100);
        if (roll < 40) {
            return LuckyBlockType.GUARDIAN.createItemStack();
        } else if (roll < 80) {
            return LuckyBlockType.WEAPONRY.createItemStack();
        } else {
            return LuckyBlockType.WILD.createItemStack();
        }
    }

    private static ItemStack getCenterLuckyBlock() {
        int roll = RANDOM.nextInt(100);
        if (roll < 20) {
            return LuckyBlockType.GUARDIAN.createItemStack();
        } else if (roll < 40) {
            return LuckyBlockType.WEAPONRY.createItemStack();
        } else if (roll < 55) {
            return LuckyBlockType.WILD.createItemStack();
        } else if (roll < 70) {
            return LuckyBlockType.CRAZY.createItemStack();
        } else if (roll < 90) {
            return LuckyBlockType.INSANE.createItemStack();
        } else {
            return LuckyBlockType.OP_RULE.createItemStack();
        }
    }

    private static ItemStack getNormalWeapon() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.WOODEN_SWORD);
            case 1 -> ItemStack.of(Material.STONE_SWORD);
            default -> ItemStack.of(Material.WOODEN_AXE);
        };
    }

    private static ItemStack getInsaneWeapon() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.STONE_SWORD);
            case 1 -> ItemStack.of(Material.IRON_SWORD);
            default -> ItemStack.of(Material.STONE_AXE);
        };
    }

    private static ItemStack getCenterWeapon() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.IRON_SWORD);
            case 1 -> ItemStack.of(Material.DIAMOND_SWORD);
            default -> ItemStack.of(Material.IRON_AXE);
        };
    }

    private static ItemStack getInsaneCenterWeapon() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.DIAMOND_SWORD);
            case 1 -> ItemStack.of(Material.NETHERITE_SWORD);
            default -> ItemStack.of(Material.DIAMOND_AXE);
        };
    }

    private static ItemStack getNormalArmor() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.LEATHER_HELMET);
            case 1 -> ItemStack.of(Material.LEATHER_CHESTPLATE);
            case 2 -> ItemStack.of(Material.CHAINMAIL_HELMET);
            default -> ItemStack.of(Material.CHAINMAIL_BOOTS);
        };
    }

    private static ItemStack getInsaneArmor() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.IRON_HELMET);
            case 1 -> ItemStack.of(Material.IRON_CHESTPLATE);
            case 2 -> ItemStack.of(Material.IRON_LEGGINGS);
            default -> ItemStack.of(Material.IRON_BOOTS);
        };
    }

    private static ItemStack getCenterArmor() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.IRON_HELMET);
            case 1 -> ItemStack.of(Material.IRON_CHESTPLATE);
            case 2 -> ItemStack.of(Material.DIAMOND_HELMET);
            default -> ItemStack.of(Material.DIAMOND_BOOTS);
        };
    }

    private static ItemStack getInsaneCenterArmor() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.DIAMOND_HELMET);
            case 1 -> ItemStack.of(Material.DIAMOND_CHESTPLATE);
            case 2 -> ItemStack.of(Material.DIAMOND_LEGGINGS);
            default -> ItemStack.of(Material.DIAMOND_BOOTS);
        };
    }

    private static ItemStack getFood() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.COOKED_BEEF, 3 + RANDOM.nextInt(4));
            case 1 -> ItemStack.of(Material.BREAD, 4 + RANDOM.nextInt(5));
            case 2 -> ItemStack.of(Material.COOKED_PORKCHOP, 3 + RANDOM.nextInt(4));
            default -> ItemStack.of(Material.APPLE, 2 + RANDOM.nextInt(3));
        };
    }

    private static ItemStack getTool() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.STONE_PICKAXE);
            case 1 -> ItemStack.of(Material.STONE_AXE);
            default -> ItemStack.of(Material.FLINT_AND_STEEL);
        };
    }

    private static ItemStack getNormalSpecial() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.SNOWBALL, 8);
            case 1 -> ItemStack.of(Material.FISHING_ROD);
            default -> ItemStack.of(Material.COBWEB, 2);
        };
    }

    private static ItemStack getInsaneSpecial() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.ENDER_PEARL, 2);
            case 1 -> ItemStack.of(Material.TNT, 2);
            case 2 -> ItemStack.of(Material.LAVA_BUCKET);
            default -> ItemStack.of(Material.WATER_BUCKET);
        };
    }

    private static ItemStack getCenterSpecial() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> ItemStack.of(Material.ENDER_PEARL);
            case 1 -> ItemStack.of(Material.GOLDEN_APPLE);
            default -> ItemStack.of(Material.TNT, 2);
        };
    }

    private static ItemStack getInsaneCenterSpecial() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> ItemStack.of(Material.ENDER_PEARL, 3);
            case 1 -> ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE);
            case 2 -> ItemStack.of(Material.TNT, 4);
            default -> ItemStack.of(Material.TOTEM_OF_UNDYING);
        };
    }

    private static ItemStack getEnchantedItem(boolean insane) {
        return insane ? getInsaneCenterWeapon() : getCenterWeapon();
    }
}
