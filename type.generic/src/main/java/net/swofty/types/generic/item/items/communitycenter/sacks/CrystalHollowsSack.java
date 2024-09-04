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

public class CrystalHollowsSack implements SkullHead, NotFinishedYet, Sack {

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Item pickups go directly into your",
                "§7sacks.",
                "",
                "§7§7Items: §aAscension Rope§7, §aBob-omb§7, §aControl Switch§7,",
                "§7§aCorleonite§7, §aElectron Transmitter§7, §aFTX 3070§7, §aJungle Key§7,",
                "§7§aOil Barrel§7, §aRobotron Reflector§7, §aSludge Juice§7, §aSuperlite",
                "§aMotor§7, §aSynthetic Heart§7, §aWishing Compass§7, §aYoggie",
                "",
                "§7Capacity: §e20,160 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "a6b46beeb5f6e0006163eda4a50703a40e6591080b0e67779312adcfec46152";
    }

    @Override
    public List<ItemTypeLinker> getSackItems() {
        return List.of(
                //ItemTypeLinker.ASCENSION_ROPE,
                ItemTypeLinker.BOB_OMB,
                ItemTypeLinker.CONTROL_SWITCH,
                //ItemTypeLinker.CORLEONITE,
                ItemTypeLinker.ELECTRON_TRANSMITTER,
                ItemTypeLinker.FTX_3070,
                ItemTypeLinker.JUNGLE_KEY,
                ItemTypeLinker.OIL_BARREL,
                ItemTypeLinker.ROBOTRON_REFLECTOR,
                ItemTypeLinker.SLUDGE_JUICE,
                ItemTypeLinker.SUPERLITE_MOTOR,
                ItemTypeLinker.SYNTHETIC_HEART,
                ItemTypeLinker.WISHING_COMPASS
                //ItemTypeLinker.YOGGIE
        );
    }

    @Override
    public int getMaximumCapacity() {
        return 20160;
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUISack(ItemTypeLinker.CRYSTAL_HOLLOWS_SACK, true).open(player);
    };
}
