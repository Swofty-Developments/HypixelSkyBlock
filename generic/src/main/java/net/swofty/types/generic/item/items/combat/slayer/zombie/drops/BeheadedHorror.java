package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.crafting.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class BeheadedHorror implements CustomSkyBlockItem, SkullHead, Sellable, Unstackable, Interactable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 15000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "dbad99ed3c820b7978190ad08a934a68dfa90d9986825da1c97f6f21f49ad626";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§eRight-click to view recipes!"));
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(ItemType.REAPER_MASK, null).open(player);
    }
}