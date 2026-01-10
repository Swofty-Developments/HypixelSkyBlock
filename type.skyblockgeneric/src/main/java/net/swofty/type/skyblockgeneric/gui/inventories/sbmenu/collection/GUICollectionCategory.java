package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCollection;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollectionCategory extends StatelessView {
    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final CollectionCategory type;
    private final List<String> display;
    private final int page;

    public GUICollectionCategory(CollectionCategory category, List<String> display) {
        this(category, display, 0);
    }

    public GUICollectionCategory(CollectionCategory category, List<String> display, int page) {
        this.type = category;
        this.display = display;
        this.page = page;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(type.getName() + " Collections", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            List<String> lore = new ArrayList<>(List.of(
                    "§7View your " + type.getName() + " Collections!",
                    " "
            ));
            lore.addAll(display);
            return ItemStackCreator.getStack("§a" + type.getName() + " Collections", Material.STONE_PICKAXE, 1, lore);
        });

        CollectionCategory.ItemCollection[] collections = type.getCollections();
        int itemsPerPage = PAGINATED_SLOTS.length;
        int totalPages = (int) Math.ceil((double) collections.length / itemsPerPage);

        // Pagination buttons
        if (page > 0) {
            //layout.slot(45, (s, c) -> ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1),
            //        (click, c) -> new GUICollectionCategory(type, display, page - 1).open((SkyBlockPlayer) c.player()));
        }
        if (page < totalPages - 1) {
            //layout.slot(53, (s, c) -> ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1),
            //        (click, c) -> new GUICollectionCategory(type, display, page + 1).open((SkyBlockPlayer) c.player()));
        }

        int startIndex = page * itemsPerPage;
        for (int i = 0; i < PAGINATED_SLOTS.length && startIndex + i < collections.length; i++) {
            CollectionCategory.ItemCollection item = collections[startIndex + i];
            int slot = PAGINATED_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                DatapointCollection.PlayerCollection collection = player.getCollection();

                if (!collection.unlocked(item.type())) {
                    return ItemStackCreator.getStack("§c" + item.type().getDisplayName(), Material.GRAY_DYE, 1,
                            "§7Find this item to add it to your",
                            "§7collection and unlock collection",
                            "§7rewards!");
                }

                List<String> lore = new ArrayList<>(List.of(
                        "§7View all your " + item.type().getDisplayName() + " Collection",
                        "§7progress and rewards!",
                        " "
                ));
                collection.getDisplay(lore, item);
                lore.add(" ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§e" + item.type().getDisplayName(), item.type().material, 1, lore);
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                DatapointCollection.PlayerCollection collection = player.getCollection();

                if (!collection.unlocked(item.type())) {
                    player.sendMessage("§cYou haven't found this item yet!");
                    return;
                }
                player.openView(new GUICollectionItem(item.type()));
            });
        }
    }
}
