package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.MinionComponent;
import net.swofty.types.generic.item.components.MinionFuelComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.MinionExtensions;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUIMinion extends SkyBlockAbstractInventory {
    private static final int[] SLOTS = new int[]{
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
            39, 40, 41, 42, 43
    };
    private final IslandMinionData.IslandMinion minion;

    public GUIMinion(IslandMinionData.IslandMinion minion) {
        super(InventoryType.CHEST_6_ROW);
        this.minion = minion;

        doAction(new SetTitleAction(Component.text(minion.getMinion().getDisplay() + " " +
                StringUtility.getAsRomanNumeral(minion.getTier()))));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        if (player.isCoop()) {
            CoopDatabase.Coop coop = player.getCoop();
            coop.getOnlineMembers().forEach(member -> {
                if (member.getUuid() == player.getUuid()) return;

                if (SkyBlockAbstractInventory.GUI_MAP.containsKey(member.getUuid())) {
                    if (SkyBlockAbstractInventory.GUI_MAP.get(member.getUuid()) instanceof GUIMinion) {
                        player.closeInventory();
                        player.sendMessage("§cYou can't open this inventory while a coop member has it open!");
                    }
                }
            });
        }

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());
        setupPickupButton();
        setupCollectAllButton();
        setupLayoutButton();
        setupMinionInfoDisplay();
        setupNextTierButton();
        setupExtensionSlots();
        setupStorageSlots();

        startLoop("refresh", 5, () -> refreshItems(player));
    }

    private void setupPickupButton() {
        attachItem(GUIItem.builder(53)
                .item(ItemStackCreator.getStack("§aPickup Minion", Material.BEDROCK, 1,
                        "§eClick to pickup!").build())
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    player.closeInventory();

                    player.addAndUpdateItem(minion.asSkyBlockItem());
                    minion.getItemsInMinion().forEach(minionItem ->
                            player.addAndUpdateItem(minionItem.toSkyBlockItem()));

                    for (SkyBlockItem upgradeItem : minion.getExtensionData().getMinionUpgrades()) {
                        player.addAndUpdateItem(upgradeItem);
                    }

                    player.addAndUpdateItem(minion.getExtensionData().getMinionSkin());
                    player.addAndUpdateItem(minion.getExtensionData().getAutomatedShipping());

                    if (minion.getExtensionData().getFuel() != null) {
                        long timeFuelLasts = minion.getExtensionData().getFuel()
                                .getComponent(MinionFuelComponent.class).getFuelLastTimeInMS();
                        if (timeFuelLasts == 0) {
                            player.addAndUpdateItem(minion.getExtensionData().getFuel());
                        }
                    }

                    minion.removeMinion();
                    player.getSkyBlockIsland().getMinionData().getMinions().remove(minion);

                    player.sendMessage("§aYou picked up a minion! You currently have " +
                            player.getSkyBlockIsland().getMinionData().getMinions().size() +
                            " out of a maximum of " + player.getDataHandler().get(
                            DataHandler.Data.MINION_DATA,
                            DatapointMinionData.class).getValue().getSlots()
                            + " minions placed.");
                    return true;
                })
                .build());
    }

    private void setupCollectAllButton() {
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aCollect All", Material.CHEST, 1,
                        "§eClick to collect all items!").build())
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    if (minion.getItemsInMinion().isEmpty()) {
                        player.sendMessage("§cThis Minion does not have any items stored!");
                        return true;
                    }

                    minion.getItemsInMinion().forEach(minionItem -> {
                        SkyBlockItem skyBlockItem = minionItem.getItem();
                        skyBlockItem.setAmount(minionItem.getAmount());
                        player.addAndUpdateItem(skyBlockItem);
                    });
                    minion.setItemsInMinion(new ArrayList<>());
                    refreshItems(player);
                    return true;
                })
                .build());
    }

    private void setupLayoutButton() {
        attachItem(GUIItem.builder(3)
                .item(ItemStackCreator.getStack("§aIdeal Layout", Material.REDSTONE_TORCH, 1,
                        "§7View the most efficient spot for this",
                        "§7minion to be placed in.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, unused) -> true)
                .build());
    }

    private void setupMinionInfoDisplay() {
        List<Component> lore = new ArrayList<>();
        MinionComponent.getLore(minion.asSkyBlockItem(), minion.getSpeedPercentage()).forEach(line -> {
            lore.add(Component.text("§r" + line.replace("&", "§"))
                    .decorations(Collections.singleton(TextDecoration.ITALIC), false));
        });
        ItemStack item = PlayerItemUpdater.playerUpdate(owner, minion.asSkyBlockItem().getItemStack())
                .set(ItemComponent.LORE, lore)
                .build();

        attachItem(GUIItem.builder(4)
                .item(item)
                .build());
    }

    private void setupNextTierButton() {
        List<SkyBlockMinion.MinionTier> minionTiers = minion.getMinion().asSkyBlockMinion().getTiers();
        int speedPercentage = minion.getSpeedPercentage();
        final DecimalFormat formatter = new DecimalFormat("#.##");

        ItemStack item = ItemStackCreator.getStack("§aNext Tier", Material.GOLD_INGOT, 1,
                "§7View the items required to upgrade",
                "§7this minion to the next tier.",
                " ",
                "§7Time Between Actions: §8" + formatter.format(minionTiers.get(minion.getTier() - 1).timeBetweenActions() / (1. + speedPercentage/100.)) + "s"
                + " §l> §a" + formatter.format(minionTiers.get(minion.getTier()).timeBetweenActions() / (1. + speedPercentage/100.)) + "s",
                "§7Max Storage: §8" + minionTiers.get(minion.getTier() - 1).storage() + " §l> " +
                "§e" + minionTiers.get(minion.getTier()).storage(),
                " ",
                "§eClick to view!").build();

        attachItem(GUIItem.builder(5)
                .item(item)
                .onClick((ctx, unused) -> {
                    ctx.player().openInventory(new GUIMinionRecipes(minion.getMinion(), null));
                    return true;
                })
                .build());
    }

    private void setupExtensionSlots() {
        MinionExtensionData extensionData = minion.getExtensionData();
        Arrays.stream(MinionExtensions.values()).forEach(extensionValue -> {
            for (int slot : extensionValue.getSlots()) {
                MinionExtension minionExtension = extensionData.getMinionExtension(slot);
                attachItem(minionExtension.getDisplayItem(minion, slot, this));
            }
        });
    }

    private void setupStorageSlots() {
        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers()
                .get(minion.getTier() - 1);

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;
            final int index = i;

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        if (!unlocked) return ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE).build();
                        if (minion.getItemsInMinion().size() < index) return ItemStack.AIR;

                        ItemQuantifiable item = minion.getItemsInMinion().get(index - 1);
                        SkyBlockItem skyBlockItem = item.getItem();
                        return new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem().build();
                    })
                    .onClick((ctx, clickedItem) -> {
                        if (!ctx.cursorItem().isAir()) {
                            ctx.player().sendMessage("§cYou can't put items in this inventory!");
                            return true;
                        }

                        if (!unlocked) return true;
                        if (minion.getItemsInMinion().size() < index) return true;

                        ItemQuantifiable item = minion.getItemsInMinion().get(index - 1);
                        minion.getItemsInMinion().remove(item);

                        item.getItem().setAmount(item.getAmount());
                        ctx.player().addAndUpdateItem(item.getItem());
                        refreshItems(ctx.player());
                        return true;
                    })
                    .build());
        }
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!player.getSkyBlockIsland().getMinionData().getMinions().contains(minion)) {
            player.closeInventory();
            return;
        }

        setupExtensionSlots();
        setupStorageSlots();
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {

    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }
}
