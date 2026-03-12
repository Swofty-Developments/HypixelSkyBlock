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

public class GUIVisitorMilestones extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Visitor Milestones", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 49, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        int stagesCompleted = GardenGuiSupport.core(player).getVisitorMilestones().values().stream()
            .mapToInt(Integer::intValue)
            .sum();

        layout.slot(13, ItemStackCreator.getStackHead(
            "§aVisitor Milestones",
            "8d34f38c1bb106e11908ad3cc90162c18b863d678265c84a84a358903f8f7a1c",
            1,
            "§7Unique visitors served: §e" + GardenGuiSupport.core(player).getServedUniqueVisitors().size(),
            "§7Tracked milestone lines: §e" + GardenGuiSupport.core(player).getVisitorMilestones().size(),
            "§7Total milestone stages: §e" + StringUtility.commaify(stagesCompleted),
            "",
            "§7The milestone counters are live and",
            "§7persisted through Garden profile data."
        ));

        layout.slot(15, ItemStackCreator.getStack(
            "§aVisitor's Logbook",
            Material.BOOK,
            1,
            "§7Browse through every visitor and",
            "§7review who can currently visit",
            "§7your Garden.",
            "",
            "§eClick to browse!"
        ), (click, c) -> c.push(new GUIVisitorLogbook()));
    }
}
