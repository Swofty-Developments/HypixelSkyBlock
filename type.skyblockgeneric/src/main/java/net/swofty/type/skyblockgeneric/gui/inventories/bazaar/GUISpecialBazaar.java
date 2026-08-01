package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

public class GUISpecialBazaar extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Special Bazaar", InventoryType.CHEST_3_ROW);
    }

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slots(Layouts.border(0, 44), (_, _) -> Components.asFiller(Material.YELLOW_STAINED_GLASS_PANE));
        layout.slots(Layouts.rectangle(10, 35));
        layout.autoUpdating(20, (_, _) -> ItemStackCreator.getStack("§eSpecial Bazaar Rules", System.currentTimeMillis() % 2 == 0 ? Material.IRON_CHESTPLATE : Material.OAK_WOOD, 1, List.of("§7Your profile's mode prevents you", "§7from using the bazaar", "", "§7Mode: Ironman", "", "§aEXCEPT: You may BUY booster cookies!")), Duration.ofSeconds(1));
        layout.slot(22, new NonPlayerItemUpdater(new SkyBlockItem(ItemType.BOOSTER_COOKIE)).getUpdatedItem(), (_, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) context.player();
            cookie(player);
        });
        layout.slot(24, ItemStackCreator.getStack("§aBuy §ehalf a dozen §acookies!", Material.COOKIE, 1, "§8Booster Cookie", "", "§7Amount: §a6§7x", "", "§7Per unit: §e0", "§7Price: §e0", "", "§eClick to buy now!"), (defaultStateClickContext, viewContext) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) viewContext.player();
            cookie(player);
        });
        Components.close(layout, 40);
    }

    private void cookie(SkyBlockPlayer player) {
        player.getBazaarConnector().getItemStatistics(ItemType.BOOSTER_COOKIE)
                .thenAccept(stats -> {
                    if (stats.bestAsk() <= 0) {
                        player.sendMessage(I18n.t("gui_bazaar.item.buy_no_offers_message"));
                        return;
                    }

                    int maxSpace = player.maxItemFit(ItemType.BOOSTER_COOKIE);
                    if (maxSpace <= 0) {
                        player.sendMessage(I18n.t("gui_bazaar.item.buy_inventory_full"));
                        return;
                    }

                    double priceWithFee = stats.bestAsk() * 1.04;

                    if (priceWithFee > player.getCoins()) {
                        player.sendMessage(I18n.string("gui_bazaar.item.buy_need_coins", player.getLocale(), Component.text(FORMATTER.format(priceWithFee))));
                        return;
                    }

                    player.getBazaarConnector().instantBuy(ItemType.BOOSTER_COOKIE, 1)
                            .thenAccept(result -> {
                                player.sendMessage(I18n.t("gui_bazaar.item.bazaar_result_prefix").append(Component.text(" " + (result.success() ? "§a" : "§c") + result.message())));
                                if (result.success()) player.closeInventory();
                            });
                });
    }
}
