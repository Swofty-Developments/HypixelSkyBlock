package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.collection.CustomCollectionAward;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIYourBags extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Your Bags", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        // Sack of Sacks
        if (player.hasCustomCollectionAward(CustomCollectionAward.SACK_OF_SACKS)) {
            layout.slot(20, (s, c) -> ItemStackCreator.getStackHead("§aSack of Sacks",
                            "80a077e248d142772ea800864f8c578b9d36885b29daf836b64a706882b6ec10", 1,
                            "§7A sack which contains other sacks.",
                            "§7Sackception!",
                            "",
                            "§eClick to open!"),
                    (click, c) -> c.player().openView(new GUISackOfSacks()));
        } else {
            layout.slot(20, (s, c) -> ItemStackCreator.getStack("§cSack of Sacks", Material.GRAY_DYE, 1,
                    "§7A sack which contains other sacks.",
                    "§7Sackception!",
                    "",
                    "§cRequires §aClownfish Collection IV§c."));
        }

        // Fishing Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.FISHING_BAG)) {
            layout.slot(21, (s, c) -> ItemStackCreator.getStackHead("§aFishing Bag",
                    "eb8e297df6b8dffcf135dba84ec792d420ad8ecb458d144288572a84603b1631", 1,
                    "§7A useful bag which can hold all",
                    "§7types of fish, bait, and fishing loot!",
                    "",
                    "§eClick to open!"));
        } else {
            layout.slot(21, (s, c) -> ItemStackCreator.getStack("§cFishing Bag", Material.GRAY_DYE, 1,
                    "§7A useful bag which can hold all",
                    "§7types of fish, bait, and fishing loot!",
                    "",
                    "§cRequires §aRaw Fish Collection III§c."));
        }

        // Potion Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.POTION_BAG)) {
            layout.slot(22, (s, c) -> ItemStackCreator.getStackHead("§aPotion Bag",
                    "9f8b82427b260d0a61e6483fc3b2c35a585851e08a9a9df372548b4168cc817c", 1,
                    "§7A handy bag for holding your",
                    "§7Potions in.",
                    "",
                    "§eClick to open!"));
        } else {
            layout.slot(22, (s, c) -> ItemStackCreator.getStack("§cPotion Bag", Material.GRAY_DYE, 1,
                    "§7A handy bag for holding your",
                    "§7Potions in.",
                    "",
                    "§cRequires §aNether Wart Collection II§c."));
        }

        // Quiver
        if (player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) {
            layout.slot(23, (s, c) -> ItemStackCreator.getStackHead("§aQuiver",
                            "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                            "§7A masterfully crafted Quiver which",
                            "§7holds any kind of projectile you can",
                            "§7think of!",
                            " ",
                            "§eClick to open!"),
                    (click, c) -> c.player().openView(new GUIQuiver()));
        } else {
            layout.slot(23, (s, c) -> ItemStackCreator.getStack("§cQuiver", Material.GRAY_DYE, 1,
                    "§7A masterfully crafted Quiver which",
                    "§7holds any kind of projectile you can",
                    "§7think of!",
                    " ",
                    "§cRequires §aString Collection III§c."));
        }

        // Accessory Bag
        if (player.hasCustomCollectionAward(CustomCollectionAward.ACCESSORY_BAG)) {
            layout.slot(24, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                return ItemStackCreator.getStackHead("§aAccessory Bag",
                        "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                        "§7A special bag which can hold",
                        "§7Talismans, Rings, Artifacts, and Orbs",
                        "§7within it. All will still work while in this",
                        "§7bag!",
                        " ",
                        "§7Magical Power: §6" + StringUtility.commaify(p.getMagicalPower()),
                        " ",
                        "§eClick to open!");
            }, (click, c) -> c.player().openView(new GUIAccessoryBag()));
        } else {
            layout.slot(24, (s, c) -> ItemStackCreator.getStack("§cAccessory Bag", Material.GRAY_DYE, 1,
                    "§7A special bag which can hold",
                    "§7Talismans, Rings, Artifacts, and Orbs",
                    "§7within it. All will still work while in this",
                    "§7bag!",
                    " ",
                    "§cRequires §aRedstone Collection II§c."));
        }
    }
}
