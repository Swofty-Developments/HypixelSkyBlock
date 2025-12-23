package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.gui.GUIEnderChest;
import net.swofty.type.bedwarsgame.gui.GUIItemShop;
import net.swofty.type.bedwarsgame.gui.GUITeamChest;
import net.swofty.type.bedwarsgame.gui.GUITeamShop;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.bedwarsgeneric.game.MapsConfig;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionGamePlayerEvent implements HypixelEventClass {

	public static final ConcurrentHashMap<String, String> ACTIVE_TEAM_CHESTS = new ConcurrentHashMap<>();

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(InventoryPreClickEvent event) {
		if (!(event.getInventory() instanceof PlayerInventory)) {
			return;
		}
		Player player = event.getPlayer();
		if (player.hasTag(Tag.String("gameId"))) {
			if (event.getClickedItem().material().isArmor()) {
				event.setCancelled(true);
				return;
			}
			String gameId = player.getTag(Tag.String("gameId"));
			Game game = TypeBedWarsGameLoader.getGameById(gameId);
			if (game.getGameStatus() == GameStatus.WAITING) {
				event.setCancelled(true);
			}
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerEntityInteractEvent event) {
		if (event.getTarget().getEntityType() == EntityType.VILLAGER) {
			Player player = event.getPlayer();
			if (!event.getTarget().hasTag(Tag.String("type"))) {
				return;
			}
			String type = event.getTarget().getTag(Tag.String("type"));
			switch (type) {
				case "shop" -> new GUIItemShop().open((HypixelPlayer) player);
				case "team" -> new GUITeamShop().open((HypixelPlayer) player);
			}
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(ItemDropEvent event) {
		ItemEntity itemEntity = new ItemEntity(event.getItemStack());
		itemEntity.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition().add(0, event.getPlayer().getEyeHeight(), 0));
		itemEntity.setVelocity(event.getPlayer().getPosition().add(0, 1, 0).direction().mul(6));
		itemEntity.setPickupDelay(Duration.ofMillis(500));
		if (event.getPlayer().hasTag(Tag.String("gameId"))) {
			switch (event.getItemStack().material().name()) {
				case "stone_pickaxe":
				case "stone_axe":
				case "iron_pickaxe":
				case "iron_axe":
				case "diamond_pickaxe":
				case "diamond_axe":
					event.setCancelled(true);
					event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>You cannot drop your tools!</red>"));
					break;
				default:
					break;
			}
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
	public void run(PlayerBlockInteractEvent event) {
		Block block = event.getBlock();
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		if (!player.hasTag(Tag.String("gameId"))) {
			event.setCancelled(true);
			return;
		}
		if (block.registry().material() == Material.ENDER_CHEST) {
			new GUIEnderChest().open((HypixelPlayer) player);
			return;
		}

		if (block.registry().material() != Material.CHEST) return;

		String gameId = player.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			event.setCancelled(true);
			return;
		}

		String teamName = player.getTag(Tag.String("team"));
		if (teamName == null) {
			event.setCancelled(true);
			return;
		}

		String chestTeamName = null;
		double closestDistance = Double.MAX_VALUE;

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
			MapsConfig.PitchYawPosition teamSpawn = team.getSpawn();
			if (teamSpawn == null) continue;

			double distance = Math.sqrt(
					Math.pow(event.getBlockPosition().x() - teamSpawn.x(), 2) +
							Math.pow(event.getBlockPosition().y() - teamSpawn.y(), 2) +
							Math.pow(event.getBlockPosition().z() - teamSpawn.z(), 2)
			);

			if (distance < 10.0 && distance < closestDistance) {
				closestDistance = distance;
				chestTeamName = team.getName();
			}
		}

		if (chestTeamName == null) {
			return;
		}

		boolean sameTeam = chestTeamName.equals(teamName);
		if (!sameTeam && game.getTeamManager().getTeamBedStatus().getOrDefault(chestTeamName, false)) {
			event.setCancelled(true);
			player.sendMessage("§cYou can only access enemy team chests if their bed is destroyed!");
			return;
		}

		String existingContextId = ACTIVE_TEAM_CHESTS.get(chestTeamName);
		GUITeamChest teamChest = new GUITeamChest(chestTeamName);

		if (existingContextId != null) {
			teamChest.joinSharedContext(player, existingContextId);
		} else {
			String contextId = teamChest.createSharedContext(player);
			ACTIVE_TEAM_CHESTS.put(chestTeamName, contextId);
		}
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerStartDiggingEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInMainHand();

		if (!player.hasTag(Tag.String("gameId"))) {
			return;
		}

		String gameId = player.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);
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
			player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You cannot store this item in the chest!</red>"));
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
				} else {
					player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Your ender chest is full!</red>"));
				}
			} catch (Exception e) {
				Logger.error("Failed to add item to ender chest for player {}: {}", player.getUsername(), e.getMessage());
				player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to add item to ender chest!</red>"));
			}
			return;
		}

		if (block.registry().material() == Material.CHEST) {
			event.setCancelled(true);
			String playerTeamName = player.getTag(Tag.String("team"));
			if (playerTeamName == null) {
				return;
			}

			String chestTeamName = null;
			double closestDistance = Double.MAX_VALUE;

			for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
				MapsConfig.PitchYawPosition teamSpawn = team.getSpawn();
				if (teamSpawn == null) continue;

				double distance = Math.sqrt(
						Math.pow(event.getBlockPosition().x() - teamSpawn.x(), 2) +
								Math.pow(event.getBlockPosition().y() - teamSpawn.y(), 2) +
								Math.pow(event.getBlockPosition().z() - teamSpawn.z(), 2)
				);

				if (distance < 10.0 && distance < closestDistance) {
					closestDistance = distance;
					chestTeamName = team.getName();
				}
			}

			if (chestTeamName == null) {
				event.setCancelled(false);
				return;
			}

			boolean sameTeam = chestTeamName.equals(playerTeamName);
			if (!sameTeam && game.getTeamManager().getTeamBedStatus().getOrDefault(chestTeamName, false)) {
				player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You can only access enemy team chests if their bed is destroyed!</red>"));
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
				} else {
					player.sendMessage(MiniMessage.miniMessage().deserialize("<red>The team chest is full!</red>"));
				}
			} catch (Exception e) {
				Logger.error("Failed to add item to team chest for team {}: {}", chestTeamName, e.getMessage());
				player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to add item to chest!</red>"));
			}
		}
	}

}
