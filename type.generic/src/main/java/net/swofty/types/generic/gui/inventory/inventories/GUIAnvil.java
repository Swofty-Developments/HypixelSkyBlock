package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.AnvilCombinableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIAnvil extends SkyBlockAbstractInventory {
    private final int[] borderSlots = {
            45, 46, 47, 48, 50, 51, 52, 53
    };

    private final int upgradeItemSlot = 29;
    private final int[] upgradeItemSlots = {
            11, 12, 20
    };

    private final int sacrificeItemSlot = 33;
    private final int[] sacrificeItemSlots = {
            14, 15, 24
    };

    private final int resultSlot = 13;

    public GUIAnvil() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Anvil")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        fill(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE, "").build(), 45, 53);

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Result slot
        attachItem(GUIItem.builder(resultSlot)
                .item(ItemStackCreator.getStack("§cAnvil", Material.BARRIER, 1,
                        "§7Place a target item in the left slot",
                        "§7and a sacrifice item in the right slot",
                        "§7to combine them!").build())
                .build());

        // Combine items text
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1,
                        "§7Combine the items in the slots to the",
                        "§7left and right below.").build())
                .build());

        updateItemToUpgrade(null);
        updateItemToSacrifice(null);
        updateItemToCraft();
    }

    private void updateItemToUpgrade(SkyBlockItem item) {
        if (item == null) {
            attachItem(GUIItem.builder(upgradeItemSlot)
                    .item(ItemStack.AIR)
                    .onClick((ctx, clickedItem) -> {
                        ItemStack stack = ctx.cursorItem();

                        if (stack.get(ItemComponent.CUSTOM_NAME) == null) {
                            updateItemToUpgrade(null);
                            return true;
                        }

                        giveResult(ctx.player());
                        updateItemToUpgrade(new SkyBlockItem(stack));
                        return true;
                    })
                    .build());

            updateItemToCraft();
            return;
        }

        attachItem(GUIItem.builder(upgradeItemSlot)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build())
                .onClick((ctx, clickedItem) -> {
                    if (clickedItem.isAir()) return true;

                    updateItemToUpgrade(null);
                    ctx.player().addAndUpdateItem(new SkyBlockItem(clickedItem));
                    return true;
                })
                .build());

        updateItemToCraft();
    }

    private void updateItemToUpgradeValid(Material material) {
        ItemStack.Builder stack = ItemStackCreator.getStack("§6Item to Upgrade", material, 1,
                "§7The item you want to upgrade should",
                "§7be placed in the slot on this side.");

        for (int slot : upgradeItemSlots) {
            attachItem(GUIItem.builder(slot)
                    .item(stack.build())
                    .build());
        }
    }

    private void updateItemToSacrifice(SkyBlockItem item) {
        if (item == null) {
            attachItem(GUIItem.builder(sacrificeItemSlot)
                    .item(ItemStack.AIR)
                    .onClick((ctx, clickedItem) -> {
                        ItemStack stack = ctx.cursorItem();

                        if (stack.get(ItemComponent.CUSTOM_NAME) == null) {
                            updateItemToSacrifice(null);
                            return true;
                        }

                        giveResult(ctx.player());
                        updateItemToSacrifice(new SkyBlockItem(stack));
                        return true;
                    })
                    .build());

            updateItemToCraft();
            return;
        }

        attachItem(GUIItem.builder(sacrificeItemSlot)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build())
                .onClick((ctx, clickedItem) -> {
                    if (clickedItem.isAir()) return true;

                    updateItemToSacrifice(null);
                    ctx.player().addAndUpdateItem(new SkyBlockItem(clickedItem));
                    return true;
                })
                .build());

        updateItemToCraft();
    }

    private void updateItemToSacrificeValid(Material material) {
        ItemStack.Builder stack = ItemStackCreator.getStack("§6Item to Sacrifice", material, 1,
                "§7The item you are sacrificing in order",
                "§7to upgrade the item on the left",
                "§7should be placed in the slot on this",
                "§7side.");

        for (int slot : sacrificeItemSlots) {
            attachItem(GUIItem.builder(slot)
                    .item(stack.build())
                    .build());
        }
    }

    private void updateItemToCraft() {
        SkyBlockItem upgradeItem = new SkyBlockItem(getItemStack(upgradeItemSlot));
        SkyBlockItem sacrificeItem = new SkyBlockItem(getItemStack(sacrificeItemSlot));

        boolean isUpgradeItemValid = !(upgradeItem.isAir() || upgradeItem.isNA());
        boolean isSacrificeItemValid = !(sacrificeItem.isAir() || sacrificeItem.isNA());

        boolean canCraft = isUpgradeItemValid && isSacrificeItemValid &&
                sacrificeItem.hasComponent(AnvilCombinableComponent.class) &&
                sacrificeItem.getComponent(AnvilCombinableComponent.class).canApply(owner, upgradeItem, sacrificeItem);

        updateItemToSacrificeValid(canCraft || (isSacrificeItemValid && !isUpgradeItemValid) ?
                Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        updateItemToUpgradeValid(canCraft || (!isSacrificeItemValid && isUpgradeItemValid) ?
                Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

        for (int slot : borderSlots) {
            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.createNamedItemStack(
                            canCraft ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).build())
                    .build());
        }

        if (!canCraft) {
            attachItem(GUIItem.builder(13)
                    .item(ItemStackCreator.getStack(
                            isUpgradeItemValid && isSacrificeItemValid ? "§cError!" : "§cAnvil",
                            Material.BARRIER, 1,
                            isUpgradeItemValid && isSacrificeItemValid ?
                                    "§7You can not combine those Items" :
                                    "§7Place a target item in the left slot",
                            "§7and a sacrifice item in the right slot",
                            "§7to combine them!").build())
                    .build());

            attachItem(GUIItem.builder(22)
                    .item(ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1,
                            "§7Combine the items in the slots to the",
                            "§7left and right below.").build())
                    .build());
            return;
        }

        SkyBlockItem result = new SkyBlockItem(getItemStack(upgradeItemSlot));
        sacrificeItem.getComponent(AnvilCombinableComponent.class).apply(result, sacrificeItem);

        attachItem(GUIItem.builder(13)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, result.getItemStack()).build())
                .build());

        int levelCost = sacrificeItem.getComponent(AnvilCombinableComponent.class)
                .applyCostLevels(upgradeItem, sacrificeItem, owner);

        List<String> lore = new ArrayList<>();
        lore.add("§7Combine the items in the slots to the");
        lore.add("§7left and right below.");
        if (levelCost > 0) {
            lore.add("");
            lore.add("§7Cost");
            lore.add("§9" + levelCost + " Exp Levels");
        }
        lore.add("");
        lore.add("§eClick to combine!");

        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1, lore).build())
                .onClick((ctx, clickedItem) -> {
                    craftResult(ctx.player());
                    return true;
                })
                .build());
    }

    private void craftResult(SkyBlockPlayer player) {
        SkyBlockItem sacrificeItem = new SkyBlockItem(getItemStack(sacrificeItemSlot));
        int requiredLevels = sacrificeItem.getComponent(AnvilCombinableComponent.class)
                .applyCostLevels(new SkyBlockItem(getItemStack(upgradeItemSlot)), sacrificeItem, player);

        if (player.getLevel() < requiredLevels) {
            player.sendMessage("§cYou don't have enough Experience Levels!");
            return;
        }

        player.setLevel(player.getLevel() - requiredLevels);
        SkyBlockItem result = new SkyBlockItem(getItemStack(resultSlot));

        updateItemToUpgrade(null);
        updateItemToSacrifice(null);

        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§aAnvil", Material.OAK_SIGN, 1,
                        "§7Claim the result item above!").build())
                .build());

        attachItem(GUIItem.builder(resultSlot)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, result.getItemStack()).build())
                .onClick((ctx, clickedItem) -> {
                    giveResult(ctx.player());
                    player.openInventory(new GUIAnvil());
                    return true;
                })
                .build());
    }

    private void giveResult(SkyBlockPlayer player) {
        List<GUIItem> items = getItemsInSlot(resultSlot);
        if (!items.isEmpty() && items.get(0).getStateRequirements().isEmpty()) {
            player.addAndUpdateItem(new SkyBlockItem(getItemStack(resultSlot)));

            attachItem(GUIItem.builder(resultSlot)
                    .item(ItemStackCreator.getStack("§cAnvil", Material.BARRIER, 1,
                            "§7Place a target item in the left slot",
                            "§7and a sacrifice item in the right slot",
                            "§7to combine them!").build())
                    .build());
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(sacrificeItemSlot)));
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(upgradeItemSlot)));
        giveResult(player);
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(false);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(sacrificeItemSlot)));
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(upgradeItemSlot)));
        giveResult(player);
    }
}