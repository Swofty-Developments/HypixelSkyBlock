package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentType;

import java.util.Arrays;
import java.util.List;

public final class GUIExperiments extends StatelessView {
    private static final int[] BORDER_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 51, 52, 53};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Experimentation Table", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        layout.filler(Arrays.stream(BORDER_SLOTS).boxed().toList(),
                ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1));
        Components.close(layout, 49);

        layout.slot(22, ExperimentationGuiSupport.experimentIcon(ExperimentType.SUPERPAIRS),
                (_, viewCtx) -> viewCtx.push(new GUISuperPairs()));
        layout.slot(29, ExperimentationGuiSupport.experimentIcon(ExperimentType.CHRONOMATRON),
                (_, viewCtx) -> viewCtx.push(new GUIChronomatron()));
        layout.slot(33, ExperimentationGuiSupport.experimentIcon(ExperimentType.ULTRASEQUENCER),
                (_, viewCtx) -> viewCtx.push(new GUIUltrasequencer()));

        layout.filler(List.of(20, 21, 23, 24),
                ItemStackCreator.getStack("§7Pending Experiment...", Material.PINK_STAINED_GLASS_PANE, 1));
        layout.slot(50, ItemStackCreator.getStack(
                "§bExperience Bottles",
                Material.EXPERIENCE_BOTTLE,
                1,
                "§7Use experience bottles to restore missing experience.",
                "",
                "§7Experiment rewards grant §bEnchanting XP§7."
        ));
    }
}
