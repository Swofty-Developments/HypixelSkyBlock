package net.swofty.type.bedwarsgame.shop;

import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.PotionType;
import net.swofty.type.bedwarsgame.shop.impl.*;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
public class ShopManager {

	private final Map<Integer, List<ShopItem>> categorizedShopItems = new HashMap<>();

	private final ShopItem WOOL = new Wool();
	private final ShopItem HARDENED_CLAY = new HardenedClay();
	private final ShopItem GLASS = new BasicItem("Blast-Proof Glass", "Immune to explosions.", 12, 4, Currency.IRON, Material.GLASS);
	private final ShopItem ENDSTONE = new BasicItem("Endstone", "Solid block to defend your bed. w", 24, 12, Currency.IRON, Material.END_STONE);
	private final ShopItem OBSIDIAN = new BasicItem("Obsidian", "Great for defending.", 4, 4, Currency.EMERALD, Material.OBSIDIAN);
	private final ShopItem LADDER = new BasicItem("Ladder", "Useful to save cats stuck in trees.", 8, 8, Currency.IRON, Material.LADDER);
	private final ShopItem PLANKS = new BasicItem("Wood", "Good block to defend your bed.\nStrong against pickaxes.", 4, 16, Currency.GOLD, Material.OAK_PLANKS);
	private final ShopItem STONE_SWORD = new ReplaceAdderItem("Stone Sword", "", 10, Currency.IRON, Material.STONE_SWORD);
	private final ShopItem IRON_SWORD = new ReplaceAdderItem("Iron Sword", "", 7, Currency.GOLD, Material.IRON_SWORD);
	private final ShopItem DIAMOND_SWORD = new ReplaceAdderItem("Diamond Sword", "", (t) -> t.isDoublesSolo() ? 3 : 4, Currency.EMERALD, Material.DIAMOND_SWORD);
	private final ShopItem ENDER_PEARL = new ReplaceAdderItem("Ender Pearl", "The quickest way to invade enemy\nbases.", 2, Currency.EMERALD, Material.ENDER_PEARL);
	private final ShopItem TNT = new BasicItem("TNT", "Instantly ignites, appropriate to\nexpode things!", 4, 1, Currency.GOLD, Material.TNT);
	private final ShopItem WATER_BUCKET = new BasicItem("Water Bucket", "Great to slow down approaching\nenemies. Can also protect against\nTNT.", 6, 1, Currency.GOLD, Material.WATER_BUCKET);
	private final ShopItem BRIDGE_EGG = new BridgeEggShopItem();
	private final ShopItem ARROW = new BasicItem("Arrows", "", 3, 16, Currency.GOLD, Material.ARROW);
	private final ShopItem BOW = new BowShopItem("Bow", "", 6, Currency.IRON, EnchantmentList.EMPTY.with(Enchantment.POWER, 1));
	private final ShopItem PICKAXE = new PickaxeShopItem();
	private final ShopItem AXE = new AxeShopItem();
	private final ShopItem FIREBALL = new FireballShopItem();
	private final ShopItem POPUP_TOWER = new PopupTowerItem();
	private final ShopItem GOLDEN_APPLE = new GappleShopItem();
	private final ShopItem INVISIBILITY_POTION = new PotionShopItem("Invisibility Potion (30 seconds)", "§9Complete invisibility (0:30).", 2, 1, Currency.EMERALD, PotionType.INVISIBILITY);
	private final ShopItem SPEED_POTION = new PotionShopItem("Speed II Potion (45 seconds)", "§9Speed II (0:45).", 2, 1, Currency.EMERALD, PotionType.SWIFTNESS);
	private final ShopItem JUMP_POTION = new PotionShopItem("Jump V Potion (45 seconds)", "§9Jump Boost V (0:45).", 2, 1, Currency.EMERALD, PotionType.LEAPING);
	private final ShopItem CHAINMAIL_ARMOR = new ArmorShopItem("Permanent Chainmail Armor", "", 24, Currency.IRON, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, 1);
	private final ShopItem IRON_ARMOR = new ArmorShopItem("Permanent Iron Armor", "", 12, Currency.GOLD, Material.IRON_BOOTS, Material.IRON_LEGGINGS, 2);
	private final ShopItem DIAMOND_ARMOR = new ArmorShopItem("Permanent Diamond Armor", "", 6, Currency.EMERALD, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, 3);
	private final ShopItem SHEARS = new BasicItem("Shears", "Great to get rid of wool. You will\nalways spawn with these shears.", 20, 1, Currency.IRON, Material.SHEARS);


	public ShopManager() {
		addItemToCategories(WOOL, 1);
		addItemToCategories(HARDENED_CLAY, 1);
		addItemToCategories(GLASS, 1);
		addItemToCategories(OBSIDIAN, 1);
		addItemToCategories(ENDSTONE, 1);
		addItemToCategories(LADDER, 1);
		addItemToCategories(PLANKS, 1);
		addItemToCategories(STONE_SWORD, 2);
		addItemToCategories(IRON_SWORD, 2);
		addItemToCategories(DIAMOND_SWORD, 2);
		addItemToCategories(SHEARS, 4);
		addItemToCategories(PICKAXE, 4);
		addItemToCategories(AXE, 4);
		addItemToCategories(FIREBALL, 7);
		addItemToCategories(CHAINMAIL_ARMOR, 3);
		addItemToCategories(IRON_ARMOR, 3);
		addItemToCategories(DIAMOND_ARMOR, 3);
		addItemToCategories(ENDER_PEARL, 7);
		addItemToCategories(TNT, 7);
		addItemToCategories(POPUP_TOWER, 7);
		addItemToCategories(WATER_BUCKET, 7);
		addItemToCategories(GOLDEN_APPLE, 7);
		addItemToCategories(BRIDGE_EGG, 7);
		addItemToCategories(INVISIBILITY_POTION, 6);
		addItemToCategories(SPEED_POTION, 6);
		addItemToCategories(JUMP_POTION, 6);
		addItemToCategories(ARROW, 5);
		addItemToCategories(BOW, 5);
	}

	private void addItemToCategory(int categoryId, ShopItem item) {
		categorizedShopItems.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(item);
	}

	private void addItemToCategories(ShopItem item, int... categoryIds) {
		for (int categoryId : categoryIds) {
			addItemToCategory(categoryId, item);
		}
	}

	public ShopItem getShopItem(int categoryId, int itemIndex) {
		List<ShopItem> itemsInCategory = categorizedShopItems.get(categoryId);
		if (itemsInCategory != null && itemIndex >= 0 && itemIndex < itemsInCategory.size()) {
			return itemsInCategory.get(itemIndex);
		}
		return null;
	}

	// TODO: actual fetching
	public Map<Integer, ShopItem> getQuickBuy(BedWarsPlayer player) {
		Map<Integer, ShopItem> quickBuy = new HashMap<>();
		quickBuy.put(0, WOOL);
		quickBuy.put(1, STONE_SWORD);
		quickBuy.put(2, CHAINMAIL_ARMOR);
		quickBuy.put(3, PICKAXE);
		quickBuy.put(4, BOW);
		quickBuy.put(5, INVISIBILITY_POTION);
		quickBuy.put(6, TNT);
		quickBuy.put(7, PLANKS);
		quickBuy.put(8, IRON_SWORD);
		quickBuy.put(9, IRON_ARMOR);
		quickBuy.put(10, SHEARS);
		quickBuy.put(11, ARROW);
		quickBuy.put(12, SPEED_POTION);
		quickBuy.put(13, WATER_BUCKET);
		quickBuy.put(14, GOLDEN_APPLE);
		quickBuy.put(15, JUMP_POTION);
		quickBuy.put(16, GLASS);
		quickBuy.put(17, ENDSTONE);
		quickBuy.put(18, AXE);
		quickBuy.put(19, WOOL);
		quickBuy.put(20, WOOL);
		return quickBuy;
	}

	public ShopItem getQuickShopItem(BedWarsPlayer player, int itemIndex) {
		Map<Integer, ShopItem> quickBuy = getQuickBuy(player);
		return quickBuy.get(itemIndex);
	}

}
