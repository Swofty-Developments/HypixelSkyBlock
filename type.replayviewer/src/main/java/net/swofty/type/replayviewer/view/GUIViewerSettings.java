package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: implement
public class GUIViewerSettings extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Viewer Settings", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(10, ItemStackCreator.getStack(
            "§aChat Messages",
            Material.LIME_DYE,
            1,
            "§7Toggle chat messages such as kills",
            "§7and other actions in the game.",
            "",
            "§eClick to disable!"
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§cChat Timeline",
            Material.GRAY_DYE,
            1,
            "§7Toggle a timeline of the replay which",
            "§7is displayed via the chat.",
            "",
            "§eClick to enable!"
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§aShow Spectators",
            Material.LIME_DYE,
            1,
            "§7Toggle the ability to see spectators",
            "§7from the replay.",
            "",
            "§eClick to disable!"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§aNight Vision",
            Material.LIME_DYE,
            1,
            "§7Toggle having night vision when",
            "§7watching a replay.",
            "",
            "§eClick to disable!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aShow Particles",
            Material.LIME_DYE,
            1,
            "§7Toggle showing particles when",
            "§7watching a replay.",
            "",
            "§eClick to disable!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§cAdvancing Time",
            Material.GRAY_DYE,
            1,
            "§7Toggle the time on the scoreboard",
            "§7advancing while the replay is being",
            "§7played.",
            "",
            "§eClick to enable!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§aFly Speed",
            Material.PAPER,
            1,
            "§7Change the speed you fly at in the",
            "§7replay.",
            "",
            "§aCurrently Selected: §61x",
            "",
            "§7Click to set Fly Speed to §62x.",
            "",
            "§eClick to cycle!"
        ));
        layout.slot(17, ItemStackCreator.getStack(
            "§aSkip Intervals",
            Material.PAPER,
            1,
            "§7Change the interval used when using",
            "§7the forward & backward buttons. Can",
            "§7also be toggled by left clicking the",
            "§7buttons themselves.",
            "",
            "§aCurrently Selected: §630s",
            "",
            "§7Click to set Skip Intervals to §660s.",
            "",
            "§eClick to cycle!"
        ));
        layout.slot(31, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Replay Viewer"
        ));
    }
}
