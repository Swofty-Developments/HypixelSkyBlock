package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.events.ActionGamePlayerEvent;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.generic.gui.inventory.SharedInventory;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;

public class GUITeamChest extends SharedInventory {

    private final String teamName;

    public GUITeamChest(String teamName) {
        super(teamName.toUpperCase() + "'s Team Chest", InventoryType.CHEST_3_ROW);
        this.teamName = teamName;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        if (sharedContext == null) return;

        HypixelPlayer player = e.player();
        String gameId = player.getTag(Tag.String("gameId"));
        if (gameId == null) return;

        Game game = TypeBedWarsGameLoader.getGameById(gameId);
        if (game == null) return;

        Map<Integer, ItemStack> teamChest = game.getChests().get(teamName);
        if (teamChest != null) {
            setSharedItems(teamChest);
        }

        for (int slot = 0; slot < size.getSize(); slot++) {
            final int finalSlot = slot;
            set(new GUIClickableItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    ItemStack sharedItem = getSharedItem(finalSlot);
                    return sharedItem.isAir() ? ItemStack.AIR.builder() : sharedItem.builder();
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    handleChestClick(e, finalSlot, player);
                }
            });
        }
    }

    private void handleChestClick(InventoryPreClickEvent e, int slot, HypixelPlayer player) {
        ItemStack clickedItem = e.getClickedItem();
        ItemStack cursorItem = e.getCursorItem();

        ItemStack newItem = null;

        if (e.getClickType() == ClickType.LEFT_CLICK) {
            if (cursorItem.isAir()) {
                newItem = ItemStack.AIR;
                player.getInventory().setCursorItem(clickedItem);
            } else if (clickedItem.isAir()) {
                newItem = cursorItem;
                player.getInventory().setCursorItem(ItemStack.AIR);
            } else if (clickedItem.isSimilar(cursorItem)) {
                int totalAmount = clickedItem.amount() + cursorItem.amount();
                int maxStack = clickedItem.material().maxStackSize();

                if (totalAmount <= maxStack) {
                    newItem = clickedItem.withAmount(totalAmount);
                    player.getInventory().setCursorItem(ItemStack.AIR);
                } else {
                    newItem = clickedItem.withAmount(maxStack);
                    player.getInventory().setCursorItem(cursorItem.withAmount(totalAmount - maxStack));
                }
            } else {
                newItem = cursorItem;
                player.getInventory().setCursorItem(clickedItem);
            }
        } else if (e.getClickType() == ClickType.RIGHT_CLICK) {
            if (cursorItem.isAir() && !clickedItem.isAir()) {
                int halfAmount = (clickedItem.amount() + 1) / 2;
                int remainingAmount = clickedItem.amount() - halfAmount;

                if (remainingAmount > 0) {
                    newItem = clickedItem.withAmount(remainingAmount);
                } else {
                    newItem = ItemStack.AIR;
                }
                player.getInventory().setCursorItem(clickedItem.withAmount(halfAmount));
            } else if (!cursorItem.isAir() && clickedItem.isAir()) {
                newItem = cursorItem.withAmount(1);
                if (cursorItem.amount() > 1) {
                    player.getInventory().setCursorItem(cursorItem.withAmount(cursorItem.amount() - 1));
                } else {
                    player.getInventory().setCursorItem(ItemStack.AIR);
                }
            } else if (!cursorItem.isAir() && !clickedItem.isAir() && clickedItem.isSimilar(cursorItem)) {
                if (clickedItem.amount() < clickedItem.material().maxStackSize()) {
                    newItem = clickedItem.withAmount(clickedItem.amount() + 1);
                    if (cursorItem.amount() > 1) {
                        player.getInventory().setCursorItem(cursorItem.withAmount(cursorItem.amount() - 1));
                    } else {
                        player.getInventory().setCursorItem(ItemStack.AIR);
                    }
                }
            }
        } else if (e.getClickType() == ClickType.SHIFT_CLICK) {
            if (!clickedItem.isAir()) {
                if (player.getInventory().addItemStack(clickedItem)) {
                    newItem = ItemStack.AIR;
                }
            }
        }

        if (newItem != null) {
            updateSharedItem(slot, newItem);
        }

        e.setCancelled(true);
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        if (e.getClickType() == ClickType.SHIFT_CLICK && !e.getClickedItem().isAir()) {
            ItemStack itemToMove = e.getClickedItem();

            for (int slot = 0; slot < size.getSize(); slot++) {
                ItemStack currentItem = getSharedItem(slot);

                if (currentItem.isAir()) {
                    updateSharedItem(slot, itemToMove);
                    e.getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
                    e.setCancelled(true);
                    return;
                } else if (currentItem.isSimilar(itemToMove)) {
                    int totalAmount = currentItem.amount() + itemToMove.amount();
                    int maxStack = currentItem.material().maxStackSize();

                    if (totalAmount <= maxStack) {
                        updateSharedItem(slot, currentItem.withAmount(totalAmount));
                        e.getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
                        e.setCancelled(true);
                        return;
                    } else {
                        updateSharedItem(slot, currentItem.withAmount(maxStack));
                        e.getPlayer().getInventory().setItemStack(e.getSlot(), itemToMove.withAmount(totalAmount - maxStack));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            e.setCancelled(true);
        }
    }

    @Override
    protected void onContextClosed(SharedInventoryContext context) {
        ActionGamePlayerEvent.ACTIVE_TEAM_CHESTS.remove(teamName);
    }

    @Override
    protected void onSharedItemChanged(int slot, ItemStack oldItem, ItemStack newItem) {
        if (sharedContext != null) {
            for (HypixelPlayer player : sharedContext.getPlayers()) {
                String gameId = player.getTag(Tag.String("gameId"));
                if (gameId != null) {
                    Game game = TypeBedWarsGameLoader.getGameById(gameId);
                    if (game != null) {
                        Map<Integer, ItemStack> teamChest = game.getChests().computeIfAbsent(teamName, k -> new java.util.concurrent.ConcurrentHashMap<>());
                        if (newItem.isAir()) {
                            teamChest.remove(slot);
                        } else {
                            teamChest.put(slot, newItem);
                        }
                        break; // Only need to update once
                    }
                }
            }
        }
    }
}
