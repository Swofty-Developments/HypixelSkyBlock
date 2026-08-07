package net.swofty.type.ravengardgeneric.hud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.DisplayScoreboardPacket;
import net.minestom.server.network.packet.server.play.ScoreboardObjectivePacket;
import net.minestom.server.network.packet.server.play.UpdateScorePacket;
import net.minestom.server.scoreboard.Sidebar;
import net.swofty.type.generic.user.HypixelPlayer;

public final class RavengardHealthObjective {
    public static final String OBJECTIVE = "health";
    private static final int SEGMENTS = 10;
    private static final char FILLED = '■';
    private static final byte CREATE = 0;
    private static final byte UPDATE = 2;
    private static final byte BELOW_NAME_SLOT = 2;

    private RavengardHealthObjective() {
    }

    public static void create(HypixelPlayer player) {
        player.sendPacket(new ScoreboardObjectivePacket(
                OBJECTIVE,
                CREATE,
                Component.empty(),
                ScoreboardObjectivePacket.Type.INTEGER,
                Sidebar.NumberFormat.blank()));
        player.sendPacket(new DisplayScoreboardPacket(BELOW_NAME_SLOT, OBJECTIVE));
    }

    public static void update(HypixelPlayer viewer, String owner, int health, int maxHealth) {
        viewer.sendPacket(new UpdateScorePacket(
                owner,
                OBJECTIVE,
                health,
                null,
                bar(health, maxHealth)));
    }

    private static Sidebar.NumberFormat bar(int health, int maxHealth) {
        int filled = maxHealth <= 0 ? 0
                : Math.max(0, Math.min(SEGMENTS, Math.round((float) health / maxHealth * SEGMENTS)));

        Component component = Component.text(String.valueOf(FILLED).repeat(filled), NamedTextColor.RED)
                .append(Component.text(String.valueOf(FILLED).repeat(SEGMENTS - filled), NamedTextColor.DARK_GRAY));

        return Sidebar.NumberFormat.fixed(component);
    }
}
