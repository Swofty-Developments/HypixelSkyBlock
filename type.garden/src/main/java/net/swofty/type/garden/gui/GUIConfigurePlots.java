package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.garden.config.GardenPlotDefinition;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.garden.GardenData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUIConfigurePlots extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Configure Plots", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 31, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        GardenData.GardenCoreData core = GardenGuiSupport.core(player);
        List<GardenPlotDefinition> plots = GardenGuiSupport.lockedPlotsFirst(player);
        long unlockedCount = plots.stream()
            .filter(plot -> plot.defaultUnlocked() || core.getUnlockedPlots().contains(plot.id()))
            .count();
        GardenPlotDefinition nextPlot = plots.stream()
            .filter(plot -> !plot.defaultUnlocked() && !core.getUnlockedPlots().contains(plot.id()))
            .findFirst()
            .orElse(null);

        layout.slot(11, ItemStackCreator.getStack(
            "§aPlot Unlocks",
            Material.GRASS_BLOCK,
            1,
            "§7Unlocked plots: §e" + unlockedCount + "§7/§a" + plots.size(),
            "§7Plot bonus: §6+" + (unlockedCount * 3) + "☘ Farming Fortune",
            "§7SkyBlock XP: §b" + (unlockedCount * 5),
            "",
            nextPlot == null ? "§aAll available plots are unlocked!" : "§7Next plot: §e" + nextPlot.displayName(),
            nextPlot == null ? "" : "§7Requires Garden Level §a" + StringUtility.getAsRomanNumeral(nextPlot.gardenLevel())
        ));

        layout.slot(15, ItemStackCreator.getStack(
            "§aCleaning Progress",
            Material.OAK_LEAVES,
            1,
            "§7Leaf decay begins after a plot reaches",
            "§e70% §7cleaning progress.",
            "",
            "§7Tracked plots: §e" + core.getCleanedPlots().size(),
            "§7Saved presets: §e" + core.getPlotPresets().size()
        ));
    }
}
