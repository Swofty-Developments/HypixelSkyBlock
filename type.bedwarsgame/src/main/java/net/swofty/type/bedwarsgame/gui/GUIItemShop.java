package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.bedwarsgame.shop.UpgradeableItemTier;
import net.swofty.type.bedwarsgame.shop.UpgradeableShopItem;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.swofty.type.bedwarsgame.util.ComponentManipulator.noItalic;

public class GUIItemShop extends HypixelInventoryGUI {

	private static final ItemStack QUICK_BUY = ItemStack.builder(Material.NETHER_STAR)
			.customName(Component.text("Quick Buy").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.AQUA))
			.build();

	private static final ItemStack BLOCKS = ItemStack.builder(Material.TERRACOTTA)
			.customName(Component.text("Blocks").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack WEAPONS = ItemStack.builder(Material.GOLDEN_SWORD)
			.customName(Component.text("Weapons").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack ARMOR = ItemStack.builder(Material.CHAINMAIL_BOOTS)
			.customName(Component.text("Armor").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack TOOLS = ItemStack.builder(Material.STONE_PICKAXE)
			.customName(Component.text("Tools").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack BOWS = ItemStack.builder(Material.BOW)
			.customName(Component.text("Bows & Arrows").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack POTIONS = ItemStack.builder(Material.BREWING_STAND)
			.customName(Component.text("Potions").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack UTILITY = ItemStack.builder(Material.TNT)
			.customName(Component.text("Utility").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();

	private static final ItemStack ROTATING_ITEMS = ItemStack.builder(Material.REDSTONE_TORCH)
			.customName(Component.text("Rotating Items").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
			.build();
	private static final List<List<Material>> TIERED_ITEM_GROUPS = List.of(
			List.of(Material.LEATHER_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS)
	);
	private final ShopManager shopService = TypeBedWarsGameLoader.shopManager;
	private int currentPage = 0;

	public GUIItemShop() {
		super("Item Shop", InventoryType.CHEST_6_ROW);
	}

	@Override
	public boolean allowHotkeying() {
		return true;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		updateGUI(e.player());
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}

	private void updateGUI(HypixelPlayer player) {
		fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 9, 17);
		set(currentPage + 9, ItemStackCreator.createNamedItemStack(Material.GREEN_STAINED_GLASS_PANE));
		set(new GUIClickableItem(0) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 0;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return QUICK_BUY.builder();
			}
		});

		set(new GUIClickableItem(1) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 1;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return BLOCKS.builder();
			}
		});

		set(new GUIClickableItem(2) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 2;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return WEAPONS.builder();
			}
		});

		set(new GUIClickableItem(3) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 3;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ARMOR.builder();
			}
		});

		set(new GUIClickableItem(4) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 4;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return TOOLS.builder();
			}
		});

		set(new GUIClickableItem(5) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 5;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return BOWS.builder();
			}
		});

		set(new GUIClickableItem(6) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 6;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return POTIONS.builder();
			}
		});

		set(new GUIClickableItem(7) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 7;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return UTILITY.builder();
			}
		});

		set(new GUIClickableItem(8) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				currentPage = 8;
				updateGUI(player);
				playClickSound(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ROTATING_ITEMS.builder();
			}
		});

		populateShopItems(player);
		updateItemStacks(getInventory(), getPlayer());
	}

	private void populateShopItems(HypixelPlayer player) {
		int[] shopSlots = {
				19, 20, 21, 22, 23, 24, 25,
				28, 29, 30, 31, 32, 33, 34,
				37, 38, 39, 40, 41, 42, 43
		};

		for (int i = 0; i < shopSlots.length; i++) {
			int slot = shopSlots[i];
			int index = i;

			set(new GUIClickableItem(slot) {
				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					ShopItem shopItem = shopService.getShopItem(currentPage, index);
					if (shopItem == null) return;

					if (shopItem instanceof UpgradeableShopItem upgradeableShopItem) {
						int nextLevel = upgradeableShopItem.getNextLevel(player);
						if (nextLevel >= upgradeableShopItem.getTiers().size()) {
							player.sendMessage(noItalic(Component.text("You have already purchased the maximum tier of this item!").color(NamedTextColor.RED)));
							return;
						}
						UpgradeableItemTier nextTier = upgradeableShopItem.getNextTier(player);
						if (!hasPlayerEnoughCurrencyForTier(player, nextTier)) {
							int owned = Arrays.stream(player.getInventory().getItemStacks())
									.filter(s -> s.material() == nextTier.getCurrency().getMaterial())
									.mapToInt(ItemStack::amount)
									.sum();
							int needed = nextTier.getPrice() - owned;
							player.sendMessage(noItalic(Component.text("You don't have enough " + nextTier.getCurrency().getName() + "! Need " + needed + " more!").color(NamedTextColor.RED)));
							return;
						}
						upgradeableShopItem.handlePurchase(player);
						player.sendMessage(noItalic(Component.text("You purchased " + nextTier.getName() + "!").color(NamedTextColor.GREEN)));
						playBuySound(player);
						updateGUI(player);
						return;
					}

					if (!hasPlayerEnoughCurrency(player, shopItem)) {
						player.sendMessage(noItalic(Component.text("You don't have enough " + shopItem.getCurrency().getName() + "!").color(NamedTextColor.RED)));
						return;
					}
					if (!shopItem.canBeBought(player)) {
						player.sendMessage(noItalic(Component.text("You cannot buy this item!").color(NamedTextColor.RED)));
						return;
					}
					if (hasBetterItem(player, shopItem.getDisplay().material())) {
						player.sendMessage(noItalic(Component.text("You already have a better item!").color(NamedTextColor.RED)));
						return;
					}
					shopItem.handlePurchase(player);
					playBuySound(player);
					updateGUI(player);
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					ShopItem shopItem = shopService.getShopItem(currentPage, index);
					if (shopItem == null) {
						return ItemStack.builder(Material.AIR);
					}

					if (shopItem instanceof UpgradeableShopItem upgradeableShopItem) {
						int nextLevel = upgradeableShopItem.getNextLevel(player);
						if (nextLevel >= upgradeableShopItem.getTiers().size()) {
							UpgradeableItemTier lastTier = upgradeableShopItem.getTiers().getLast();
							return ItemStack.builder(lastTier.getMaterial())
									.customName(noItalic(Component.text(upgradeableShopItem.getName()).decorationIfAbsent(ITALIC, TextDecoration.State.FALSE)))
									.lore(List.of(
											noItalic(Component.text("You own the highest level available!").color(NamedTextColor.GREEN)),
											Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
											noItalic(Component.text(upgradeableShopItem.getDescription()).color(NamedTextColor.GRAY))
									));
						}
						UpgradeableItemTier nextTier = upgradeableShopItem.getNextTier(player);
						boolean hasEnough = hasPlayerEnoughCurrencyForTier(player, nextTier);
						Component buy = hasEnough
								? noItalic(Component.text("Click to buy!").color(NamedTextColor.YELLOW))
								: noItalic(Component.text("You don't have enough " + nextTier.getCurrency().getName() + "!").color(NamedTextColor.RED));
						return ItemStack.builder(nextTier.getMaterial())
								.amount(1)
								.customName(
										hasEnough
												? noItalic(Component.text(nextTier.getName()).decorationIfAbsent(ITALIC, TextDecoration.State.FALSE))
												: noItalic(Component.text(nextTier.getName()).decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.RED)
										))
								.lore(List.of(
										noItalic(Component.text("Cost: ").color(NamedTextColor.GRAY)
												.append(Component.text(nextTier.getPrice() + " " + nextTier.getCurrency().getName()).color(nextTier.getCurrency().getColor()))),
										Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
										noItalic(Component.text(upgradeableShopItem.getDescription()).color(NamedTextColor.GRAY)),
										Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
										buy
								));
					}

					boolean hasEnough = hasPlayerEnoughCurrency(player, shopItem);
					Component buy;
					if (!hasEnough) {
						buy = noItalic(Component.text("You don't have enough " + shopItem.getCurrency().getName() + "!").color(NamedTextColor.RED));
					} else if (!shopItem.canBeBought(player)) {
						buy = noItalic(Component.text("You cannot buy this item!").color(NamedTextColor.RED));
					} else if (hasBetterItem(player, shopItem.getDisplay().material())) {
						buy = noItalic(Component.text("You already have a better item!").color(NamedTextColor.RED));
					} else {
						buy = noItalic(Component.text("Click to buy!").color(NamedTextColor.YELLOW));
					}
					return shopItem.getDisplay().builder()
							.amount(shopItem.getAmount())
							.customName(hasEnough
									? noItalic(Component.text(shopItem.getName()).decorationIfAbsent(ITALIC, TextDecoration.State.FALSE))
									: noItalic(Component.text(shopItem.getName()).decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.RED)
							))
							.lore(List.of(
									noItalic(Component.text("Cost: ").color(NamedTextColor.GRAY)
											.append(Component.text(shopItem.getPrice() + " " + shopItem.getCurrency().getName()).color(shopItem.getCurrency().getColor()))),
									Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
									noItalic(Component.text(shopItem.getDescription()).color(NamedTextColor.GRAY)),
									Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
									buy
							));
				}
			});
		}
	}

	private boolean hasPlayerEnoughCurrency(HypixelPlayer player, ShopItem shopItem) {
		int requiredAmount = shopItem.getPrice();
		Material currencyMaterial = shopItem.getCurrency().getMaterial();

		int playerAmount = 0;
		for (ItemStack item : player.getInventory().getItemStacks()) {
			if (item.material() == currencyMaterial) {
				playerAmount += item.amount();
			}
		}

		return playerAmount >= requiredAmount;
	}

	private boolean hasPlayerEnoughCurrencyForTier(HypixelPlayer player, UpgradeableItemTier tier) {
		int required = tier.getPrice();
		Material cur = tier.getCurrency().getMaterial();
		int have = 0;
		for (ItemStack it : player.getInventory().getItemStacks()) {
			if (it.material() == cur) have += it.amount();
		}
		return have >= required;
	}

	private boolean hasBetterItem(Player player, Material materialToBuy) {
		for (List<Material> group : TIERED_ITEM_GROUPS) {
			if (!group.contains(materialToBuy)) {
				continue;
			}

			int tierToBuy = group.indexOf(materialToBuy);
			for (ItemStack stack : player.getInventory().getItemStacks()) {
				if (group.contains(stack.material()) && group.indexOf(stack.material()) > tierToBuy) {
					return true;
				}
			}
			for (ItemStack stack : List.of(
					player.getEquipment(EquipmentSlot.BOOTS),
					player.getEquipment(EquipmentSlot.LEGGINGS),
					player.getEquipment(EquipmentSlot.CHESTPLATE),
					player.getEquipment(EquipmentSlot.HELMET))) {
				if (group.contains(stack.material()) && group.indexOf(stack.material()) > tierToBuy) {
					return true;
				}
			}
			return false;
		}

		return false;
	}

	private void playClickSound(HypixelPlayer player) {
		player.playSound(Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.MASTER, 1.0f, 1.0f));
	}

	private void playBuySound(HypixelPlayer player) {
		player.playSound(Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0f, 1.0f));
	}

}
