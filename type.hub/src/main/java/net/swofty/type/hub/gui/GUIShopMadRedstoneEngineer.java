package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopMadRedstoneEngineer extends ShopView {
    public GUIShopMadRedstoneEngineer() {
        super("Mad Redstone Engineer", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.REDSTONE_TORCH), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.REDSTONE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DAYLIGHT_DETECTOR), 1, new CoinShopPrice(18)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_PRESSURE_PLATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE_PRESSURE_PLATE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.HEAVY_WEIGHTED_PRESSURE_PLATE), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.LIGHT_WEIGHTED_PRESSURE_PLATE), 1, new CoinShopPrice(16)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.LEVER), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COMPARATOR), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.REPEATER), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TRIPWIRE_HOOK), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TNT), 1, new CoinShopPrice(50)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.HOPPER), 1, new CoinShopPrice(50)));
    }
}
