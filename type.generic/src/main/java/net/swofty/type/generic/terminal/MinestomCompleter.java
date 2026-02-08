package net.swofty.type.generic.terminal;

import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public final class MinestomCompleter implements Completer {

    private final CommandManager commandManager;

    public MinestomCompleter(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String buffer = line.line();
        String[] args = buffer.split(" ", -1);

        if (args.length <= 1) {
            String prefix = args[0];
            for (Command command : commandManager.getCommands()) {
                if (command.getName().startsWith(prefix)) {
                    candidates.add(new Candidate(command.getName()));
                }
            }
            return;
        }

        String commandName = args[0];
        Command command = commandManager.getCommand(commandName);
        if (command == null) return;

        List<Command> subCommands = command.getSubcommands();
        String lastArg = args[args.length - 1];
        for (Command subCommand : subCommands) {
            if (subCommand.getName().startsWith(lastArg)) {
                candidates.add(new Candidate(subCommand.getName()));
            }
        }
    }
}
