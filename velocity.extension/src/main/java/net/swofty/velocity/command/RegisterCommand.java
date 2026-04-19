package net.swofty.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.data.AuthenticationDatabase;

public class RegisterCommand implements SimpleCommand {

    @Override
    public boolean hasPermission(Invocation invocation) {
        return ConfigProvider.settings().isRequireAuth();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            return;
        }

        if (!ConfigProvider.settings().isRequireAuth()) return;
        if (!SkyBlockVelocity.getUnauthenticated().contains(player.getUniqueId())) return;

        AuthenticationDatabase.AuthenticationData data = new AuthenticationDatabase(player.getUniqueId()).getAuthenticationData();
        if (data != null) {
            player.sendPlainMessage("§cYou have already registered your account!");
        }

        String[] args = invocation.arguments();
        if (args.length != 2) {
            player.sendPlainMessage("§cYou must first register to play this server!");
            player.sendPlainMessage("§cIn the Minecraft chat, type §6/register <password> <password>§c.");
            return;
        }

        if (!args[0].equals(args[1])) {
            player.sendPlainMessage("§cYour passwords do not match.");
            return;
        }

        AuthenticationDatabase.AuthenticationData newData = AuthenticationDatabase.makeFromPassword(args[1]);
        new AuthenticationDatabase(player.getUniqueId()).setAuthenticationData(newData);

        player.sendPlainMessage("§aYou have successfully registered your account!");
        player.sendPlainMessage("§aNow, in the Minecraft chat, type §6/login <password>§a.");
    }
}
