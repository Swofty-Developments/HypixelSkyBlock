package net.swofty.types.generic.item.items.communitycenter.upgradecomponents;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
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
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
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
        return "b6acfc643f608e16de19335ddca71a828dfb8daca5793eb5bbcb0c7d1559249";
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(ItemType.JUMBO_BACKPACK, null).open(player);
    }
}
