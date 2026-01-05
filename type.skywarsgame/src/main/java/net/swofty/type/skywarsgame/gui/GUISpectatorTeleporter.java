package net.swofty.type.skywarsgame.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUISpectatorTeleporter extends HypixelInventoryGUI {

    private final SkywarsGame game;

    public GUISpectatorTeleporter(SkywarsGame game) {
        super("Teleporter", InventoryType.CHEST_4_ROW);
        this.game = game;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        int slot = 10;
        for (SkywarsPlayer target : game.getPlayers()) {
            if (target.isEliminated()) continue;
            if (slot > 25) break;

            final SkywarsPlayer targetPlayer = target;
            set(new GUIClickableItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStackHead(
                            "§a" + targetPlayer.getUsername(),
                            targetPlayer.getPlayerSkin(),
                            1,
                            "§7Status: §aAlive",
                            "",
                            "§eClick to teleport!"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    player.closeInventory();
                    player.teleport(targetPlayer.getPosition());
                    player.sendMessage(Component.text("Teleported to " + targetPlayer.getUsername(), NamedTextColor.GREEN));
                }
            });
            slot++;
        }

        set(GUIClickableItem.getCloseItem(31));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
