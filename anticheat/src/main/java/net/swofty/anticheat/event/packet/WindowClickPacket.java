package net.swofty.anticheat.event.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class WindowClickPacket extends SwoftyPacket {
    private final int windowId;
    private final int stateId;
    private final int slot;
    private final int button;
    private final ClickType clickType;

    public WindowClickPacket(UUID uuid, int windowId, int stateId, int slot, int button, ClickType clickType) {
        super(uuid);
        this.windowId = windowId;
        this.stateId = stateId;
        this.slot = slot;
        this.button = button;
        this.clickType = clickType;
    }

    public enum ClickType {
        PICKUP,
        QUICK_MOVE,
        SWAP,
        CLONE,
        THROW,
        QUICK_CRAFT,
        PICKUP_ALL
    }
}
