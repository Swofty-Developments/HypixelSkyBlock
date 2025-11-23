package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.event.packet.WindowClickPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerWindowClickPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return WindowClickPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        int windowId = packet.getIntegers().read(0);
        int stateId = packet.getIntegers().size() > 1 ? packet.getIntegers().read(1) : 0;
        int slot = packet.getIntegers().size() > 2 ? packet.getIntegers().read(2) : 0;
        int button = packet.getIntegers().size() > 3 ? packet.getIntegers().read(3) : 0;
        int clickTypeId = packet.getIntegers().size() > 4 ? packet.getIntegers().read(4) : 0;

        WindowClickPacket.ClickType clickType = mapClickType(clickTypeId);

        return new WindowClickPacket(uuid, windowId, stateId, slot, button, clickType);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }

    private WindowClickPacket.ClickType mapClickType(int type) {
        return switch (type) {
            case 0 -> WindowClickPacket.ClickType.PICKUP;
            case 1 -> WindowClickPacket.ClickType.QUICK_MOVE;
            case 2 -> WindowClickPacket.ClickType.SWAP;
            case 3 -> WindowClickPacket.ClickType.CLONE;
            case 4 -> WindowClickPacket.ClickType.THROW;
            case 5 -> WindowClickPacket.ClickType.QUICK_CRAFT;
            case 6 -> WindowClickPacket.ClickType.PICKUP_ALL;
            default -> WindowClickPacket.ClickType.PICKUP;
        };
    }
}
