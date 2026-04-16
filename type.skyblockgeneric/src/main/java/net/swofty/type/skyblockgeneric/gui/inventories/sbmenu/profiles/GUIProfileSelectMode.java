package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;

public class GUIProfileSelectMode extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.mode.title", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        layout.slot(11, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.profiles.mode.classic", Material.GRASS_BLOCK, 1,
                        "gui_sbmenu.profiles.mode.classic.lore"),
                (click, c) -> c.player().openView(new GUIProfileCreate()));

        layout.slot(15, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.profiles.mode.special", Material.BLAZE_POWDER, 1,
                        "gui_sbmenu.profiles.mode.special.lore"),
                (click, c) -> c.player().sendMessage(I18n.string("gui_sbmenu.profiles.mode.msg.unavailable", c.player().getLocale())));
    }
}
