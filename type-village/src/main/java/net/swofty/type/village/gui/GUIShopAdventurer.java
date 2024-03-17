package net.swofty.type.village.gui;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinAndItemShopPrice;
import net.swofty.types.generic.shop.type.CoinShopPrice;
import net.swofty.types.generic.shop.type.ItemShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class GUIShopAdventurer extends SkyBlockShopGUI {

    public GUIShopAdventurer() {
        super("Adventurer", 1);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ROTTEN_FLESH), 1, new CoinAndItemShopPrice(ItemType.DIRT, 1, 8), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BONE), 1, new CoinShopPrice(8), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STRING), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SLIME_BALL), 1, new CoinShopPrice(14), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GUNPOWDER), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ZOMBIE_TALISMAN), 1, new CoinShopPrice(500), 1));
    }
}
