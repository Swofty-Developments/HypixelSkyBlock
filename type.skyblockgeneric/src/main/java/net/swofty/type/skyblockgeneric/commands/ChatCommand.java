package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Changes chat mode",
        usage = "/chat <type>",
        permission = Rank.DEFAULT,
        aliases = "chatmode",
        allowsConsole = false)
public class ChatCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<PickerChatType> chatType = ArgumentType.Enum("type", PickerChatType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            PickerChatType type = context.get(chatType);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getChatType().switchTo(type.getChatType());
            sender.sendMessage("§aYou are now in the §6" + type.chatType.name() + " §achannel");
        }, chatType);
    }

    enum PickerChatType {
        p(DatapointChatType.Chats.PARTY),
        party(DatapointChatType.Chats.PARTY),
        a(DatapointChatType.Chats.ALL),
        all(DatapointChatType.Chats.ALL);

        private final DatapointChatType.Chats chatType;

        PickerChatType(DatapointChatType.Chats chatType) {
            this.chatType = chatType;
        }

        public DatapointChatType.Chats getChatType() {
            return chatType;
        }
    }
}
