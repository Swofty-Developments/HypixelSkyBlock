package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem;

import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.levels.SkyBlockEmblems;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIEmblems extends StatelessView {
    private static final int[] SLOTS = new int[]{11, 12, 13, 14, 15};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Emblems", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);
        Components.back(layout, 30, ctx);

        for (SkyBlockEmblems emblem : SkyBlockEmblems.values()) {
            if (emblem.ordinal() >= SLOTS.length) break;
            int slot = SLOTS[emblem.ordinal()];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                String displayName = emblem.toString();
                GUIMaterial guiMaterial = emblem.getGuiMaterial();
                ArrayList<String> description = new ArrayList<>(emblem.getDescription());

                ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                        "§8" + emblem.amountUnlocked(player) + " Unlocked",
                        " "
                ));
                lore.addAll(description);
                lore.add(" ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getUsingGUIMaterial("§a" + displayName, guiMaterial, 1, lore);
            }, (click, c) -> c.push(new GUIEmblem(emblem), GUIEmblem.createInitialState(emblem)));
        }
    }
}
