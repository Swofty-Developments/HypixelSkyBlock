package net.swofty.type.skyblockgeneric.commands;

import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "runhash",
        description = "Tests the bank hashing system",
        usage = "/runhash",
        permission = Rank.STAFF,
        allowsConsole = false)
public class TestBankHashCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            ProxyPlayer proxyPlayer = player.asProxyPlayer();

            player.sendMessage("§8Sending out request for bank hash...");
            proxyPlayer.getBankHash().thenAccept((hash) -> {
                player.sendMessage("§aBank hash: §6" + hash);
            });
        });
    }
}
