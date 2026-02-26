package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.levels.LevelsGuide;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILevelsGuide extends StatelessView {
    private final LevelsGuide guide;

    private static final int[] BORDER_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53
    };

    private static final int[] TASK_SLOTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public GUILevelsGuide(LevelsGuide guide) {
        this.guide = guide;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Guide -> " + StringUtility.toNormalCase(guide.name()), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Border slots with colored glass
        for (int slot : BORDER_SLOTS) {
            layout.slot(slot, ItemStackCreator.createNamedItemStack(guide.getGlassMaterial()));
        }

        // Guide info
        layout.slot(50, (s, c) -> ItemStackCreator.getStack("§6SkyBlock Guide", Material.REDSTONE_TORCH, 1,
                "§7Your §6SkyBlock Guide §7tracks the",
                "§7progress you have made through",
                "§7SkyBlock.",
                "",
                "§7Complete tasks within your current",
                "§7game stage to increase your",
                "§bSkyBlock Level §7and become a §dMaster",
                "§7of SkyBlock!"));

        // Task items
        LevelsGuide.TasksSet[] tasks = guide.getTasksSets().toArray(new LevelsGuide.TasksSet[0]);
        for (int i = 0; i < TASK_SLOTS.length && i < tasks.length; i++) {
            int slot = TASK_SLOTS[i];
            LevelsGuide.TasksSet task = tasks[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                List<String> lore = new ArrayList<>();

                if (task.getCauses().size() > 1) {
                    lore.add("§8" + task.getCauses().size() + " Tasks");
                    lore.add("");
                }

                lore.addAll(task.getDisplay().apply(player));

                return ItemStackCreator.updateLore(ItemStackCreator.getFromStack(task.getMaterial().build()), lore);
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                player.openView(task.getGuiToOpen());
            });
        }

    }
}
