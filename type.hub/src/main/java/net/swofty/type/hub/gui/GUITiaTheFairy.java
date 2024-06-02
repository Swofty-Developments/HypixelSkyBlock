package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.data.datapoints.DatapointFairySouls;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.fairysouls.FairySoulExchangeLevels;

import java.util.ArrayList;
import java.util.List;

public class GUITiaTheFairy extends SkyBlockInventoryGUI {
    public GUITiaTheFairy() {
        super("Fairy", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        int collectedAmount = getPlayer().getFairySouls().getCollectedFairySouls().size();
        boolean canExchange = collectedAmount >= 5;
        FairySoulExchangeLevels nextLevel = getPlayer().getFairySouls().getNextExchangeLevel();

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (!canExchange) {
                    player.sendMessage("§cYou don't have enough Fairy Souls!");
                    return;
                }

                player.closeInventory();
                player.getFairySouls().exchange();
                player.getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointFairySouls.class)
                                .setValue(player.getFairySouls());
                player.sendMessage("§aYou have exchanged your Fairy Souls for rewards!");
                nextLevel.getDisplay().forEach(player::sendMessage);

                player.getSkyBlockExperience().addExperience(
                        SkyBlockLevelCause.getFairySoulExchangeCause(nextLevel.ordinal())
                );

                DatapointBackpacks.PlayerBackpacks backpacks = getPlayer().getDataHandler().get(
                        DataHandler.Data.BACKPACKS, DatapointBackpacks.class
                ).getValue();
                backpacks.setUnlockedSlots(backpacks.getUnlockedSlots() +
                        nextLevel.getBackpackSlots());
                getPlayer().getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class)
                        .setValue(backpacks);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                        (canExchange ? "§eClick to exchange!" : "§cYou don't have enough Fairy Souls!"  )
                ));

                return ItemStackCreator.getStackHead("§aExchange Fairy Souls",
                        "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387",
                        1, lore);
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
