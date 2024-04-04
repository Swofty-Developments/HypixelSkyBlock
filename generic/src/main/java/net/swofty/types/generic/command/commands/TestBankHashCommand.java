package net.swofty.types.generic.command.commands;

import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "runhash",
        description = "Tests the bank hashing system",
        usage = "/runhash",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class TestBankHashCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
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
