package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Set;

public class GUINavigator extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Navigator", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        ServerType currentServer = HypixelConst.getTypeLoader().getType();
        boolean inBayou = currentServer == ServerType.SKYBLOCK_BACKWATER_BAYOU;
        boolean unlocked = inBayou || player.getShipState().hasDestination("BACKWATER_BAYOU");

        layout.slots(Layouts.row(0));
        layout.slots(Layouts.row(5));
        layout.slots(Layouts.rectangle(9, 45), (_, _) -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                .set(DataComponents.CUSTOM_NAME, Component.space())
                .set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of())));
        Components.close(layout, 49);

        if (inBayou) {
            layout.slot(10, ItemStackCreator.getStackHead(
                "§bFishing Outpost",
                "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8",
                1,
                "§7Your base of operations.",
                "",
                "§eClick to travel!"
            ), (_, viewCtx) -> ((SkyBlockPlayer) viewCtx.player()).sendTo(ServerType.SKYBLOCK_HUB));
            layout.slot(40, ItemStackCreator.getStackHead(
                "§2Backwater Bayou",
                "1c0cd33590f64d346d98cdd01606938742e715dda6737353306a44f81c8ba426",
                1,
                "§7A small, marshy outlet in the middle",
                "§7of nowhere. Due to its isolated",
                "§7nature, people frequently come here",
                "§7to dump their trash.",
                "",
                "§a§lYOU ARE HERE!"
            ));
            return;
        }

        layout.slot(10, ItemStackCreator.getStackHead(
            "§2Backwater Bayou",
            "1c0cd33590f64d346d98cdd01606938742e715dda6737353306a44f81c8ba426",
            1,
            "§7A small, marshy outlet in the middle",
            "§7of nowhere. Due to its isolated",
            "§7nature, people frequently come here",
            "§7to dump their trash.",
            "",
            "§7Activities:",
            "§8 ■ §7Fish up §2Junk §7and trade it with §2Junker",
            "    §2Joel §7for useful items!",
            "§8 ■ §7Apply §9Rod Parts §7with §2Roddy§7.",
            "§8 ■ §7Fish §2Bayou Sea Creatures§7.",
            "§8 ■ §7Learn about §dFishing Hotspots §7from",
            "    §dHattie§7.",
            "",
            unlocked ? "§eClick to travel!" : "§cDestination Locked"
        ), unlocked
            ? (_, viewCtx) -> ((SkyBlockPlayer) viewCtx.player()).sendTo(ServerType.SKYBLOCK_BACKWATER_BAYOU)
            : (_, viewCtx) -> viewCtx.player().sendMessage("§cYou have not unlocked this destination yet."));
        layout.slot(40, ItemStackCreator.getStackHead(
            "§bFishing Outpost",
            "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8",
            1,
            "§7Your base of operations.",
            "",
            "§a§lYOU ARE HERE!"
        ));
    }
}
