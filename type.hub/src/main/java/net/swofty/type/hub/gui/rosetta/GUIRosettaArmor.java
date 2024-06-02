package net.swofty.type.hub.gui.rosetta;

import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttributeHandler;
import net.swofty.types.generic.shop.type.CoinShopPrice;

import java.util.Map;

public class GUIRosettaArmor extends SkyBlockShopGUI {
    public GUIRosettaArmor() {
        super("Rosetta's Armor", 1, DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                itemAttributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return itemAttributeHandler.asSkyBlockItem();
    }
    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.ROSETTA_HELMET), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(1050)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.ROSETTA_CHESTPLATE), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(1320)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.ROSETTA_LEGGINGS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(1200)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.ROSETTA_BOOTS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(960)));
    }
}
