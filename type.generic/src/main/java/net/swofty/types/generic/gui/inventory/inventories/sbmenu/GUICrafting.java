package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GUICrafting extends SkyBlockAbstractInventory {
    private static final String STATE_NO_RECIPE = "no_recipe";
    private static final String STATE_VALID_RECIPE = "valid_recipe";
    private static final String STATE_CRAFTABLE = "craftable";

    private static final ItemStack RECIPE_REQUIRED = ItemStackCreator.getStack("§cRecipe Required", Material.BARRIER, 1,
            "§7Add the items for a valid", "§7recipe in the crafting grid", "§7to the left!").build();
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 23;

    public GUICrafting() {
        super(InventoryType.CHEST_6_ROW);
        startLoop("refresh", 5, () -> refreshItems(owner));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Initial state
        doAction(new AddStateAction(STATE_NO_RECIPE));

        // Base background
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build(), 13, 34);

        // State-based borders
        borderWithState(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE).build(), STATE_NO_RECIPE, 0, 44);
        borderWithStates(ItemStackCreator.createNamedItemStack(Material.LIME_STAINED_GLASS_PANE).build(),
                new String[]{STATE_VALID_RECIPE, STATE_CRAFTABLE}, 0, 44);
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build(), 0, 44);

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Default recipe slot
        attachItem(GUIItem.builder(RESULT_SLOT)
                .item(RECIPE_REQUIRED)
                .requireState(STATE_NO_RECIPE)
                .build());
    }

    private void refreshItems(SkyBlockPlayer player) {
        SkyBlockRecipe<?> recipe = SkyBlockRecipe.parseRecipe(getCurrentRecipe());

        if (recipe == null) {
            doAction(new RemoveStateAction(STATE_VALID_RECIPE));
            doAction(new RemoveStateAction(STATE_CRAFTABLE));
            doAction(new AddStateAction(STATE_NO_RECIPE));
            return;
        }

        doAction(new RemoveStateAction(STATE_NO_RECIPE));
        doAction(new AddStateAction(STATE_VALID_RECIPE));

        recipe = recipe.clone();
        SkyBlockRecipe.CraftingResult result = recipe.getCanCraft().apply(player);

        if (!result.allowed()) {
            doAction(new RemoveStateAction(STATE_CRAFTABLE));
            attachItem(GUIItem.builder(RESULT_SLOT)
                    .item(ItemStackCreator.getStack(result.errorMessage()[0],
                            Material.BEDROCK,
                            1,
                            Arrays.copyOfRange(result.errorMessage(), 1, result.errorMessage().length)).build())
                    .requireState(STATE_VALID_RECIPE)
                    .build());
            return;
        }

        doAction(new AddStateAction(STATE_CRAFTABLE));
        setupCraftingResult(recipe, player);
    }

    private void setupCraftingResult(SkyBlockRecipe<?> recipe, SkyBlockPlayer player) {
        int amount = recipe.getAmount();
        SkyBlockRecipe<?> finalRecipe = recipe;

        attachItem(GUIItem.builder(RESULT_SLOT)
                .item(() -> {
                    ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, finalRecipe.getResult().getItemStack())
                            .amount(amount);

                    ArrayList<String> lore = new ArrayList<>();
                    builder.build().get(ItemComponent.LORE).stream()
                            .map(line -> "§7" + StringUtility.getTextFromComponent(line))
                            .forEach(lore::add);
                    lore.add("§8§m------------------");
                    lore.add("§7This is the item you are crafting.");
                    builder.set(ItemComponent.LORE, lore.stream()
                            .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                            .collect(Collectors.toList()));

                    return builder.build();
                })
                .requireStates(new String[]{STATE_VALID_RECIPE, STATE_CRAFTABLE})
                .onClick((ctx, item) -> handleCraftingClick(ctx, finalRecipe))
                .build());
    }

    private boolean handleCraftingClick(GUIItem.ClickContext ctx, SkyBlockRecipe<?> recipe) {
        SkyBlockItem cursorItem = new SkyBlockItem(ctx.cursorItem());
        ItemType cursorItemType = cursorItem.getAttributeHandler().getPotentialType();
        ItemType resultItemType = recipe.getResult().getAttributeHandler().getPotentialType();
        boolean isShift = ctx.clickType().equals(ClickType.START_SHIFT_CLICK);

        if (!ctx.cursorItem().isAir() &&
                (cursorItemType == null || !cursorItemType.equals(resultItemType))) {
            ctx.player().sendMessage("§cYou must empty your cursor first!");
            return false;
        }

        ItemStack craftedItem = PlayerItemUpdater.playerUpdate(ctx.player(),
                recipe.getResult().getItemStack()).amount(recipe.getAmount()).build();

        SkyBlockEventHandler.callSkyBlockEvent(new ItemCraftEvent(ctx.player(),
                new SkyBlockItem(craftedItem), recipe));

        SkyBlockItem[] toReplace = recipe.consume(getCurrentRecipeAsItems());
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            if (toReplace[i] == null || toReplace[i].getItemStack().material() == Material.BEDROCK) {
                setItemStack(CRAFT_SLOTS[i], ItemStack.AIR);
            } else {
                setItemStack(CRAFT_SLOTS[i], PlayerItemUpdater.playerUpdate(ctx.player(),
                        toReplace[i].getItemStack()).build());
            }
        }

        if (isShift) {
            ctx.player().addAndUpdateItem(craftedItem);
        } else {
            if (cursorItemType != null && cursorItemType.equals(resultItemType)) {
                ctx.player().addAndUpdateItem(cursorItem);
            }
            ctx.player().getInventory().setCursorItem(craftedItem);
        }

        ctx.player().getInventory().update();
        refreshItems(ctx.player());
        return true;
    }

    private ItemStack[] getCurrentRecipe() {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            stacks[i] = getItemStack(CRAFT_SLOTS[i]);
        }
        return stacks;
    }

    private SkyBlockItem[] getCurrentRecipeAsItems() {
        SkyBlockItem[] stacks = new SkyBlockItem[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            stacks[i] = new SkyBlockItem(getItemStack(CRAFT_SLOTS[i]));
        }
        return stacks;
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            ((SkyBlockPlayer) event.getPlayer()).addAndUpdateItem(new SkyBlockItem(getItemStack(slot)));
        });
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());
        if (clickedItem.isNA() || clickedItem.getMaterial().equals(Material.AIR)) return;
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            player.addAndUpdateItem(new SkyBlockItem(getItemStack(slot)));
        });
    }
}