package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
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

public class GUIMayorMenu extends HypixelInventoryGUI {

    public GUIMayorMenu() {
        super("Mayor", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) return;

        ElectionData data = ElectionManager.getElectionData();
        List<SkyBlockMayor.Perk> activePerks = data.getCurrentMayorPerkEnums();

        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Perks List");
                lore.add("");
                lore.add("§8§m--------------------------");

                for (SkyBlockMayor.Perk perk : activePerks) {
                    lore.add(mayor.getColor() + perk.getDisplayName());
                    lore.add(perk.getDescription());
                    lore.add("");
                }

                lore.add("§8§m--------------------------");
                lore.add("");
                lore.add("§7The listed perks are available to");
                lore.add("§7all players until the closing of");
                lore.add("§7the next elections.");

                return ItemStackCreator.getStackHead(
                        mayor.getColor() + "Mayor " + mayor.getDisplayName(),
                        new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                        1,
                        lore.toArray(new String[0])
                );
            }
        });

        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§bElection & Voting",
                        Material.JUKEBOX,
                        1,
                        "§7View the current election",
                        "§7candidates and cast your vote.",
                        "",
                        "§eClick to view!"
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
