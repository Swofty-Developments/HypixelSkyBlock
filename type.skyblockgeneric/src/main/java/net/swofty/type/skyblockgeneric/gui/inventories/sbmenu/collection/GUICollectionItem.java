package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCollection;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUICollectionItem extends StatelessView {
    private final ItemType item;
    private final CollectionCategory category;
    private final CollectionCategory.ItemCollection collection;

    public GUICollectionItem(ItemType item) {
        this.item = item;
        this.category = CollectionCategories.getCategory(item);
        this.collection = category.getCollection(item);
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(item.getDisplayName() + " Collection", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            return ItemStackCreator.getStack("§e" + item.getDisplayName(), item.material, 1,
                    "§7View all your " + item.getDisplayName() + " Collection",
                    "§7progress and rewards!",
                    " ",
                    "§7Total Collected: §e" + player.getCollection().get(item));
        });

        int slot = 17;
        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            slot++;
            int finalSlot = slot;

            layout.slot(finalSlot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                DatapointCollection.PlayerCollection playerCollection = player.getCollection();

                List<String> lore = new ArrayList<>();
                lore.add(" ");
                playerCollection.getDisplay(lore, collection, reward);
                lore.add(" ");
                lore.add("§eClick to view rewards!");

                if (playerCollection.getReward(collection) == null) {
                    return ItemStackCreator.getStack(
                            "§7" + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward) + 1),
                            Material.GREEN_STAINED_GLASS_PANE, 1, lore);
                }

                Material material;
                String colour;
                if (playerCollection.getReward(collection) == reward) {
                    material = Material.YELLOW_STAINED_GLASS_PANE;
                    colour = "§e";
                } else if (collection.getPlacementOf(playerCollection.getReward(collection)) > collection.getPlacementOf(reward)) {
                    material = Material.LIME_STAINED_GLASS_PANE;
                    colour = "§a";
                } else {
                    material = Material.RED_STAINED_GLASS_PANE;
                    colour = "§c";
                }

                return ItemStackCreator.getStack(
                        colour + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward) + 1),
                        material, 1, lore);
            }, (click, c) -> {
                ctx.push(new GUICollectionReward(item, reward));
            });
        }
    }
}
