package net.swofty.anticheat.engine;

import net.swofty.anticheat.event.AntiCheatListener;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.packet.*;
import net.swofty.anticheat.math.Pos;

public class MovementEvents extends AntiCheatListener {
    @ListenerMethod
    public void onPacketReceive(AnticheatPacketEvent event) {
        SwoftyPacket packet = event.getPacket();
        SwoftyPlayer player = packet.getPlayer();

        Pos pos = player.getCurrentTick().getPos();
        boolean onGround = player.getCurrentTick().isOnGround();

        switch (packet) {
            case PositionPacket positionPacket -> {
                pos = new Pos(positionPacket.getX(),
                        positionPacket.getY(), positionPacket.getZ(),
                        pos.yaw(), pos.pitch());
                onGround = positionPacket.isOnGround();
            }
            case PositionAndRotationPacket positionAndRotationPacket -> {
                pos = positionAndRotationPacket.getPos();
                onGround = positionAndRotationPacket.isOnGround();
            }
            case IsOnGroundPacket isOnGroundPacket -> onGround = isOnGroundPacket.isOnGround();
            case RotationPacket rotationPacket ->
                    pos = new Pos(pos.x(), pos.y(), pos.z(), rotationPacket.getYaw(), rotationPacket.getPitch());
            default -> {
            }
        }

        player.processMovement(pos, onGround);
    }
}
