package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.DragonEntity;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CommandParameters(description = "Dragon management commands",
        usage = "/dragon <spawn|follow|stop|remove>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class DragonCommand extends HypixelCommand {
    private static final Map<UUID, DragonEntity> playerDragons = new ConcurrentHashMap<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString subcommand = ArgumentType.String("subcommand");
        ArgumentDouble speedArg = ArgumentType.Double("speed");

        command.addSyntax((sender, context) -> {
            showHelp((HypixelPlayer) sender);
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            String sub = context.get(subcommand);

            switch (sub.toLowerCase()) {
                case "spawn" -> spawnDragon(player, 0.8);
                case "follow" -> followPlayer(player, 0.8);
                case "stop" -> stopFollowing(player);
                case "remove" -> removeDragon(player);
                default -> showHelp(player);
            }
        }, subcommand);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            String sub = context.get(subcommand);
            double speed = context.get(speedArg);

            switch (sub.toLowerCase()) {
                case "spawn" -> spawnDragon(player, speed);
                case "follow" -> followPlayer(player, speed);
                default -> showHelp(player);
            }
        }, subcommand, speedArg);
    }

    private void spawnDragon(HypixelPlayer player, double speed) {
        DragonEntity existing = playerDragons.get(player.getUuid());
        if (existing != null && !existing.isDead()) {
            player.sendMessage("§cYou already have a dragon spawned! Use /dragon remove first.");
            return;
        }

        DragonEntity dragon = new DragonEntity();
        dragon.setInstance(player.getInstance(), player.getPosition().add(0, 5, 0));
        playerDragons.put(player.getUuid(), dragon);

        player.sendMessage("§aDragon spawned!");
    }

    private void followPlayer(HypixelPlayer player, double speed) {
        DragonEntity dragon = playerDragons.get(player.getUuid());
        if (dragon == null || dragon.isDead()) {
            player.sendMessage("§cYou don't have a dragon! Use /dragon spawn first.");
            return;
        }

        dragon.followPlayer(player, speed);
        player.sendMessage("§aDragon is now following you at speed " + speed + "!");
    }

    private void stopFollowing(HypixelPlayer player) {
        DragonEntity dragon = playerDragons.get(player.getUuid());
        if (dragon == null || dragon.isDead()) {
            player.sendMessage("§cYou don't have a dragon!");
            return;
        }

        dragon.clearTarget();
        player.sendMessage("§aDragon stopped following.");
    }

    private void removeDragon(HypixelPlayer player) {
        DragonEntity dragon = playerDragons.remove(player.getUuid());
        if (dragon == null) {
            player.sendMessage("§cYou don't have a dragon!");
            return;
        }

        if (!dragon.isDead()) {
            dragon.remove();
        }
        player.sendMessage("§aDragon removed!");
    }

    private void showHelp(HypixelPlayer player) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6Dragon Commands");
        player.sendMessage("§e/dragon spawn [speed] §8- §7Spawn a dragon at your location");
        player.sendMessage("§e/dragon follow [speed] §8- §7Make the dragon follow you");
        player.sendMessage("§e/dragon stop §8- §7Stop the dragon from following");
        player.sendMessage("§e/dragon remove §8- §7Remove your dragon");
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    public static void cleanupDragon(UUID playerUuid) {
        DragonEntity dragon = playerDragons.remove(playerUuid);
        if (dragon != null && !dragon.isDead()) {
            dragon.remove();
        }
    }
}
