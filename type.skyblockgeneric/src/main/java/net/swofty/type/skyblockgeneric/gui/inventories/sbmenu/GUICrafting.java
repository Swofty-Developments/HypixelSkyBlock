package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.event.custom.ItemCraftEvent;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GUICrafting extends HypixelInventoryGUI implements RefreshingGUI {
    private static final ItemStack.Builder RECIPE_REQUIRED = ItemStackCreator.getStack("§cRecipe Required", Material.BARRIER, 1, "§7Add the items for a valid", "§7recipe in the crafting grid", "§7to the left!");
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 23;

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
            ((SkyBlockPlayer) e.getPlayer()).addAndUpdateItem(new SkyBlockItem(e.getInventory().getItemStack(slot)));
        });
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            player.addAndUpdateItem(new SkyBlockItem(inventory.getItemStack(slot)));
        });
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        if (clickedItem.isNA() || clickedItem.getMaterial().equals(Material.AIR)) return;
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
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

        recipe = recipe.clone();

        SkyBlockRecipe.CraftingResult result = recipe.getCanCraft().apply((SkyBlockPlayer) player);
        if (!result.allowed()) {
            set(RESULT_SLOT, ItemStackCreator.getStack(result.errorMessage()[0],
                    Material.BEDROCK,
                    1,
                    Arrays.copyOfRange(result.errorMessage(), 1, result.errorMessage().length)));
            return;
        }

        int amount = recipe.getAmount();

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
        border(ItemStackCreator.createNamedItemStack(Material.LIME_STAINED_GLASS_PANE));
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);

        SkyBlockRecipe<?> finalRecipe = recipe;
        set(new GUIClickableItem(RESULT_SLOT) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockItem cursorItem = new SkyBlockItem(e.getCursorItem());
                ItemType cursorItemType = cursorItem.getAttributeHandler().getPotentialType();
                ItemType resultItemType = finalRecipe.getResult().getAttributeHandler().getPotentialType();
                boolean isShift = e.getClickType().equals(ClickType.START_SHIFT_CLICK);

                if (!e.getCursorItem().isAir() &&
                        (cursorItemType == null || !cursorItemType.equals(resultItemType))) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§cYou must empty your cursor first!");
                    return;
                }

                ItemStack craftedItem = PlayerItemUpdater.playerUpdate(
                        player,
                        finalRecipe.getResult().getItemStack()).amount(amount).build();
                e.setClickedItem(craftedItem);
                HypixelEventHandler.callCustomEvent(new ItemCraftEvent(player, new SkyBlockItem(craftedItem), finalRecipe));

                SkyBlockItem[] toReplace = finalRecipe.consume(getCurrentRecipeAsItems(inventory));
                for (int i = 0; i < CRAFT_SLOTS.length; i++) {
                    if (toReplace[i] == null || toReplace[i].getItemStack().material() == Material.BEDROCK) {
                        inventory.setItemStack(CRAFT_SLOTS[i], ItemStack.builder(Material.AIR).build());
                    } else {
                        inventory.setItemStack(CRAFT_SLOTS[i], PlayerItemUpdater.playerUpdate(
                                player,
                                toReplace[i].getItemStack()).build());
                    }
                }
                if (isShift){
                    // if is a shift click add updated item to player inventory
                    e.setCancelled(true);
                    player.addAndUpdateItem(e.getClickedItem());
                }

                if (cursorItemType != null && cursorItemType.equals(resultItemType) && !isShift) {
                    e.setCancelled(true);
                    player.addAndUpdateItem(cursorItem);
                }

                player.getInventory().update();
                refreshItems(player);
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, finalRecipe.getResult().getItemStack()).amount(amount);

                ArrayList<String> lore = new ArrayList<>();
                builder.build().get(ItemComponent.LORE).stream().map(line -> "§7" + StringUtility.getTextFromComponent(line)).forEach(lore::add);
                lore.add("§8§m------------------");
                lore.add("§7This is the item you are crafting.");
                builder.set(ItemComponent.LORE, lore.stream().map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()));

                return builder;
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
