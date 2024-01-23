package net.swofty.types.generic.item.items.backpacks;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.crafting.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JumboBackpackUpgrade implements CustomSkyBlockItem, SkullHead, Interactable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Craft with a §dGreater Backpack §7to upgrade",
                "§7it to §a45 §7slots.",
                " ",
                "§eRight-click to view recipes!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8ae187145ee732e7c00905a9c16ed1e43ba88d5624a6f4af829f10453fcc9165";
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(ItemType.JUMBO_BACKPACK, null).open(player);
    }
}
