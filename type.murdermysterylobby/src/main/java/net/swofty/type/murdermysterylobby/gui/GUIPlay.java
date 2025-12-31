package net.swofty.type.murdermysterylobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.lobby.LobbyOrchestratorConnector;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIPlay extends HypixelInventoryGUI {

    private final MurderMysteryGameType type;

    public GUIPlay(MurderMysteryGameType type) {
        super("Play Murder Mystery", InventoryType.CHEST_4_ROW);
        this.type = type;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getSingleLoreStackLineSplit(
                        "§aMurder Mystery " + type.getDisplayName(), "§7",
                        Material.IRON_SWORD, 1,
                        "§7Play a game of Murder Mystery " + type.getDisplayName() + "\n\n" + lore() + "\n\n§eClick to play!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();

                if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
                    player.sendMessage("§cYou are already searching for a game!");
                    return;
                }

                LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
                connector.sendToGame(ServerType.MURDER_MYSTERY_GAME, type.toString());
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aMap Selector " + type.getDisplayName(),
                        Material.OAK_SIGN, 1,
                        "§7Pick which map you want to play from",
                        "§7a list of available maps.",
                        "",
                        "§eClick to browse!");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMapSelection(type).open(player);
            }
        });

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

    private String lore() {
        return switch (type) {
            case CLASSIC ->
                    "One player is the Murderer!\nFind out who it is before\nthey kill everyone. One player\nis the Detective with a bow.";
            case DOUBLE_UP ->
                    "Two Murderers hunt the innocents!\nTwo Detectives must stop them!\nMore chaos, more fun!";
            case ASSASSINS ->
                    "Everyone has a target to eliminate!\nKill your target and inherit theirs.\nLast one standing wins!";
        };
    }
}
