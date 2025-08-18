package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.bedwarsgame.shop.impl.AxeShopItem;
import net.swofty.type.bedwarsgame.shop.impl.PickaxeShopItem;
import net.swofty.type.bedwarsgame.util.ColorUtil;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ActionGameDeath implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
	public void run(PlayerDeathEvent event) {
		Player player = event.getPlayer();

		if (!player.hasTag(Tag.String("gameId"))) {
			return;
		}
		String gameId = player.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);

		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			// If game not in progress, or no game, let default death happen or handle differently
			// For now, we only care about in-progress game deaths for custom respawn.
			// Player might be sent to lobby or a global spawn point by Minestom's default.
			return;
		}

		Integer pickaxeLevel = player.getTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG);
		if (pickaxeLevel != null && pickaxeLevel > 1) {
			player.setTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG, pickaxeLevel - 1);
		}

		Integer axeLevel = player.getTag(AxeShopItem.AXE_UPGRADE_TAG);
		if (axeLevel != null && axeLevel > 1) {
			player.setTag(AxeShopItem.AXE_UPGRADE_TAG, axeLevel - 1);
		}

		// get the amount of iron, gold, diamond, and emeralds the player had
		int iron = 0;
		int gold = 0;
		int diamonds = 0;
		int emeralds = 0;
		ItemStack[] inventory = player.getInventory().getItemStacks();
		for (ItemStack item : inventory) {
			if (item.material() == Material.IRON_INGOT) {
				iron = item.amount();
			} else if (item.material() == Material.GOLD_INGOT) {
				gold = item.amount();
			} else if (item.material() == Material.DIAMOND) {
				diamonds = item.amount();
			} else if (item.material() == Material.EMERALD) {
				emeralds = item.amount();
			}
		}
		if (player.getLastDamageSource() != null && player.getLastDamageSource().getSource() instanceof Player k) {
			if (iron > 0) {
				k.getInventory().addItemStack(ItemStack.of(Material.IRON_INGOT, iron));
				k.sendMessage(MiniMessage.miniMessage().deserialize("<white>+ " + iron + " iron</white>"));
			}
			if (gold > 0) {
				k.getInventory().addItemStack(ItemStack.of(Material.GOLD_INGOT, gold));
				k.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>+ " + gold + " gold</yellow>"));
			}
			if (diamonds > 0) {
				k.getInventory().addItemStack(ItemStack.of(Material.DIAMOND, diamonds));
				k.sendMessage(MiniMessage.miniMessage().deserialize("<blue>+ " + diamonds + " diamond</blue>"));
			}
			if (emeralds > 0) {
				k.getInventory().addItemStack(ItemStack.of(Material.EMERALD, emeralds));
				k.sendMessage(MiniMessage.miniMessage().deserialize("<green>+ " + emeralds + " emerald</green>"));
			}
		}

		String teamName = player.getTag(Tag.String("team"));
		String teamColorName = player.getTag(Tag.String("teamColor"));
		String javaColorName = player.getTag(Tag.String("javaColor"));
		TextColor tColor = ColorUtil.getTextColorByName(teamColorName != null ? teamColorName : "gray");
		if (tColor == null) {
			tColor = NamedTextColor.GRAY;
		}
		TextColor victimTeamTextColor = TextColor.color(tColor);

		boolean bedExists = game.getTeamBedStatus().getOrDefault(teamName, false);

		Component deathMessage = calculateDeathMessage(player, victimTeamTextColor);

		if (!bedExists) {
			// Append FINAL KILL! to the death message
			deathMessage = deathMessage.append(Component.text(" ", NamedTextColor.GRAY)) // Add a space
					.append(Component.text("FINAL KILL!", NamedTextColor.AQUA, TextDecoration.BOLD));
		}
		event.setChatMessage(deathMessage);

		// Player is in an active game, handle custom death
		player.setGameMode(GameMode.SPECTATOR);
		player.getInventory().clear();

		if (bedExists) {
			// Regular death with respawn timer
			final Title.Times titleTimes = Title.Times.times(Duration.ofMillis(100), Duration.ofSeconds(1), Duration.ofMillis(100));
			final AtomicInteger countdown = new AtomicInteger(5);
			final AtomicReference<Task> taskRef = new AtomicReference<>();

			final Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
				if (!player.isOnline()) {
					// Task will be cancelled automatically by Minestom if player disconnects.
					return;
				}

				int secondsRemaining = countdown.getAndDecrement();

				if (secondsRemaining > 0) {
					Component mainTitleText = Component.text("YOU DIED!", NamedTextColor.RED);
					Component subTitleText = Component.text("You will respawn in " + secondsRemaining + " second" + (secondsRemaining == 1 ? "" : "s") + "!", NamedTextColor.YELLOW);
					Title title = Title.title(mainTitleText, subTitleText, titleTimes);
					player.showTitle(title);
				} else {
					// Time to respawn
					player.clearTitle();

					if (teamName == null || teamColorName == null) {
						Logger.warn("Player {} died in game {} but had no team tag. Sending to waiting spawn.", player.getUsername(), gameId);
						MapsConfig.Position waitingPos = game.getMapEntry().getConfiguration().getLocations().getWaiting();
						player.teleport(new Pos(waitingPos.x(), waitingPos.y(), waitingPos.z()));
						player.setGameMode(GameMode.ADVENTURE);
					} else {
						MapsConfig.MapEntry.MapConfiguration.MapTeam playerTeam = game.getMapEntry().getConfiguration().getTeams().stream()
								.filter(t -> t.getName().equalsIgnoreCase(teamName))
								.findFirst()
								.orElse(null);

						if (playerTeam != null) {
							MapsConfig.PitchYawPosition spawnPos = playerTeam.getSpawn();
							player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.pitch(), spawnPos.yaw()));
							player.setGameMode(GameMode.SURVIVAL);
							player.setInvisible(false);
							player.setFlying(false);
							player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));

							// Give back the downgraded tools
							AxeShopItem axeShopItem = new AxeShopItem();
							Integer currentAxeLevel = player.getTag(AxeShopItem.AXE_UPGRADE_TAG);
							if (currentAxeLevel != null && currentAxeLevel > 0) {
								player.getInventory().addItemStack(ItemStack.of(axeShopItem.getTier(currentAxeLevel - 1).getMaterial()));
							}

							PickaxeShopItem pickaxeShopItem = new PickaxeShopItem();
							Integer currentPickaxeLevel = player.getTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG);
							if (currentPickaxeLevel != null && currentPickaxeLevel > 0) {
								player.getInventory().addItemStack(ItemStack.of(pickaxeShopItem.getTier(currentPickaxeLevel - 1).getMaterial()));
							}

							Integer armorLevel = player.getTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG);
							if (armorLevel == null) armorLevel = 0;

							Material boots = null, leggings = null;
							switch (armorLevel) {
								case 1:
									boots = Material.CHAINMAIL_BOOTS;
									leggings = Material.CHAINMAIL_LEGGINGS;
									break;
								case 2:
									boots = Material.IRON_BOOTS;
									leggings = Material.IRON_LEGGINGS;
									break;
								case 3:
									boots = Material.DIAMOND_BOOTS;
									leggings = Material.DIAMOND_LEGGINGS;
									break;
							}

							java.awt.Color awtColor = ColorUtil.getColorByName(javaColorName);
							if (awtColor != null) {
								Color minestomColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
								if (boots != null && leggings != null) {
									player.setEquipment(net.minestom.server.entity.EquipmentSlot.BOOTS, ItemStack.of(boots));
									player.setEquipment(net.minestom.server.entity.EquipmentSlot.LEGGINGS, ItemStack.of(leggings));
								} else {
									player.setEquipment(net.minestom.server.entity.EquipmentSlot.BOOTS, ItemStack.of(Material.LEATHER_BOOTS).with(ItemComponent.DYED_COLOR, new DyedItemColor(minestomColor.asRGB())));
									player.setEquipment(net.minestom.server.entity.EquipmentSlot.LEGGINGS, ItemStack.of(Material.LEATHER_LEGGINGS).with(ItemComponent.DYED_COLOR, new DyedItemColor(minestomColor.asRGB())));
								}
								player.setEquipment(net.minestom.server.entity.EquipmentSlot.CHESTPLATE, ItemStack.of(Material.LEATHER_CHESTPLATE).with(ItemComponent.DYED_COLOR, new DyedItemColor(minestomColor.asRGB())));
								player.setEquipment(net.minestom.server.entity.EquipmentSlot.HELMET, ItemStack.of(Material.LEATHER_HELMET).with(ItemComponent.DYED_COLOR, new DyedItemColor(minestomColor.asRGB())));
							} else {
								Logger.warn("Could not parse color: " + javaColorName + " for player " + player.getUsername() + "'s armor.");
							}

							Integer protectionLevel = player.getTag(Tag.Integer("upgrade_reinforced_armor"));
							if (protectionLevel != null) {
								TypeBedWarsGameLoader.getTeamShopService().getUpgrade("reinforced_armor").applyEffect(game, teamName, protectionLevel);
							}

							Integer cushionedBootsLevel = player.getTag(Tag.Integer("upgrade_cushioned_boots"));
							if (cushionedBootsLevel != null) {
								TypeBedWarsGameLoader.getTeamShopService().getUpgrade("cushioned_boots").applyEffect(game, teamName, cushionedBootsLevel);
							}

							Integer sharpnessLevel = player.getTag(Tag.Integer("upgrade_sharpness"));
							if (sharpnessLevel != null) {
								TypeBedWarsGameLoader.getTeamShopService().getUpgrade("sharpness").applyEffect(game, teamName, sharpnessLevel);
							}


						} else {
							Logger.warn("Player {} had team tag '{}' but team was not found. Sending to waiting spawn.", player.getUsername(), teamName);
							MapsConfig.Position waitingPos = game.getMapEntry().getConfiguration().getLocations().getWaiting();
							player.teleport(new Pos(waitingPos.x(), waitingPos.y(), waitingPos.z()));
							player.setGameMode(GameMode.ADVENTURE);
						}
					}
					if (game != null && teamName != null) game.notifyPlayerOrBedStateChanged(teamName);

					// Cancel the repeating task
					Task currentTask = taskRef.get();
					if (currentTask != null) {
						currentTask.cancel();
					}
				}
			}).repeat(TaskSchedule.seconds(1)).schedule();
			taskRef.set(task);
		} else {
			// Final kill - no bed
			player.sendTitlePart(TitlePart.TITLE, Component.text("YOU DIED!", NamedTextColor.RED, TextDecoration.BOLD));
			player.sendTitlePart(TitlePart.SUBTITLE, Component.text("You will not respawn.", NamedTextColor.GRAY));
			player.getInventory().clear();
			// Ensure player is fully spectator and cannot interact
			player.setGameMode(GameMode.SPECTATOR);
			player.setInvisible(true);
			player.setFlying(true);

			if (game != null) {
				game.playerEliminated(player); // This will also call notifyPlayerOrBedStateChanged and checkForWinCondition
			}
		}

		// Notify sidebar update immediately on death (player becomes spectator or is eliminated)
		if (game != null && teamName != null) {
			game.notifyPlayerOrBedStateChanged(teamName);
		}
	}

	private static Component calculateDeathMessage(Player player, TextColor victimTeamColor) {
		Damage lastDamage = player.getLastDamageSource();

		Component genericDeathMessage = Component.text(player.getUsername()).color(victimTeamColor)
				.append(Component.text(" died.").color(NamedTextColor.GRAY));

		if (lastDamage == null) {
			if (player.getPosition().y() <= 1) {
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" fell into the void.").color(NamedTextColor.GRAY));
			}

			return genericDeathMessage;
		}

		Entity attacker = lastDamage.getAttacker();
		DynamicRegistry.@NotNull Key<DamageType> damageType = lastDamage.getType();

		// Case 1: Killed by another player
		if (attacker instanceof Player killer) {
			String killerTeamColorName = killer.getTag(Tag.String("teamColor"));
			TextColor killerActualColor = NamedTextColor.GRAY;

			if (killerTeamColorName != null && !killerTeamColorName.isEmpty()) {
				TextColor parsedColor = NamedTextColor.NAMES.value(killerTeamColorName.toLowerCase());
				if (parsedColor != null) {
					killerActualColor = parsedColor;
				}
			}

			// Subcase 1.1: Knocked off / into void by a player
			if (damageType == DamageType.OUT_OF_WORLD) {
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" was knocked into the void by ").color(NamedTextColor.GRAY))
						.append(Component.text(killer.getUsername()).color(killerActualColor))
						.append(Component.text(".").color(NamedTextColor.GRAY));
			} else {
				// Subcase 1.2: Regular kill by a player
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" was slain by ").color(NamedTextColor.GRAY))
						.append(Component.text(killer.getUsername()).color(killerActualColor))
						.append(Component.text(".").color(NamedTextColor.GRAY));
			}
		}
		// Case 2: Killed by a non-player entity (mob)
		else if (attacker != null) {
			String entityTypeName = attacker.getEntityType().name().toLowerCase().replace("_", " ");
			// Consider a mapping for more friendly entity names if desired
			return Component.text(player.getUsername()).color(victimTeamColor)
					.append(Component.text(" was slain by a ").color(NamedTextColor.GRAY))
					.append(Component.text(entityTypeName).color(NamedTextColor.GRAY))
					.append(Component.text(".").color(NamedTextColor.GRAY));
		} else {
			if (damageType == DamageType.OUT_OF_WORLD) {
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" fell into the void.").color(NamedTextColor.GRAY));
			} else if (damageType == DamageType.FALL) {
				if (player.getPosition().y() <= 1) {
					return Component.text(player.getUsername()).color(victimTeamColor)
							.append(Component.text(" fell into the void.").color(NamedTextColor.GRAY));
				}
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" fell from a high place.").color(NamedTextColor.GRAY));
			} else {
				return Component.text(player.getUsername()).color(victimTeamColor)
						.append(Component.text(" died due to environmental causes.").color(NamedTextColor.GRAY));
			}
		}
	}

}
