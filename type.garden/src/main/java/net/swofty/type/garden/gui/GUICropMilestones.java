package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUICropMilestones extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Crop Milestones", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 31, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        int stagesCompleted = GardenGuiSupport.core(player).getCropMilestones().values().stream()
            .mapToInt(Integer::intValue)
            .sum();

        layout.slot(13, ItemStackCreator.getStack(
            "§aCrop Milestones",
            Material.WHEAT,
            1,
            "§7Crops with milestone progress: §e" + GardenGuiSupport.core(player).getCropMilestones().size(),
            "§7Total milestone stages: §e" + StringUtility.commaify(stagesCompleted),
            "",
            "§7The milestone progression data is fully",
            "§7saved, but the detailed reward table UI",
            "§7still needs to be moved off the static",
            "§7copy."
        ));
    }
}
