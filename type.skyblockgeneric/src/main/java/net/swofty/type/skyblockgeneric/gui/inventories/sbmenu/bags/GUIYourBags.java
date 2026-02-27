package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.collection.CustomCollectionAward;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;

public class GUIYourBags extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.bags.main.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        // Sack of Sacks
        if (player.hasCustomCollectionAward(CustomCollectionAward.SACK_OF_SACKS)) {
            layout.slot(20, (s, c) -> TranslatableItemStackCreator.getStackHead(c.player(), "gui_sbmenu.bags.sack_of_sacks.unlocked",
                            "80a077e248d142772ea800864f8c578b9d36885b29daf836b64a706882b6ec10", 1,
                            "gui_sbmenu.bags.sack_of_sacks.unlocked.lore"),
                    (click, c) -> c.player().openView(new GUISackOfSacks()));
        } else {
            layout.slot(20, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.bags.sack_of_sacks.locked", Material.GRAY_DYE, 1,
                    "gui_sbmenu.bags.sack_of_sacks.locked.lore"));
        }

        // Fishing Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.FISHING_BAG)) {
            layout.slot(21, (s, c) -> TranslatableItemStackCreator.getStackHead(c.player(), "gui_sbmenu.bags.fishing_bag.unlocked",
                    "eb8e297df6b8dffcf135dba84ec792d420ad8ecb458d144288572a84603b1631", 1,
                    "gui_sbmenu.bags.fishing_bag.unlocked.lore"));
        } else {
            layout.slot(21, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.bags.fishing_bag.locked", Material.GRAY_DYE, 1,
                    "gui_sbmenu.bags.fishing_bag.locked.lore"));
        }

        // Potion Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.POTION_BAG)) {
            layout.slot(22, (s, c) -> TranslatableItemStackCreator.getStackHead(c.player(), "gui_sbmenu.bags.potion_bag.unlocked",
                    "9f8b82427b260d0a61e6483fc3b2c35a585851e08a9a9df372548b4168cc817c", 1,
                    "gui_sbmenu.bags.potion_bag.unlocked.lore"));
        } else {
            layout.slot(22, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.bags.potion_bag.locked", Material.GRAY_DYE, 1,
                    "gui_sbmenu.bags.potion_bag.locked.lore"));
        }

        // Quiver
        if (player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) {
            layout.slot(23, (s, c) -> TranslatableItemStackCreator.getStackHead(c.player(), "gui_sbmenu.bags.quiver.unlocked",
                            "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                            "gui_sbmenu.bags.quiver.unlocked.lore"),
                    (click, c) -> c.player().openView(new GUIQuiver()));
        } else {
            layout.slot(23, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.bags.quiver.locked", Material.GRAY_DYE, 1,
                    "gui_sbmenu.bags.quiver.locked.lore"));
        }

        // Accessory Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.ACCESSORY_BAG)) {
            layout.slot(24, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                return TranslatableItemStackCreator.getStackHead(c.player(), "gui_sbmenu.bags.accessory_bag.unlocked",
                        "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                        "gui_sbmenu.bags.accessory_bag.unlocked.lore", Map.of(
                                "magical_power", StringUtility.commaify(p.getMagicalPower())
                        ));
            }, (click, c) -> c.player().openView(new GUIAccessoryBag()));
        } else {
            layout.slot(24, (s, c) -> TranslatableItemStackCreator.getStack(c.player(), "gui_sbmenu.bags.accessory_bag.locked", Material.GRAY_DYE, 1,
                    "gui_sbmenu.bags.accessory_bag.locked.lore"));
        }
    }
}
