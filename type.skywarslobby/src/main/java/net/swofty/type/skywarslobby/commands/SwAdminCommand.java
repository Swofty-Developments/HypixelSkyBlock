package net.swofty.type.skywarslobby.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(
        aliases = "skywarsadmin",
        description = "Admin command for managing SkyWars currencies",
        usage = "/swadmin <give|set> <coins|souls|tokens> <amount>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class SwAdminCommand extends HypixelCommand {

    public enum CurrencyType {
        COINS,
        SOULS,
        TOKENS
    }

    public enum ActionType {
        GIVE,
        SET
    }

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ActionType> actionArgument = ArgumentType.Enum("action", ActionType.class)
                .setFormat(ArgumentEnum.Format.LOWER_CASED);
        ArgumentEnum<CurrencyType> currencyArgument = ArgumentType.Enum("currency", CurrencyType.class)
                .setFormat(ArgumentEnum.Format.LOWER_CASED);
        ArgumentNumber<Long> amountArgument = ArgumentType.Long("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            ActionType action = context.get(actionArgument);
            CurrencyType currency = context.get(currencyArgument);
            long amount = context.get(amountArgument);

            SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
            if (handler == null) {
                player.sendMessage("§cCould not find your SkyWars data. Please try again.");
                return;
            }

            SkywarsDataHandler.Data dataType = switch (currency) {
                case COINS -> SkywarsDataHandler.Data.COINS;
                case SOULS -> SkywarsDataHandler.Data.SOULS;
                case TOKENS -> SkywarsDataHandler.Data.TOKENS;
            };

            DatapointLong datapoint = handler.get(dataType, DatapointLong.class);
            long currentValue = datapoint.getValue();
            long newValue;

            switch (action) {
                case GIVE -> {
                    newValue = currentValue + amount;
                    datapoint.setValue(newValue);
                    player.sendMessage("§aAdded §e" + amount + " " + currency.name().toLowerCase() +
                            "§a! New balance: §e" + newValue);
                }
                case SET -> {
                    newValue = amount;
                    datapoint.setValue(newValue);
                    player.sendMessage("§aSet your " + currency.name().toLowerCase() +
                            " to §e" + newValue + "§a! (was: §7" + currentValue + "§a)");
                }
            }
        }, actionArgument, currencyArgument, amountArgument);

        // Default syntax showing usage
        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
            if (handler == null) {
                player.sendMessage("§cCould not find your SkyWars data. Please try again.");
                return;
            }

            long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
            long souls = handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
            long tokens = handler.get(SkywarsDataHandler.Data.TOKENS, DatapointLong.class).getValue();

            player.sendMessage("§6§lSkyWars Admin Commands");
            player.sendMessage("§7Current balances:");
            player.sendMessage("  §eCoins: §f" + coins);
            player.sendMessage("  §dSouls: §f" + souls);
            player.sendMessage("  §bTokens: §f" + tokens);
            player.sendMessage("");
            player.sendMessage("§7Usage:");
            player.sendMessage("  §e/swadmin give <coins|souls|tokens> <amount>");
            player.sendMessage("  §e/swadmin set <coins|souls|tokens> <amount>");
        });
    }
}
