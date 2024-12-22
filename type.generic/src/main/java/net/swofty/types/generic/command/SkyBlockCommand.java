package net.swofty.types.generic.command;

import lombok.Getter;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class SkyBlockCommand {
    public static final String COMMAND_SUFFIX = "Command";

    @Getter
    private final CommandParameters params;
    @Getter
    private final String name;
    @Getter
    private final MinestomCommand command;

    protected SkyBlockCommand() {
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
        SkyBlockPlayer player = (SkyBlockPlayer) sender;
        DataHandler dataHandler = player.getDataHandler();
        if (!player.hasAuthenticated) return false;
        boolean passes = dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(params.permission());

        if (!passes) {
            player.sendMessage("§cYou do not have permission to use this command.");
        }

        return passes;
    }

    public static class MinestomCommand extends Command {

        public MinestomCommand(SkyBlockCommand command) {
            super(command.getName());

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("§cUsage: " + command.getParams().usage());
            });

            setCondition((commandSender, string) -> {
                if (commandSender.isConsole()) {
                    return command.getParams().allowsConsole();
                }

                SkyBlockPlayer player = (SkyBlockPlayer) commandSender;
                DataHandler dataHandler = player.getDataHandler();

                if (!player.hasAuthenticated) return false;
                return dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(command.getParams().permission());
            });

            command.registerUsage(this);
        }

        public MinestomCommand(SkyBlockCommand command, String... aliases) {
            super(command.getName(), aliases);

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("§cUsage: " + command.getParams().usage());
            });

            setCondition((commandSender, string) -> {
                if (commandSender.isConsole()) {
                    return command.getParams().allowsConsole();
                }

                SkyBlockPlayer player = (SkyBlockPlayer) commandSender;
                DataHandler dataHandler = player.getDataHandler();

                if (!player.hasAuthenticated) return false;
                return dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(command.getParams().permission());
            });

            command.registerUsage(this);
        }
    }
}
