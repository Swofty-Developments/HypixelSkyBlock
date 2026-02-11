package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class TeleporterItem extends SimpleInteractableItem {


    public TeleporterItem() {
        super("teleporter");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack("§a§lTeleporter §7(Right Click)", Material.COMPASS, 1, "§7Right-click to spectate players!").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {

    }
}
