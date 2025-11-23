package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.levels.LevelsGuide;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILevelsGuide extends HypixelInventoryGUI {
    private final LevelsGuide guide;

    private final int[] borderSlots = {
            9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53
    };

    private final int[] taskSlots = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public GUILevelsGuide(LevelsGuide guide) {
        super("Guide -> " + StringUtility.toNormalCase(guide.name()), InventoryType.CHEST_6_ROW);
        this.guide = guide;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockLevels()));

        for (int slot : borderSlots) {
            set(slot, ItemStackCreator.createNamedItemStack(guide.getGlassMaterial()));
        }

        set(new GUIItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§6SkyBlock Guide", Material.REDSTONE_TORCH, 1,
                        "§7Your §6SkyBlock Guide §7tracks the",
                        "§7progress you have made through",
                        "§7SkyBlock.",
                        "",
                        "§7Complete tasks within your current",
                        "§7game stage to increase your",
                        "§bSkyBlock Level §7and become a §dMaster",
                        "§7of SkyBlock!");
            }
        });
        int index = 0;
        LevelsGuide.TasksSet[] tasks = guide.getTasksSets().toArray(new LevelsGuide.TasksSet[0]);
        for (int slot : taskSlots) {
            if (index >= tasks.length) break;
            LevelsGuide.TasksSet task = tasks[index];

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    task.getGuiToOpen().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    List<String> lore = new ArrayList<>();

                    if (task.getCauses().size() > 1) {
                        lore.add("§8" + task.getCauses().size() + " Tasks");
                        lore.add("");
                    }

                    lore.addAll(task.getDisplay().apply(player));

                    return ItemStackCreator.updateLore(ItemStackCreator.getFromStack(task.getMaterial().build()), lore);
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

    }
}
