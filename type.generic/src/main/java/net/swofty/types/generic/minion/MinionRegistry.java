package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.minion.minions.farming.MinionWheat;
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
    ICE(MinionGold.class, ItemTypeLinker.ICE_MINION),
    QUARTZ(MinionGold.class, ItemTypeLinker.QUARTZ_MINION),
    OBSIDIAN(MinionGold.class, ItemTypeLinker.OBSIDIAN_MINION),
    GRAVEL(MinionGold.class, ItemTypeLinker.GRAVEL_MINION),
    SAND(MinionGold.class, ItemTypeLinker.SAND_MINION),
    ACACIA(MinionAcacia.class, ItemTypeLinker.ACACIA_MINION),
    BIRCH(MinionBirch.class, ItemTypeLinker.BIRCH_MINION),
    DARK_OAK(MinionDarkOak.class, ItemTypeLinker.DARK_OAK_MINION),
    JUNGLE(MinionJungle.class, ItemTypeLinker.JUNGLE_MINION),
    OAK(MinionOak.class, ItemTypeLinker.OAK_MINION),
    SPRUCE(MinionSpruce.class, ItemTypeLinker.SPRUCE_MINION),
    WHEAT(MinionWheat.class, ItemTypeLinker.WHEAT_MINION)
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
