package net.swofty.type.murdermysterygame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.gui.GUITeleporter;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.murdermysterygame.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class SpectatorCompass extends SimpleInteractableItem {

    public SpectatorCompass() {
        super("spectator_compass");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack("§aTeleporter §7(Right Click)", Material.COMPASS, 1,
                "§7Right-click to teleport to players!").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();
        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            new GUITeleporter(game).open(player);
        }
    }
}
