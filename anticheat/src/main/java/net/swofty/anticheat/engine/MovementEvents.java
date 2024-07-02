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

        if (packet instanceof PositionPacket) {
            PositionPacket positionPacket = (PositionPacket) packet;
            pos = new Pos(positionPacket.getX(),
                    positionPacket.getY(), positionPacket.getZ(),
                    pos.yaw(), pos.pitch());
            onGround = positionPacket.isOnGround();
        } else if (packet instanceof PositionAndRotationPacket) {
            PositionAndRotationPacket positionAndRotationPacket = (PositionAndRotationPacket) packet;
            pos = positionAndRotationPacket.getPos();
            onGround = positionAndRotationPacket.isOnGround();
        } else if (packet instanceof IsOnGroundPacket) {
            IsOnGroundPacket isOnGroundPacket = (IsOnGroundPacket) packet;
            onGround = isOnGroundPacket.isOnGround();
        } else if (packet instanceof RotationPacket) {
            RotationPacket rotationPacket = (RotationPacket) packet;
            pos = new Pos(pos.x(), pos.y(), pos.z(), rotationPacket.getYaw(), rotationPacket.getPitch());
        }

        player.processMovement(pos, onGround);
    }
}
