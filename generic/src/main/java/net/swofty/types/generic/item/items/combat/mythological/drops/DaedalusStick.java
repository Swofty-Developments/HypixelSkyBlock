package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.crafting.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class DaedalusStick implements CustomSkyBlockItem, Enchanted, PetItem, Sellable, Interactable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Drops rare off of Minotaurs from",
                "§7Diana's Mythological Ritual.",
                "",
                "§eRight-click to view recipes!"));
    }

    @Override
    public double getSellValue() {
        return 250000;
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(ItemType.DAEDALUS_AXE, null).open(player);
    }
}
