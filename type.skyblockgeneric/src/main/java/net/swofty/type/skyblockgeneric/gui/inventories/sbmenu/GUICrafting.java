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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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
        SkyBlockRecipe<?> recipe = parseCurrentRecipe(ctx);
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
                30, (slot, oldItem, newItem, state1) -> {}
        );

        if (!hasValidRecipe) {
            layout.slot(RESULT_SLOT, (s, c) -> RECIPE_REQUIRED);
        } else if (!canCraft) {
            layout.slot(RESULT_SLOT, (s, c) -> ItemStackCreator.getStack(result.errorMessage()[0],
                    Material.BEDROCK, 1,
                    Arrays.copyOfRange(result.errorMessage(), 1, result.errorMessage().length)));
        } else {
            int amount = recipe.getAmount();
            SkyBlockRecipe<?> finalRecipe = recipe;

            layout.slot(RESULT_SLOT, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(p, finalRecipe.getResult().getItemStack()).amount(amount);

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
            }, (click, c) -> handleCraft(click, c, finalRecipe, amount));
        }
    }

    private SkyBlockRecipe<?> parseCurrentRecipe(ViewContext ctx) {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            stacks[i] = ctx.inventory().getItemStack(CRAFT_SLOTS[i]);
        }
        return SkyBlockRecipe.parseRecipe(stacks);
    }

    private void handleCraft(ClickContext<CraftingState> click, ViewContext ctx, SkyBlockRecipe<?> recipe, int amount) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
        ItemType cursorItemType = cursorItem.getAttributeHandler().getPotentialType();
        ItemType resultItemType = recipe.getResult().getAttributeHandler().getPotentialType();
        boolean isShift = click.click() instanceof Click.LeftShift || click.click() instanceof Click.RightShift;

        if (!player.getInventory().getCursorItem().isAir() &&
                (cursorItemType == null || !cursorItemType.equals(resultItemType))) {
            player.sendMessage("§cYou must empty your cursor first!");
            return;
        }

        ItemStack craftedItem = PlayerItemUpdater.playerUpdate(
                player,
                recipe.getResult().getItemStack()).amount(amount).build();

        if (isShift) {
            player.addAndUpdateItem(craftedItem);
        } else {
            player.getInventory().setCursorItem(craftedItem);
        }
        HypixelEventHandler.callCustomEvent(new ItemCraftEvent(player, new SkyBlockItem(craftedItem), recipe));

        SkyBlockItem[] currentItems = getCurrentRecipeAsItems(ctx);
        SkyBlockItem[] toReplace = recipe.consume(currentItems);
        for (int i = 0; i < CRAFT_SLOTS.length; i++) {
            if (toReplace[i] == null || toReplace[i].getItemStack().material() == Material.BEDROCK) {
                ctx.inventory().setItemStack(CRAFT_SLOTS[i], ItemStack.builder(Material.AIR).build());
            } else {
                ctx.inventory().setItemStack(CRAFT_SLOTS[i], PlayerItemUpdater.playerUpdate(
                        player,
                        toReplace[i].getItemStack()).build());
            }
        }

        if (cursorItemType != null && cursorItemType.equals(resultItemType) && !isShift) {
            player.addAndUpdateItem(cursorItem);
        }

        player.getInventory().update();
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

    public record CraftingState(int lastGridHash, SkyBlockRecipe<?> lastParsedRecipe) {}
}
