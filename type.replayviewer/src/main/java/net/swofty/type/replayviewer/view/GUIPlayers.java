package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: implement
public class GUIPlayers extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Players", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(10, ItemStackCreator.getStackHead(
            "§b[MVP§9+§b] ArikSquad",
            "859269972cf257cd23db45a61bbd1f7d647b7966d14bd6ba07ef1d2630b23be2",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(11, ItemStackCreator.getStackHead(
            "§7aspera6552",
            "1d53376cbd3b3689e3a7b08fd458afbac6453c670334c5db21ef7efc95438c9d",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(12, ItemStackCreator.getStackHead(
            "§7Barbapapa2011",
            "731e97aa0570184fdf61a8e520d22094d1057e90e47efd45c8f3b09a1fdb7783",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(13, ItemStackCreator.getStackHead(
            "§a[VIP] Clayterzz",
            "e5cdc3243b2153ab28a159861be643a4fc1e3c17d291cdd3e57a7f370ad676f3",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(14, ItemStackCreator.getStackHead(
            "§a[VIP] Netanel_Yair",
            "7ee7ee4724242643b70ef39e50349a497b8f0dc594bd2d0158df7aac36d5bee7",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(15, ItemStackCreator.getStackHead(
            "§7RenzeHo",
            "588a477aa4772223e6b57b835b15bdd2d745beeb8265f76a695b7fb27a813f7d",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(16, ItemStackCreator.getStackHead(
            "§a[VIP] robin_631",
            "fcdafd8af31454376e9f695b5f0f26f7f59733a8724b62ae6dd9ca1d91b20b71",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
        layout.slot(19, ItemStackCreator.getStackHead(
            "§7sereklo",
            "bfcc96260f0f1849e04cea0c4b3714bb7e4b0f7cdf19de28a0d40a19580e26eb",
            1,
            "§7Food Level: §f20",
            "",
            "§eLeft Click to teleport!",
            "§eRight Click for first person!"
        ));
    }
}
