package net.swofty.commons.skyblock.command;

import lombok.Getter;
import net.minestom.server.command.builder.Command;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointRank;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

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

                return dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(command.getParams().permission());
            });

            command.run(this);
        }
    }
}
