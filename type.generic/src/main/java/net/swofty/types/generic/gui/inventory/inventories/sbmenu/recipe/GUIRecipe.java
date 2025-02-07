package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.CraftableComponent;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIRecipe extends SkyBlockAbstractInventory {
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final String STATE_HAS_NEXT = "has_next";
    private static final String STATE_HAS_PREVIOUS = "has_previous";

    private final SkyBlockItem item;
    private final SkyBlockAbstractInventory previousGUI;
    private final int recipeIndex;

    public GUIRecipe(ItemType type, SkyBlockAbstractInventory previousGUI) {
        this(new SkyBlockItem(type), previousGUI, 0);
    }

    public GUIRecipe(SkyBlockItem item, SkyBlockAbstractInventory previousGUI) {
        this(item, previousGUI, 0);
    }

    public GUIRecipe(SkyBlockItem item, SkyBlockAbstractInventory previousGUI, int recipeIndex) {
        super(InventoryType.CHEST_6_ROW);
        this.item = item;
        this.previousGUI = previousGUI;
        this.recipeIndex = recipeIndex;
        doAction(new SetTitleAction(Component.text(item.getAttributeHandler().getPotentialType().getDisplayName() + " Recipe")));
    }

    @SneakyThrows
    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        if (previousGUI != null) {
            attachItem(GUIItem.builder(48)
                    .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                            "§7To " + previousGUI.getTitleAsString()).build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(previousGUI);
                        return true;
                    })
                    .build());
        }

        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack(
                        "§aCrafting Table", Material.CRAFTING_TABLE, 1,
                        "§7Craft this recipe by using a",
                        "§7crafting table."
                ).build())
                .build());

        ItemType itemTypeLinker = item.getAttributeHandler().getPotentialType();
        if (item.toConfigurableItem() == null) {
            player.closeInventory();
            player.sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }

        List<SkyBlockRecipe<?>> recipes = SkyBlockRecipe.getFromType(itemTypeLinker);
        if (recipes.isEmpty()) {
            player.closeInventory();
            player.sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }

        int safeRecipeIndex = recipeIndex >= recipes.size() ? 0 : recipeIndex;
        SkyBlockRecipe<?> recipe = recipes.get(safeRecipeIndex);

        if (recipes.size() > safeRecipeIndex + 1) {
            doAction(new AddStateAction(STATE_HAS_NEXT));
        }
        if (safeRecipeIndex > 0) {
            doAction(new AddStateAction(STATE_HAS_PREVIOUS));
        }

        // Next Recipe Button
        attachItem(GUIItem.builder(32)
                .item(ItemStackCreator.getStack(
                        "§aNext Recipe", Material.ARROW, 1,
                        "§7Click to view the next recipe!"
                ).build())
                .requireState(STATE_HAS_NEXT)
                .onClick((ctx, clickedItem) -> {
                    ctx.player().openInventory(new GUIRecipe(item, this, safeRecipeIndex + 1));
                    return true;
                })
                .build());

        // Previous Recipe Button
        attachItem(GUIItem.builder(14)
                .item(ItemStackCreator.getStack(
                        "§aPrevious Recipe", Material.ARROW, 1,
                        "§7Click to view the previous recipe!"
                ).build())
                .requireState(STATE_HAS_PREVIOUS)
                .onClick((ctx, clickedItem) -> {
                    ctx.player().openInventory(new GUIRecipe(item, this, safeRecipeIndex - 1));
                    return true;
                })
                .build());

        // Result Item
        attachItem(GUIItem.builder(25)
                .item(() -> PlayerItemUpdater.playerUpdate(player, recipe.getResult().getItemStack())
                        .amount(recipe.getResult().getAmount())
                        .build())
                .build());

        // Recipe Ingredients
        SkyBlockItem[] ingredients = recipe.getRecipeDisplay();
        int slot = 0;

        for (int craftSlot : CRAFT_SLOTS) {
            if (slot < ingredients.length) {
                SkyBlockItem ingredient = ingredients[slot];
                if (ingredient != null) {
                    attachItem(createIngredientItem(craftSlot, ingredient));
                } else {
                    attachItem(GUIItem.builder(craftSlot)
                            .item(ItemStack.builder(Material.AIR).build())
                            .build());
                }
            }
            slot++;
        }
    }

    private GUIItem createIngredientItem(int slot, SkyBlockItem ingredient) {
        return GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(owner, ingredient.getItemStack());

                    if (ingredient.hasComponent(CraftableComponent.class)) {
                        ArrayList<Component> lore = new ArrayList<>(builder.build().get(ItemComponent.LORE));
                        lore.add(Component.text(" "));
                        lore.add(Component.text("§eClick to view recipe!"));
                        builder.set(ItemComponent.LORE, lore);
                    }

                    if (ingredient.getAttributeHandler().shouldBeEnchanted()) {
                        ItemStackCreator.enchant(builder);
                    }

                    return builder.build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (ingredient.hasComponent(CraftableComponent.class)) {
                        ctx.player().openInventory(new GUIRecipe(ingredient, this, 0));
                    }
                    return true;
                })
                .build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}