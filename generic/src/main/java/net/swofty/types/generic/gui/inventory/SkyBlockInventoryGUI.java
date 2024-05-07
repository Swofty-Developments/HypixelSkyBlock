package net.swofty.types.generic.gui.inventory;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.event.actions.player.ActionPlayerChangeSkyBlockMenuDisplay;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class SkyBlockInventoryGUI {
    public static final Map<UUID, SkyBlockInventoryGUI> GUI_MAP = new ConcurrentHashMap<>();

    protected String title;
    protected InventoryType size;
    protected final List<GUIItem> items;
    private Inventory inventory;
    private SkyBlockPlayer player;
    private boolean hasFinishedLoading = false;
    private int itemInHand = 0;

    public SkyBlockInventoryGUI(String title, InventoryType size) {
        this.title = title;
        this.size = size;
        this.items = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Set an item inside the GUI
     *
     * @param a the item you want to set
     */
    public void set(GUIItem a) {
        synchronized (items) {
            items.removeIf(i -> i.itemSlot == a.itemSlot);
            items.add(a);
        }
    }

    /**
     * Set an item inside the gui
     *
     * @param slot   the slot the item should be in (any number between 0-53 depending on your GUI size)
     * @param stack  the {@link ItemStack} that should be in the slot
     * @param pickup can the player pick up the item by clicking on it or no
     */
    public void set(int slot, ItemStack.Builder stack, boolean pickup) {
        if (stack == null) {
            items.removeIf(i -> i.itemSlot == slot);
            return;
        }
        set(new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return stack;
            }

            @Override
            public boolean canPickup() {
                return pickup;
            }
        });
    }

    /**
     * Set an item inside the gui
     * In this method pickup is set to false by default
     *
     * @param slot  the slot the item should be in (any number between 0-53 depending on your GUI size)
     * @param stack the {@link ItemStack} that should be in the slot
     */
    public void set(int slot, ItemStack.Builder stack) {
        set(slot, stack, false);
    }

    /**
     * Get a GUI item that is in a slot
     *
     * @param slot which slot the GUI item is in
     * @return the GUIItem in that slot, if there is none, null
     */
    public GUIItem get(int slot) {
        for (GUIItem item : items) {
            if (item.itemSlot == slot)
                return item;
        }
        return null;
    }

    /**
     * Fill a rectangle between two corners of the GUI
     *
     * @param stack       what {@link ItemStack} are we filling our rectangle with
     * @param cornerSlot  the top left corner of our rectangle
     * @param cornerSlot2 the bottom right corner of our rectangle
     * @param overwrite   if this is set to true, the method will ignore any items in the rectangle and replace them with 'stack'
     * @param pickup      is the player able to pick up the items in the rectangle?
     */
    public void fill(ItemStack.Builder stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup) {
        if (cornerSlot < 0 || cornerSlot > size.getSize())
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        if (cornerSlot2 < 0 || cornerSlot2 > size.getSize())
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = Math.max(cornerSlot, cornerSlot2);
        int topRight;
        for (topRight = bottomRight; topRight > topLeft; topRight -= 9) ;
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) ;
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9) {
            for (int x = y; x <= topRight - topLeft + y; x++) {
                int f = x;
                if (items.stream().filter(item -> item.itemSlot == f).toArray().length != 0 && !overwrite)
                    continue;
                set(x, stack, pickup);
            }
        }
    }

    /**
     * Fill a rectangle between two corners of the GUI
     * Overwrite is set to true by default in this method
     *
     * @param stack       what {@link ItemStack} are we filling our rectangle with
     * @param cornerSlot  the top left corner of our rectangle
     * @param cornerSlot2 the bottom right corner of our rectangle
     * @param pickup      is the player able to pick up the items in the rectangle?
     */
    public void fill(ItemStack.Builder stack, int cornerSlot, int cornerSlot2, boolean pickup) {
        fill(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    /**
     * Fill a rectangle between two corners of the GUI
     * Overwrite is set to true by default in this method
     * Pickup is set to false by default in this method
     *
     * @param stack       what {@link ItemStack} are we filling our rectangle with
     * @param cornerSlot  the top left corner of our rectangle
     * @param cornerSlot2 the bottom right corner of our rectangle
     */
    public void fill(ItemStack.Builder stack, int cornerSlot, int cornerSlot2) {
        fill(stack, cornerSlot, cornerSlot2, false);
    }

    /**
     * Fills the whole inventory with the item stack of your choice
     *
     * @param stack the {@link ItemStack} of your choice
     */
    public void fill(ItemStack.Builder stack) {
        fill(stack, 0, size.getSize() - 1);
    }

    /**
     * Fills the whole inventory with the Material of your choice
     *
     * @param material the {@link Material} of your choice
     */
    public void fill(Material material, String name) {
        fill(ItemStack.builder(material).displayName(Component.text(name)));
    }

    /**
     * Fills the border of a rectangle between two slots
     *
     * @param stack       the {@link ItemStack} of your choice
     * @param cornerSlot  the top left corner of your rectangle
     * @param cornerSlot2 the bottom right corner of your rectangle
     * @param overwrite   if this is set to true, the method will ignore any items in between the lines and replace them
     * @param pickup      if this is set to true, players will be able to pick up the items in your border
     */
    public void border(ItemStack.Builder stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup) {
        if (cornerSlot < 0 || cornerSlot > size.getSize())
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        if (cornerSlot2 < 0 || cornerSlot2 > size.getSize())
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = Math.max(cornerSlot, cornerSlot2);
        int topRight;
        for (topRight = bottomRight; topRight > topLeft; topRight -= 9) ;
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) ;
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9) {
            for (int x = y; x <= topRight - topLeft + y; x++) {
                int f = x;
                if (items.stream().filter(item -> item.itemSlot == f).toArray().length != 0 && !overwrite)
                    continue;
                if (y == topLeft || y == bottomLeft)
                    set(x, stack, pickup);
                if (x == y || x == topRight - topLeft + y)
                    set(x, stack, pickup);
            }
        }
    }

    /**
     * Fills the border in a rectangle between two slots\
     * Overwrite is set to true by default in this method
     *
     * @param stack       the {@link ItemStack} of your choice
     * @param cornerSlot  the top left corner of your rectangle
     * @param cornerSlot2 the bottom right corner of your rectangle
     * @param pickup      if this is set to true, players will be able to pick up the items in your border
     */
    public void border(ItemStack.Builder stack, int cornerSlot, int cornerSlot2, boolean pickup) {
        border(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    /**
     * Fills the border in a rectangle between two slots\
     * Overwrite is set to true by default in this method
     * Pickup is set to false by default in this method
     *
     * @param stack       the {@link ItemStack} of your choice
     * @param cornerSlot  the top left corner of your rectangle
     * @param cornerSlot2 the bottom right corner of your rectangle
     */
    public void border(ItemStack.Builder stack, int cornerSlot, int cornerSlot2) {
        border(stack, cornerSlot, cornerSlot2, false);
    }

    /**
     * Fills the border between two corners of your GUI with the item stack of your choice
     *
     * @param stack the {@link ItemStack} of your choice
     */
    public void border(ItemStack.Builder stack) {
        border(stack, 0, size.getSize() - 1);
    }

    /**
     * Iterates through all the slots and finds the first empty one
     *
     * @return an empty slot index
     */
    public int firstEmpty() {
        for (int i = 0; i < size.getSize(); i++) {
            int finalI = i;
            long found = items.stream().filter((item) -> item.itemSlot == finalI).count();
            if (found == 0)
                return i;
        }
        return -1;
    }

    /**
     * Opens the GUI for a player
     *
     * @param player the player the gui is being opened for
     */
    public void open(SkyBlockPlayer player) {
        this.player = player;
        this.itemInHand = player.getHeldSlot();
        this.inventory = new Inventory(size, getTitle());

        SkyBlockInventoryGUI previouslyOpen = GUI_MAP.get(player.getUuid());
        if (previouslyOpen != null) {
            if (!previouslyOpen.hasFinishedLoading) {
                player.sendMessage("§cPlease wait before doing this!");
                return;
            }

            previouslyOpen.onClose(
                    new InventoryCloseEvent(previouslyOpen.getInventory(), player),
                    SkyBlockInventoryGUI.CloseReason.SERVER_EXITED
            );
            GUI_MAP.remove(player.getUuid());

            if (previouslyOpen.getInventory().getInventoryType() == size) {
                inventory = previouslyOpen.getInventory();
                inventory.setTitle(Component.text(getTitle()));
                for (int slot = 0; slot < inventory.getSize(); slot++) {
                    inventory.setItemStack(slot, ItemStack.AIR);
                }
            }
        }

        InventoryGUIOpenEvent openEvent = new InventoryGUIOpenEvent(player, this, inventory);

        // Initializing GUI
        Thread.startVirtualThread(() -> {
            setItems(openEvent);
            updateItemStacks(inventory, player);
            ActionPlayerChangeSkyBlockMenuDisplay.setMainMenu(player);
            onOpen(openEvent);
            if (player.getHeldSlot() != itemInHand) {
                player.sendMessage("§cYour item in hand cannot change in between opening GUIs");
                return;
            }
            hasFinishedLoading = true;

            if (this instanceof RefreshingGUI gui) {
                MinecraftServer.getSchedulerManager().submitTask(() -> {
                    // Player is removed from Map on disconnect, so we just need to check that
                    if (!GUI_MAP.containsKey(player.getUuid()) || GUI_MAP.get(player.getUuid()) != this) {
                        return TaskSchedule.stop();
                    }
                    Thread.startVirtualThread(() -> {
                        synchronized (items) {
                            gui.refreshItems(player);
                            updateItemStacks(inventory, player);
                        }
                    });
                    return TaskSchedule.tick(gui.refreshRate());
                });
            }
        });
        player.openInventory(inventory);

        GUI_MAP.put(player.getUuid(), this);
        afterOpen(openEvent);
    }

    /**
     * Method to allow people to hotkey items into the GUI or not
     *
     * @return a boolean
     */
    public abstract boolean allowHotkeying();

    /**
     * Runs when the player opens the gui
     *
     * @param e the event of the gui opening
     */
    public void onOpen(InventoryGUIOpenEvent e) {
    }

    /**
     * Runs when the player closes the gui
     *
     * @param e the event of the gui closing
     */
    public void onClose(InventoryCloseEvent e, CloseReason reason) {}

    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {}

    /**
     * Runs when the player clicks on their own inventory whole this GUI is open
     *
     * @param e the event of the click
     */
    public abstract void onBottomClick(InventoryPreClickEvent e);

    /**
     * Runs after the GUI opens to the player
     *
     * @param e the event of the gui opening
     */
    public void afterOpen(InventoryGUIOpenEvent e) {
    }

    /**
     * Runs before the GUI opens, in order to set the items in
     *
     * @param e the event of the GUI opening
     */
    public void setItems(InventoryGUIOpenEvent e) {
    }

    /**
     * re-set all the GUIItems inside the inventory
     *
     * @param inventory an inventory object to set the items in
     */
    public void updateItemStacks(Inventory inventory, SkyBlockPlayer player) {
        for (GUIItem item : items) {
            ItemStack toReplace = item.getItem(player).build();
            if (!inventory.getItemStack(item.itemSlot).equals(toReplace)) {
                inventory.setItemStack(item.itemSlot, toReplace);
            }
        }
    }

    public record InventoryGUIOpenEvent(SkyBlockPlayer player, SkyBlockInventoryGUI opened, Inventory inventory) {
    }

    public enum CloseReason {
        SIGN_OPENED,
        PLAYER_EXITED,
        SERVER_EXITED
    }
}
