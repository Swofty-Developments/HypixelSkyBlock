package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.collection.CustomCollectionAward;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIYourBags extends HypixelInventoryGUI {
    public GUIYourBags() {
        super("Your Bags", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(48) {
            @Override
            public void run(net.minestom.server.event.inventory.InventoryPreClickEvent e, HypixelPlayer player) {
                player.openView(new GUISkyBlockMenu());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu");
            }
        });

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();

        if (player.hasCustomCollectionAward(CustomCollectionAward.SACK_OF_SACKS))
            set(new GUIClickableItem(20) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUISackOfSacks().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStackHead("§aSack of Sacks", "80a077e248d142772ea800864f8c578b9d36885b29daf836b64a706882b6ec10", 1,
                            "§7A sack which contains other sacks.",
                            "§7Sackception!",
                            "",
                            "§eClick to open!");
                }
            });
        else {
            set(new GUIItem(20) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§cSack of Sacks", Material.GRAY_DYE, 1,
                            "§7A sack which contains other sacks.",
                            "§7Sackception!",
                            "",
                            "§cRequires §aClownfish Collection IV§c.");
                }
            });
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.FISHING_BAG)) {
            set(new GUIClickableItem(21) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStackHead("§aFishing Bag", "eb8e297df6b8dffcf135dba84ec792d420ad8ecb458d144288572a84603b1631", 1,
                            "§7A useful bag which can hold all",
                            "§7types of fish, bait, and fishing loot!",
                            "",
                            "§eClick to open!");
                }
            });
        } else {
            set(new GUIItem(21) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§cFishing Bag", Material.GRAY_DYE, 1,
                            "§7A useful bag which can hold all",
                            "§7types of fish, bait, and fishing loot!",
                            "",
                            "§cRequires §aRaw Fish Collection III§c.");
                }
            });
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.POTION_BAG)) {
            set(new GUIClickableItem(22) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStackHead("§aPotion Bag", "9f8b82427b260d0a61e6483fc3b2c35a585851e08a9a9df372548b4168cc817c", 1,
                            "§7A handy bag for holding your",
                            "§7Potions in.",
                            "",
                            "§eClick to open!");
                }
            });
        } else {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§cPotion Bag", Material.GRAY_DYE, 1,
                            "§7A handy bag for holding your",
                            "§7Potions in.",
                            "",
                            "§cRequires §aNether Wart Collection II§c.");
                }
            });
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) {
            set(new GUIClickableItem(23) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIQuiver().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStackHead("§aQuiver",
                            "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                            "§7A masterfully crafted Quiver which",
                            "§7holds any kind of projectile you can",
                            "§7think of!",
                            " ",
                            "§eClick to open!");
                }
            });
        } else {
            set(new GUIItem(23) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§cQuiver", Material.GRAY_DYE, 1,
                            "§7A masterfully crafted Quiver which",
                            "§7holds any kind of projectile you can",
                            "§7think of!",
                            " ",
                            "§cRequires §aString Collection III§c.");
                }
            });
        }
        if (player.hasCustomCollectionAward(CustomCollectionAward.ACCESSORY_BAG)) {
            set(new GUIClickableItem(24) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIAccessoryBag().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStackHead("§aAccessory Bag",
                            "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                            "§7A special bag which can hold",
                            "§7Talismans, Rings, Artifacts, and Orbs",
                            "§7within it. All will still work while in this",
                            "§7bag!",
                            " ",
                            "§7Magical Power: §6" + StringUtility.commaify(player.getMagicalPower()),
                            " ",
                            "§eClick to open!");
                }
            });
        } else {
            set(new GUIItem(24) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§cAccessory Bag", Material.GRAY_DYE, 1,
                            "§7A special bag which can hold",
                            "§7Talismans, Rings, Artifacts, and Orbs",
                            "§7within it. All will still work while in this",
                            "§7bag!",
                            " ",
                            "§cRequires §aRedstone Collection II§c.");
                }
            });
        }

        updateItemStacks(e.inventory(), e.player());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
