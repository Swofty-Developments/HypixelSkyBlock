package net.swofty.type.generic.command;

import lombok.Getter;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class HypixelCommand {
    public static final String COMMAND_SUFFIX = "Command";

    @Getter
    private final CommandParameters params;
    @Getter
    private final String name;
    @Getter
    private final MinestomCommand command;

    protected HypixelCommand() {
        this.params = this.getClass().getAnnotation(CommandParameters.class);
        this.name = this.getClass().getSimpleName().replace(COMMAND_SUFFIX, "").toLowerCase();

        List<String> aliases = new ArrayList<>();
        if (params.aliases() != null && !params.aliases().trim().isEmpty()) {
            aliases.addAll(Arrays.asList(params.aliases().split(" ")));
        }

        if (aliases.isEmpty()) {
            this.command = new MinestomCommand(this);
        } else {
            this.command = new MinestomCommand(this, aliases.toArray(new String[0]));
        }
    }

    public abstract void registerUsage(MinestomCommand command);

    public boolean permissionCheck(CommandSender sender) {
        HypixelPlayer player = (HypixelPlayer) sender;
        HypixelDataHandler dataHandler = player.getDataHandler();
        boolean passes = dataHandler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(params.permission());

        if (!passes) {
            player.sendMessage("§cYou do not have permission to use this command.");
        }

        return passes;
    }

    public static class MinestomCommand extends Command {

        public MinestomCommand(HypixelCommand command) {
            super(command.getName());

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("§cUsage: " + command.getParams().usage());
            });

            setCondition((commandSender, string) -> {
                if (commandSender instanceof ConsoleSender) {
                    return command.getParams().allowsConsole();
                }

                HypixelPlayer player = (HypixelPlayer) commandSender;
                HypixelDataHandler dataHandler = player.getDataHandler();

                return dataHandler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(command.getParams().permission());
            });

            command.registerUsage(this);
        }

        public MinestomCommand(HypixelCommand command, String... aliases) {
            super(command.getName(), aliases);

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("§cUsage: " + command.getParams().usage());
            });

            setCondition((commandSender, string) -> {
                if (commandSender instanceof ConsoleSender) {
                    return command.getParams().allowsConsole();
                }

                HypixelPlayer player = (HypixelPlayer) commandSender;
                HypixelDataHandler dataHandler = player.getDataHandler();

                return dataHandler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(command.getParams().permission());
            });

            command.registerUsage(this);
        }
    }
}
