package net.swofty.type.island.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIPatchNotes extends HypixelInventoryGUI {
    public GUIPatchNotes() {
        super("Patch Notes", InventoryType.CHEST_4_ROW);
    }
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIJerry().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Jerry the Assistant"
                );
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1225968992909525056")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.0.2", Material.BOOK, 1,
                        "§76th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1226864370122887229")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.0.3", Material.STICK, 1,
                        "§78th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1227143736669114459")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.1.0", Material.BLAZE_POWDER, 1,
                        "§79th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1227909009336569906")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.1.1", Material.HOPPER, 1,
                        "§711th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1229007700302495765")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.1.3", Material.GOLD_INGOT, 1,
                        "§715th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1230477957764612146")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.1.4", Material.DISPENSER, 1,
                        "§718th April 2024",
                        "",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                        .clickEvent(ClickEvent.openUrl("https://discord.com/channels/830345347867476000/849739331278733332/1231214757114282065")));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aSkyBlock v1.1.5", Material.DIAMOND, 1,
                        "§720th April 2024",
                        "",
                        "§eClick to view!");
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
