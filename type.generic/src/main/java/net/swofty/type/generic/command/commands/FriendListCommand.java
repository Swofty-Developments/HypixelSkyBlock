package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.friend.FriendManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(
        aliases = "fl friendlist",
        description = "List your friends",
        usage = "/fl [page]",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class FriendListCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString pageArg = ArgumentType.String("page");

        // No args -> page 1
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            FriendManager.listFriends(player, 1, false);
        });

        // With page/best
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            String arg = context.get(pageArg);

            if ("best".equalsIgnoreCase(arg)) {
                FriendManager.listFriends(player, 1, true);
                return;
            }

            int page = parsePageNumber(arg);
            FriendManager.listFriends(player, page, false);
        }, pageArg);
    }

    private int parsePageNumber(String arg) {
        try {
            return Math.max(1, Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}


