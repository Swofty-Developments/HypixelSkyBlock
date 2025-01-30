package net.swofty.type.hub.gui.kat;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.PetComponent;
import net.swofty.types.generic.item.handlers.pet.KatUpgrade;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIConfirmKat extends SkyBlockAbstractInventory {
    private static final String STATE_UPGRADEABLE = "upgradeable";
    private static final String STATE_NOT_UPGRADEABLE = "not_upgradeable";

    private final SkyBlockItem pet;

    public GUIConfirmKat(SkyBlockItem pet) {
        super(InventoryType.CHEST_3_ROW);
        this.pet = pet;
        doAction(new SetTitleAction(Component.text("Confirm Hiring Kat")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Determine upgrade availability
        boolean canUpgrade = pet.getComponent(PetComponent.class).getKatUpgrades()
                .getForRarity(pet.getAttributeHandler().getRarity().upgrade()) != null;
        doAction(new AddStateAction(canUpgrade ? STATE_UPGRADEABLE : STATE_NOT_UPGRADEABLE));

        // Confirm button
        attachItem(GUIItem.builder(11)
                .item(() -> {
                    if (!canUpgrade) {
                        return ItemStackCreator.getStack("§aSomething went wrong!",
                                Material.RED_TERRACOTTA, 1).build();
                    }

                    KatUpgrade katUpgrade = pet.getComponent(PetComponent.class).getKatUpgrades()
                            .getForRarity(pet.getAttributeHandler().getRarity().upgrade());
                    long time = katUpgrade.getTime();

                    return ItemStackCreator.getStack("§aConfirm", Material.GREEN_TERRACOTTA, 1,
                            "§cWARNING: You will not be able to",
                            "§cretrieve your pet for " + StringUtility.formatTimeLeftWrittenOut(time) + " and its",
                            "§clevel will change as a result of the",
                            "§crarity upgrade.").build();
                })
                .requireState(STATE_UPGRADEABLE)
                .onClick((ctx, item) -> {
                    KatUpgrade katUpgrade = pet.getComponent(PetComponent.class).getKatUpgrades()
                            .getForRarity(pet.getAttributeHandler().getRarity().upgrade());

                    int coins = katUpgrade.getCoins();
                    long time = katUpgrade.getTime();
                    long timeWhenFinished = time + System.currentTimeMillis();

                    player.getKatData().setKatMap(timeWhenFinished, pet);
                    player.setCoins(player.getCoins() - coins);
                    player.closeInventory();
                    return true;
                })
                .build());

        // Cancel button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§cCancel", Material.RED_TERRACOTTA, 1).build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    ctx.player().addAndUpdateItem(pet);
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
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}