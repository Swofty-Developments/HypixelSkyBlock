package net.swofty.type.bedwarsgame.item.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class SpectatorSettingsItem extends SimpleInteractableItem {


    public SpectatorSettingsItem() {
        super("spectator_settings");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack("§b§lSpectator Settings §7(Right Click)", Material.COMPASS, 1, "§7Right-click to change your spectator settings!").build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        event.getPlayer().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
            .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
