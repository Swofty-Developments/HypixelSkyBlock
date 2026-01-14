package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@CommandParameters(
        description = "Changes chat mode",
        usage = "/chat <channel>",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class ChatCommand extends HypixelCommand {

    private static final ConcurrentHashMap<UUID, Boolean> staffView = new ConcurrentHashMap<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord channelArg = ArgumentType.Word("channel");

        channelArg.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof HypixelPlayer player)) return;

            for (String channel : getValidChannels(player)) {
                suggestion.addEntry(new SuggestionEntry(channel));
            }

            // staff-only toggle (not a channel)
            if (player.getRank().isStaff()) {
                suggestion.addEntry(new SuggestionEntry("staffview"));
                suggestion.addEntry(new SuggestionEntry("sv"));
            }
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String raw = context.get(channelArg).toLowerCase(Locale.ROOT);

            // staffview toggle
            if (raw.equals("staffview") || raw.equals("sv")) {
                if (!player.getRank().isStaff()) {
                    sendInvalidUsage(player);
                    return;
                }

                boolean enabled = staffView.getOrDefault(player.getUuid(), true);
                staffView.put(player.getUuid(), !enabled);
                sender.sendMessage("§aStaff chat viewing is now " +
                        (!enabled ? "§aenabled" : "§cdisabled"));
                return;
            }

            PickerChatType type = PickerChatType.fromString(raw);

            if (type == null || !isAllowed(player, type)) {
                sendInvalidUsage(player);
                return;
            }

            player.getChatType().switchTo(type.chatType);
            sender.sendMessage("§aYou are now in the §6" +
                    type.chatType.name().toLowerCase(Locale.ROOT) +
                    " §achannel");
        }, channelArg);
    }

    /* ---------------- helpers ---------------- */

    private static void sendInvalidUsage(HypixelPlayer player) {
        player.sendMessage("§cInvalid usage! Correct usage: §e/chat <channel>");
        player.sendMessage("§cValid channels: §e" + String.join(", ", getValidChannels(player)));
    }

    private static List<String> getValidChannels(HypixelPlayer player) {
        List<String> channels = new ArrayList<>();
        channels.add("all");
        channels.add("party");

        if (player.getRank().isStaff()) {
            channels.add("staff");
        }

        return channels;
    }

    private static boolean isAllowed(HypixelPlayer player, PickerChatType type) {
        if (type.chatType == DatapointChatType.Chats.STAFF) {
            return player.getRank().isStaff();
        }
        return true;
    }

    /* ---------------- picker ---------------- */

    enum PickerChatType {
        a(DatapointChatType.Chats.ALL),
        all(DatapointChatType.Chats.ALL),

        p(DatapointChatType.Chats.PARTY),
        party(DatapointChatType.Chats.PARTY),

        s(DatapointChatType.Chats.STAFF),
        staff(DatapointChatType.Chats.STAFF);

        final DatapointChatType.Chats chatType;

        PickerChatType(DatapointChatType.Chats chatType) {
            this.chatType = chatType;
        }

        static PickerChatType fromString(String input) {
            for (PickerChatType val : values()) {
                if (val.name().equalsIgnoreCase(input)) return val;
            }
            return null;
        }
    }

    public static boolean isStaffViewEnabled(UUID uuid) {
        return staffView.getOrDefault(uuid, true);
    }
}
