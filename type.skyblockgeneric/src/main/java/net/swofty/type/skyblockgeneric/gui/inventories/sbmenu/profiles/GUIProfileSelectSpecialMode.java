package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.ProfileMode;

public class GUIProfileSelectSpecialMode extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Select a Special Mode", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        layout.slot(12, ItemStackCreator.getStack(
                "§7♲ §7Ironman", Material.IRON_CHESTPLATE, 1,
                "§8Special Mode", "", "§c✖ No Auction House!", "§c✖ No Bazaar!", "§c✖ No /trade!", "",
                "§a✔ Can buy Booster Cookies!", "§a✔ Can buy cosmetics!", "",
                "§bThis mode can be played in co-op!", "§8Co-op members may still trade together.", "",
                "§eClick to start new profile!"),
                (click, c) -> c.player().openView(new GUIProfileCreate(ProfileMode.IRONMAN)));
        layout.slot(31, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Choose a SkyBlock Mode"),
                (click, c) -> c.player().openView(new GUIProfileSelectMode()));
    }
}
