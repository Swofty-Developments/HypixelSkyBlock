package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;

public class GUIMinisterMenu extends HypixelInventoryGUI {

    public GUIMinisterMenu() {
        super("Minister", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockMayor minister = ElectionManager.getCurrentMinister();
        if (minister == null) return;

        ElectionData data = ElectionManager.getElectionData();
        SkyBlockMayor.Perk activePerk = data.getMinisterPerkEnum();

        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Active Perk");
                lore.add("");
                lore.add("§8§m--------------------------");

                if (activePerk != null) {
                    lore.add(minister.getColor() + activePerk.getDisplayName());
                    lore.add(activePerk.getDescription());
                }

                lore.add("§8§m--------------------------");
                lore.add("");
                lore.add("§7The Minister is who came in 2nd Place");
                lore.add("§7during the election. They have one");
                lore.add("§7of their perks active.");

                return ItemStackCreator.getStackHead(
                        minister.getColor() + "Minister " + minister.getDisplayName(),
                        new PlayerSkin(minister.getTexture(), minister.getSignature()),
                        1,
                        lore.toArray(new String[0])
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
