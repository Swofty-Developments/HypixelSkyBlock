package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.event.custom.ItemCraftEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUICrafting implements StatefulView<GUICrafting.CraftingState> {
    private static final ItemStack.Builder RECIPE_REQUIRED = ItemStackCreator.getStack("§cRecipe Required", Material.BARRIER, 1, "§7Add the items for a valid", "§7recipe in the crafting grid", "§7to the left!");
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 23;

    @Override
    public ViewConfiguration<CraftingState> configuration() {
        return new ViewConfiguration<>("Craft Item", InventoryType.CHEST_6_ROW);
    }

    @Override
    public CraftingState initialState() {
        return new CraftingState(0, null);
    }

    @Override
    public void layout(ViewLayout<CraftingState> layout, CraftingState state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        int currentHash = computeGridHash(ctx);
        SkyBlockRecipe<?> recipe = (state.lastParsedRecipe() != null && state.lastGridHash() == currentHash)
                ? state.lastParsedRecipe()
                : parseCurrentRecipe(ctx);

        boolean hasValidRecipe = recipe != null;
        SkyBlockRecipe.CraftingResult result = hasValidRecipe ? recipe.getCanCraft().apply(player) : null;
        boolean canCraft = hasValidRecipe && result != null && result.allowed();

        Material borderMaterial = canCraft ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        Components.fill(layout);
        layout.slots(Layouts.row(5), (s, c) -> ItemStackCreator.createNamedItemStack(borderMaterial));
        Components.close(layout, 49);

        Components.containerGrid(
                layout,
                10,
                30, (_, _, _, state1) -> {
                    int newHash = computeGridHash(ctx);
                    SkyBlockRecipe<?> newRecipe = SkyBlockRecipe.parseRecipe(getCurrentRecipeStacks(ctx));
                    if (state1.lastGridHash() == newHash && Objects.equals(state1.lastParsedRecipe(), newRecipe)) {
                        return;
                    }
                    ctx.session(CraftingState.class).setState(new CraftingState(newHash, newRecipe));
                }
        );

        if (!hasValidRecipe) {
            layout.slot(RESULT_SLOT, (s, c) -> RECIPE_REQUIRED);
        } else if (!canCraft) {
            layout.slot(RESULT_SLOT, (s, c) -> ItemStackCreator.getStack(result.errorMessage()[0],
                    Material.BEDROCK, 1,
                    Arrays.copyOfRange(result.errorMessage(), 1, result.errorMessage().length)));
        } else {
            int amount = recipe.getAmount();
            layout.slot(RESULT_SLOT, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(p, recipe.getResult().getItemStack()).amount(amount);

                ArrayList<String> lore = new ArrayList<>();
                var existingLore = builder.build().get(DataComponents.LORE);
                if (existingLore != null) {
                    existingLore.stream().map(line -> "§7" + StringUtility.getTextFromComponent(line)).forEach(lore::add);
                }
                lore.add("§8§m------------------");
                lore.add("§7This is the item you are crafting.");
                builder.set(DataComponents.LORE, lore.stream().map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()));

                return builder;
            }, (click, c) -> handleCraft(click, c, recipe, amount));
        }
    }

    private ItemStack[] getCurrentRecipeStacks(ViewContext ctx) {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            stacks[i] = ctx.inventory().getItemStack(CRAFT_SLOTS[i]);
        }
        return stacks;
    }

    private int computeGridHash(ViewContext ctx) {
        // Stable & cheap hash: material id + amount per slot (no streams/allocations beyond the 9 reads).
        int hash = 1;
        for (int slot : CRAFT_SLOTS) {
            ItemStack item = ctx.inventory().getItemStack(slot);
            int mat = item.material().id();
            hash = 31 * hash + (mat * 37 + item.amount());
        }
        return hash;
    }

    private SkyBlockRecipe<?> parseCurrentRecipe(ViewContext ctx) {
        return SkyBlockRecipe.parseRecipe(getCurrentRecipeStacks(ctx));
    }

    private void handleCraft(ClickContext<CraftingState> click, ViewContext ctx, SkyBlockRecipe<?> recipe, int amount) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        ItemStack cursorItemStack = player.getInventory().getCursorItem();
        SkyBlockItem cursorItem = new SkyBlockItem(cursorItemStack);
        ItemType cursorItemType = cursorItem.getAttributeHandler().getPotentialType();
        ItemType resultItemType = recipe.getResult().getAttributeHandler().getPotentialType();
        boolean isShift = click.click() instanceof Click.LeftShift || click.click() instanceof Click.RightShift;

        if (!cursorItemStack.isAir() && (cursorItemType == null || !cursorItemType.equals(resultItemType))) {
            return;
        }

        ItemStack craftedItem = PlayerItemUpdater.playerUpdate(player, recipe.getResult().getItemStack())
                .amount(amount)
                .build();
        int maxStackSize = craftedItem.material().maxStackSize();

        if (isShift) {
            int expectedGridHash = computeGridHash(ctx);

            while (true) {
                if (computeGridHash(ctx) != expectedGridHash) {
                    break;
                }

                SkyBlockRecipe.CraftingResult craftResult = recipe.getCanCraft().apply(player);
                if (craftResult == null || !craftResult.allowed()) {
                    break;
                }
                if (!player.canFitItem(craftedItem)) {
                    break;
                }

                SkyBlockItem[] currentItems = getCurrentRecipeAsItems(ctx);
                SkyBlockItem[] toReplace;
                try {
                    toReplace = recipe.consume(currentItems);
                } catch (Exception e) {
                    break;
                }

                applyConsumedGrid(player, ctx, toReplace);
                expectedGridHash = computeGridHash(ctx);

                player.addAndUpdateItem(craftedItem);
                HypixelEventHandler.callCustomEvent(
                        new ItemCraftEvent(player, new SkyBlockItem(craftedItem), recipe));
            }
        } else {
            int currentCursorAmount = cursorItemStack.isAir() ? 0 : cursorItemStack.amount();
            int newAmount = currentCursorAmount + amount;
            if (newAmount > maxStackSize) {
                return;
            }
            ItemStack newCursorItem = craftedItem.withAmount(newAmount);
            player.getInventory().setCursorItem(newCursorItem);

            HypixelEventHandler.callCustomEvent(new ItemCraftEvent(player, new SkyBlockItem(craftedItem), recipe));
            SkyBlockItem[] currentItems = getCurrentRecipeAsItems(ctx);
            SkyBlockItem[] toReplace = recipe.consume(currentItems);
            applyConsumedGrid(player, ctx, toReplace);
        }

        player.getInventory().update();
    }

    private void applyConsumedGrid(SkyBlockPlayer player, ViewContext ctx, SkyBlockItem[] toReplace) {
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            if (toReplace[i] == null || toReplace[i].getItemStack().material() == Material.BEDROCK) {
                ctx.inventory().setItemStack(CRAFT_SLOTS[i], ItemStack.AIR);
            } else {
                ctx.inventory().setItemStack(
                        CRAFT_SLOTS[i],
                        PlayerItemUpdater.playerUpdate(player, toReplace[i].getItemStack()).build()
                );
            }
        }
    }

    private SkyBlockItem[] getCurrentRecipeAsItems(ViewContext ctx) {
        SkyBlockItem[] stacks = new SkyBlockItem[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            stacks[i] = new SkyBlockItem(ctx.inventory().getItemStack(CRAFT_SLOTS[i]));
        }
        return stacks;
    }

    @Override
    public void onClose(CraftingState state, ViewContext ctx, ViewSession.CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            ItemStack item = ctx.inventory().getItemStack(slot);
            if (!item.isAir()) {
                player.addAndUpdateItem(new SkyBlockItem(item));
            }
        });
    }

    @Override
    public boolean onBottomClick(ClickContext<CraftingState> click, ViewContext ctx) {
        return true;
    }

    public record CraftingState(int lastGridHash, SkyBlockRecipe<?> lastParsedRecipe) {
    }
}

