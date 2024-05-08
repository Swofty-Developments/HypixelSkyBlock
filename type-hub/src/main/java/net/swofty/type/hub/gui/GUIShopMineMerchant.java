package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;
import net.swofty.types.generic.shop.type.ItemShopPrice;

public class GUIShopMineMerchant extends SkyBlockShopGUI{
    public GUIShopMineMerchant() {
        super("Mine Merchant", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COAL), 2, new CoinShopPrice(8)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.IRON_INGOT), 4, new CoinShopPrice(22)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GOLD_INGOT), 2, new CoinShopPrice(12)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ROOKIE_PICKAXE), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.PROMISING_PICKAXE), 1, new CoinShopPrice(35)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.GOLDEN_PICKAXE), 1, new ItemShopPrice(ItemType.GOLD_INGOT, 3)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TORCH), 32, new CoinShopPrice(16)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAVEL), 2, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE), 1, new CoinShopPrice(3)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE), 2, new CoinShopPrice(4)));
        //attachItem(ShopItem.Single(new SkyBlockItem(Material.ONYX), 1, new CoinShopPrice(100)));
    }

}
