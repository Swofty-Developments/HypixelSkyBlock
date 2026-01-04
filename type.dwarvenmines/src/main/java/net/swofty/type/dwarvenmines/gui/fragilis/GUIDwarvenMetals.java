package net.swofty.type.dwarvenmines.gui.fragilis;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Arrays;
import java.util.List;

public class GUIDwarvenMetals extends HypixelInventoryGUI {
    private final int[] SLOTS = {10, 11, 12, 13, 14};

    public GUIDwarvenMetals() {
        super("Dwarven Metals", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(48, new GUIHandyBlockGuide()));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aDwarven Metals",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Dwarven Metal Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread"
                );
            }
        });

        List<DwarvenMetalData> metals = Arrays.asList(DwarvenMetalData.values());

        for (int i = 0; i < metals.size() && i < SLOTS.length; i++) {
            final DwarvenMetalData metal = metals.get(i);
            final int slot = SLOTS[i];

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    List<String> lore = Arrays.asList(
                            "§8Dwarven Metal",
                            "",
                            "§7Properties",
                            "§7 Breaking Power: §2" + metal.getBreakingPower(),
                            "§7 Block Strength: §e" + String.format("%,d", metal.getBlockStrength())
                    );

                    return ItemStackCreator.getStack(
                            "§a" + metal.getDisplayName(),
                            metal.getMaterial(),
                            1,
                            lore.toArray(new String[0])
                    );
                }
            });
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

    @Getter
    public enum DwarvenMetalData {
        MITHRIL_ORE("Mithril Ore", Material.PRISMARINE_BRICKS, 4, 800),
        TITANIUM_ORE("Titanium Ore", Material.POLISHED_DIORITE, 5, 2000),
        UMBER("Umber", Material.RED_SANDSTONE, 9, 5600),
        TUNGSTEN("Tungsten", Material.CLAY, 9, 5600),
        GLACITE("Glacite", Material.PACKED_ICE, 9, 6000);

        private final String displayName;
        private final Material material;
        private final int breakingPower;
        private final int blockStrength;

        DwarvenMetalData(String displayName, Material material, int breakingPower, int blockStrength) {
            this.displayName = displayName;
            this.material = material;
            this.breakingPower = breakingPower;
            this.blockStrength = blockStrength;
        }
    }
}