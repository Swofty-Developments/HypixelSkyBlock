package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopBuilderGreenThumb extends SkyBlockShopGUI {
    public GUIShopBuilderGreenThumb() {
        super("Green Thumb", 1, GREENTHUMB);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_LEAVES), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SHORT_GRASS), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TALL_GRASS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.FLOWER_POT), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_SAPLING), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.FERN), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.LARGE_FERN), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DEAD_BUSH), 1, new CoinShopPrice(4)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DANDELION), 1, new CoinShopPrice(50)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POPPY), 1, new CoinShopPrice(50)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BLUE_ORCHID), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.AZURE_BLUET), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OXEYE_DAISY), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ALLIUM), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PEONY), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.LILAC), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ROSE_BUSH), 1, new CoinShopPrice(20)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.WHITE_TULIP), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PINK_TULIP), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.WATER_BUCKET), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_TULIP), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ORANGE_TULIP), 1, new CoinShopPrice(20)));
    }
}
