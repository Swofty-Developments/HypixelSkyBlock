package net.swofty.types.generic.gui.inventory.actions;

import net.kyori.adventure.text.Component;
import net.minestom.server.network.packet.server.play.OpenWindowPacket;

import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class SetTitleAction implements GUIAction {
    private final Component title;

    public SetTitleAction(Component title) {
        this.title = title;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        gui.setTitle(title);
        gui.getViewers().forEach(player ->
                player.sendPacket(new OpenWindowPacket(
                        gui.getWindowId(),
                        gui.getInventoryType().getWindowType(),
                        title
                ))
        );
    }
}