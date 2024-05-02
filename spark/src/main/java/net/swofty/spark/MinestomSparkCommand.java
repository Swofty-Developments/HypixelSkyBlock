package net.swofty.spark;

import me.lucko.spark.common.SparkPlatform;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * From <a href="https://github.com/lucko/spark/blob/891914f313ea5742e71b6a42135f5451e99ca516/spark-minestom/src/main/java/me/lucko/spark/minestom/MinestomSparkPlugin.java">lucko/spark</a>.
 */
final class MinestomSparkCommand extends Command implements CommandExecutor, SuggestionCallback {
    private final SparkPlatform platform;

    MinestomSparkCommand(SparkPlatform platform) {
        super("spark");
        this.platform = platform;

        ArgumentStringArray arrayArgument = ArgumentType.StringArray("args");
        arrayArgument.setSuggestionCallback(this);

        addSyntax(this, arrayArgument);
        setDefaultExecutor((sender, context) -> platform.executeCommand(new MinestomCommandSender(sender), new String[0]));
    }

    // execute
    @Override
    public void apply(@NotNull CommandSender sender, @NotNull CommandContext context) {
        String[] args = processArgs(context, false);
        if (args == null) {
            return;
        }

        this.platform.executeCommand(new MinestomCommandSender(sender), args);
    }

    // tab complete
    @Override
    public void apply(@NotNull CommandSender sender, @NotNull CommandContext context, @NotNull Suggestion suggestion) {
        String[] args = processArgs(context, true);
        if (args == null) {
            return;
        }

        Iterable<String> suggestionEntries = this.platform.tabCompleteCommand(new MinestomCommandSender(sender), args);
        for (String suggestionEntry : suggestionEntries) {
            suggestion.addEntry(new SuggestionEntry(suggestionEntry));
        }
    }


    private static String [] processArgs(CommandContext context, boolean tabComplete) {
        String[] split = context.getInput().split(" ", tabComplete ? -1 : 0);
        if (split.length == 0 || !split[0].equals("/spark") && !split[0].equals("spark")) {
            return null;
        }

        return Arrays.copyOfRange(split, 1, split.length);
    }
}