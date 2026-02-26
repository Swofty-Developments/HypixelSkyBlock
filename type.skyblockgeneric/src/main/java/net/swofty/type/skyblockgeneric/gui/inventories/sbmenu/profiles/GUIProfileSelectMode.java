package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIProfileSelectMode extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(I18n.string("gui_sbmenu.profiles.mode.title"), InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        layout.slot(11, (s, c) -> ItemStackCreator.getStack(I18n.string("gui_sbmenu.profiles.mode.classic"), Material.GRASS_BLOCK, 1,
                        I18n.lore("gui_sbmenu.profiles.mode.classic.lore")),
                (click, c) -> c.player().openView(new GUIProfileCreate()));

        layout.slot(15, (s, c) -> ItemStackCreator.getStack(I18n.string("gui_sbmenu.profiles.mode.special"), Material.BLAZE_POWDER, 1,
                        I18n.lore("gui_sbmenu.profiles.mode.special.lore")),
                (click, c) -> c.player().sendMessage(I18n.string("gui_sbmenu.profiles.mode.msg.unavailable")));
    }
}
