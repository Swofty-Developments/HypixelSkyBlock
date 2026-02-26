package net.swofty.type.goldmine.gui;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.ItemAttributeHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

import java.util.Map;

public class GUIShopIronForger extends ShopView {
    public GUIShopIronForger() {
        super("Iron Forger", DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                itemAttributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return itemAttributeHandler.asSkyBlockItem();
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.IRON_INGOT), 20, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.CHAINMAIL_HELMET), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(50)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.CHAINMAIL_CHESTPLATE), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.CHAINMAIL_LEGGINGS), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(75)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.CHAINMAIL_BOOTS), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(50)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.IRON_PICKAXE), Map.of(EnchantmentType.SMELTING_TOUCH, 1)), 1, new CoinShopPrice(60)));
    }
}
