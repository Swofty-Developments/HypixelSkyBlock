package net.swofty.type.village.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopBuilderVariety extends SkyBlockShopGUI {
    public GUIShopBuilderVariety() {
        super("Variety", 1, VARIETY);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.QUARTZ_BLOCK), 1, new CoinShopPrice(50), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.QUARTZ_STAIRS), 1, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.QUARTZ_SLAB), 1, new CoinShopPrice(2), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CHISELED_QUARTZ_BLOCK), 1, new CoinShopPrice(3), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.QUARTZ_PILLAR), 1, new CoinShopPrice(3), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_BRICKS), 1, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_BRICK_STAIRS), 1, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_BRICK_SLAB), 1, new CoinShopPrice(2), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_BRICK_FENCE), 1, new CoinShopPrice(4), 1));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLOWSTONE), 1, new CoinShopPrice(80), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SOUL_SAND), 1, new CoinShopPrice(8), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHERRACK), 1, new CoinShopPrice(2), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PRISMARINE), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PRISMARINE_BRICKS), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DARK_PRISMARINE), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SEA_LANTERN), 1, new CoinShopPrice(90), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPONGE), 1, new CoinShopPrice(800), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.WET_SPONGE), 1, new CoinShopPrice(800), 1));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLASS_PANE), 1, new CoinShopPrice(1.5), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLASS), 1, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.LAVA_BUCKET), 1, new CoinShopPrice(20), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.JACK_O_LANTERN), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_ORANGE), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_LEMON), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_LETTUCE), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_MELON), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_APPLE), 1, new CoinShopPrice(1000), 1));

        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BLUE_CORN), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BROWN_MUSHROOM), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_CORN), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_CACTUS), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BUSH), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BEETROOT), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BERRY), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_ANCIENT_FRUIT), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_KIWI), 1, new CoinShopPrice(1000), 1));

        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_PINK_BERRY), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BERRY_BUSH), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_CHESTO_BERRY), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_LILAC_FRUIT), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_BANANA_BUNCH), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_APPALLED_PUMPKIN), 1, new CoinShopPrice(1000), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECORATION_ONION), 1, new CoinShopPrice(1000), 1));
    }
}
