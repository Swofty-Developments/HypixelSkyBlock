package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.event.custom.ItemCraftEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUICrafting implements StatefulView<GUICrafting.CraftingState> {
    private static final String[] DEFAULT_CRAFT_ERROR = new String[]{"§cYou cannot craft this item right now."};
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 23;

    @Override
    public ViewConfiguration<CraftingState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.crafting.title", InventoryType.CHEST_6_ROW);
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
        String[] craftErrorMessages = getCraftErrorMessages(result);
        boolean canCraft = hasValidRecipe && result != null && result.allowed();

        Material borderMaterial = canCraft ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        Components.fill(layout);
        layout.slots(Layouts.row(5), (_, _) -> ItemStackCreator.createNamedItemStack(borderMaterial));
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
            layout.slot(RESULT_SLOT, (_, _) -> TranslatableItemStackCreator.getStack("gui_sbmenu.crafting.recipe_required", Material.BARRIER, 1, "gui_sbmenu.crafting.recipe_required.lore"));
        } else if (!canCraft) {
            layout.slot(RESULT_SLOT, (_, _) -> ItemStackCreator.getStack(craftErrorMessages[0],
                Material.BEDROCK, 1,
                Arrays.copyOfRange(craftErrorMessages, 1, craftErrorMessages.length)));
        } else {
            int amount = recipe.getAmount();
            layout.slot(RESULT_SLOT, (_, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(p, recipe.getResult().getItemStack()).amount(amount);

                ArrayList<Object> lore = new ArrayList<>();
                var existingLore = builder.build().get(DataComponents.LORE);
                if (existingLore != null) {
                    existingLore.stream().map(line -> "§7" + StringUtility.getTextFromComponent(line)).forEach(lore::add);
                }
                lore.addAll(Arrays.asList(I18n.iterable("gui_sbmenu.crafting.crafting_item.lore")));
                builder.set(DataComponents.LORE, ItemStackCreator.literalLoreComponents(lore).stream().map(line -> line.decoration(TextDecoration.ITALIC, false))
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
        int hash = 1;
        for (int slot : CRAFT_SLOTS) {
            ItemStack item = ctx.inventory().getItemStack(slot);
            hash = 31 * hash + item.material().id();
            hash = 31 * hash + item.amount();
            String itemType = item.getTag(Tag.String("item_type"));
            hash = 31 * hash + (itemType != null ? itemType.hashCode() : 0);
        }
        return hash;
    }

    private String[] getCraftErrorMessages(SkyBlockRecipe.CraftingResult result) {
        if (result == null || result.errorMessage() == null || result.errorMessage().length == 0) {
            return DEFAULT_CRAFT_ERROR;
        }
        return result.errorMessage();
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

        SkyBlockRecipe.CraftingResult permissionResult = recipe.getCanCraft().apply(player);
        if (permissionResult == null || !permissionResult.allowed()) {
            return;
        }

        if (!cursorItemStack.isAir() && (cursorItemType == null || !cursorItemType.equals(resultItemType))) {
            return;
        }

        ItemStack craftedItem = PlayerItemUpdater.playerUpdate(player, recipe.getResult().getItemStack())
            .amount(amount)
            .build();
        int maxStackSize = craftedItem.material().maxStackSize();
        boolean crafted = false;

        if (isShift) {
            int maxCraftsByInventory = getMaxCraftsByInventory(player, craftedItem, amount);
            SkyBlockItem[] simulatedItems = getCurrentRecipeAsItems(ctx);
            int craftedCount = 0;

            while (craftedCount < maxCraftsByInventory) {
                SkyBlockRecipe.CraftingResult craftResult = recipe.getCanCraft().apply(player);
                if (craftResult == null || !craftResult.allowed()) {
                    break;
                }

                try {
                    simulatedItems = recipe.consume(simulatedItems);
                } catch (Exception e) {
                    break;
                }

                craftedCount++;
            }

            int totalCraftedAmount = craftedCount * amount;
            if (totalCraftedAmount > 0) {
                applyConsumedGrid(player, ctx, simulatedItems);
                addCraftedOutput(player, craftedItem, totalCraftedAmount);
                crafted = true;

                HypixelEventHandler.callCustomEvent(
                    new ItemCraftEvent(player, new SkyBlockItem(craftedItem.withAmount(totalCraftedAmount)), recipe));
            }
        } else {
            int currentCursorAmount = cursorItemStack.isAir() ? 0 : cursorItemStack.amount();
            int newAmount = currentCursorAmount + amount;
            if (newAmount > maxStackSize) {
                return;
            }

            SkyBlockItem[] currentItems = getCurrentRecipeAsItems(ctx);
            SkyBlockItem[] toReplace;
            try {
                toReplace = recipe.consume(currentItems);
            } catch (Exception e) {
                return;
            }
            applyConsumedGrid(player, ctx, toReplace);

            ItemStack newCursorItem = craftedItem.withAmount(newAmount);
            player.getInventory().setCursorItem(newCursorItem);
            crafted = true;

            HypixelEventHandler.callCustomEvent(new ItemCraftEvent(player, new SkyBlockItem(craftedItem), recipe));
        }

        if (crafted) {
            syncCraftingState(ctx);
        }

        ctx.inventory().update();
        player.getInventory().update();
    }

    private int getMaxCraftsByInventory(SkyBlockPlayer player, ItemStack craftedItem, int amountPerCraft) {
        if (amountPerCraft <= 0) {
            return 0;
        }

        int maxStackSize = craftedItem.material().maxStackSize();
        int availableSpace = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (stack.isAir()) {
                availableSpace += maxStackSize;
                continue;
            }
            if (stack.isSimilar(craftedItem) && stack.amount() < maxStackSize) {
                availableSpace += maxStackSize - stack.amount();
            }
        }

        return availableSpace / amountPerCraft;
    }

    private void addCraftedOutput(SkyBlockPlayer player, ItemStack craftedItem, int totalAmount) {
        player.addAndUpdateItem(craftedItem.withAmount(totalAmount));
    }

    private void syncCraftingState(ViewContext ctx) {
        int newHash = computeGridHash(ctx);
        SkyBlockRecipe<?> newRecipe = SkyBlockRecipe.parseRecipe(getCurrentRecipeStacks(ctx));
        ctx.session(CraftingState.class).setState(new CraftingState(newHash, newRecipe));
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

