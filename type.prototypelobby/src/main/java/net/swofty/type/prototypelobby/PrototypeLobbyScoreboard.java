package net.swofty.type.prototypelobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.translation.Argument;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                Locale l = player.getLocale();
                HypixelDataHandler dataHandler = player.getDataHandler();
                PrototypeLobbyDataHandler prototypeDataHandler = PrototypeLobbyDataHandler.getUser(player);

                if (dataHandler == null || prototypeDataHandler == null) {
                    continue;
                }

                long hype = prototypeDataHandler.get(PrototypeLobbyDataHandler.Data.HYPE, DatapointLeaderboardLong.class).getValue();

                List<Component> lines = new ArrayList<>();
                lines.add(I18n.t("scoreboard.common.date_line", Argument.tagResolver(Formatter.date("date", LocalDateTime.now(ZoneId.systemDefault()))), Argument.string("id", HypixelConst.getServerName())));
                lines.add(Component.space());
                lines.add(I18n.t("scoreboard.prototype_lobby.dev_notice_line1"));
                lines.add(I18n.t("scoreboard.prototype_lobby.dev_notice_line2"));
                lines.add(Component.space());
                lines.add(I18n.t("scoreboard.prototype_lobby.bug_report_line1"));
                lines.add(I18n.t("scoreboard.prototype_lobby.bug_report_line2"));
                lines.add(I18n.t("scoreboard.prototype_lobby.bug_report_url"));
                lines.add(Component.space());
                lines.add(I18n.t("scoreboard.prototype_lobby.hype", Component.text(String.valueOf(hype))));
                lines.add(Component.space());
                lines.add(I18n.t("scoreboard.common.footer"));

                if (!scoreboard.hasScoreboard(player)) {
                    scoreboard.createScoreboard(player, Component.text(getSidebarName(prototypeName, l)));
                }

                scoreboard.updateLines(player, lines);
                scoreboard.updateTitle(player, Component.text(getSidebarName(prototypeName, l)));
            }
            return TaskSchedule.tick(4);
        });
    }

    public static void removeCache(Player player) {
        scoreboard.removeScoreboard(player);
    }

    private static String getSidebarName(int counter, Locale locale) {
        String baseText = I18n.string("scoreboard.prototype_lobby.title_base", locale);
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";

        if (counter > 0 && counter <= 8) {
            return colors[0] + baseText.substring(0, counter - 1) +
                    colors[1] + baseText.charAt(counter - 1) +
                    colors[2] + baseText.substring(counter) +
                    endColor;
        } else if ((counter >= 9 && counter <= 19) ||
                (counter >= 25 && counter <= 29)) {
            return colors[0] + baseText + endColor;
        } else {
            return colors[2] + baseText + endColor;
        }
    }
}
