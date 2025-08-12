package net.swofty.type.hub.gui;

import net.minestom.server.color.Color;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUISeymour extends HypixelInventoryGUI {
    public GUISeymour() {
        super("Seymour's Fancy Suits", InventoryType.CHEST_4_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));

        double cheapTuxedoPrice = 3000000;
        double fancyTuxedoPrice = 20000000;
        double elegantTuxedoPrice = 74999999;

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                double coins = skyBlockPlayer.getCoins();
                if (coins < cheapTuxedoPrice) {
                    return;
                }
                skyBlockPlayer.addAndUpdateItem(ItemType.CHEAP_TUXEDO_CHESTPLATE);
                skyBlockPlayer.addAndUpdateItem(ItemType.CHEAP_TUXEDO_BOOTS);
                skyBlockPlayer.addAndUpdateItem(ItemType.CHEAP_TUXEDO_LEGGINGS);
                skyBlockPlayer.playSuccessSound();
                skyBlockPlayer.removeCoins(cheapTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
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
                        "§7Cost: §6" + StringUtility.commaify(cheapTuxedoPrice) + " Coins",
                        "",
                        skyBlockPlayer.getCoins() >= cheapTuxedoPrice ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(new Color(56, 56, 56)));
                return builder;
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                double coins = skyBlockPlayer.getCoins();
                if (coins < fancyTuxedoPrice) {
                    return;
                }
                skyBlockPlayer.addAndUpdateItem(ItemType.FANCY_TUXEDO_CHESTPLATE);
                skyBlockPlayer.addAndUpdateItem(ItemType.FANCY_TUXEDO_BOOTS);
                skyBlockPlayer.addAndUpdateItem(ItemType.FANCY_TUXEDO_LEGGINGS);
                skyBlockPlayer.playSuccessSound();
                skyBlockPlayer.removeCoins(fancyTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
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
                        "§7Cost: §6" + StringUtility.commaify(fancyTuxedoPrice) + " Coins",
                        "",
                        player.getCoins() >= fancyTuxedoPrice ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(new Color(51, 42, 42)));

                return builder;
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                double coins = skyBlockPlayer.getCoins();
                if (coins < elegantTuxedoPrice) {
                    return;
                }
                skyBlockPlayer.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_CHESTPLATE);
                skyBlockPlayer.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_BOOTS);
                skyBlockPlayer.addAndUpdateItem(ItemType.ELEGANT_TUXEDO_LEGGINGS);
                skyBlockPlayer.playSuccessSound();
                skyBlockPlayer.removeCoins(elegantTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
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
                        "§7Cost: §6" + StringUtility.commaify(elegantTuxedoPrice) + " Coins",
                        "",
                        player.getCoins() >= elegantTuxedoPrice ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(new Color(25, 25, 25)));
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
