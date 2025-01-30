package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.Rarity;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.PetComponent;
import net.swofty.types.generic.item.components.PetItemComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIGeorge extends SkyBlockAbstractInventory {
    private static final String STATE_EMPTY = "empty";
    private static final String STATE_HAS_PET = "has_pet";
    private static final String STATE_INVALID_PET = "invalid_pet";
    private boolean pricePaid = false;

    public GUIGeorge() {
        super(InventoryType.CHEST_5_ROW);
        doAction(new SetTitleAction(Component.text("Offer Pets")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Close button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Initial state is empty
        doAction(new AddStateAction(STATE_EMPTY));
        setupEmptyState();
        setupPetState();
        setupInvalidState();
    }

    private void setupEmptyState() {
        // Empty pet slot
        attachItem(GUIItem.builder(13)
                .item(ItemStack.AIR)
                .requireState(STATE_EMPTY)
                .onClick((ctx, clickedItem) -> {
                    ItemStack cursorItem = ctx.cursorItem();
                    if (!cursorItem.isAir() && cursorItem.get(ItemComponent.CUSTOM_NAME) != null) {
                        handlePetUpdate(new SkyBlockItem(cursorItem));
                    }
                    return true;
                })
                .build());

        // Empty info slot
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§eOffer a Pet", Material.RED_TERRACOTTA, 1,
                        "§7Place a pet above and George will",
                        "§7tell you what he's willing to pay for it!").build())
                .requireState(STATE_EMPTY)
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cPlace a pet in the empty slot for George to evaluate it!");
                    return false;
                })
                .build());
    }

    private void setupPetState() {
        // Pet display slot
        attachItem(GUIItem.builder(13)
                .requireState(STATE_HAS_PET)
                .onClick((ctx, clickedItem) -> {
                    if (!clickedItem.isAir()) {
                        ctx.player().addAndUpdateItem(new SkyBlockItem(clickedItem));
                        transitionToEmpty();
                    }
                    return true;
                })
                .build());

        // Offer slot
        attachItem(GUIItem.builder(22)
                .requireState(STATE_HAS_PET)
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    SkyBlockItem petItem = new SkyBlockItem(getItemStack(13));

                    DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
                    Rarity rarity = petItem.getAttributeHandler().getRarity();
                    PetComponent petComponent = petItem.getComponent(PetComponent.class);
                    Integer price = petComponent.getGeorgePrice().getForRarity(rarity);

                    if (price == 0) return false;
                    coins.setValue(coins.getValue() + price);
                    pricePaid = true;
                    player.closeInventory();
                    return true;
                })
                .item(() -> {
                    SkyBlockItem petItem = new SkyBlockItem(getItemStack(13));
                    int price = petItem.getComponent(PetComponent.class)
                            .getGeorgePrice()
                            .getForRarity(petItem.getAttributeHandler().getRarity());

                    return ItemStackCreator.getStack(
                            "§aAccept Offer", Material.GREEN_TERRACOTTA, 1,
                            "§7George is willing to make an offer on",
                            "§7your pet!",
                            "",
                            "§9Offer:",
                            "§6" + StringUtility.commaify(price),
                            "",
                            "§7§cWARNING: This will permanently",
                            "§cremove your pet.",
                            "",
                            "§eClick to accept offer!").build();
                })
                .build());
    }

    private void setupInvalidState() {
        // Invalid pet display slot
        attachItem(GUIItem.builder(13)
                .requireState(STATE_INVALID_PET)
                .onClick((ctx, clickedItem) -> {
                    if (!clickedItem.isAir()) {
                        ctx.player().addAndUpdateItem(new SkyBlockItem(clickedItem));
                        transitionToEmpty();
                    }
                    return true;
                })
                .build());

        // Error message slot
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§cError!", Material.BARRIER, 1,
                        "§7George only wants to buy pets!").build())
                .requireState(STATE_INVALID_PET)
                .build());
    }

    private void handlePetUpdate(SkyBlockItem item) {
        clearStates();

        if (item == null) {
            doAction(new AddStateAction(STATE_EMPTY));
            return;
        }

        if (item.getAmount() > 1 || item.hasComponent(PetItemComponent.class)) {
            doAction(new AddStateAction(STATE_INVALID_PET));
            setItemStack(13, PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build());
        } else {
            doAction(new AddStateAction(STATE_HAS_PET));
            setItemStack(13, PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build());
        }
    }

    private void transitionToEmpty() {
        clearStates();
        doAction(new AddStateAction(STATE_EMPTY));
        setItemStack(13, ItemStack.AIR);
    }

    private void clearStates() {
        doAction(new RemoveStateAction(STATE_EMPTY));
        doAction(new RemoveStateAction(STATE_HAS_PET));
        doAction(new RemoveStateAction(STATE_INVALID_PET));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        if (reason == CloseReason.SERVER_EXITED && pricePaid) return;
        ((SkyBlockPlayer) event.getPlayer()).addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
    }
}