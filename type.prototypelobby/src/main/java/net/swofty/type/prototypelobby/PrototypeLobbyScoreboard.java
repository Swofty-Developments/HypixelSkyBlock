package net.swofty.type.prototypelobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.PrototypeLobbyDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrototypeLobbyScoreboard {
    private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
    private static Integer prototypeName = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            prototypeName++;
            if (prototypeName > 50) {
                prototypeName = 0;
            }

            for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
                HypixelDataHandler dataHandler = player.getDataHandler();
                PrototypeLobbyDataHandler prototypeDataHandler = PrototypeLobbyDataHandler.getUser(player);

                if (dataHandler == null || prototypeDataHandler == null) {
                    continue;
                }

                long hype = prototypeDataHandler.get(PrototypeLobbyDataHandler.Data.HYPE, DatapointLeaderboardLong.class).getValue();

                List<Component> lines = new ArrayList<>();
                lines.add(HypixelScoreboard.legacy("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName()));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.dev_notice_line1")));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.dev_notice_line2")));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.bug_report_line1")));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.bug_report_line2")));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.bug_report_url")));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.prototype_lobby.hype_label") + hype + I18n.string("scoreboard.prototype_lobby.hype_max")));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.common.footer")));

                if (!scoreboard.hasScoreboard(player)) {
                    scoreboard.createScoreboard(player, getSidebarName(prototypeName));
                }

                scoreboard.updateLines(player, lines);
                scoreboard.updateTitle(player, getSidebarName(prototypeName));
            }
            return TaskSchedule.tick(4);
        });
    }

    public static void removeCache(Player player) {
        scoreboard.removeScoreboard(player);
    }

    private static Component getSidebarName(int counter) {
        return HypixelScoreboard.animatedSidebarName(
            I18n.string("scoreboard.prototype_lobby.title_base"),
            counter,
            NamedTextColor.WHITE,
            NamedTextColor.GOLD,
            NamedTextColor.YELLOW,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            8,
            9,
            19,
            25,
            29,
            Component.text("LOBBY", NamedTextColor.GREEN, net.kyori.adventure.text.format.TextDecoration.BOLD)
        );
    }
}
