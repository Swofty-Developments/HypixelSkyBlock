package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class RevenantCatalyst implements CustomSkyBlockItem, SkullHead, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 8000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "b88cfafa5f03f8aef042a143799e964342df76b7c1eb461f618e398f84a99a63";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Drops rarely from Revenant",
                "§7Horror when you are at least",
                "§7Zombie Slayer LVL 6.",
                "",
                "§5Catalysts upgrade other items!",
                "§eRight-click to view upgrades!"));
    }
}
