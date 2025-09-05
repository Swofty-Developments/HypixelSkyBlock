package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.*;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.bedwarsgame.util.ColorUtil;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.swofty.type.bedwarsgame.util.ComponentManipulator.noItalic;

public class GUITeamShop extends HypixelInventoryGUI {

	private static final int[] UPGRADE_SLOTS = {10, 11, 12, 19, 20, 21};
	private static final int[] TRAP_SHOP_SLOTS = {14, 15, 16, 23, 24, 25};
	private static final int[] TRAP_QUEUE_SLOTS = {39, 40, 41};
	private static final int[] SEPARATOR_SLOTS = {27, 28, 29, 30, 31, 32, 33, 34, 35};
	private final TeamShopManager teamShopService = TypeBedWarsGameLoader.getTeamShopManager();
	private final TrapManager trapManager = TypeBedWarsGameLoader.getTrapManager();

	public GUITeamShop() {
		super("Team Shop", InventoryType.CHEST_6_ROW);
	}

	@Override
	public boolean allowHotkeying() {
		return false;
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
		// Clear / fill separators
		for (int slot : SEPARATOR_SLOTS) {
			set(slot, ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
					.customName(noItalic(Component.text(" "))));
		}

		Game game = TypeBedWarsGameLoader.getPlayerGame(player);
		String teamName = player.getTag(Tag.String("team"));
		if (game == null || teamName == null) {
			// Show error placeholders
			for (int slot : UPGRADE_SLOTS) {
				set(slot, ItemStack.builder(Material.BARRIER)
						.customName(noItalic(Component.text("No Game/Team").color(NamedTextColor.RED))));
			}
			return;
		}

		List<TeamUpgrade> upgrades = teamShopService.getUpgrades();
		List<Trap> traps = trapManager.getTraps();

		// Upgrades
		for (int i = 0; i < UPGRADE_SLOTS.length; i++) {
			int slot = UPGRADE_SLOTS[i];
			int index = i;
			set(new GUIClickableItem(slot) {
				@Override
				public void run(InventoryPreClickEvent event, HypixelPlayer p) {
					if (index >= upgrades.size()) return;
					BedWarsPlayer player = (BedWarsPlayer) p;
					Game playerGame = TypeBedWarsGameLoader.getPlayerGame(player);
					String tag = player.getTag(Tag.String("team"));
					if (playerGame == null || tag == null) return;
					TeamUpgrade upgrade = upgrades.get(index);
					TeamUpgradeTier nextTier = upgrade.getNextTier(playerGame, tag);
					if (nextTier == null) {
						player.sendMini("<red>Your team has already bought this upgrade!");
						playClickSound(player);
						return;
					}
					if (!upgrade.hasEnoughCurrency(player, nextTier)) {
						player.sendMini("<red>You don't have enough " + nextTier.getCurrency().getName() + "!</red>");
						playClickSound(player);
						return;
					}
					upgrade.purchase(playerGame, player);
					playBuySound(player);
					updateGUI(player);
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					if (index >= upgrades.size()) return ItemStack.builder(Material.AIR);
					Game playerGame = TypeBedWarsGameLoader.getPlayerGame(player);
					String tag = player.getTag(Tag.String("team"));
					if (playerGame == null || tag == null) return ItemStack.builder(Material.BARRIER)
							.customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));

					TeamUpgrade upgrade = upgrades.get(index);
					TeamUpgradeTier nextTier = upgrade.getNextTier(playerGame, tag);
					boolean isMaxed = nextTier == null;
					boolean canAfford = !isMaxed && upgrade.hasEnoughCurrency(player, nextTier);

					List<Component> lore = new ArrayList<>();
					lore.add(noItalic(Component.text(upgrade.getDescription()).color(NamedTextColor.GRAY)));
					lore.add(Component.empty().decoration(ITALIC, TextDecoration.State.FALSE));

					int currentLevel = upgrade.getCurrentLevel(playerGame, tag);
					for (TeamUpgradeTier tier : upgrade.getTiers()) {
						boolean owned = tier.getLevel() <= currentLevel;
						boolean next = !owned && !isMaxed && tier.getLevel() == nextTier.getLevel();
						TextColor color = owned ? NamedTextColor.GREEN : (next ? NamedTextColor.YELLOW : NamedTextColor.GRAY);
						lore.add(noItalic(Component.text("Tier " + tier.getLevel() + ": " + tier.getDescription() + ", ")
								.color(color)
								.append(Component.text(tier.getPrice() + " " + tier.getCurrency().getName() + (tier.getPrice() != 1 ? "s" : ""))
										.color(NamedTextColor.AQUA))));
					}
					lore.add(Component.empty().decoration(ITALIC, TextDecoration.State.FALSE));
					if (isMaxed) {
						lore.add(noItalic(Component.text("UNLOCKED").color(NamedTextColor.GREEN)));
					} else if (canAfford) {
						lore.add(noItalic(Component.text("Click to purchase!").color(NamedTextColor.YELLOW)));
					} else {
						lore.add(noItalic(Component.text("You don't have enough " + nextTier.getCurrency().getName() + "!").color(NamedTextColor.RED)));
					}

					return upgrade.getDisplayItem().builder()
							.customName(noItalic(Component.text(upgrade.getName())
									.color(isMaxed || canAfford ? NamedTextColor.GREEN : NamedTextColor.RED)))
							.lore(lore);
				}
			});
		}

		// Trap shop slots
		for (int i = 0; i < TRAP_SHOP_SLOTS.length; i++) {
			int slot = TRAP_SHOP_SLOTS[i];
			int index = i;
			set(new GUIClickableItem(slot) {
				@Override
				public void run(InventoryPreClickEvent event, HypixelPlayer player) {
					if (index >= traps.size()) return;
					Game playerGame = TypeBedWarsGameLoader.getPlayerGame(player);
					String tag = player.getTag(Tag.String("team"));
					if (playerGame == null || tag == null) return;
					Trap trap = traps.get(index);
					int price = trap.getPrice(playerGame, tag);
					int owned = Arrays.stream(player.getInventory().getItemStacks())
							.filter(s -> s.material() == trap.getCurrency().getMaterial())
							.mapToInt(ItemStack::amount).sum();
					if (owned < price) {
						player.sendMini("<red>You don't have enough " + trap.getCurrency().getName() + "!</red>");
						playClickSound(player);
						return;
					}
					BedWarsInventoryManipulator.removeItems(player, trap.getCurrency().getMaterial(), price);
					playerGame.addTeamTrap(tag, trap.getKey());
					broadcastTeamPurchase(playerGame, tag, player, trap.getName());
					playBuySound(player);
					updateGUI(player);
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					if (index >= traps.size()) return ItemStack.builder(Material.AIR);
					Game playerGame = TypeBedWarsGameLoader.getPlayerGame(player);
					String t = player.getTag(Tag.String("team"));
					if (playerGame == null || t == null) return ItemStack.builder(Material.BARRIER)
							.customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));
					Trap trap = traps.get(index);
					int price = trap.getPrice(playerGame, t);
					int owned = Arrays.stream(player.getInventory().getItemStacks())
							.filter(s -> s.material() == trap.getCurrency().getMaterial())
							.mapToInt(ItemStack::amount).sum();
					boolean canAfford = owned >= price;
					return trap.getDisplayItem().builder()
							.customName(noItalic(Component.text(trap.getName())
									.color(canAfford ? NamedTextColor.GREEN : NamedTextColor.RED)))
							.lore(List.of(
									noItalic(Component.text(trap.getDescription()).color(NamedTextColor.GRAY)),
									Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
									noItalic(Component.text("Cost: ").color(NamedTextColor.GRAY)
											.append(Component.text(price + " " + trap.getCurrency().getName() + (price != 1 ? "s" : ""))
													.color(NamedTextColor.AQUA))),
									Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
									canAfford ? noItalic(Component.text("Click to purchase!").color(NamedTextColor.YELLOW))
											: noItalic(Component.text("You don't have enough " + trap.getCurrency().getName() + "!").color(NamedTextColor.RED))
							));
				}
			});
		}

		// Trap queue display
		for (int i = 0; i < TRAP_QUEUE_SLOTS.length; i++) {
			int slot = TRAP_QUEUE_SLOTS[i];
			int index = i;
			set(new GUIClickableItem(slot) {
				@Override
				public void run(InventoryPreClickEvent event, HypixelPlayer player) {
					event.setCancelled(true); // purely informational
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					Game g = TypeBedWarsGameLoader.getPlayerGame(player);
					String t = player.getTag(Tag.String("team"));
					if (g == null || t == null) return ItemStack.builder(Material.BARRIER)
							.customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));
					List<String> queued = g.getTeamTraps(t);
					if (index < queued.size()) {
						Trap trap = traps.stream().filter(tr -> tr.getKey().equals(queued.get(index))).findFirst().orElse(null);
						if (trap != null) {
							return ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
									.customName(noItalic(Component.text("Trap #" + (index + 1) + ": " + trap.getName())
											.color(NamedTextColor.AQUA)))
									.amount(index + 1);
						}
					}
					return ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
							.customName(noItalic(Component.text("Trap #" + (index + 1) + ": No Trap")
									.color(NamedTextColor.RED)))
							.lore(List.of(
									noItalic(Component.text("The first enemy to walk into your").color(NamedTextColor.GRAY)),
									noItalic(Component.text("base will trigger this trap!").color(NamedTextColor.GRAY)),
									Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
									noItalic(Component.text("Purchasing a trap will queue it here.").color(NamedTextColor.GRAY)),
									noItalic(Component.text("Its cost scales with traps queued.").color(NamedTextColor.GRAY))
							))
							.amount(index + 1);
				}

				@Override
				public boolean canPickup() {
					return false;
				}
			});
		}

		updateItemStacks(getInventory(), getPlayer());
	}

	private void broadcastTeamPurchase(Game game, String teamName, HypixelPlayer buyer, String name) {
		String teamColorName = buyer.getTag(Tag.String("teamColor"));
		TextColor teamColor = ColorUtil.getTextColorByName(teamColorName);
		if (teamColor == null) teamColor = NamedTextColor.WHITE;
		for (var pl : game.getPlayers()) {
			if (teamName.equals(pl.getTag(Tag.String("team")))) {
				pl.sendMessage(
						Component.text(buyer.getUsername()).color(teamColor)
								.append(Component.text(" purchased ").color(NamedTextColor.GREEN))
								.append(Component.text(name).color(NamedTextColor.GOLD))
				);
			}
		}
	}

	private void playClickSound(HypixelPlayer player) {
		player.playSound(Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.MASTER, 1.0f, 1.0f));
	}

	private void playBuySound(HypixelPlayer player) {
		player.playSound(Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0f, 1.0f));
	}

}
