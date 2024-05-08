package net.swofty.type.hub.gui.rosetta;

import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIStarlightArmor extends SkyBlockShopGUI {
    public GUIStarlightArmor() {
        super("Starlight Armor", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(SkyBlockShopGUI.ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_HELMET), 1, new CoinShopPrice(35000)));
        attachItem(SkyBlockShopGUI.ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_CHESTPLATE), 1, new CoinShopPrice(70000)));
        attachItem(SkyBlockShopGUI.ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_LEGGINGS), 1, new CoinShopPrice(45000)));
        attachItem(SkyBlockShopGUI.ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_BOOTS), 1, new CoinShopPrice(30000)));
    }
}
