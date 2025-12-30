package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.SoulflowComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIConsumeSoulflow extends HypixelInventoryGUI {

    private final SkyBlockItem item;

    public GUIConsumeSoulflow(SkyBlockItem item) {
        super("Consume Soulflow?", InventoryType.CHEST_4_ROW);
        this.item = item;

        if (!item.hasComponent(SoulflowComponent.class)) {
            throw new IllegalArgumentException("Item does not have SoulflowComponent");
        }
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockDataHandler data = player.getSkyblockDataHandler();
                int soulflow = data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).getValue();
                int itemSoulflow = item.getComponent(SoulflowComponent.class).getAmount();
                int addition = item.getAmount() * itemSoulflow;

                data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).setValue(soulflow + addition);
                player.sendMessage("§bYou internalized §3+" + addition + "⸎ Soulflow §band have a total of §3" + (soulflow + addition) + "⸎§b!");

                player.getInventory().setItemStack(player.getHeldSlot(), ItemStack.AIR);
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockDataHandler data = player.getSkyblockDataHandler();
                int soulflow = data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).getValue();

                int itemSoulflow = item.getComponent(SoulflowComponent.class).getAmount();
                int addition = item.getAmount() * itemSoulflow;

                return ItemStackCreator.getStackHead(
                        "§aConsume Soulflow?",
                        "94f0c693b85658b0bae792c9f9b717eb024ab8c4b349455648ea08358b50ddc4",
                        1,
                        "§7Takes all the §3⸎ Soulflow §7items in",
                        "§7your inventory and internalizes them",
                        "§7to be ready for use.",
                        "",
                        "§7Internalized: §3" + soulflow + "⸎",
                        "",
                        "§7Adding from inventory: §3+" + addition + "⸎ Soulflow",
                        "",
                        "§eClick to consume!"
                );
            }
        });
        set(GUIClickableItem.getCloseItem(31));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
