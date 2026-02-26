package net.swofty.type.hub.gui.rosetta;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.ItemAttributeHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

import java.util.Map;

public class GUIRosettaIronArmor extends ShopView {
    public GUIRosettaIronArmor() {
        super("Iron Armor", DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                itemAttributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return itemAttributeHandler.asSkyBlockItem();
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_HELMET), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(15)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_CHESTPLATE), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(25)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_LEGGINGS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(30)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_BOOTS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(20)));
    }
}
