package net.swofty.item.items.miscellaneous;

import net.swofty.gui.inventory.inventories.GUISkyBlockMenu;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.Interactable;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public class SkyBlockMenu implements CustomSkyBlockItem, Interactable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
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
        new GUISkyBlockMenu().open(player);
    }

    @Override
    public void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUISkyBlockMenu().open(player);
    }

    @Override
    public boolean onInventoryInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUISkyBlockMenu().open(player);
        return true;
    }
}
