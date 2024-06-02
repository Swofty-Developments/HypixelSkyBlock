package net.swofty.types.generic.item.items.miscellaneous;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public class SkyBlockMenu implements CustomSkyBlockItem, Interactable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getAbsoluteLore(SkyBlockPlayer player, SkyBlockItem item) {
        return Arrays.asList(
                "§7View all of your SkyBlock progress,",
                "§7including your Skills, Collections,",
                "§7Recipes, and more!",
                "§e ",
                "§eClick to open!"
        );
    }

    @Override
    public String getAbsoluteName(SkyBlockPlayer player, SkyBlockItem item) {
        return "§aSkyBlock Menu §7(Click)";
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        Thread.startVirtualThread(() -> {
            new GUISkyBlockMenu().open(player);
        });
    }

    @Override
    public void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
        Thread.startVirtualThread(() -> {
            new GUISkyBlockMenu().open(player);
        });
    }

    @Override
    public boolean onInventoryInteract(SkyBlockPlayer player, SkyBlockItem item) {
        if (!player.getInventory().getCursorItem().isAir()) return true;
        if (player.getOpenInventory() != null && !player.getOpenInventory().getCursorItem(player).isAir())
            return true;

        Thread.startVirtualThread(() -> {
            new GUISkyBlockMenu().open(player);
        });
        return true;
    }
}
