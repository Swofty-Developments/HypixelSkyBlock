package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.gui.inventory.shop.ShopItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopWoolWeaverCool extends SkyBlockShopGUI {
    public GUIShopWoolWeaverCool() {
        super("Wool Weaver (Cool)", 1, WOOLWEAVER_COOL);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        super.handleOpen(player);

        attachItem(GUIItem.builder(45)
                .item(ItemStackCreator.createNamedItemStack(Material.ARROW, "§a->").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIShopWoolWeaverVibrant());
                    return true;
                })
                .build());
    }

    @Override
    protected void initializeShopItems() {
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.CYAN_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_BLUE_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLACK_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAY_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_GRAY_TERRACOTTA), 1, new CoinShopPrice(8)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_TERRACOTTA), 1, new CoinShopPrice(8)));

        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.CYAN_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLACK_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAY_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_STAINED_GLASS_PANE), 1, new CoinShopPrice(16)));

        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.CYAN_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_BLUE_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLACK_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAY_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_GRAY_STAINED_GLASS), 1, new CoinShopPrice(16)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_STAINED_GLASS), 1, new CoinShopPrice(16)));

        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.CYAN_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_BLUE_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLACK_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAY_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_GRAY_CARPET), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_CARPET), 1, new CoinShopPrice(32)));

        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.CYAN_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_BLUE_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.BLACK_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAY_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_GRAY_WOOL), 1, new CoinShopPrice(32)));
        attachShopItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_WOOL), 1, new CoinShopPrice(32)));
    }
}