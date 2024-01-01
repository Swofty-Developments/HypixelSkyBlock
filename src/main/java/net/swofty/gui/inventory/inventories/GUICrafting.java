package net.swofty.gui.inventory.inventories;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.ItemCraftEvent;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.RefreshingGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GUICrafting extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final ItemStack.Builder RECIPE_REQUIRED = ItemStackCreator.getStack("§cRecipe Required", Material.BARRIER, (short) 0, 1, "§7Add the items for a valid recipe in", "§7the crafting grid to the left!");
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 24;

    public GUICrafting() {
        super("Craft Item", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
        border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE));
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);
        set(GUIClickableItem.getCloseItem(49));

        set(RESULT_SLOT, RECIPE_REQUIRED);
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            e.getPlayer().getInventory().addItemStack(e.getInventory().getItemStack(slot));
        });
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            player.getInventory().addItemStack(inventory.getItemStack(slot));
        });
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        Inventory inventory = getInventory();
        SkyBlockRecipe<?> recipe = SkyBlockRecipe.parseRecipe(getCurrentRecipe(inventory));

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
        border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE));
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);
        set(GUIClickableItem.getCloseItem(49));

        if (recipe == null) {
            set(RESULT_SLOT, RECIPE_REQUIRED);
            return;
        }

        SkyBlockRecipe.CraftingResult result = recipe.getCanCraft().apply(player);
        if (!result.allowed()) {
            set(RESULT_SLOT, ItemStackCreator.getStack(result.errorMessage()[0],
                    Material.BEDROCK,
                    (short) 0,
                    1,
                    Arrays.copyOfRange(result.errorMessage(), 1, result.errorMessage().length)));
            return;
        }

        int amount = recipe.getAmount();

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
        border(ItemStackCreator.createNamedItemStack(Material.LIME_STAINED_GLASS_PANE));
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                e.setCancelled(false);

                ItemStack craftedItem = PlayerItemUpdater.playerUpdate(
                        player,
                        null,
                        recipe.getResult().getItemStack()).amount(amount).build();
                SkyBlockEvent.callSkyBlockEvent(new ItemCraftEvent(player, new SkyBlockItem(craftedItem), recipe));

                if (!e.getCursorItem().isAir()) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§cYou must empty your cursor first!");
                    return;
                }

                AtomicInteger stopAfter = new AtomicInteger();
                MinecraftServer.getSchedulerManager().submitTask(() -> {
                    stopAfter.getAndIncrement();
                    player.getInventory().setCursorItem(craftedItem);
                    if (stopAfter.get() == 3) {
                        return TaskSchedule.stop();
                    }
                    return TaskSchedule.tick(1);
                });

                SkyBlockItem[] toReplace = recipe.consume(getCurrentRecipeAsItems(inventory));
                for (int i = 0; i < CRAFT_SLOTS.length; i++) {
                    if (toReplace[i] == null || toReplace[i].getItemStack().getMaterial() == Material.BEDROCK) {
                        int finalI = i;
                        set(new GUIItem() {
                            @Override
                            public boolean canPickup() {
                                return true;
                            }

                            @Override
                            public int getSlot() {
                                return CRAFT_SLOTS[finalI];
                            }

                            @Override
                            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                                return ItemStack.builder(Material.AIR);
                            }
                        });
                    } else {
                        int finalI1 = i;
                        set(new GUIItem() {
                            @Override
                            public boolean canPickup() {
                                return true;
                            }

                            @Override
                            public int getSlot() {
                                return CRAFT_SLOTS[finalI1];
                            }

                            @Override
                            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                                return PlayerItemUpdater.playerUpdate(
                                        player,
                                        null,
                                        toReplace[finalI1].getItemStack());
                            }
                        });
                    }
                }

                player.getInventory().update();
                refreshItems(player);
            }

            @Override
            public int getSlot() {
                return RESULT_SLOT;
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, null, recipe.getResult().getItemStack()).amount(amount);
            }
        });
    }

    @Override
    public int refreshRate() {
        return 5;
    }

    private ItemStack[] getCurrentRecipe(Inventory inventory) {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++)
            stacks[i] = inventory.getItemStack(CRAFT_SLOTS[i]);
        return stacks;
    }

    private SkyBlockItem[] getCurrentRecipeAsItems(Inventory inventory) {
        SkyBlockItem[] stacks = new SkyBlockItem[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++)
            stacks[i] = new SkyBlockItem(inventory.getItemStack(CRAFT_SLOTS[i]));
        return stacks;
    }
}
