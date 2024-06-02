package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopBuilderWoodworking extends SkyBlockShopGUI {
    public GUIShopBuilderWoodworking() {
        super("Woodworking", 1, UPPER5ROWS);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_PLANKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_DOOR), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_DOOR), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_DOOR), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_DOOR), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_DOOR), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_DOOR), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_TRAPDOOR), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TORCH), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_BUTTON), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_FENCE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_SIGN), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.LADDER), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_PRESSURE_PLATE), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OAK_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPRUCE_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BIRCH_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JUNGLE_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_OAK_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ACACIA_FENCE_GATE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CRAFTING_TABLE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CHEST), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BOOKSHELF), 1, new CoinShopPrice(100)));
    }
}
