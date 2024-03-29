package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class ScytheBlade implements CustomSkyBlockItem, Enchanted, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.REAPER_SCYTHE;
    }
}
