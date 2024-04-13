package net.swofty.type.village.gui.rosetta;

import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.AttributeHandler;
import net.swofty.types.generic.shop.type.CoinShopPrice;

import java.util.Map;

public class GUIRosettaIronArmor extends SkyBlockShopGUI {
    public GUIRosettaIronArmor() {
        super("Iron Armor", 1, DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        AttributeHandler attributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                attributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return attributeHandler.asSkyBlockItem();
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_HELMET), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(15), 1));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_CHESTPLATE), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(25), 1));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_LEGGINGS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(30), 1));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_BOOTS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(20), 1));
    }
}
