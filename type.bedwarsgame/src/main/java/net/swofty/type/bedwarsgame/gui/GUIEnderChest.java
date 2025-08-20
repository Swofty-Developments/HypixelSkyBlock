package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIEnderChest extends HypixelInventoryGUI {

    public GUIEnderChest() {
        super("Ender Chest", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        String gameId = player.getTag(Tag.String("gameId"));
        if (gameId == null) return;

        Game game = TypeBedWarsGameLoader.getGameById(gameId);
        if (game == null) return;

        Map<Integer, ItemStack> enderChest = game.getEnderchests().get(player);
        if (enderChest != null) {
            for (Map.Entry<Integer, ItemStack> entry : enderChest.entrySet()) {
                if (entry.getKey() < size.getSize()) {
                    set(entry.getKey(), entry.getValue().builder(), true);
                }
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        super.onClose(e, reason);

        HypixelPlayer player = (HypixelPlayer) e.getPlayer();
        closeSave(player);
    }

	@Override
	public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
		super.suddenlyQuit(inventory, player);
		closeSave(player);
	}

	private void closeSave(HypixelPlayer player) {
		String gameId = player.getTag(Tag.String("gameId"));
		if (gameId == null) return;

		Game game = TypeBedWarsGameLoader.getGameById(gameId);
		if (game == null) return;

		Map<Integer, ItemStack> enderChest = new ConcurrentHashMap<>();
		for (int slot = 0; slot < getInventory().getSize(); slot++) {
			ItemStack item = getInventory().getItemStack(slot);
			if (!item.isAir()) {
				enderChest.put(slot, item);
			}
		}

		game.getEnderchests().put(player, enderChest);
	}

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
