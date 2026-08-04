package net.swofty.type.ravengardgeneric.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.world.HypixelWorldLoader;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@CommandParameters(
        labels = "switch",
        description = "Switches between captured polar worlds",
        usage = "/switch <world>",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class SwitchCommand extends HypixelCommand {
    private static final Path CAPTURES = Path.of("configuration/world/captures");
    private static final String EXTENSION = ".polar";

    // captures are several MB each, so a world is read once and reused on later switches
    private static final Map<String, Instance> loaded = new ConcurrentHashMap<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        var worldArg = ArgumentType.Word("world").setSuggestionCallback((sender, context, suggestion) -> {
            for (String name : available()) {
                suggestion.addEntry(new SuggestionEntry(name));
            }
        });

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String name = context.get(worldArg);

            if (!available().contains(name)) {
                player.sendMessage("§cNo capture named §f" + name + "§c. Available: §f"
                        + String.join("§7, §f", available()));
                return;
            }

            Instance instance = loaded.get(name);
            if (instance == null) {
                player.sendMessage("§7Loading §f" + name + "§7...");
                try {
                    InstanceContainer source = MinecraftServer.getInstanceManager().createInstanceContainer();
                    instance = HypixelWorldLoader.loadFrom(CAPTURES.resolve(name + EXTENSION),
                            source, MinecraftServer.getInstanceManager());
                    loaded.put(name, instance);
                } catch (IOException exception) {
                    Logger.error(exception, "Failed to load capture {}", name);
                    player.sendMessage("§cFailed to load §f" + name + "§c: " + exception.getMessage());
                    return;
                }
            }

            if (player.getInstance() == instance) {
                player.sendMessage("§7You are already in §f" + name + "§7.");
                return;
            }

            player.setInstance(instance, player.getPosition()).thenRun(() ->
                    player.sendMessage("§aSwitched to §f" + name + "§a."));
        }, worldArg);

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            List<String> names = available();
            if (names.isEmpty()) {
                player.sendMessage("§cNo captures found in " + CAPTURES + ".");
                return;
            }
            player.sendMessage("§7Captures: §f" + String.join("§7, §f", names));
        });
    }

    private static List<String> available() {
        if (!Files.isDirectory(CAPTURES)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(CAPTURES)) {
            List<String> names = new ArrayList<>();
            files.filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        String file = path.getFileName().toString();
                        names.add(file.substring(0, file.length() - EXTENSION.length()));
                    });
            names.sort(String::compareTo);
            return names;
        } catch (IOException exception) {
            Logger.error(exception, "Failed to list captures in {}", CAPTURES);
            return List.of();
        }
    }
}
