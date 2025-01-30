package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIYourBags extends SkyBlockAbstractInventory {
    private static final String STATE_SACK_OF_SACKS = "sack_of_sacks_unlocked";
    private static final String STATE_FISHING_BAG = "fishing_bag_unlocked";
    private static final String STATE_POTION_BAG = "potion_bag_unlocked";
    private static final String STATE_QUIVER = "quiver_unlocked";
    private static final String STATE_ACCESSORY_BAG = "accessory_bag_unlocked";

    public GUIYourBags() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Your Bags")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Set states based on unlocked bags
        if (player.hasCustomCollectionAward(CustomCollectionAward.SACK_OF_SACKS)) {
            doAction(new AddStateAction(STATE_SACK_OF_SACKS));
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.FISHING_BAG)) {
            doAction(new AddStateAction(STATE_FISHING_BAG));
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.POTION_BAG)) {
            doAction(new AddStateAction(STATE_POTION_BAG));
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) {
            doAction(new AddStateAction(STATE_QUIVER));
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.ACCESSORY_BAG)) {
            doAction(new AddStateAction(STATE_ACCESSORY_BAG));
        }

        // Sack of Sacks
        attachItem(GUIItem.builder(20)
                .item(ItemStackCreator.getStackHead("§aSack of Sacks",
                        "80a077e248d142772ea800864f8c578b9d36885b29daf836b64a706882b6ec10", 1,
                        "§7A sack which contains other sacks.",
                        "§7Sackception!",
                        "",
                        "§eClick to open!").build())
                .requireState(STATE_SACK_OF_SACKS)
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISackOfSacks());
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(20)
                .item(ItemStackCreator.getStack("§cSack of Sacks", Material.GRAY_DYE, 1,
                        "§7A sack which contains other sacks.",
                        "§7Sackception!",
                        "",
                        "§cRequires §aClownfish Collection IV§c.").build())
                .requireAnyState("default")
                .build());

        // Fishing Bag
        attachItem(GUIItem.builder(21)
                .item(ItemStackCreator.getStackHead("§aFishing Bag",
                        "eb8e297df6b8dffcf135dba84ec792d420ad8ecb458d144288572a84603b1631", 1,
                        "§7A useful bag which can hold all",
                        "§7types of fish, bait, and fishing loot!",
                        "",
                        "§eClick to open!").build())
                .requireState(STATE_FISHING_BAG)
                .onClick((ctx, item) -> {
                    // TODO: Open fishing bag GUI
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(21)
                .item(ItemStackCreator.getStack("§cFishing Bag", Material.GRAY_DYE, 1,
                        "§7A useful bag which can hold all",
                        "§7types of fish, bait, and fishing loot!",
                        "",
                        "§cRequires §aRaw Fish Collection III§c.").build())
                .requireAnyState("default")
                .build());

        // Potion Bag
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStackHead("§aPotion Bag",
                        "9f8b82427b260d0a61e6483fc3b2c35a585851e08a9a9df372548b4168cc817c", 1,
                        "§7A handy bag for holding your",
                        "§7Potions in.",
                        "",
                        "§eClick to open!").build())
                .requireState(STATE_POTION_BAG)
                .onClick((ctx, item) -> {
                    // TODO: Open potion bag GUI
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§cPotion Bag", Material.GRAY_DYE, 1,
                        "§7A handy bag for holding your",
                        "§7Potions in.",
                        "",
                        "§cRequires §aNether Wart Collection II§c.").build())
                .requireAnyState("default")
                .build());

        // Quiver
        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStackHead("§aQuiver",
                        "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                        "§7A masterfully crafted Quiver which",
                        "§7holds any kind of projectile you can",
                        "§7think of!",
                        " ",
                        "§eClick to open!").build())
                .requireState(STATE_QUIVER)
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIQuiver());
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack("§cQuiver", Material.GRAY_DYE, 1,
                        "§7A masterfully crafted Quiver which",
                        "§7holds any kind of projectile you can",
                        "§7think of!",
                        " ",
                        "§cRequires §aString Collection III§c.").build())
                .requireAnyState("default")
                .build());

        // Accessory Bag
        attachItem(GUIItem.builder(24)
                .item(() -> ItemStackCreator.getStackHead("§aAccessory Bag",
                        "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                        "§7A special bag which can hold",
                        "§7Talismans, Rings, Artifacts, and Orbs",
                        "§7within it. All will still work while in this",
                        "§7bag!",
                        " ",
                        "§7Magical Power: §6" + StringUtility.commaify(player.getMagicalPower()),
                        " ",
                        "§eClick to open!").build())
                .requireState(STATE_ACCESSORY_BAG)
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAccessoryBag());
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(24)
                .item(ItemStackCreator.getStack("§cAccessory Bag", Material.GRAY_DYE, 1,
                        "§7A special bag which can hold",
                        "§7Talismans, Rings, Artifacts, and Orbs",
                        "§7within it. All will still work while in this",
                        "§7bag!",
                        " ",
                        "§cRequires §aRedstone Collection II§c.").build())
                .requireAnyState("default")
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}