package gg.itzkatze.thehypixelrecreationmod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public final class ChatUtils {
    private static final Style LINE_STYLE = Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0xFFD700));
    private static final Style LOG_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(0x808080));
    private static final Style ERROR_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000));
    private static final Style WARN_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFF00));

    private ChatUtils() {
    }

    public static void sendLine() {
        send(Component.literal("------------------------------------").withStyle(LINE_STYLE));
    }

    public static void message(String message) {
        send(Component.literal(message));
    }

    public static void log(String message) {
        send(Component.literal(message).withStyle(LOG_STYLE));
    }

    public static void error(String message) {
        send(Component.literal(message).withStyle(ERROR_STYLE));
    }

    public static void warn(String message) {
        send(Component.literal(message).withStyle(WARN_STYLE));
    }

    public static void send(Component component) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            return;
        }

        client.player.sendSystemMessage(component);
    }
}
