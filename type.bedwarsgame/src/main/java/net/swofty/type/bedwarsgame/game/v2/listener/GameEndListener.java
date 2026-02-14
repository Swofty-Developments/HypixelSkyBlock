package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ChatUtility;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.GameTeamWinConditionEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class GameEndListener implements HypixelEventClass {

    private static final String THICK_BAR = "§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEnd(GameTeamWinConditionEvent<BedWarsTeam> event) {
        String gameId = event.gameId();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(gameId);

        String titleMessage;
        String subtitleMessage;

        if (event.team() != null) {
            titleMessage = event.team().getColorCode() + "Team " + event.team().getName() + " has won!";
            subtitleMessage = "Congratulations!";
        } else {
            titleMessage = "§cGame Over!";
            subtitleMessage = "It's a draw!";
        }

        // Show results to all players
        for (BedWarsPlayer player : game.getPlayers()) {
            player.sendTitlePart(TitlePart.TITLE, Component.text(titleMessage));
            player.sendTitlePart(TitlePart.SUBTITLE, Component.text(subtitleMessage));
            player.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"),
                Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());

            // Record win
            if (event.team() != null && event.team().hasPlayer(player.getUuid())) {
                BedWarsStatsRecorder.recordWin(player, game.getGameType());
                player.getAchievementHandler().addProgressByTrigger("bedwars.wins", 1);
            }

            player.setGameMode(GameMode.ADVENTURE);
        }

        game.getReplayManager().stopRecording();

        game.getGeneratorManager().stopAllGenerators();
        game.getGameEventManager().stop();

        Logger.info("Ending game " + gameId);
        game.end();

        Collection<UUID> uuids = event.team().getPlayerIds();
        String playerNamesConcatenated = uuids.stream()
                .map(game::getPlayer)
                .filter(Optional::isPresent)
                .map(p -> LegacyComponentSerializer.legacySection().serialize(p.get().getColouredName()))
                .reduce((a, b) -> a + "§7," + b)
                .orElse("");

        for (BedWarsPlayer player : game.getPlayers()) {
            player.sendMessage(Component.text(THICK_BAR));
            player.sendMessage(Component.text(ChatUtility.FontInfo.center("§lBed Wars"), NamedTextColor.WHITE));
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text(ChatUtility.FontInfo.center(event.team().getColorCode() + event.team().getName() + " §7- " + playerNamesConcatenated), NamedTextColor.WHITE));
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text(ChatUtility.FontInfo.center("§e§l1st Killer §7- Username §7- 0")));
            player.sendMessage(Component.text(ChatUtility.FontInfo.center("§6§l2nd Killer §7- Username §7- 0")));
            player.sendMessage(Component.text(ChatUtility.FontInfo.center("§c§l3rd Killer §7- Username §7- 0")));
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text(THICK_BAR));
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (BedWarsPlayer player : game.getPlayers()) {
                player.sendMessage(Component.text(THICK_BAR));
                player.sendMessage(Component.text(ChatUtility.FontInfo.center("§lReward Summary"), NamedTextColor.WHITE));
                player.sendMessage(Component.empty());
                player.sendMessage(Component.text("   §7You earned:"));
                player.sendMessage(Component.text("    §f• §2" + player.getTokensThisGame() + " Bed Wars Tokens"));
                player.sendMessage(Component.text("    §f• §3" + player.getHypixelXpThisGame() + " Hypixel Experience"));
                player.sendMessage(Component.text("    §f• §20 Guild Experience"));
                player.sendMessage(Component.empty());
                player.sendMessage(Component.text(ChatUtility.FontInfo.center("Bed Wars XP"), NamedTextColor.AQUA));

                long currentLevel = player.getCurrentBedWarsLevel();
                player.sendMessage(Component.text(
                    "§f          §bLevel " + currentLevel + "                                     §bLevel " + (currentLevel + 1)
                ));

                long experience = player.getCurrentBedWarsExperience();
                int progress = BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience);
                int maxExperience = BedwarsLevelUtil.calculateMaxExperienceFromExperience(experience);

                double percentage = Math.min(1.0, (double) progress / maxExperience);
                int filledSquares = (int) Math.round(percentage * 34);
                StringBuilder progressBar = new StringBuilder("§8[");
                for (int i = 0; i < 34; i++) {
                    if (i < filledSquares) {
                        progressBar.append("§b■");
                    } else {
                        progressBar.append("§7■");
                    }
                }
                progressBar.append("§8]");
                player.sendMessage(Component.text("§f          " + progressBar));

                String prettyExperience = String.format("%,d", experience);
                String prettyMaxExperience = String.format("%,d", maxExperience);

                String percentageString = String.format("%.1f", percentage * 100);

                player.sendMessage(ChatUtility.FontInfo.center(
                    "§b" + prettyExperience + " §7/ §a" + prettyMaxExperience + " §7(" + percentageString + "%)"
                ));

                player.sendMessage(Component.empty());
                player.sendMessage("§7You earned §b" + player.getXpThisGame() + " §bBed Wars XP");
                player.sendMessage(Component.empty());
                // xp multipliers shown here
                player.sendMessage(Component.text(THICK_BAR));
            }
        }).delay(TaskSchedule.seconds(2)).schedule();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            game.getPlayers().forEach(p -> p.sendTo(ServerType.BEDWARS_LOBBY));
            game.dispose();
        }).delay(TaskSchedule.seconds(7)).schedule();
    }

}
