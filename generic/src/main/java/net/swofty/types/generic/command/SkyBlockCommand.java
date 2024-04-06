package net.swofty.types.generic.command;

import lombok.Getter;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
        this.command = new MinestomCommand(this);
    }

    public abstract void run(MinestomCommand command);

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
            super(command.getName(), command.getParams().aliases().split(" ").length == 0 ? null : command.getParams().aliases().split(" "));

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

            command.run(this);
        }
    }
}
