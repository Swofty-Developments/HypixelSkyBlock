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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIOres extends HypixelInventoryGUI {
    private final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30};

    public GUIOres() {
        super("Ores", InventoryType.CHEST_6_ROW);
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
                        "§6Ores",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Ore Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread"
                );
            }
        });

        List<OreData> ores = Arrays.asList(OreData.values());

        for (int i = 0; i < ores.size() && i < SLOTS.length; i++) {
            final OreData ore = ores.get(i);
            final int slot = SLOTS[i];

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    List<String> lore = new ArrayList<>();

                    lore.add("§8Ore");

                    if (ore.hasProperties) {
                        lore.add("");
                        lore.add("§7Properties");
                        lore.add("§7 Breaking Power: §2" + ore.getBreakingPower());
                        lore.add("§7 Block Strength: §e" + ore.getBlockStrength());
                    }

                    return ItemStackCreator.getStack(
                            "§6" + ore.getDisplayName(),
                            ore.getMaterial(),
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
    public enum OreData {
        COAL_ORE("Coal Ore", Material.COAL_ORE, false, 0, 0),
        IRON_ORE("Iron Ore", Material.IRON_ORE, false, 0, 0),
        GOLD_ORE("Gold Ore", Material.GOLD_ORE, false, 0, 0),
        LAPIS_LAZULI_ORE("Lapis Lazuli Ore", Material.LAPIS_ORE, false, 0, 0),
        REDSTONE_ORE("Redstone Ore", Material.REDSTONE_ORE, false, 0, 0),
        EMERALD_ORE("Emerald Ore", Material.EMERALD_ORE, false, 0, 0),
        DIAMOND_ORE("Diamond Ore", Material.DIAMOND_ORE, false, 0, 0),

        PURE_COAL("Pure Coal", Material.COAL_BLOCK, true, 3, 600),
        PURE_IRON("Pure Iron", Material.IRON_BLOCK, true, 3, 600),
        PURE_GOLD("Pure Gold", Material.GOLD_BLOCK, true, 3, 600),
        PURE_LAPIS("Pure Lapis", Material.LAPIS_BLOCK, true, 3, 600),
        PURE_REDSTONE("Pure Redstone", Material.REDSTONE_BLOCK, true, 3, 600),
        PURE_EMERALD("Pure Emerald", Material.EMERALD_BLOCK, true, 3, 600),
        PURE_DIAMOND("Pure Diamond", Material.DIAMOND_BLOCK, true, 3, 600),

        NETHER_QUARTZ_ORE("Nether Quartz Ore", Material.NETHER_QUARTZ_ORE, false, 0, 0),
        PURE_QUARTZ("Pure Quartz", Material.QUARTZ_BLOCK, true, 3, 600),
        SULPHUR_ORE("Sulphur Ore", Material.SPONGE, true, 8, 500);

        private final String displayName;
        private final Material material;
        private final boolean hasProperties;
        private final int breakingPower;
        private final int blockStrength;

        OreData(String displayName, Material material, boolean hasProperties, int breakingPower, int blockStrength) {
            this.displayName = displayName;
            this.material = material;
            this.hasProperties = hasProperties;
            this.breakingPower = breakingPower;
            this.blockStrength = blockStrength;
        }
    }
}