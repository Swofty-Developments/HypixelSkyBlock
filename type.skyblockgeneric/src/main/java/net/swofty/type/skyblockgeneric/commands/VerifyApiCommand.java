package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.api.APIAuthenticateCodeProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "allowaccessforapiauthentication",
        description = "Allows access for API authentication for the given code",
        usage = "/allowaccessforapiauthentication <code>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class VerifyApiCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString code = new ArgumentString("code");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§7Checking if API is online...");

            Thread.startVirtualThread(() -> {
                if (!(new ProxyService(ServiceType.API).isOnline().join())) {
                    sender.sendMessage("§cAPI is offline.");
                    return;
                }

                sender.sendMessage("§7API is online, checking if code is valid...");
                String codeString = context.get(code);

                SkyBlockPlayer player = (SkyBlockPlayer) sender;
                ProxyService apiService = new ProxyService(ServiceType.API);
                apiService.handleRequest(new APIAuthenticateCodeProtocolObject.AuthenticateCodeMessage(
                        codeString,
                        player.getUsername(),
                        player.getUuid()
                )).thenAccept(nonCastedResponse -> {
                    APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse response = (APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse) nonCastedResponse;
                    if (response.successful) {
                        sender.sendMessage("§aCode '" + codeString + "' has successfully been verified! Check your web browser.");
                    } else {
                        sender.sendMessage("§cInvalid code!");
                    }
                });
            });
        }, code);
    }
}
