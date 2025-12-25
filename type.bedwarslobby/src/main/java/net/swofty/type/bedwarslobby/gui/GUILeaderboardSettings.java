package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsLeaderboardPeriod;
import net.swofty.commons.bedwars.BedwarsLeaderboardView;
import net.swofty.commons.bedwars.BedwarsTextAlignment;
import net.swofty.type.bedwarslobby.hologram.LeaderboardHologramManager;
import net.swofty.type.bedwarslobby.hologram.LeaderboardHologramManager.PlayerLeaderboardState;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILeaderboardSettings extends HypixelInventoryGUI {
	private BedwarsLeaderboardMode selectedMode;
	private BedwarsLeaderboardPeriod selectedPeriod;
	private BedwarsLeaderboardView selectedView;
	private BedwarsTextAlignment selectedAlignment;

	public GUILeaderboardSettings() {
		super("Leaderboard Settings", InventoryType.CHEST_5_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		HypixelPlayer player = e.player();

		PlayerLeaderboardState currentState = LeaderboardHologramManager.getState(player.getUuid());
		selectedMode = currentState.mode();
		selectedPeriod = currentState.period();
		selectedView = currentState.view();
		selectedAlignment = currentState.textAlignment();

		setupItems(player);
		updateItemStacks(getInventory(), player);
	}

	private void setupItems(HypixelPlayer player) {
		set(new GUIClickableItem(11) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				List<String> lore = new ArrayList<>();
				lore.add("");
				for (BedwarsLeaderboardMode mode : BedwarsLeaderboardMode.values()) {
					if (mode == selectedMode) {
						lore.add("§a➠ §7" + mode.getDisplayName());
					} else {
						lore.add("   §7" + mode.getDisplayName());
					}
				}
				lore.add("");
				lore.add("§8This setting will save across lobbies.");
				lore.add("");
				lore.add("§8Leaderboard data is cached and");
				lore.add("§8does not update immediately.");
				lore.add("");
				lore.add("§eLeft/Right Click to change!");

				return ItemStackCreator.getStack("§aSelect the Mode!",
						Material.RED_BED, 1, lore.toArray(new String[0]));
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				if (e.getClick() instanceof Click.Left) {
					selectedMode = selectedMode.next();
				} else if (e.getClick() instanceof Click.Right) {
					selectedMode = selectedMode.previous();
				}
				setupItems(player);
				updateItemStacks(getInventory(), player);
			}
		});

		set(new GUIClickableItem(12) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				List<String> lore = new ArrayList<>();
				lore.add("");
				for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
					if (period == selectedPeriod) {
						lore.add("§a➠ §7" + period.getDisplayName());
					} else {
						lore.add("   §7" + period.getDisplayName());
					}
				}
				lore.add("");
				lore.add("§8This setting will save across lobbies.");
				lore.add("");
				lore.add("§8Leaderboard data is cached and");
				lore.add("§8does not update immediately.");
				lore.add("");
				lore.add("§eLeft/Right Click to change!");

				return ItemStackCreator.getStack("§aSelect the Time!",
						Material.CLOCK, 1, lore.toArray(new String[0]));
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				if (e.getClick() instanceof Click.Left) {
					selectedPeriod = selectedPeriod.next();
				} else if (e.getClick() instanceof Click.Right) {
					selectedPeriod = selectedPeriod.previous();
				}
				setupItems(player);
				updateItemStacks(getInventory(), player);
			}
		});

		set(new GUIClickableItem(13) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				List<String> lore = new ArrayList<>();
				lore.add("");
				for (BedwarsLeaderboardView view : BedwarsLeaderboardView.values()) {
					if (view == selectedView) {
						lore.add("§a➠ §7" + view.getDisplayName());
					} else {
						lore.add("   §7" + view.getDisplayName());
					}
				}
				lore.add("");
				lore.add("§8Leaderboard data is cached and");
				lore.add("§8does not update immediately.");
				lore.add("");
				lore.add("§eLeft/Right Click to change!");

				return ItemStackCreator.getStack("§aSelect the View!",
						Material.LADDER, 1, lore.toArray(new String[0]));
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				if (e.getClick() instanceof Click.Left) {
					selectedView = selectedView.next();
				} else if (e.getClick() instanceof Click.Right) {
					selectedView = selectedView.previous();
				}
				setupItems(player);
				updateItemStacks(getInventory(), player);
			}
		});

		set(new GUIClickableItem(14) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§aSelect the Players!",
						Material.SKELETON_SKULL, 1,
						"",
						"§a➠ §7All",
						"   §8Friends §c(Coming Soon)",
						"   §8Best Friends §c(Coming Soon)",
						"   §8Guild Members §c(Coming Soon)",
						"",
						"§8Leaderboard data is cached and",
						"§8does not update immediately.");
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.sendMessage("§cThis feature is coming soon!");
			}
		});

		set(new GUIClickableItem(15) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				List<String> lore = new ArrayList<>();
				lore.add("");
				for (BedwarsTextAlignment alignment : BedwarsTextAlignment.values()) {
					if (alignment == selectedAlignment) {
						lore.add("§a➠ §7" + alignment.getDisplayName());
					} else {
						lore.add("   §7" + alignment.getDisplayName());
					}
				}
				lore.add("");
				lore.add("§cBlock alignment is showing correctly");
				lore.add("§conly for Vanilla Minecraft font sizes.");
				lore.add("");
				lore.add("§8This setting will save across the");
				lore.add("§8network.");
				lore.add("");
				lore.add("§8Leaderboard data is cached and");
				lore.add("§8does not update immediately.");
				lore.add("");
				lore.add("§eLeft/Right Click to change!");

				return ItemStackCreator.getStack("§aSelect the Text Alignment!",
						Material.ITEM_FRAME, 1, lore.toArray(new String[0]));
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				if (e.getClick() instanceof Click.Left) {
					selectedAlignment = selectedAlignment.next();
				} else if (e.getClick() instanceof Click.Right) {
					selectedAlignment = selectedAlignment.previous();
				}
				setupItems(player);
				updateItemStacks(getInventory(), player);
			}
		});

		set(new GUIClickableItem(30) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§aApply changes",
						Material.GREEN_TERRACOTTA, 1,
						"§eClick to apply the changes!");
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				PlayerLeaderboardState newState = new PlayerLeaderboardState(
						selectedPeriod, selectedMode, selectedView, selectedAlignment);

				LeaderboardHologramManager.setState(player.getUuid(), newState);
				LeaderboardHologramManager.saveStateToDataHandler(player, newState);
				LeaderboardHologramManager.refreshAllHologramsForPlayer(player);

				player.sendMessage("§aLeaderboard settings applied!");
				player.closeInventory();
			}
		});

		set(new GUIClickableItem(32) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§cDiscard changes",
						Material.RED_TERRACOTTA, 1,
						"§eClose the menu without applying",
						"§echanges!");
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.closeInventory();
			}
		});
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}
}
