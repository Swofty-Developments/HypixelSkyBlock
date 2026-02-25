package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;

public class MayorMenuView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Mayor", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);

        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) return;

        ElectionData data = ElectionManager.getElectionData();
        List<SkyBlockMayor.Perk> activePerks = data.getCurrentMayorPerkEnums();

        layout.slot(11, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§8Perks List");
            lore.add("");
            lore.add("§8§m--------------------------");

            for (SkyBlockMayor.Perk perk : activePerks) {
                lore.add(mayor.getColor() + perk.getDisplayName());
                lore.add(perk.getDescription());
                lore.add("");
            }

            lore.add("§8§m--------------------------");
            lore.add("");
            lore.add("§7The listed perks are available to");
            lore.add("§7all players until the closing of");
            lore.add("§7the next elections.");

            return ItemStackCreator.getStackHead(
                    mayor.getColor() + "Mayor " + mayor.getDisplayName(),
                    new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                    1,
                    lore
            );
        });

        layout.slot(15, (s, c) -> ItemStackCreator.getStack(
                "§bElection & Voting",
                Material.JUKEBOX,
                1,
                "§7View the current election",
                "§7candidates and cast your vote.",
                "",
                "§eClick to view!"
        ), (click, c) -> c.push(new ElectionView()));
    }
}
