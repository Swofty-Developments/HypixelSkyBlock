package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollections extends StatelessView {
    private static final int[] DISPLAY_SLOTS = {
            20, 21, 22, 23, 24,
            31
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Collections", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>(List.of(
                    "§7View all of the items available in",
                    "§7SkyBlock. Collect more of an item to",
                    "§7unlock rewards on your way to",
                    "§7becoming a master of SkyBlock!",
                    " "
            ));
            player.getCollection().getDisplay(lore);
            lore.add(" ");
            lore.add("§eClick to view!");
            return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1, lore.toArray(new String[0]));
        });

        layout.slot(50, (s, c) -> ItemStackCreator.getStackHead("§aCrafted Minions",
                        "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407", 1,
                        "§7View all the unique minions that you",
                        "§7have crafted.",
                        "",
                        "§eClick to view!"),
                (click, c) -> {
                    c.player().openView(new GUICraftedMinions(), GUICraftedMinions.createInitialState());
                });

        ArrayList<CollectionCategory> allCategories = CollectionCategories.getCategories();
        for (int i = 0; i < DISPLAY_SLOTS.length && i < allCategories.size(); i++) {
            CollectionCategory category = allCategories.get(i);
            int slot = DISPLAY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                ArrayList<String> display = new ArrayList<>();
                player.getCollection().getDisplay(display, category);

                ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                        "§7View your " + category.getName() + " Collections!",
                        " "
                ));
                lore.addAll(display);

                return ItemStackCreator.getStack("§a" + category.getName() + " Collections",
                        category.getDisplayIcon(), 1, lore);
            }, (_, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                ArrayList<String> display = new ArrayList<>();
                player.getCollection().getDisplay(display, category);

                player.openView(new GUICollectionCategory(category, display));
            });
        }
    }
}
