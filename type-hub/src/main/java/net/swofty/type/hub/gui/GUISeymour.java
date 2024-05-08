package net.swofty.type.hub.gui;

import net.minestom.server.color.Color;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.LeatherArmorMeta;
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
        set(GUIClickableItem.getCloseItem(31));

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
                ItemStack.Builder builder = ItemStackCreator.getStack("§5Cheap Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+100%",
                        "§7Intelligence: §a+100",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c75♥§7.",
                        "§7Deal §c+50% §7damage!",
                        "§8Very stylish.",
                        "",
                        "§7Cost: §63,000,000 Coins",
                        "",
                        player.getCoins() >= 3000000 ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.meta(meta -> {
                    LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(meta.tagHandler());
                    leatherMeta.color(new Color(56, 56, 56));
                    leatherMeta.hideFlag(ItemHideFlag.HIDE_DYE,
                            ItemHideFlag.HIDE_ATTRIBUTES);
                });
                return builder;
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
                ItemStack.Builder builder = ItemStackCreator.getStack("§6Fancy Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+150%",
                        "§7Intelligence: §a+300",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c150♥§7.",
                        "§7Deal §c+100% §7damage!",
                        "§8Very stylish.",
                        "",
                        "§7Cost: §620,000,000 Coins",
                        "",
                        player.getCoins() >= 20000000 ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.meta(meta -> {
                    LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(meta.tagHandler());
                    leatherMeta.color(new Color(51, 42, 42));
                    leatherMeta.hideFlag(ItemHideFlag.HIDE_DYE,
                            ItemHideFlag.HIDE_ATTRIBUTES
                    );
                });
                return builder;
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
                ItemStack.Builder builder = ItemStackCreator.getStack("§6Elegant Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+200%",
                        "§7Intelligence: §a+500",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c1250♥§7.",
                        "§7Deal §c+150% §7damage!",
                        "§8Very stylish.",
                        "",
                        "§7Cost: §674,999,999 Coins",
                        "",
                        player.getCoins() >= 74999999 ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.meta(meta -> {
                    LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(meta.tagHandler());
                    leatherMeta.color(new Color(25, 25, 25));
                    leatherMeta.hideFlag(ItemHideFlag.HIDE_DYE,
                            ItemHideFlag.HIDE_ATTRIBUTES);
                });
                return builder;
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
