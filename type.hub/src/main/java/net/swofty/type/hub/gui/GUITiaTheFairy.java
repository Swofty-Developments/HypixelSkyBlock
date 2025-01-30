package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.data.datapoints.DatapointFairySouls;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.fairysouls.FairySoulExchangeLevels;

import java.util.ArrayList;
import java.util.List;

public class GUITiaTheFairy extends SkyBlockAbstractInventory {
    private static final String STATE_CAN_EXCHANGE = "can_exchange";
    private static final String STATE_CANNOT_EXCHANGE = "cannot_exchange";

    public GUITiaTheFairy() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Fairy")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        int collectedAmount = player.getFairySouls().getCollectedFairySouls().size();
        boolean canExchange = collectedAmount >= 5;
        FairySoulExchangeLevels nextLevel = player.getFairySouls().getNextExchangeLevel();

        // Set state based on exchange availability
        if (canExchange) {
            doAction(new AddStateAction(STATE_CAN_EXCHANGE));
        } else {
            doAction(new AddStateAction(STATE_CANNOT_EXCHANGE));
        }

        // Exchange button
        attachItem(GUIItem.builder(22)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(
                            "§7Find §dFairy Souls §7around the",
                            "§7world and bring them back to me",
                            "§7and I will reward you with",
                            "§7SkyBlock XP and Backpack Slots!",
                            "",
                            "§7Fairy Souls: " + (canExchange ? "§a" : "§e") + collectedAmount + "§7/§d5",
                            "",
                            "§7Next Reward:"
                    ));

                    nextLevel.getDisplay().forEach(s -> lore.add("§7" + s));

                    lore.addAll(List.of(
                            "",
                            (canExchange ? "§eClick to exchange!" : "§cYou don't have enough Fairy Souls!")
                    ));

                    return ItemStackCreator.getStackHead("§aExchange Fairy Souls",
                            "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387",
                            1, lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (!hasState(STATE_CAN_EXCHANGE)) {
                        ctx.player().sendMessage("§cYou don't have enough Fairy Souls!");
                        return false;
                    }

                    processFairyExchange(ctx.player(), nextLevel);
                    return true;
                })
                .build());
    }

    private void processFairyExchange(SkyBlockPlayer player, FairySoulExchangeLevels nextLevel) {
        player.closeInventory();
        player.getFairySouls().exchange();
        player.getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointFairySouls.class)
                .setValue(player.getFairySouls());
        player.sendMessage("§aYou have exchanged your Fairy Souls for rewards!");
        nextLevel.getDisplay().forEach(player::sendMessage);

        player.getSkyBlockExperience().addExperience(
                SkyBlockLevelCause.getFairySoulExchangeCause(nextLevel.ordinal())
        );

        DatapointBackpacks.PlayerBackpacks backpacks = player.getDataHandler().get(
                DataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();
        backpacks.setUnlockedSlots(backpacks.getUnlockedSlots() + nextLevel.getBackpackSlots());
        player.getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class)
                .setValue(backpacks);
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