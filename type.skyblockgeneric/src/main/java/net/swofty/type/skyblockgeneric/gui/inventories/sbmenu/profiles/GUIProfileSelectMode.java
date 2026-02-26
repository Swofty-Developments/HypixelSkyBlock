package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIProfileSelectMode extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Choose a SkyBlock Mode", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        // Classic Profile
        layout.slot(11, (s, c) -> ItemStackCreator.getStack("§aClassic Profile", Material.GRASS_BLOCK, 1,
                        "§8SkyBlock Mode",
                        "",
                        "§7A SkyBlock adventure with the",
                        "§7default rules.",
                        "",
                        "§7Start on a new tiny island,",
                        "§7without gear and build your",
                        "§7way up to become the",
                        "§agreatest player§7 in the",
                        "§7universe!",
                        "",
                        "§eClick to play this mode!"),
                (click, c) -> c.player().openView(new GUIProfileCreate()));

        // Special Modes
        layout.slot(15, (s, c) -> ItemStackCreator.getStack("§6Special Modes", Material.BLAZE_POWDER, 1,
                        "§7Choose a SkyBlock mode with",
                        "§7special rules and unique",
                        "§7mechanics.",
                        "",
                        "§eClick to choose a mode!"),
                (click, c) -> c.player().sendMessage("§cSpecial Modes in SkyBlock are currently unavailable. Please check back another time."));
    }
}
