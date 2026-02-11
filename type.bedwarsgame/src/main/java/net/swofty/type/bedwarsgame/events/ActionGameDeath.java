package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.death.BedWarsCombatTracker;
import net.swofty.type.bedwarsgame.death.BedWarsDeathHandler;
import net.swofty.type.bedwarsgame.death.BedWarsDeathResult;
import net.swofty.type.bedwarsgame.death.BedWarsDeathType;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.shop.impl.AxeShopItem;
import net.swofty.type.bedwarsgame.shop.impl.PickaxeShopItem;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.ScheduleUtility;

import java.util.function.Consumer;

public class ActionGameDeath implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDeathEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();

        BedWarsGame game = player.getGame();
        if (game == null || game.getState() != GameState.IN_PROGRESS) {
            return;
        }

        death(player, game, event::setChatMessage, false);
    }

    public static void death(BedWarsPlayer player, BedWarsGame game, Consumer<Component> deathMessageConsumer, boolean voidDeath) {
        BedWarsMapsConfig.Position position = game.getMapEntry().getConfiguration().getLocations().getSpectator();
        player.teleport(new Pos(position.x(), position.y(), position.z()));

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
                iron += item.amount();
            } else if (item.material() == Material.GOLD_INGOT) {
                gold += item.amount();
            } else if (item.material() == Material.DIAMOND) {
                diamonds += item.amount();
            } else if (item.material() == Material.EMERALD) {
                emeralds += item.amount();
            }
        }

        BedWarsDeathResult deathResult = BedWarsDeathHandler.calculateDeath(player, game, voidDeath);
        if (deathResult.deathType() == BedWarsDeathType.VOID) {
            player.getAchievementHandler().completeAchievement("bedwars.its_dark_down_there");
        }
        if (deathResult.isFinalKill()) {
            BedWarsStatsRecorder.recordFinalDeath(player, game.getGameType());
        }

        TeamKey teamKey = player.getTeamKey();
        boolean bedExists = teamKey != null && game.isBedAlive(teamKey);

        Component deathMessage = BedWarsDeathHandler.createDeathMessage(deathResult);
        handleDeathTypeActions(deathResult, game);
        deathMessageConsumer.accept(deathMessage);

        if (!bedExists && deathResult.getKillCreditPlayer() != null) {
            BedWarsStatsRecorder.recordFinalKill(deathResult.getKillCreditPlayer(), game.getGameType());
        }

        BedWarsCombatTracker.clearCombatData(player);
        BedWarsGame.literalSetupSpectator(player);

        final int finalIron = iron;
        final int finalGold = gold;
        final int finalDiamonds = diamonds;
        final int finalEmeralds = emeralds;
        ScheduleUtility.nextTick(() -> {
            BedWarsPlayer itemRecipient = deathResult.getKillCreditPlayer();
            if (itemRecipient != null) {
                if (finalIron > 0) {
                    itemRecipient.getInventory().addItemStack(ItemStack.of(Material.IRON_INGOT, finalIron));
                    itemRecipient.sendMessage("§f+" + finalIron + " Iron");
                }
                if (finalGold > 0) {
                    itemRecipient.getInventory().addItemStack(ItemStack.of(Material.GOLD_INGOT, finalGold));
                    itemRecipient.sendMessage("§6+" + finalGold + " Gold");
                }
                if (finalDiamonds > 0) {
                    itemRecipient.getInventory().addItemStack(ItemStack.of(Material.DIAMOND, finalDiamonds));
                    itemRecipient.sendMessage("§b+" + finalDiamonds + " Diamond");
                }
                if (finalEmeralds > 0) {
                    itemRecipient.getInventory().addItemStack(ItemStack.of(Material.EMERALD, finalEmeralds));
                    itemRecipient.sendMessage("§3+" + finalEmeralds + " Emerald");
                }
            }
        });

        if (bedExists) {
            game.getRespawnHandler().startRespawn(player);
        } else {
            // Final kill if the bed doesn't exist
            player.getInventory().clear();
            game.onPlayerEliminated(player);
        }
    }

    private static void handleDeathTypeActions(BedWarsDeathResult result, BedWarsGame game) {
        BedWarsPlayer victim = result.victim();
        BedWarsPlayer creditPlayer = result.getKillCreditPlayer();

        if (creditPlayer != null) {
            BedWarsStatsRecorder.recordKill(creditPlayer, game.getGameType());
        }

        BedWarsStatsRecorder.recordDeath(victim, game.getGameType());

        if (creditPlayer != null) {
            game.getReplayManager().recordKill(creditPlayer, victim, result.isFinalKill());
        }

        if (result.isFinalKill() && creditPlayer != null) {
            creditPlayer.getAchievementHandler().addProgress("bedwars.bed_wars_killer", 1);
        }

        if (creditPlayer != null && result.weaponUsed() != null) {
            if (result.weaponUsed() == Material.SHEARS) {
                creditPlayer.getAchievementHandler().completeAchievement("bedwars.shear_luck");
            } else if (result.weaponUsed() == Material.STICK) {
                creditPlayer.getAchievementHandler().addProgressByTrigger("bedwars.knockback_void_kill", 1);
            }
        }
    }

}
