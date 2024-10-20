package net.swofty.types.generic.item.items.sacks;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags.GUISack;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Sack;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmallMiningSack implements SkullHead, NotFinishedYet, Sack {

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Item pickups go directly into your",
                "§7sacks.",
                "",
                "§7Items: §aCoal§7, §aCobblestone§7, §aDiamond§7, §aEmerald§7, §aEnd Stone§7,",
                "§aFlint§7, §aGlacite§7, §aGlowstone Dust§7, §aGold Ingot§7, §aGravel§7, §aHard",
                "§aStone§7, §aIron Ingot§7, §aLapis Lazuli§7, §aMithril§7, §aMycelium§7, §aNether",
                "§aQuartz§7, §aNetherrack§7, §aObsidian§7, §aOil Barrel§7, §aPlasma§7, §aRed",
                "§aSand§7, §aRedstone§7, §aRefined Mineral§7, §aSand§7, §aStarfall§7, §aStone§7,",
                "§7§aTitanium§7, §aTreasurite§7, §aTungsten§7, §aUmber§7, §aVolta",
                "",
                "§7Capacity: §e2,240 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f07dff657d61f302c7d2e725265d17b64aa73642391964fb48fc15be950831d8";
    }

    @Override
    public List<ItemTypeLinker> getSackItems() {
        return List.of(
                ItemTypeLinker.COAL,
                ItemTypeLinker.COBBLESTONE,
                ItemTypeLinker.DIAMOND,
                ItemTypeLinker.EMERALD,
                ItemTypeLinker.END_STONE,
                ItemTypeLinker.FLINT,
                //ItemTypeLinker.GLACITE,
                ItemTypeLinker.GLOWSTONE_DUST,
                ItemTypeLinker.GOLD_INGOT,
                ItemTypeLinker.GRAVEL,
                ItemTypeLinker.HARD_STONE,
                ItemTypeLinker.IRON_INGOT,
                ItemTypeLinker.LAPIS_LAZULI,
                ItemTypeLinker.MITHRIL,
                ItemTypeLinker.MYCELIUM,
                ItemTypeLinker.QUARTZ,
                ItemTypeLinker.NETHERRACK,
                ItemTypeLinker.OBSIDIAN,
                ItemTypeLinker.OIL_BARREL,
                ItemTypeLinker.PLASMA,
                ItemTypeLinker.RED_SAND,
                ItemTypeLinker.REDSTONE,
                //ItemTypeLinker.REFINED_MINERAL,
                ItemTypeLinker.SAND,
                ItemTypeLinker.STARFALL,
                ItemTypeLinker.STONE,
                ItemTypeLinker.TITANIUM,
                ItemTypeLinker.TREASURITE,
                //ItemTypeLinker.TUNGSTEN,
                //ItemTypeLinker.UMBER,
                ItemTypeLinker.VOLTA
        );
    }

    @Override
    public int getMaximumCapacity() {
        return 2240;
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUISack(ItemTypeLinker.SMALL_MINING_SACK, true).open(player);
    };
}
