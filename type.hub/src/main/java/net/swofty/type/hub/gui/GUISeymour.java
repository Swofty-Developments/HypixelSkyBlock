package net.swofty.type.hub.gui;

import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public class GUISeymour extends HypixelInventoryGUI {
    private final List<SkyBlockItem> cheapTuxedoSet = List.of(
            new SkyBlockItem(ItemType.CHEAP_TUXEDO_CHESTPLATE),
            new SkyBlockItem(ItemType.CHEAP_TUXEDO_LEGGINGS),
            new SkyBlockItem(ItemType.CHEAP_TUXEDO_BOOTS)
    );

    private final List<SkyBlockItem> fancyTuxedoSet = List.of(
            new SkyBlockItem(ItemType.FANCY_TUXEDO_CHESTPLATE),
            new SkyBlockItem(ItemType.FANCY_TUXEDO_LEGGINGS),
            new SkyBlockItem(ItemType.FANCY_TUXEDO_BOOTS)
    );

    private final List<SkyBlockItem> elegantTuxedoSet = List.of(
            new SkyBlockItem(ItemType.ELEGANT_TUXEDO_CHESTPLATE),
            new SkyBlockItem(ItemType.ELEGANT_TUXEDO_LEGGINGS),
            new SkyBlockItem(ItemType.ELEGANT_TUXEDO_BOOTS)
    );

    private final double cheapTuxedoPrice = 3_000_000;
    private final double fancyTuxedoPrice = 20_000_000;
    private final double elegantTuxedoPrice = 74_999_999;

    private final double cheapTuxedoCritDamage = cheapTuxedoSet.stream()
                    .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.CRITICAL_DAMAGE))
                    .sum();
    private final double fancyTuxedoCritDamage = cheapTuxedoSet.stream()
            .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.CRITICAL_DAMAGE))
            .sum();
    private final double elegantTuxedoCritDamage = cheapTuxedoSet.stream()
            .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.CRITICAL_DAMAGE))
            .sum();

    private final double cheapTuxedoIntelligence = cheapTuxedoSet.stream()
            .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.INTELLIGENCE))
            .sum();
    private final double fancyTuxedoIntelligence = cheapTuxedoSet.stream()
            .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.INTELLIGENCE))
            .sum();
    private final double elegantTuxedoIntelligence = cheapTuxedoSet.stream()
            .mapToDouble(item -> item.getAttributeHandler().getStatistics().getOverall(ItemStatistic.INTELLIGENCE))
            .sum();

    public GUISeymour() {
        super("Seymour's Fancy Suits", InventoryType.CHEST_4_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                double coins = player.getCoins();
                if (coins < cheapTuxedoPrice) {
                    player.sendMessage("§cYou don't have enough coins!");
                    return;
                }
                cheapTuxedoSet.forEach(player::addAndUpdateItem);
                player.playSuccessSound();
                player.removeCoins(cheapTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                ItemStack.Builder builder = ItemStackCreator.getStack("§5Cheap Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+" + (int) cheapTuxedoCritDamage + "%",
                        "§7Intelligence: §a+" + (int) cheapTuxedoIntelligence,
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c75♥§7.",
                        "§7Deal §c+50% §7damage!",
                        "§8Very stylish.",
                        "",
                        "§7Cost: §6" + StringUtility.commaify(cheapTuxedoPrice) + " Coins",
                        "",
                        player.getCoins() >= cheapTuxedoPrice ? "§eClick to purchase" : "§cCan't afford this!"
                );

                builder.set(DataComponents.DYED_COLOR, new Color(56, 56, 56));
                return builder;
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                double coins = player.getCoins();
                if (coins < fancyTuxedoPrice) {
                    player.sendMessage("§cYou don't have enough coins!");
                    return;
                }
                fancyTuxedoSet.forEach(player::addAndUpdateItem);
                player.playSuccessSound();
                player.removeCoins(fancyTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                ItemStack.Builder builder = ItemStackCreator.getStack("§6Fancy Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+" + (int) fancyTuxedoCritDamage + "%",
                        "§7Intelligence: §a+" + (int) fancyTuxedoIntelligence,
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

                builder.set(DataComponents.DYED_COLOR, new Color(51, 42, 42));

                return builder;
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                double coins = player.getCoins();
                if (coins < elegantTuxedoPrice) {
                    player.sendMessage("§cYou don't have enough coins!");
                    return;
                }
                elegantTuxedoSet.forEach(player::addAndUpdateItem);
                player.playSuccessSound();
                player.removeCoins(elegantTuxedoPrice);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                ItemStack.Builder builder = ItemStackCreator.getStack("§6Elegant Tuxedo", Material.LEATHER_CHESTPLATE, 1,
                        "",
                        "§8Complete suit",
                        "§7Crit Damage: §c+" + (int) elegantTuxedoCritDamage + "%",
                        "§7Intelligence: §a+" + (int) elegantTuxedoIntelligence,
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

                builder.set(DataComponents.DYED_COLOR, new Color(25, 25, 25));
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
