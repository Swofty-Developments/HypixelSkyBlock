package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.ReforgableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

import java.util.HashMap;
import java.util.Map;

public class GUIReforge extends SkyBlockAbstractInventory {
    private static final Map<Rarity, Integer> COST_MAP = new HashMap<>();
    private final int[] borderSlots = {
            0, 8, 9, 17, 18, 26, 27, 35, 36, 44
    };

    static {
        COST_MAP.put(Rarity.COMMON, 250);
        COST_MAP.put(Rarity.UNCOMMON, 500);
        COST_MAP.put(Rarity.RARE, 1000);
        COST_MAP.put(Rarity.EPIC, 2500);
        COST_MAP.put(Rarity.LEGENDARY, 5000);
        COST_MAP.put(Rarity.MYTHIC, 10000);
    }

    public GUIReforge() {
        super(InventoryType.CHEST_5_ROW);
        doAction(new SetTitleAction(Component.text("Reforge Item")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return false;
                })
                .build());

        updateFromItem(null);
    }

    public void updateFromItem(SkyBlockItem item) {
        border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE).build());

        if (item == null) {
            setupEmptySlot();
            return;
        }

        setupItemSlot(item);

        if (item.getAmount() > 1 || !item.hasComponent(ReforgableComponent.class)) {
            attachItem(GUIItem.builder(22)
                    .item(ItemStackCreator.getStack(
                            "§cError!", Material.BARRIER, 1,
                            "§7You cannot reforge this item!").build())
                    .build());
            return;
        }

        border(ItemStackCreator.createNamedItemStack(Material.LIME_STAINED_GLASS_PANE).build());
        setupReforgeButton(item);
    }

    private void setupEmptySlot() {
        attachItem(GUIItem.builder(13)
                .item(ItemStack.AIR)
                .onClick((ctx, item) -> {
                    ItemStack stack = ctx.cursorItem();
                    if (stack.get(ItemComponent.CUSTOM_NAME) == null) {
                        updateFromItem(null);
                        return true;
                    }

                    updateFromItem(new SkyBlockItem(stack));
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§eReforge Item", Material.ANVIL, 1,
                        "§7Place an item above to reforge it!",
                        "§7Reforging items adds a random",
                        "§7modifier to the item that grants stat",
                        "§7boosts.").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cPlace an item in the empty slot above to reforge it!");
                    return true;
                })
                .build());
    }

    private void setupItemSlot(SkyBlockItem item) {
        attachItem(GUIItem.builder(13)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build())
                .onClick((ctx, clicked) -> {
                    if (clicked.isAir()) return true;
                    updateFromItem(null);
                    ctx.player().addAndUpdateItem(new SkyBlockItem(clicked));
                    return true;
                })
                .build());
    }

    private void setupReforgeButton(SkyBlockItem item) {
        attachItem(GUIItem.builder(22)
                .item(() -> ItemStackCreator.getStack(
                        "§eReforge Item", Material.ANVIL, 1,
                        "§7Reforges the above item, giving it a",
                        "§7random stat modifier that boosts its",
                        "§7stats.",
                        "§2 ",
                        "§7Cost",
                        "§6" + COST_MAP.get(item.getAttributeHandler().getRarity()) + " Coins",
                        "§2 ",
                        "§eClick to reforge!").build())
                .onClick((ctx, clicked) -> {
                    DatapointDouble coins = ctx.player().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
                    int cost = COST_MAP.get(item.getAttributeHandler().getRarity());

                    if (coins.getValue() - cost < 0) {
                        ctx.player().sendMessage("§cYou don't have enough Coins!");
                        return true;
                    }

                    coins.setValue(coins.getValue() - cost);

                    ReforgeType reforgeType = item.getComponent(ReforgableComponent.class).getReforgeType();
                    ReforgeType.Reforge reforge = reforgeType.getReforges()
                            .get(MathUtility.random(0, reforgeType.getReforges().size() - 1));
                    String oldPrefix = item.getAttributeHandler().getReforge() == null ? "" :
                            " " + item.getAttributeHandler().getReforge().prefix();

                    try {
                        item.getAttributeHandler().setReforge(reforge);
                    } catch (IllegalArgumentException ex) {
                        ctx.player().sendMessage("§c" + ex.getMessage());
                        return true;
                    }

                    String itemName = item.getDisplayName();
                    ctx.player().sendMessage("§aYou reforged your" +
                            item.getAttributeHandler().getRarity().getColor() + oldPrefix + " " + itemName + "§a into a " +
                            item.getAttributeHandler().getRarity().getColor() + reforge.prefix() + " " + itemName + "§a!");

                    updateFromItem(item);
                    return true;
                })
                .build());
    }

    private void border(ItemStack stack) {
        for (int slot : borderSlots) {
            attachItem(GUIItem.builder(slot)
                    .item(stack)
                    .build());
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        ((SkyBlockPlayer) e.getPlayer()).addAndUpdateItem(new SkyBlockItem(e.getInventory().getItemStack(13)));
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
    }
}
