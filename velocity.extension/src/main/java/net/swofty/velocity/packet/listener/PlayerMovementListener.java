package net.swofty.velocity.packet.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.velocitypowered.api.proxy.Player;
import net.swofty.velocity.gamemanager.TransferHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMovementListener implements PacketListener {

    private static final class MovementData {
        double x, y, z;
        float yaw, pitch;
    }

    private final Map<UUID, MovementData> last = new HashMap<>(1024 * 16);

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
            && event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION
            && event.getPacketType() != PacketType.Play.Client.PLAYER_ROTATION) {
            return;
        }

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        TransferHandler handler = new TransferHandler(player);
        if (!handler.isInAfkLimbo()) {
            return;
        }

        MovementData prev = last.get(uuid);
        double x = 0, y = 0, z = 0;
        float yaw = 0, pitch = 0;

        boolean hasPos = false;
        boolean hasRot = false;
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation w =
                new WrapperPlayClientPlayerPositionAndRotation(event);

            Vector3d p = w.getPosition();
            x = p.getX();
            y = p.getY();
            z = p.getZ();
            yaw = w.getYaw();
            pitch = w.getPitch();

            hasPos = hasRot = true;

        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerPosition w =
                new WrapperPlayClientPlayerPosition(event);

            Vector3d p = w.getPosition();
            x = p.getX();
            y = p.getY();
            z = p.getZ();

            hasPos = true;
        } else {
            WrapperPlayClientPlayerRotation w =
                new WrapperPlayClientPlayerRotation(event);

            yaw = w.getYaw();
            pitch = w.getPitch();

            hasRot = true;
        }

        if (prev == null) {
            MovementData data = last.getOrDefault(uuid, new MovementData());

            if (hasPos) {
                data.x = x;
                data.y = y;
                data.z = z;
            }

            if (hasRot) {
                data.yaw = yaw;
                data.pitch = pitch;
            }

            last.put(uuid, data);
            return;
        }

        double dx, dy, dz;
        float dyaw, dpitch;

        if (hasPos) {
            dx = x - prev.x;
            dy = y - prev.y;
            dz = z - prev.z;

            boolean significantMove = dx * dx + dy * dy + dz * dz >= 1.0;
            if (significantMove) {
                moved(uuid, handler);
                return;
            } else {
                prev.x = x;
                prev.y = y;
                prev.z = z;
                last.put(uuid, prev);
            }
        }

        if (hasRot) {
            dyaw = yaw - prev.yaw;
            dpitch = pitch - prev.pitch;

            boolean significantPitch = dyaw * dyaw + dpitch * dpitch >= 4.0f;
            if (significantPitch) {
                moved(uuid, handler);
            } else {
                prev.yaw = yaw;
                prev.pitch = pitch;
                last.put(uuid, prev);
            }
        }
    }

    private void moved(UUID uuid, TransferHandler handler) {
        handler.returnFromAfkLimbo();
        last.remove(uuid);
    }
}