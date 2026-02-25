package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
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

public class MinisterMenuView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Minister", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);

        SkyBlockMayor minister = ElectionManager.getCurrentMinister();
        if (minister == null) return;

        ElectionData data = ElectionManager.getElectionData();
        SkyBlockMayor.Perk activePerk = data.getMinisterPerkEnum();

        layout.slot(11, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§8Active Perk");
            lore.add("");
            lore.add("§8§m--------------------------");

            if (activePerk != null) {
                lore.add(minister.getColor() + activePerk.getDisplayName());
                lore.add(activePerk.getDescription());
            }

            lore.add("§8§m--------------------------");
            lore.add("");
            lore.add("§7The Minister is who came in 2nd Place");
            lore.add("§7during the election. They have one");
            lore.add("§7of their perks active.");

            return ItemStackCreator.getStackHead(
                    minister.getColor() + "Minister " + minister.getDisplayName(),
                    new PlayerSkin(minister.getTexture(), minister.getSignature()),
                    1,
                    lore
            );
        });
    }
}
