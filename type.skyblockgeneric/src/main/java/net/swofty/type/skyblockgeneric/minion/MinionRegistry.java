package net.swofty.type.skyblockgeneric.minion;

import lombok.Getter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.minion.minions.combat.*;
import net.swofty.type.skyblockgeneric.minion.minions.farming.*;
import net.swofty.type.skyblockgeneric.minion.minions.fishing.MinionClay;
import net.swofty.type.skyblockgeneric.minion.minions.fishing.MinionFishing;
import net.swofty.type.skyblockgeneric.minion.minions.foraging.*;
import net.swofty.type.skyblockgeneric.minion.minions.mining.*;

import java.util.Arrays;

public enum MinionRegistry {
    COBBLESTONE(MinionCobblestone.class, ItemType.COBBLESTONE_MINION),
    COAL(MinionCoal.class, ItemType.COAL_MINION),
    SNOW(MinionSnow.class, ItemType.SNOW_MINION),
    DIAMOND(MinionDiamond.class, ItemType.DIAMOND_MINION),
    GOLD(MinionGold.class, ItemType.GOLD_MINION),
    LAPIS(MinionLapis.class, ItemType.LAPIS_MINION),
    IRON(MinionIron.class, ItemType.IRON_MINION),
    ENDSTONE(MinionEndstone.class, ItemType.END_STONE_MINION),
    REDSTONE(MinionRedstone.class, ItemType.REDSTONE_MINION),
    EMERALD(MinionEmerald.class, ItemType.EMERALD_MINION),
    ICE(MinionIce.class, ItemType.ICE_MINION),
    QUARTZ(MinionQuartz.class, ItemType.QUARTZ_MINION),
    OBSIDIAN(MinionObsidian.class, ItemType.OBSIDIAN_MINION),
    GRAVEL(MinionGravel.class, ItemType.GRAVEL_MINION),
    SAND(MinionSand.class, ItemType.SAND_MINION),
    GLOWSTONE(MinionGlowstone.class, ItemType.GLOWSTONE_MINION),
    HARD_STONE(MinionHardStone.class, ItemType.HARD_STONE_MINION),
    MITHRIL(MinionMithril.class, ItemType.MITHRIL_MINION),

    ACACIA(MinionAcacia.class, ItemType.ACACIA_MINION),
    BIRCH(MinionBirch.class, ItemType.BIRCH_MINION),
    DARK_OAK(MinionDarkOak.class, ItemType.DARK_OAK_MINION),
    JUNGLE(MinionJungle.class, ItemType.JUNGLE_MINION),
    OAK(MinionOak.class, ItemType.OAK_MINION),
    SPRUCE(MinionSpruce.class, ItemType.SPRUCE_MINION),
    FLOWER(MinionFlower.class, ItemType.FLOWER_MINION),

    FISHING(MinionFishing.class, ItemType.FISHING_MINION),
    CLAY(MinionClay.class, ItemType.CLAY_MINION),

    WHEAT(MinionWheat.class, ItemType.WHEAT_MINION),
    POTATO(MinionPotato.class, ItemType.POTATO_MINION),
    CACTUS(MinionCactus.class, ItemType.CACTUS_MINION),
    CARROT(MinionCarrot.class, ItemType.CARROT_MINION),
    COCOA_BEANS(MinionCocoaBeans.class, ItemType.COCOA_BEANS_MINION),
    MELON(MinionMelon.class, ItemType.MELON_MINION),
    MUSHROOM(MinionMushroom.class, ItemType.MUSHROOM_MINION),
    NETHER_WART(MinionNetherWart.class, ItemType.NETHER_WART_MINION),
    PUMPKIN(MinionPumpkin.class, ItemType.PUMPKIN_MINION),
    SUGAR_CANE(MinionSugarCane.class, ItemType.SUGAR_CANE_MINION),
    CHICKEN(MinionChicken.class, ItemType.CHICKEN_MINION),
    COW(MinionCow.class, ItemType.COW_MINION),
    PIG(MinionPig.class, ItemType.PIG_MINION),
    RABBIT(MinionRabbit.class, ItemType.RABBIT_MINION),
    SHEEP(MinionSheep.class, ItemType.SHEEP_MINION),

    BLAZE(MinionBlaze.class, ItemType.BLAZE_MINION),
    CAVE_SPIDER(MinionCaveSpider.class, ItemType.CAVE_SPIDER_MINION),
    CREEPER(MinionCreeper.class, ItemType.CREEPER_MINION),
    ENDERMAN(MinionEnderman.class, ItemType.ENDERMAN_MINION),
    GHAST(MinionGhast.class, ItemType.GHAST_MINION),
    MAGMA_CUBE(MinionMagmaCube.class, ItemType.MAGMA_CUBE_MINION),
    SKELETON(MinionSkeleton.class, ItemType.SKELETON_MINION),
    SLIME(MinionSlime.class, ItemType.SLIME_MINION),
    SPIDER(MinionSpider.class, ItemType.SPIDER_MINION),
    ZOMBIE(MinionZombie.class, ItemType.ZOMBIE_MINION),
    ;

    private final Class<? extends SkyBlockMinion> minionClass;
    @Getter
    private final ItemType itemType;

    MinionRegistry(Class<? extends SkyBlockMinion> minionClass, ItemType ItemType) {
        this.minionClass = minionClass;
        this.itemType = ItemType;
    }
    public static MinionRegistry fromItemType(ItemType itemType) {
        return Arrays.stream(values()).filter(minionType -> minionType.itemType == itemType).findFirst().orElse(null);
    }

    public SkyBlockMinion asSkyBlockMinion() {
        try {
            return minionClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create minion instance", e);
        }
    }

    public String getDisplay() {
        return StringUtility.toNormalCase(name()) + " Minion";
    }
}
