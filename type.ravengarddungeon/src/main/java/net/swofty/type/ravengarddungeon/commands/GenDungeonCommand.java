package net.swofty.type.ravengarddungeon.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.ravengarddungeon.generator.RavengardDungeonGenerator;

import java.util.concurrent.ThreadLocalRandom;

@CommandParameters(
        labels = "gendungeon",
        description = "Generates a fresh dungeon layout from the room catalog",
        usage = "/gendungeon [seed] [rooms]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class GenDungeonCommand extends HypixelCommand {
    private static final int DEFAULT_ROOMS = 24;

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentNumber<Long> seedArg = ArgumentType.Long("seed");
        ArgumentNumber<Integer> roomsArg = ArgumentType.Integer("rooms").min(3).max(120);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            run((Player) sender, ThreadLocalRandom.current().nextLong(), DEFAULT_ROOMS);
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            run((Player) sender, context.get(seedArg), DEFAULT_ROOMS);
        }, seedArg);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            run((Player) sender, context.get(seedArg), context.get(roomsArg));
        }, seedArg, roomsArg);
    }

    private static void run(Player player, long seed, int rooms) {
        player.sendMessage("§7Generating dungeon with seed §f" + seed + "§7...");
        long started = System.currentTimeMillis();
        RavengardDungeonGenerator.GeneratedDungeon dungeon;
        try {
            dungeon = RavengardDungeonGenerator.generate(seed, rooms);
        } catch (Exception exception) {
            player.sendMessage("§cGeneration failed: " + exception.getMessage());
            return;
        }

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        RavengardDungeonGenerator.stamp(dungeon, instance).thenRun(() -> {
            long took = System.currentTimeMillis() - started;
            player.scheduler().scheduleNextTick(() -> {
                player.setInstance(instance, dungeon.spawn().withY(67));
                player.sendMessage("§aGenerated §f" + dungeon.layout().placements().size()
                        + "§a rooms (§f" + dungeon.objects().size() + "§a objects, §f"
                        + dungeon.layout().sealedSockets().size() + "§a sealed doors) in §f"
                        + took + "ms§a. Seed §f" + dungeon.seed() + "§a.");
                for (String line : dungeon.layout().toString().split("\n")) {
                    if (!line.isBlank()) {
                        player.sendMessage(net.kyori.adventure.text.Component.text(line)
                                .font(net.kyori.adventure.key.Key.key("minecraft", "uniform")));
                    }
                }
            });
        });
    }
}
