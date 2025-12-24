package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.shop.impl.AxeShopItem;
import net.swofty.type.bedwarsgame.shop.impl.PickaxeShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ActionGameDeath implements HypixelEventClass {

    // TODO: this is horrible
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
        DamageType damageType = lastDamage.getType().asValue();

        // Case 1: Killed by another player
        if (attacker instanceof BedWarsPlayer killer) {
            String killerTeamColorName = killer.getTeamKey().chatColor();
            TextColor killerActualColor = NamedTextColor.GRAY;

            if (killerTeamColorName != null && !killerTeamColorName.isEmpty()) {
                TextColor parsedColor = NamedTextColor.NAMES.value(killerTeamColorName.toLowerCase());
                if (parsedColor != null) {
                    killerActualColor = parsedColor;
                }
            }

            // Subcase 1.1: Knocked off / into void by a player
            if (damageType == DamageType.OUT_OF_WORLD.asValue()) {
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
            if (damageType == DamageType.OUT_OF_WORLD.asValue()) {
                return Component.text(player.getUsername()).color(victimTeamColor)
                        .append(Component.text(" fell into the void.").color(NamedTextColor.GRAY));
            } else if (damageType == DamageType.FALL.asValue()) {
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

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDeathEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();

        if (!player.hasTag(Tag.String("gameId"))) {
            return;
        }
        String gameId = player.getTag(Tag.String("gameId"));
        Game game = TypeBedWarsGameLoader.getGameById(gameId);

        if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
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
        if (player.getLastDamageSource() != null && player.getLastDamageSource().getSource() instanceof BedWarsPlayer k) {
            if (iron > 0) {
                k.getInventory().addItemStack(ItemStack.of(Material.IRON_INGOT, iron));
                k.sendMessage("§f+ " + iron + " iron");
            }
            if (gold > 0) {
                k.getInventory().addItemStack(ItemStack.of(Material.GOLD_INGOT, gold));
                k.sendMessage("§e+ " + gold + " gold");
            }
            if (diamonds > 0) {
                k.getInventory().addItemStack(ItemStack.of(Material.DIAMOND, diamonds));
                k.sendMessage("§b+ " + diamonds + " diamond");
            }
            if (emeralds > 0) {
                k.getInventory().addItemStack(ItemStack.of(Material.EMERALD, emeralds));
                k.sendMessage("§a+ " + emeralds + " emerald");
            }
        }
        TeamKey teamKey = player.getTeamKey();
        TextColor victimTeamTextColor = NamedTextColor.GRAY;
        if (teamKey != null) {
            TextColor parsedColor = NamedTextColor.NAMES.value(teamKey.chatColor().toLowerCase());
            if (parsedColor != null) {
                victimTeamTextColor = parsedColor;
            }
        }

        boolean bedExists = teamKey != null && game.getTeamManager().isBedAlive(teamKey);

        Component deathMessage = calculateDeathMessage(player, victimTeamTextColor);

        if (!bedExists) {
            // Append FINAL KILL! to the death message
            deathMessage = deathMessage.append(
                    MiniMessage.miniMessage().deserialize("<gray> </gray><aqua><bold>FINAL KILL!</bold></aqua>")
            );
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

                    MapTeam playerTeam = game.getMapEntry().getConfiguration().getTeams().get(teamKey);

                    if (playerTeam != null) {
                        BedWarsMapsConfig.PitchYawPosition spawnPos = playerTeam.getSpawn();
                        player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.pitch(), spawnPos.yaw()));
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setInvisible(false);
                        player.setFlying(false);
                        player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));

                        // Give back the downgraded tools
                        AxeShopItem axeShopItem = new AxeShopItem();
                        Integer currentAxeLevel = player.getTag(AxeShopItem.AXE_UPGRADE_TAG);
                        if (currentAxeLevel != null && currentAxeLevel > 0) {
                            player.getInventory().addItemStack(ItemStack.of(axeShopItem.getTier(currentAxeLevel - 1).material()));
                        }

                        PickaxeShopItem pickaxeShopItem = new PickaxeShopItem();
                        Integer currentPickaxeLevel = player.getTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG);
                        if (currentPickaxeLevel != null && currentPickaxeLevel > 0) {
                            player.getInventory().addItemStack(ItemStack.of(pickaxeShopItem.getTier(currentPickaxeLevel - 1).material()));
                        }

                        // equip the player with team armor
                        game.getTeamManager().equipTeamArmor(player, teamKey);

                        Integer protectionLevel = player.getTag(Tag.Integer("upgrade_reinforced_armor"));
                        if (protectionLevel != null) {
                            TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("reinforced_armor").applyEffect(game, teamKey, protectionLevel);
                        }

                        Integer cushionedBootsLevel = player.getTag(Tag.Integer("upgrade_cushioned_boots"));
                        if (cushionedBootsLevel != null) {
                            TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("cushioned_boots").applyEffect(game, teamKey, cushionedBootsLevel);
                        }

                        Integer sharpnessLevel = player.getTag(Tag.Integer("upgrade_sharpness"));
                        if (sharpnessLevel != null) {
                            TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("sharpness").applyEffect(game, teamKey, sharpnessLevel);
                        }


                    } else {
                        Logger.warn("Player {} had team key '{}' but team was not found. Sending to lobby.", player.getUsername(), teamKey.getName());
                        player.sendMessage("§cAn unexpected error occurred while respawning you. Please contact a staff member.");
                        player.sendTo(ServerType.BEDWARS_LOBBY);
                    }

                    // if (game != null && teamName != null) game.notifyPlayerOrBedStateChanged(teamName);

                    // cancel repeating task
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

            player.setGameMode(GameMode.SPECTATOR);
            player.setInvisible(true);
            player.setFlying(true);

            if (game != null) {
                game.playerEliminated(player); // This will also call notifyPlayerOrBedStateChanged and checkForWinCondition
            }
        }
    }

}
