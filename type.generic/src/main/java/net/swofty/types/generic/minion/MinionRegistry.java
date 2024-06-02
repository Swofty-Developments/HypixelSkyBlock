package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.minion.minions.farming.MinionWheat;
import net.swofty.types.generic.minion.minions.foraging.*;
import net.swofty.types.generic.minion.minions.mining.*;
import net.swofty.types.generic.utility.StringUtility;

public enum MinionRegistry {
    COBBLESTONE(MinionCobblestone.class, ItemType.COBBLESTONE_MINION),
    COAL(MinionCoal.class, ItemType.COAL_MINION),
    SNOW(MinionSnow.class, ItemType.SNOW_MINION),
    DIAMOND(MinionDiamond.class, ItemType.DIAMOND_MINION),
    GOLD(MinionGold.class, ItemType.GOLD_MINION),
    LAPIS(MinionLapis.class, ItemType.LAPIS_MINION),
    IRON(MinionIron.class, ItemType.IRON_MINION),
    ENDSTONE(MinionEndstone.class, ItemType.ENDSTONE_MINION),
    REDSTONE(MinionRedstone.class, ItemType.REDSTONE_MINION),
    EMERALD(MinionEmerald.class, ItemType.EMERALD_MINION),
    ICE(MinionGold.class, ItemType.ICE_MINION),
    QUARTZ(MinionGold.class, ItemType.QUARTZ_MINION),
    OBSIDIAN(MinionGold.class, ItemType.OBSIDIAN_MINION),
    GRAVEL(MinionGold.class, ItemType.GRAVEL_MINION),
    SAND(MinionGold.class, ItemType.SAND_MINION),
    ACACIA(MinionAcacia.class, ItemType.ACACIA_MINION),
    BIRCH(MinionBirch.class, ItemType.BIRCH_MINION),
    DARK_OAK(MinionDarkOak.class, ItemType.DARK_OAK_MINION),
    JUNGLE(MinionJungle.class, ItemType.JUNGLE_MINION),
    OAK(MinionOak.class, ItemType.OAK_MINION),
    SPRUCE(MinionSpruce.class, ItemType.SPRUCE_MINION),
    WHEAT(MinionWheat.class, ItemType.WHEAT_MINION)
    ;

    private final Class<? extends SkyBlockMinion> minionClass;
    @Getter
    private final ItemType itemType;

    MinionRegistry(Class<? extends SkyBlockMinion> minionClass, ItemType itemType) {
        this.minionClass = minionClass;
        this.itemType = itemType;
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
