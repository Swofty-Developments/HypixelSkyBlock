package net.swofty.types.generic.gui.inventory.actions;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class SetCursorItemAction implements GUIAction {
    private final GUIItem.ClickContext context;
    private final ItemStack item;

    public SetCursorItemAction(GUIItem.ClickContext context, ItemStack item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        Player player = context.player();
        player.getInventory().setCursorItem(item);
        player.getInventory().update();
        gui.setCursorItem(player, item);
    }
}