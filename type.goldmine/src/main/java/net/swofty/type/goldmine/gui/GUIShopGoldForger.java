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

public class GUIShopGoldForger extends ShopView {
    public GUIShopGoldForger() {
        super("Gold Forger", DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                itemAttributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return itemAttributeHandler.asSkyBlockItem();
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.GOLD_INGOT), 20, new CoinShopPrice(110)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.GOLDEN_HELMET), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.GOLDEN_CHESTPLATE), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(32)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.GOLDEN_LEGGINGS), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(28)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(Material.GOLDEN_BOOTS), Map.of(EnchantmentType.PROTECTION, 1, EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(18)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FANCY_SWORD), 1, new CoinShopPrice(80)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.GOLDEN_AXE), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.GOLDEN_PICKAXE), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.GOLDEN_SHOVEL), 1, new CoinShopPrice(10)));
    }
}
