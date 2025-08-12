package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUILevelPrefixRewards extends HypixelInventoryGUI {
    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
                29, 30, 31, 32, 33
    };

    public GUILevelPrefixRewards() {
        super("Prefix Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUILevelRewards()));

        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getPlayer().getSkyBlockExperience();

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                List<String> lore = new ArrayList<>();
                lore.add("§7New colors for your level prefix");
                lore.add("§7shown in TAB and in chat!");
                lore.add(" ");
                lore.add("§7Next Reward:");

                Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = experience.getLevel().getNextPrefixChange();
                if (nextPrefix == null) {
                    lore.add("§cNo more rewards!");
                } else {
                    lore.add(nextPrefix.getValue() + nextPrefix.getKey().getPrefixDisplay());
                    lore.add("§8at Level " + nextPrefix.getKey().asInt());
                }
                lore.add(" ");
                lore.addAll(GUILevelRewards.getAsDisplay(
                        player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size(),
                        SkyBlockLevelRequirement.getAllPrefixChanges().size()
                ));

                return ItemStackCreator.getStack("§aPrefix Color Rewards",
                        Material.GRAY_DYE, 1, lore);
            }
        });

        int index = 0;
        for (Map.Entry<SkyBlockLevelRequirement, String> entry : SkyBlockLevelRequirement.getAllPrefixChanges().entrySet()) {
            SkyBlockLevelRequirement level = entry.getKey();
            int slot = SLOTS[index];
            boolean unlocked = experience.getLevel().asInt() >= level.asInt();

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                    return ItemStackCreator.getStack(level.getPrefix() + level.getPrefixDisplay(),
                            level.getPrefixItem(), 1,
                            "§8Level " + level.asInt(),
                            " ",
                            "§7Preview: " + player.getFullDisplayName(level.getPrefix()),
                            " ",
                            (unlocked ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" + (level.asInt() - experience.getLevel().asInt())));

                }
            });

            index++;
        }

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
