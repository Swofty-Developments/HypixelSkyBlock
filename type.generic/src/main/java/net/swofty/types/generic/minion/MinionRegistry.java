package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.items.minion.foraging.FlowerMinion;
import net.swofty.types.generic.minion.minions.combat.*;
import net.swofty.types.generic.minion.minions.farming.*;
import net.swofty.types.generic.minion.minions.fishing.MinionClay;
import net.swofty.types.generic.minion.minions.fishing.MinionFishing;
import net.swofty.types.generic.minion.minions.foraging.*;
import net.swofty.types.generic.minion.minions.mining.*;
import net.swofty.commons.StringUtility;

public enum MinionRegistry {
    COBBLESTONE(MinionCobblestone.class, ItemTypeLinker.COBBLESTONE_MINION),
    COAL(MinionCoal.class, ItemTypeLinker.COAL_MINION),
    SNOW(MinionSnow.class, ItemTypeLinker.SNOW_MINION),
    DIAMOND(MinionDiamond.class, ItemTypeLinker.DIAMOND_MINION),
    GOLD(MinionGold.class, ItemTypeLinker.GOLD_MINION),
    LAPIS(MinionLapis.class, ItemTypeLinker.LAPIS_MINION),
    IRON(MinionIron.class, ItemTypeLinker.IRON_MINION),
    ENDSTONE(MinionEndstone.class, ItemTypeLinker.ENDSTONE_MINION),
    REDSTONE(MinionRedstone.class, ItemTypeLinker.REDSTONE_MINION),
    EMERALD(MinionEmerald.class, ItemTypeLinker.EMERALD_MINION),
    ICE(MinionIce.class, ItemTypeLinker.ICE_MINION),
    QUARTZ(MinionQuartz.class, ItemTypeLinker.QUARTZ_MINION),
    OBSIDIAN(MinionObsidian.class, ItemTypeLinker.OBSIDIAN_MINION),
    GRAVEL(MinionGravel.class, ItemTypeLinker.GRAVEL_MINION),
    SAND(MinionSand.class, ItemTypeLinker.SAND_MINION),
    GLOWSTONE(MinionGlowstone.class, ItemTypeLinker.GLOWSTONE_MINION),
    HARD_STONE(MinionHardStone.class, ItemTypeLinker.HARD_STONE_MINION),
    MITHRIL(MinionMithril.class, ItemTypeLinker.MITHRIL_MINION),

    ACACIA(MinionAcacia.class, ItemTypeLinker.ACACIA_MINION),
    BIRCH(MinionBirch.class, ItemTypeLinker.BIRCH_MINION),
    DARK_OAK(MinionDarkOak.class, ItemTypeLinker.DARK_OAK_MINION),
    JUNGLE(MinionJungle.class, ItemTypeLinker.JUNGLE_MINION),
    OAK(MinionOak.class, ItemTypeLinker.OAK_MINION),
    SPRUCE(MinionSpruce.class, ItemTypeLinker.SPRUCE_MINION),
    FLOWER(MinionFlower.class, ItemTypeLinker.FLOWER_MINION),

    FISHING(MinionFishing.class, ItemTypeLinker.FISHING_MINION),
    CLAY(MinionClay.class, ItemTypeLinker.CLAY_MINION),

    WHEAT(MinionWheat.class, ItemTypeLinker.WHEAT_MINION),
    POTATO(MinionPotato.class, ItemTypeLinker.POTATO_MINION),
    CACTUS(MinionCactus.class, ItemTypeLinker.CACTUS_MINION),
    CARROT(MinionCarrot.class, ItemTypeLinker.CARROT_MINION),
    COCOA_BEANS(MinionCocoaBeans.class, ItemTypeLinker.COCOA_BEANS_MINION),
    MELON(MinionMelon.class, ItemTypeLinker.MELON_MINION),
    MUSHROOM(MinionMushroom.class, ItemTypeLinker.MUSHROOM_MINION),
    NETHER_WART(MinionNetherWart.class, ItemTypeLinker.NETHER_WART_MINION),
    PUMPKIN(MinionPumpkin.class, ItemTypeLinker.PUMPKIN_MINION),
    SUGAR_CANE(MinionSugarCane.class, ItemTypeLinker.SUGAR_CANE_MINION),
    CHICKEN(MinionChicken.class, ItemTypeLinker.CHICKEN_MINION),
    COW(MinionCow.class, ItemTypeLinker.COW_MINION),
    PIG(MinionPig.class, ItemTypeLinker.PIG_MINION),
    RABBIT(MinionRabbit.class, ItemTypeLinker.RABBIT_MINION),
    SHEEP(MinionSheep.class, ItemTypeLinker.SHEEP_MINION),

    BLAZE(MinionBlaze.class, ItemTypeLinker.BLAZE_MINION),
    CAVE_SPIDER(MinionCaveSpider.class, ItemTypeLinker.CAVE_SPIDER_MINION),
    CREEPER(MinionCreeper.class, ItemTypeLinker.CREEPER_MINION),
    ENDERMAN(MinionEnderman.class, ItemTypeLinker.ENDERMAN_MINION),
    GHAST(MinionGhast.class, ItemTypeLinker.GHAST_MINION),
    MAGMA_CUBE(MinionMagmaCube.class, ItemTypeLinker.MAGMA_CUBE_MINION),
    SKELETON(MinionSkeleton.class, ItemTypeLinker.SKELETON_MINION),
    SLIME(MinionSlime.class, ItemTypeLinker.SLIME_MINION),
    SPIDER(MinionSpider.class, ItemTypeLinker.SPIDER_MINION),
    ZOMBIE(MinionZombie.class, ItemTypeLinker.ZOMBIE_MINION),
    ;

    private final Class<? extends SkyBlockMinion> minionClass;
    @Getter
    private final ItemTypeLinker itemTypeLinker;

    MinionRegistry(Class<? extends SkyBlockMinion> minionClass, ItemTypeLinker itemTypeLinker) {
        this.minionClass = minionClass;
        this.itemTypeLinker = itemTypeLinker;
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
