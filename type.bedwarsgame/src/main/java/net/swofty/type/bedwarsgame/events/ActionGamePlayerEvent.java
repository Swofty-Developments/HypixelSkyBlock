package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.gui.GUIEnderChest;
import net.swofty.type.bedwarsgame.gui.GUITeamChest;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionGamePlayerEvent implements HypixelEventClass {

	public static final ConcurrentHashMap<TeamKey, String> ACTIVE_TEAM_CHESTS = new ConcurrentHashMap<>();

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(InventoryPreClickEvent event) {
		if (!(event.getInventory() instanceof PlayerInventory)) {
			return;
		}
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		if (event.getClickedItem().material().isArmor()) {
			event.setCancelled(true);
			return;
		}
		Game game = player.getGame();
		if (game == null) {
			event.setCancelled(true);
			return;
		}
		;
		if (game.getGameStatus() == GameStatus.WAITING) {
			event.setCancelled(true);
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(ItemDropEvent event) {
		switch (event.getItemStack().material().name()) {
			case "wooden_sword":
			case "wooden_pickaxe":
			case "wooden_axe":
			case "stone_axe":
			case "iron_pickaxe":
			case "iron_axe":
			case "diamond_pickaxe":
			case "diamond_axe":
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou cannot drop your tools!");
				break;
			default:
				ItemEntity itemEntity = new ItemEntity(event.getItemStack());
				itemEntity.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition().add(0, event.getPlayer().getEyeHeight(), 0));
				itemEntity.setVelocity(event.getPlayer().getPosition().add(0, 0.3, 0).direction().mul(6));
				itemEntity.setPickupDelay(Duration.ofMillis(500));
				break;
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
	public void run(PlayerBlockInteractEvent event) {
		Block block = event.getBlock();
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		if (player.getGame() == null) {
			event.setCancelled(true);
			return;
		}
		if (block.registry().material() == Material.ENDER_CHEST) {
			new GUIEnderChest().open(player);
			return;
		}

		if (block.registry().material() != Material.CHEST) return;

		Game game = player.getGame();
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			event.setCancelled(true);
			return;
		}

		TeamKey playerTeamKey = player.getTeamKey();
		if (playerTeamKey == null) {
			event.setCancelled(true);
			return;
		}

		double closestDistance = Double.MAX_VALUE;
		TeamKey chestTeamKey = null;

		for (java.util.Map.Entry<TeamKey, MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
			TeamKey teamKey = entry.getKey();
			MapTeam team = entry.getValue();
			BedWarsMapsConfig.PitchYawPosition teamSpawn = team.getSpawn();
			if (teamSpawn == null) continue;

			double distance = Math.sqrt(
					Math.pow(event.getBlockPosition().x() - teamSpawn.x(), 2) +
							Math.pow(event.getBlockPosition().y() - teamSpawn.y(), 2) +
							Math.pow(event.getBlockPosition().z() - teamSpawn.z(), 2)
			);

			if (distance < 10.0 && distance < closestDistance) {
				closestDistance = distance;
				chestTeamKey = teamKey;
			}
		}

		if (chestTeamKey == null) {
			return;
		}

		boolean sameTeam = chestTeamKey.equals(playerTeamKey);
		if (!sameTeam && game.getTeamManager().getTeamBedStatus().getOrDefault(chestTeamKey, false)) {
			event.setCancelled(true);
			player.sendMessage("§cYou can only access enemy team chests if their bed is destroyed!");
			return;
		}

		String existingContextId = ACTIVE_TEAM_CHESTS.get(chestTeamKey);
		GUITeamChest teamChest = new GUITeamChest(chestTeamKey);

		if (existingContextId != null) {
			teamChest.joinSharedContext(player, existingContextId);
		} else {
			String contextId = teamChest.createSharedContext(player);
			ACTIVE_TEAM_CHESTS.put(chestTeamKey, contextId);
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerStartDiggingEvent event) {
		Block block = event.getBlock();
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		ItemStack itemInHand = player.getItemInMainHand();

		Game game = player.getGame();
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			return;
		}

		if (itemInHand.isAir()) {
			if (block.registry().material() == Material.CHEST || block.registry().material() == Material.ENDER_CHEST) {
				event.setCancelled(true);
			}
			return;
		}

		if (block.registry().material() != Material.CHEST &&
				block.registry().material() != Material.ENDER_CHEST) {
			return;
		}

		if (!BedWarsInventoryManipulator.canBeChested(itemInHand.material())) {
			player.sendMessage("§cYou cannot store this item in the chest!");
			event.setCancelled(true);
			return;
		}

		// Handle Ender Chest
		if (block.registry().material() == Material.ENDER_CHEST) {
			event.setCancelled(true);
			try {
				Map<Integer, ItemStack> playerEnderChest = game.getEnderchests().computeIfAbsent(player, k -> new ConcurrentHashMap<>());
				boolean itemAdded = false;
				for (int i = 0; i < 27; i++) { // Standard ender chest size
					if (playerEnderChest.get(i) == null || playerEnderChest.get(i).isAir()) {
						playerEnderChest.put(i, itemInHand);
						itemAdded = true;
						break;
					}
				}

				if (itemAdded) {
					player.setItemInMainHand(ItemStack.AIR);
					player.sendMessage("§7You deposited x" + itemInHand.amount()
							+ " " + itemInHand.material().name().toLowerCase().replace("_", " ")
							+ " into your ender chest.");
					player.getAchievementHandler().addProgressByTrigger("bedwars.chest_deposit", 1);
				} else {
					player.sendMessage("§cYour ender chest is full!");
				}
			} catch (Exception e) {
				Logger.error("Failed to add item to ender chest for player {}: {}", player.getUsername(), e.getMessage());
				e.printStackTrace();
				player.sendMessage("§cFailed to add item to ender chest!");
			}
			return;
		}

		if (block.registry().material() == Material.CHEST) {
			event.setCancelled(true);

			TeamKey playerTeamName = player.getTeamKey();
			if (playerTeamName == null) {
				return;
			}

			TeamKey chestTeamName = null;
			double closestDistance = Double.MAX_VALUE;

			for (Map.Entry<TeamKey, MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
				TeamKey teamKey = entry.getKey();
				MapTeam team = entry.getValue();
				BedWarsMapsConfig.PitchYawPosition teamSpawn = team.getSpawn();
				if (teamSpawn == null) continue;

				double distance = Math.sqrt(
						Math.pow(event.getBlockPosition().x() - teamSpawn.x(), 2) +
								Math.pow(event.getBlockPosition().y() - teamSpawn.y(), 2) +
								Math.pow(event.getBlockPosition().z() - teamSpawn.z(), 2)
				);

				if (distance < 10.0 && distance < closestDistance) {
					closestDistance = distance;
					chestTeamName = teamKey;
				}
			}

			if (chestTeamName == null) {
				event.setCancelled(false);
				return;
			}

			boolean sameTeam = chestTeamName.equals(playerTeamName);
			if (!sameTeam && game.getTeamManager().getTeamBedStatus().getOrDefault(chestTeamName, false)) {
				player.sendMessage(MiniMessage.miniMessage().deserialize("§cYou can only access enemy team chests if their bed is destroyed!"));
				return;
			}

			try {
				Map<Integer, ItemStack> teamChest = game.getChests().computeIfAbsent(chestTeamName, k -> new ConcurrentHashMap<>());
				boolean itemAdded = false;
				for (int i = 0; i < 27; i++) {
					if (teamChest.get(i) == null || teamChest.get(i).isAir()) {
						teamChest.put(i, itemInHand);
						itemAdded = true;
						break;
					}
				}

				if (itemAdded) {
					player.setItemInMainHand(ItemStack.AIR);
					player.getAchievementHandler().addProgressByTrigger("bedwars.chest_deposit", 1);
				} else {
					player.sendMessage("§cThe team chest is full!");
				}
			} catch (Exception e) {
				Logger.error("Failed to add item to team chest for team {}: {}", chestTeamName, e);
				e.printStackTrace();
				player.sendMessage("§cFailed to add item to chest!");
			}
		}
	}

}
