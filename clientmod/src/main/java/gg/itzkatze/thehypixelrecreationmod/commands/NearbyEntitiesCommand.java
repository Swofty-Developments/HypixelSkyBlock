package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public final class NearbyEntitiesCommand {
    private static final double DEFAULT_RADIUS = 16;

    private NearbyEntitiesCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> RecreationCommand.register(
            dispatcher,
            ClientCommands.literal("nearbyentities")
                .executes(context -> execute(context.getSource(), DEFAULT_RADIUS))
                .then(ClientCommands.argument("radius", DoubleArgumentType.doubleArg(0))
                    .executes(context -> execute(
                        context.getSource(),
                        DoubleArgumentType.getDouble(context, "radius")
                    )))
        ));
    }

    private static int execute(FabricClientCommandSource source, double radius) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            source.sendFeedback(Component.literal("§cNo world is currently loaded."));
            return 0;
        }

        double radiusSquared = radius * radius;
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : client.level.entitiesForRendering()) {
            if (entity != client.player && entity.distanceToSqr(client.player) <= radiusSquared) {
                entities.add(entity);
            }
        }
        entities.sort(Comparator.comparingDouble(entity -> entity.distanceToSqr(client.player)));

        source.sendFeedback(Component.literal("§eNearby entities within " + radius + " blocks: " + entities.size()));
        for (Entity entity : entities) {
            source.sendFeedback(Component.literal(
                "§7- " + EntityType.getKey(entity.getType())
                    + " §8[" + entity.getUUID() + "] §7at "
                    + format(entity.getX()) + ", "
                    + format(entity.getY()) + ", "
                    + format(entity.getZ())
            ));
        }
        return 1;
    }

    private static String format(double coordinate) {
        return String.format(java.util.Locale.ROOT, "%.2f", coordinate);
    }
}
