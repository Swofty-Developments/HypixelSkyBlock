package net.swofty.types.generic.item.items.combat.slayer.enderman.drops;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.crafting.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class JudgementCore implements CustomSkyBlockItem, SkullHead, Unstackable, Interactable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2f3ddd7f81089c85b26ed597675519f03a1dcd6d1713e0cfc66afb8743cbe0";
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(ItemType.TERMINATOR, null).open(player);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§eRight-click to view recipes!"));
    }
}
