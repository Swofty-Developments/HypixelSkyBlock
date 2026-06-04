package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIHotelGuide extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Hotel Guide", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 31, ctx);

        layout.slot(10, ItemStackCreator.getStack(
            "§3The Slumber Hotel",
            Material.SAND,
            1,
            "",
            "§7The §3Slumber Hotel §7exists in a unique",
            "§7dimension between the waking world",
            "§7and the dream world.",
            "",
            "§7Residents at the hotel come from all",
            "§7sorts of §buniverses §7and §dtimelines§7. No",
            "§7one physically exists in the hotel, it",
            "§7is merely where their dreams go."
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§eSlumber Tickets",
            Material.SAND,
            2,
            "",
            "§7Earned by playing Bed Wars games,",
            "§eSlumber Tickets §7will help you unlock",
            "§7new rooms in the hotel.",
            "",
            "§7You have a §ePlatinum Membership",
            "§eWallet§7, so you can hold up to §e100,000",
            "§eSlumber Tickets§7!",
            "",
            "§bKill §7§l⮕ §e1 Slumber Ticket",
            "§bFinal Kill §7§l⮕ §e5 Slumber Tickets",
            "§bBed Destroyed §7§l⮕ §e10 Slumber Tickets",
            "§bWin §7§l⮕ §e20 Slumber Tickets",
            "§bTime Played §7§l⮕ §e5 Slumber Tickets"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aQuests & Objectives",
            Material.SAND,
            3,
            "",
            "§7Helping enough residents will add to",
            "§7the reputation of the hotel, bringing",
            "§7in even more residents!",
            "",
            "§7Residents can reward §eTickets §7and",
            "§7even unique §dcosmetics §7for purchase!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§6The Hotel Owner",
            Material.SAND,
            4,
            "",
            "§7Most will never meet the §6Hotel Owner§7,",
            "§7but if you help enough residents he",
            "§7might start to take notice of you."
        ));
    }
}
