package net.swofty.type.generic.chat;

import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.commands.ChatCommand;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class StaffChat {
    private StaffChat() {}

    public static void sendMessage(HypixelPlayer sender, String message) {
        String formatted = "§b[STAFF] " + sender.getRank().getPrefix() + sender.getUsername() + "§f: " + message;
        broadcast(formatted, sender.getUuid());
    }

    public static void sendNotification(String message) {
        String formatted = "§b[STAFF] §7" + message;
        broadcast(formatted, null);
    }

    private static void broadcast(String message, UUID senderUuid) {
        List<HypixelPlayer> viewers = HypixelGenericLoader.getLoadedPlayers().stream()
                .filter(player -> player.getRank().isStaff())
                .filter(player -> ChatCommand.isStaffViewEnabled(player.getUuid()) || (senderUuid != null && player.getUuid().equals(senderUuid)))
                .collect(Collectors.toList());

        for (HypixelPlayer viewer : viewers) {
            viewer.sendMessage(message);
        }
    }
}

