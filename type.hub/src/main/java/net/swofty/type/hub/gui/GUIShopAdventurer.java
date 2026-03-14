package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopAdventurer extends ShopView {

    public GUIShopAdventurer() {
        super("Adventurer", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ROTTEN_FLESH), 1, new CoinShopPrice(8)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BONE), 1, new CoinShopPrice(8)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STRING), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SLIME_BALL), 1, new CoinShopPrice(14)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GUNPOWDER), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ZOMBIE_TALISMAN), 1, new CoinShopPrice(500)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SKELETON_TALISMAN), 1, new CoinShopPrice(500)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.VILLAGE_AFFINITY_TALISMAN), 1, new CoinShopPrice(2500)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MINE_AFFINITY_TALISMAN), 1, new CoinShopPrice(2500)));
    }
}