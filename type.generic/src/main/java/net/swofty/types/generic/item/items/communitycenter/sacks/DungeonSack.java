package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags.GUISack;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Sack;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DungeonSack implements SkullHead, NotFinishedYet, Sack {

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Item pickups go directly into your",
                "§7sacks.",
                "",
                "§7§7Items: §aAncient Rose§7, §aBigfoot's Lasso§7, §aBonzo Fragment§7,",
                "§7§aDecoy§7, §aDungeon Chest Key§7, §aFel Pearl§7, §aHealing Tissue§7,",
                "§7§aInflatable Jerry§7, §aJolly Pink Rock§7, §aKismet Feather§7,",
                "§7§aL.A.S.R.'s Eye§7, §aLivid Fragment§7, §aMimic Fragment§7, §aScarf",
                "§aFragment§7, §aSpirit Leap§7, §aSuperboom TNT§7, §aThorn Fragment§7,",
                "§7§aTrap§7, §aWither Catalyst",
                "",
                "§7Capacity: §e20,160 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "fb96c585ccd35f073da38d165cb9bb18ff136f1a184eee3f44725354640ebbd4";
    }

    @Override
    public List<ItemTypeLinker> getSackItems() {
        return List.of(
                ItemTypeLinker.ANCIENT_ROSE,
                //ItemTypeLinker.BIGFOOT_LASSO,
                //ItemTypeLinker.BONZO_FRAGMENT,
                //ItemTypeLinker.DECOY,
                //ItemTypeLinker.DUNGEON_CHEST_KEY,
                //ItemTypeLinker.FEL_PEARL,
                //ItemTypeLinker.HEALING_TISSUE,
                //ItemTypeLinker.INFLATABLE_JERRY,
                //ItemTypeLinker.JOLLY_PINK_ROCK,
                ItemTypeLinker.KISMET_FEATHER
                //ItemTypeLinker.LASR_EYE,
                //ItemTypeLinker.LIVID_FRAGMENT,
                //ItemTypeLinker.MIMIC_FRAGMENT,
                //ItemTypeLinker.SCARF_FRAGMENT,
                //ItemTypeLinker.SPIRIT_LEAP,
                //ItemTypeLinker.SUPERBOOM_TNT,
                //ItemTypeLinker.THORN_FRAGMENT,
                //ItemTypeLinker.TRAP,
                //ItemTypeLinker.WITHER_CATALYST
        );
    }

    @Override
    public int getMaximumCapacity() {
        return 20160;
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUISack(ItemTypeLinker.DUNGEON_SACK, true).open(player);
    };
}
