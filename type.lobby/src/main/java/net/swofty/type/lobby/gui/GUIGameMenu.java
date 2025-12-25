package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.ServerInfoCache;
import net.swofty.type.lobby.game.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIGameMenu extends HypixelInventoryGUI {

    private static final int[] GAME_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
                    21,     23,
            28, 29, 30, 31, 32, 33, 33,
            37, 38, 39, 40, 41, 42, 43
    };

    public GUIGameMenu() {
        super("Game Menu", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        // Refresh cache, then populate
        ServerInfoCache.getServers().thenAccept(servers -> {
            int i = 0;
            for (GameType game : GameType.values()) {
                if (i >= GAME_SLOTS.length) break;
                set(createGameItem(game, GAME_SLOTS[i++]));
            }
            set(GUIClickableItem.getCloseItem(40));
            updateItemStacks(getInventory(), player);
        });

        updateItemStacks(getInventory(), player);
    }

    private GUIClickableItem createGameItem(GameType game, int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String playerCount = StringUtility.commaify(game.getPlayerCount());
                String titleColor = game.isImplemented() ? "§a" : "§c";

                List<String> lore = new ArrayList<>();
                lore.addAll(Arrays.asList(game.getLore()));
                lore.add("");
                if (game.isImplemented()) {
                    lore.add("§e" + playerCount + " playing");
                    lore.add("");
                    lore.add("§eClick to play!");
                }

                return ItemStackCreator.getStack(titleColor + game.getDisplayName(),
                        game.getMaterial(), 1, lore.toArray(new String[0]));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (!game.isImplemented()) {
                    player.sendMessage("§cThis game is not yet available!");
                    return;
                }
                player.closeInventory();
                player.sendTo(game.getLobbyType());
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
