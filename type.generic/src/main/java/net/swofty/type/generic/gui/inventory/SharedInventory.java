package net.swofty.type.generic.gui.inventory;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

@Deprecated
public abstract class SharedInventory extends HypixelInventoryGUI {

	private static final Map<String, SharedInventoryContext> SHARED_CONTEXTS = new ConcurrentHashMap<>();

	@Getter
	protected SharedInventoryContext sharedContext;
	protected String contextId;

	public SharedInventory(String title, InventoryType size) {
		super(title, size);
	}

	/**
	 * Opens a shared inventory with a specific context ID
	 * If the context doesn't exist, it will throw an {@link IllegalArgumentException}
	 *
	 * @param player    the player to open the inventory for
	 * @param contextId the unique identifier for this shared inventory context
	 * @throws IllegalArgumentException if contextId does not exist
	 */
	public void openShared(HypixelPlayer player, String contextId) {
		SharedInventoryContext context = SHARED_CONTEXTS.get(contextId);
		if (context == null) {
			throw new IllegalArgumentException("Shared inventory context with ID " + contextId + " does not exist.");
		}
		this.contextId = contextId;
		this.sharedContext = context;
		sharedContext.addPlayer(player);
		super.open(player);
	}


	/**
	 * Creates a new shared inventory context and returns its ID
	 *
	 * @param player the first player to open this shared inventory
	 * @return the context ID that can be used to allow other players to join
	 */
	public String createSharedContext(HypixelPlayer player) {
		String newContextId = UUID.randomUUID().toString();
		openShared(player, newContextId);
		return newContextId;
	}

	/**
	 * Joins an existing shared inventory context
	 *
	 * @param player    the player to join the shared inventory
	 * @param contextId the context ID to join
	 * @return true if successfully joined, false if context doesn't exist
	 */
	public boolean joinSharedContext(HypixelPlayer player, String contextId) {
		SharedInventoryContext context = SHARED_CONTEXTS.get(contextId);
		if (context == null) {
			return false;
		}

		this.contextId = contextId;
		this.sharedContext = context;
		sharedContext.addPlayer(player);
		super.open(player);
		return true;
	}

	@Override
	public void onClose(InventoryCloseEvent e, CloseReason reason) {
		super.onClose(e, reason);

		if (sharedContext != null && player != null) {
			sharedContext.removePlayer(player);

			// If no players are left, clean up the context
			if (sharedContext.getPlayerCount() == 0) {
				SHARED_CONTEXTS.remove(contextId);
				onContextClosed(sharedContext);
			}
		}
	}

	/**
	 * Updates an item in the shared inventory for all players
	 *
	 * @param slot      the slot to update
	 * @param itemStack the new item stack
	 */
	public void updateSharedItem(int slot, ItemStack itemStack) {
		if (sharedContext != null) {
			sharedContext.updateItem(slot, itemStack);
		}
	}

	/**
	 * Gets an item from the shared inventory
	 *
	 * @param slot the slot to get the item from
	 * @return the item stack in that slot
	 */
	public ItemStack getSharedItem(int slot) {
		if (sharedContext != null) {
			return sharedContext.getItem(slot);
		}
		return ItemStack.AIR;
	}

	/**
	 * Sets multiple items in the shared inventory
	 *
	 * @param items map of slot -> itemstack to set
	 */
	public void setSharedItems(Map<Integer, ItemStack> items) {
		if (sharedContext != null) {
			sharedContext.setItems(items);
		}
	}

	/**
	 * Gets all items in the shared inventory
	 *
	 * @return map of slot -> itemstack for all non-empty slots
	 */
	public Map<Integer, ItemStack> getAllSharedItems() {
		if (sharedContext != null) {
			return sharedContext.getAllItems();
		}
		return new HashMap<>();
	}

	/**
	 * Clears all items from the shared inventory
	 */
	public void clearSharedInventory() {
		if (sharedContext != null) {
			sharedContext.clearAll();
		}
	}

	/**
	 * Gets the number of players currently viewing this shared inventory
	 *
	 * @return the number of players
	 */
	public int getPlayerCount() {
		return sharedContext != null ? sharedContext.getPlayerCount() : 0;
	}

	/**
	 * Gets all players currently viewing this shared inventory
	 *
	 * @return set of players viewing this inventory
	 */
	public Set<HypixelPlayer> getViewingPlayers() {
		return sharedContext != null ? new HashSet<>(sharedContext.getPlayers()) : new HashSet<>();
	}

	/**
	 * Broadcasts a message to all players viewing this shared inventory
	 *
	 * @param message the message to broadcast
	 */
	public void broadcastToViewers(String message) {
		if (sharedContext != null) {
			sharedContext.broadcastMessage(message);
		}
	}

	/**
	 * Executes an action for all players viewing this shared inventory
	 *
	 * @param action the action to execute
	 */
	public void executeForAllViewers(Consumer<HypixelPlayer> action) {
		if (sharedContext != null) {
			sharedContext.executeForAllPlayers(action);
		}
	}

	/**
	 * Called when the shared context is closed (no more players viewing)
	 * Override this method to handle cleanup when all players leave
	 *
	 * @param context the context that was closed
	 */
	protected void onContextClosed(SharedInventoryContext context) {
	}

	/**
	 * Called when an item is changed in the shared inventory
	 * Override this method to handle item changes
	 *
	 * @param slot    the slot that was changed
	 * @param oldItem the previous item
	 * @param newItem the new item
	 */
	protected void onSharedItemChanged(int slot, ItemStack oldItem, ItemStack newItem) {
	}

	/**
	 * Called when a player joins the shared inventory
	 * Override this method to handle player join events
	 *
	 * @param player the player who joined
	 */
	protected void onPlayerJoined(HypixelPlayer player) {
	}

	/**
	 * Called when a player leaves the shared inventory
	 * Override this method to handle player leave events
	 *
	 * @param player the player who left
	 */
	protected void onPlayerLeft(HypixelPlayer player) {
	}

	/**
	 * Inner class to manage shared inventory context
	 */
	@Getter
	public static class SharedInventoryContext {
		private final String contextId;
		private final SharedInventory templateInventory;
		private final Set<HypixelPlayer> players;
		private final Map<Integer, ItemStack> sharedItems;

		public SharedInventoryContext(String contextId, SharedInventory templateInventory) {
			this.contextId = contextId;
			this.templateInventory = templateInventory;
			this.players = new CopyOnWriteArraySet<>();
			this.sharedItems = new ConcurrentHashMap<>();
		}

		public void addPlayer(HypixelPlayer player) {
			players.add(player);
			templateInventory.onPlayerJoined(player);
		}

		public void removePlayer(HypixelPlayer player) {
			players.remove(player);
			templateInventory.onPlayerLeft(player);
		}

		public int getPlayerCount() {
			return players.size();
		}

		public void updateItem(int slot, ItemStack itemStack) {
			ItemStack oldItem = sharedItems.get(slot);
			if (itemStack == null || itemStack.isAir()) {
				sharedItems.remove(slot);
			} else {
				sharedItems.put(slot, itemStack);
			}

			// Update the item for all players viewing this inventory
			players.forEach(player -> {
				HypixelInventoryGUI gui = GUI_MAP.get(player.getUuid());
				if (gui instanceof SharedInventory sharedGUI && sharedGUI.sharedContext == this) {
					if (gui.getInventory() != null) {
						gui.getInventory().setItemStack(slot, itemStack == null ? ItemStack.AIR : itemStack);
					}
				}
			});

			templateInventory.onSharedItemChanged(slot, oldItem, itemStack);
		}

		public ItemStack getItem(int slot) {
			return sharedItems.getOrDefault(slot, ItemStack.AIR);
		}

		public void setItems(Map<Integer, ItemStack> items) {
			items.forEach(this::updateItem);
		}

		public Map<Integer, ItemStack> getAllItems() {
			return new HashMap<>(sharedItems);
		}

		public void clearAll() {
			Set<Integer> slotsToRemove = new HashSet<>(sharedItems.keySet());
			slotsToRemove.forEach(slot -> updateItem(slot, ItemStack.AIR));
		}

		public void broadcastMessage(String message) {
			players.forEach(player -> player.sendMessage(message));
		}

		public void executeForAllPlayers(Consumer<HypixelPlayer> action) {
			players.forEach(action);
		}
	}
}