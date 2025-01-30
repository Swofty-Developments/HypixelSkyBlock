package net.swofty.type.hub.gui.kat;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.PetComponent;
import net.swofty.types.generic.item.handlers.pet.KatUpgrade;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;

public class GUIKat extends SkyBlockAbstractInventory {
    private static final String STATE_NO_PET = "no_pet";
    private static final String STATE_INVALID_PET = "invalid_pet";
    private static final String STATE_VALID_PET = "valid_pet";
    private static final String STATE_CAN_UPGRADE = "can_upgrade";

    private boolean pricePaid = false;

    public GUIKat() {
        super(InventoryType.CHEST_5_ROW);
        doAction(new SetTitleAction(Component.text("Pet Sitter")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        doAction(new AddStateAction(STATE_NO_PET));

        // Close button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Pet slot
        attachItem(GUIItem.builder(13)
                .item(ItemStack.AIR)
                .requireState(STATE_NO_PET)
                .onClick((ctx, item) -> {
                    ItemStack cursorItem = ctx.cursorItem();
                    if (!cursorItem.isAir()) {
                        updatePetItem(new SkyBlockItem(cursorItem), ctx.player());
                    }
                    return true;
                })
                .build());

        // Initial message
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§ePet Sitter", Material.RED_TERRACOTTA, 1,
                        "§7Place a pet above for Kat to take",
                        "§7care of!",
                        "",
                        "§7After some time, your pet §9Rarity §7will",
                        "§7be upgraded!").build())
                .requireState(STATE_NO_PET)
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cPlace a pet in the empty slot for Kat to take care of!");
                    return true;
                })
                .build());

        // Error message for invalid pets
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§cError!", Material.BARRIER, 1,
                        "§cKat only takes care of pets!").build())
                .requireState(STATE_INVALID_PET)
                .build());
    }

    private void updatePetItem(SkyBlockItem item, SkyBlockPlayer player) {
        // Remove all states
        doAction(new RemoveStateAction(STATE_NO_PET));
        doAction(new RemoveStateAction(STATE_INVALID_PET));
        doAction(new RemoveStateAction(STATE_VALID_PET));
        doAction(new RemoveStateAction(STATE_CAN_UPGRADE));

        if (item == null) {
            doAction(new AddStateAction(STATE_NO_PET));
            return;
        }

        // Current pet display
        attachItem(GUIItem.builder(13)
                .item(PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build())
                .onClick((ctx, clickedItem) -> {
                    if (!clickedItem.isAir()) {
                        player.addAndUpdateItem(new SkyBlockItem(clickedItem));
                        updatePetItem(null, player);
                    }
                    return true;
                })
                .build());

        if (item.getAmount() > 1 || !item.hasComponent(PetComponent.class)) {
            doAction(new AddStateAction(STATE_INVALID_PET));
            return;
        }

        doAction(new AddStateAction(STATE_VALID_PET));
        setupUpgradeButton(item, player);
    }

    private void setupUpgradeButton(SkyBlockItem item, SkyBlockPlayer player) {
        PetComponent petComponent = item.getComponent(PetComponent.class);
        KatUpgrade upgrade = petComponent.getKatUpgrades().getForRarity(item.getAttributeHandler().getRarity().upgrade());

        if (upgrade == null) {
            attachItem(GUIItem.builder(22)
                    .item(ItemStackCreator.getStack("§aSomething went wrong!", Material.RED_TERRACOTTA, 1).build())
                    .requireState(STATE_VALID_PET)
                    .build());
            return;
        }

        boolean canAfford = player.getCoins() >= upgrade.getCoins() &&
                player.getAmountInInventory(upgrade.getItem()) >= upgrade.getAmount();

        if (canAfford) {
            doAction(new AddStateAction(STATE_CAN_UPGRADE));
        }

        attachItem(GUIItem.builder(22)
                .item(() -> {
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7Kat will take care of your §5" + item.getDisplayName());
                    lore.add("§7for §9" + StringUtility.formatTimeLeftWrittenOut(upgrade.getTime()) + "§7 then its §9rarity§7 will be");
                    lore.add("§7upgraded!");
                    lore.add("");
                    lore.add("§7Cost");

                    if (upgrade.getItem() != null) {
                        lore.add("§9" + StringUtility.toNormalCase(upgrade.getItem().name()) + " §8x" + upgrade.getAmount());
                    }
                    if (upgrade.getCoins() != 0) {
                        lore.add("§6" + StringUtility.commaify(upgrade.getCoins()) + " Coins");
                    }

                    if (canAfford) {
                        lore.add("");
                        lore.add("§eClick to hire Kat!");
                    } else if (player.getCoins() < upgrade.getCoins()) {
                        lore.add("");
                        lore.add("§cYou don't have enough Coins!");
                    } else {
                        lore.add("");
                        lore.add("§cYou don't have the required items!");
                    }

                    return ItemStackCreator.getStack("§aHire Kat",
                            canAfford ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA,
                            1, lore).build();
                })
                .requireState(STATE_VALID_PET)
                .onClick((ctx, clickedItem) -> {
                    if (canAfford) {
                        ctx.player().openInventory(new GUIConfirmKat(item));
                    }
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        if (reason == CloseReason.SERVER_EXITED && pricePaid) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
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