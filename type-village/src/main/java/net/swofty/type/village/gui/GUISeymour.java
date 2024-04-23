package net.swofty.type.village.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUISeymour extends SkyBlockInventoryGUI {
    public GUISeymour() {
        super("Seymour's Fancy Suits", InventoryType.CHEST_4_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(21));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                double coins = player.getCoins();
                if (coins < 3000000) {
                    return;
                }
                player.addAndUpdateItem(ItemType.CHEAP_TUXEDO_CHESTPLATE);
                player.addAndUpdateItem(ItemType.CHEAP_TUXEDO_BOOTS);
                player.addAndUpdateItem(ItemType.CHEAP_TUXEDO_LEGGINGS);
                player.playSuccessSound();
                player.setCoins(coins - 3000000);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .displayName(Component.text("§5Cheap Tuxedo"))
                        .lore(Component.text(""))
                        .lore(Component.text("§8Complete suit"))
                        .lore(Component.text("§7Crit Damage: §c+100%"))
                        .lore(Component.text("§7Intelligence: §a+100"))
                        .lore(Component.text(""))
                        .lore(Component.text("§6Full Set Bonus: Dashing §7(0/3)"))
                        .lore(Component.text("§7Max Health set to §c75♥§7."))
                        .lore(Component.text("§7Deal §c+50% §7damage!"))
                        .lore(Component.text("§8Very stylish."))
                        .lore(Component.text(""))
                        .lore(Component.text("§7Cost: §63,000,000 Coins"))
                        .lore(Component.text(""))
                        .lore(Component.text(player.getCoins() >= 3000000 ? "§eClick to purchase" : "§cCan't afford this!"));
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                double coins = player.getCoins();
                if (coins < 20000000) {
                    return;
                }
                player.addAndUpdateItem(ItemType.FANCY_TUXEDO_CHESTPLATE);
                player.addAndUpdateItem(ItemType.FANCY_TUXEDO_BOOTS);
                player.addAndUpdateItem(ItemType.FANCY_TUXEDO_LEGGINGS);
                player.playSuccessSound();
                player.setCoins(coins - 20000000);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .displayName(Component.text("§5Fancy Tuxedo"))
                        .lore(Component.text(""))
                        .lore(Component.text("§8Complete suit"))
                        .lore(Component.text("§7Crit Damage: §c+150%"))
                        .lore(Component.text("§7Intelligence: §a+300"))
                        .lore(Component.text(""))
                        .lore(Component.text("§6Full Set Bonus: Dashing §7(0/3)"))
                        .lore(Component.text("§7Max Health set to §c150♥§7."))
                        .lore(Component.text("§7Deal §c+100% §7damage!"))
                        .lore(Component.text("§8Very stylish."))
                        .lore(Component.text(""))
                        .lore(Component.text("§7Cost: §620,000,000 Coins"))
                        .lore(Component.text(""))
                        .lore(Component.text(player.getCoins() >= 20000000 ? "§eClick to purchase" : "§cCan't afford this!"));
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                double coins = player.getCoins();
                if (coins < 74999999) {
                    return;
                }
                player.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_CHESTPLATE);
                player.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_BOOTS);
                player.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_LEGGINGS);
                player.playSuccessSound();
                player.setCoins(coins - 74999999);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .displayName(Component.text("§5Elegant Tuxedo"))
                        .lore(Component.text(""))
                        .lore(Component.text("§8Complete suit"))
                        .lore(Component.text("§7Crit Damage: §c+200%"))
                        .lore(Component.text("§7Intelligence: §a+500"))
                        .lore(Component.text(""))
                        .lore(Component.text("§6Full Set Bonus: Dashing §7(0/3)"))
                        .lore(Component.text("§7Max Health set to §250♥§7."))
                        .lore(Component.text("§7Deal §c+150% §7damage!"))
                        .lore(Component.text("§8Very stylish."))
                        .lore(Component.text(""))
                        .lore(Component.text("§7Cost: §674,999,999 Coins"))
                        .lore(Component.text(""))
                        .lore(Component.text(player.getCoins() >= 74999999 ? "§eClick to purchase" : "§cCan't afford this!"));
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
