package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLocale;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.Locale;

@CommandParameters(labels = "localedata",
    description = "Locale Data",
    usage = "/localedata",
    permission = Rank.STAFF,
    allowsConsole = false)
public class LocaleDataCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;

            if (!(sender instanceof HypixelPlayer player)) return;

            player.sendMessage("§eYour locale is currently set to §c" + player.getLocale().getDisplayName() + "§e.");
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            if (!(sender instanceof HypixelPlayer player)) return;

            HypixelDataHandler handler = player.getDataHandler();

            Locale loc = handler.get(HypixelDataHandler.Data.LOCALE, DatapointLocale.class).getValue().getCurrentLocale().getLocale();
            player.setLocale(loc);

            player.sendMessage("§eYour locale is now set to §c" + loc.getDisplayName() + "§e.");
        }, ArgumentType.Literal("update"));
    }
}
