package gg.itzkatze.thehypixelrecreationmod.commands;

import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.Comparator;
import java.util.List;

public final class GetScoreboardInfo {
    private GetScoreboardInfo() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            dispatcher.register(
                    ClientCommands.literal("getscoreboardinfo")
                            .executes(ctx -> {
                                Minecraft client = Minecraft.getInstance();
                                if (client.level == null || client.player == null) return 1;

                                Scoreboard scoreboard = client.level.getScoreboard();
                                Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
                                if (objective == null) {
                                    ChatUtils.warn("No sidebar scoreboard found.");
                                    return 1;
                                }

                                List<PlayerScoreEntry> entries = scoreboard
                                        .listPlayerScores(objective)
                                        .stream()
                                        .sorted(
                                                Comparator.comparingInt(PlayerScoreEntry::value).reversed()
                                        )
                                        .toList();

                                if (entries.isEmpty()) {
                                    ChatUtils.warn("Sidebar scoreboard is empty.");
                                    return 1;
                                }

                                ChatUtils.sendLine();

                                ChatUtils.send(Component.empty()
                                        .append(objective.getDisplayName())
                                        .withStyle(
                                                Style.EMPTY.withClickEvent(
                                                        new ClickEvent.CopyToClipboard(
                                                                StringUtility.toLegacyString(objective.getDisplayName())
                                                        )
                                                )
                                        ));

                                for (PlayerScoreEntry entry : entries) {
                                    Component line = renderEntry(scoreboard, entry);
                                    String legacy = StringUtility.toLegacyString(line);

                                    ChatUtils.send(Component.empty()
                                            .append(line)
                                            .withStyle(
                                                    Style.EMPTY.withClickEvent(
                                                            new ClickEvent.CopyToClipboard(legacy)
                                                    )
                                            ));
                                }

                                ChatUtils.sendLine();
                                return 1;
                            })
            );
        });
    }

    private static Component renderEntry(
            Scoreboard scoreboard,
            PlayerScoreEntry entry
    ) {
        Component base =
                entry.display() != null
                        ? entry.display()
                        : Component.literal(entry.owner());

        PlayerTeam team = scoreboard.getPlayersTeam(entry.owner());
        if (team != null) {
            return Component.empty()
                    .append(team.getPlayerPrefix())
                    .append(base)
                    .append(team.getPlayerSuffix());
        }

        return base;
    }
}
