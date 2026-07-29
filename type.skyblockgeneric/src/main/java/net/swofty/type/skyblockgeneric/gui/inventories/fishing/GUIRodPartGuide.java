package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIRodPartGuide extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Rod Part Guide", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);

        layout.slot(4, ItemStackCreator.getStack(
            "§9Rod Part Guide",
            Material.BOOK,
            1,
            "§7View all §9Rod Parts §7that can be applied",
            "§7to your upgraded fishing rods."
        ));
        layout.slot(20, ItemStackCreator.getStackHead(
            "§9ථ Hooks",
            "9809753cbab0380c7a1c18925faf9b51e44caadd1e5748542b0f23835f4ef64e",
            1,
            "§9Hooks §7make you more likely to catch",
            "§7certain things.",
            "",
            "§eClick to view!"
        ), (_, viewCtx) -> viewCtx.push(new GUIHookGuide()));
        layout.slot(22, ItemStackCreator.getStackHead(
            "§9ꨃ Lines",
            "9a850a4f721bc150bb72b067e5074c8251058a6b9111691da315b393467c1aa9",
            1,
            "§9Lines §7grant you stat bonuses",
            "§7everywhere.",
            "",
            "§eClick to view!"
        ), (_, viewCtx) -> viewCtx.push(new GUILineGuide()));
        layout.slot(24, ItemStackCreator.getStackHead(
            "§9࿉ Sinkers",
            "d24892a3142d2e130e5feb88b805b83de905489d2ccd1d031b9d7a2922b96500",
            1,
            "§9Sinkers §7add special fishing effects",
            "§7to your rod.",
            "",
            "§eClick to view!"
        ), (_, viewCtx) -> viewCtx.push(new GUISinkerGuide()));
        layout.slot(48, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Fishing Rod Parts"
        ), (_, viewCtx) -> viewCtx.pop());
    }
}
