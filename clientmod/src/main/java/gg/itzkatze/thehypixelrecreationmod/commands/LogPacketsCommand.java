package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.context.CommandContext;
import gg.itzkatze.thehypixelrecreationmod.features.packetlog.EntityPacketLogger;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

public final class LogPacketsCommand {
    private static final double TARGET_RANGE = 64.0;
    private static final double RAY_TOLERANCE = 1.0;

    private LogPacketsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("logpackets").executes(LogPacketsCommand::toggle)));
    }

    private static int toggle(CommandContext<FabricClientCommandSource> context) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player == null || client.level == null) {
            context.getSource().sendFeedback(Component.literal("§cNot in a world."));
            return 0;
        }

        Entity target = getLookedAtEntity(client, player);

        if (EntityPacketLogger.isActive()) {
            boolean sameEntity = target == null || EntityPacketLogger.isTracking(target);
            EntityPacketLogger.StopResult result = EntityPacketLogger.stop();
            context.getSource().sendFeedback(Component.literal(
                    "§aStopped logging "
                            + result.entityName()
                            + " §a(id "
                            + result.entityId()
                            + ") after "
                            + result.packetCount()
                            + " packets → §f"
                            + result.path()
            ));

            if (sameEntity) {
                return 1;
            }
        }

        if (target == null) {
            context.getSource().sendFeedback(Component.literal("§cNot looking at any entity."));
            return 0;
        }

        try {
            EntityPacketLogger.StartResult result = EntityPacketLogger.start(target);
            context.getSource().sendFeedback(Component.literal(
                    "§aLogging packets for "
                            + result.entityName()
                            + " §a(id "
                            + result.entityId()
                            + ", "
                            + result.trackedEntityCount()
                            + " associated entities) → §f"
                            + result.path()
            ));
            return 1;
        } catch (IOException | RuntimeException exception) {
            context.getSource().sendFeedback(Component.literal("§cFailed to start packet log: " + exception.getMessage()));
            return 0;
        }
    }

    private static Entity getLookedAtEntity(Minecraft client, Player player) {
        Vec3 eyePosition = player.getEyePosition(1.0f);
        Vec3 lookVector = player.getLookAngle();
        Vec3 reachVector = eyePosition.add(lookVector.scale(TARGET_RANGE));
        AABB searchBox = player.getBoundingBox().expandTowards(lookVector.scale(TARGET_RANGE)).inflate(1.0);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                player,
                eyePosition,
                reachVector,
                searchBox,
                entity -> entity != player,
                TARGET_RANGE * TARGET_RANGE
        );
        if (hitResult != null) {
            return hitResult.getEntity();
        }

        Entity closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Entity entity : client.level.getEntities(player, searchBox)) {
            Vec3 offset = entity.getBoundingBox().getCenter().subtract(eyePosition);
            double distanceAlongRay = offset.dot(lookVector);
            if (distanceAlongRay <= 0 || distanceAlongRay > TARGET_RANGE) {
                continue;
            }

            double distanceFromRay = offset.subtract(lookVector.scale(distanceAlongRay)).length();
            if (distanceFromRay > RAY_TOLERANCE || distanceAlongRay >= closestDistance) {
                continue;
            }

            closestDistance = distanceAlongRay;
            closest = entity;
        }
        return closest;
    }
}
