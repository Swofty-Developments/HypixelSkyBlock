package net.swofty.type.island.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIGuests extends SkyBlockInventoryGUI {
    public GUIGuests() {
        super ("Jerry's Guide to Guesting", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIJerry().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, (short) 0, 1,
                        "§7To Jerry the Assistant"
                );
            }
        });

        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aVisit player islands", Material.FEATHER, (short) 0, 1,
                        "§7You can get Guest on other islands",
                        "§7using §a/visit <player>",
                        "",
                        "§7Guests §cCan't interact with the",
                        "§7world, but it's always fun to see",
                        "§7what others are up to!"
                );
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.sendMessage("§eVisit our web store: §6https://store.hypixel.net");
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aGuests limit", Material.SHORT_GRASS, (short) 0, 1,
                        "§7You can only host a limited",
                        "§7number of §aguests §7on your",
                        "§7island concurrently.",
                        "",
                        "§7The limit depends on your rank:",
                        "§7- §c[§fYOUTUBE§c] §f= §a15",
                        "§7- §6[MVP§c++§6] §f= §a7",
                        "§7- §b[MVP] §f= §a5",
                        "§7- §a[VIP] §f= §a3",
                        "§7- Default §f= §a1",
                        "",
                        "§7Limit on the island: §a1 guests",
                        "",
                        "§bCo-op profile use the partner",
                        "§bwith the highest rank!",
                        "",
                        "§epurchase rank at store.hypixel.net!"
                );
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aAccess Permissions", Material.OAK_FENCE, (short) 0, 1,
                        "§7You may edit who is able to",
                        "§7guest on your island in your",
                        "§eIsland Settings§7.",
                        "",
                        "§7Use §c/ignore add <username> to",
                        "§7prevent a specific player from",
                        "§7joining.",
                        "",
                        "§eClick to open island settings!"
                );
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aModeration", Material.REPEATER, (short) 0, 1,
                        "§7Manage online guests using the",
                        "§eGuests Management §7menu.",
                        "",
                        "§7Alternatively, use the §c/sbkick &",
                        "§a/sbkickall commands or §aclicking",
                        "§aon them§7."
                );
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
