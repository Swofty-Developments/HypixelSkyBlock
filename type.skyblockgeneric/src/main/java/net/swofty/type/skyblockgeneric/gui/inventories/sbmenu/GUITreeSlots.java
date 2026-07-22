package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointLoadouts;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager.TreeType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUITreeSlots extends StatelessView {
    private static final List<String> HOTM_TREE_ONE = List.of(
            "", "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ",
            "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ",
            "§f█§f█§f█§f█§f█§a█§f█", "§l §0 §f█§f§l §0 §a█§a§l §0 §b█§b§l §0 ",
            "§f█§f█§f█§a█§f█§a█§f█", "§l §0 §f█§f§l §0 §a█§a§l §0 §a█§a§l §0 ",
            "§l §0 §f█§b█§a█§a█§a█§a§l §0 ", "§l §0 §0§l §0 §0§l §0 §a█§a§l §0 §0§l §0 §0§l §0 ");
    private static final List<String> HOTM_EMPTY = List.of(
            "", "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ",
            "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ",
            "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §a█§a§l §0 §f█§f§l §0 ",
            "§f█§f█§f█§f█§f█§f█§f█", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ",
            "§l §0 §f█§f█§f█§f█§f█§f§l §0 ", "§l §0 §0§l §0 §0§l §0 §f█§f§l §0 §0§l §0 §0§l §0 ");
    private static final List<String> HOTF_TREE_ONE = List.of(
            "", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ", "§6█§e█§e█§e█§f█§f█§f█",
            "§l §0 §f█§f§l §0 §6█§6§l §0 §f█§f§l §0 ", "§f█§f█§f█§e█§f█§6█§f█",
            "§l §0 §f█§f§l §0 §6█§6§l §0 §e█§e§l §0 ", "§l §0 §f█§f█§6█§e█§e█§e§l §0 ",
            "§l §0 §0§l §0 §0§l §0 §6█§6§l §0 §0§l §0 §0§l §0 ");
    private static final List<String> HOTF_EMPTY = List.of(
            "", "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ", "§f█§f█§f█§f█§f█§f█§f█",
            "§l §0 §f█§f§l §0 §6█§6§l §0 §f█§f§l §0 ", "§f█§f█§f█§f█§f█§f█§f█",
            "§l §0 §f█§f§l §0 §f█§f§l §0 §f█§f§l §0 ", "§l §0 §f█§f█§f█§f█§f█§f§l §0 ",
            "§l §0 §0§l §0 §0§l §0 §f█§f§l §0 §0§l §0 §0§l §0 ");

    private final TreeType tree;
    private final Integer loadout;

    public GUITreeSlots(TreeType tree) {
        this(tree, null);
    }

    public GUITreeSlots(TreeType tree, Integer loadout) {
        this.tree = tree;
        this.loadout = loadout;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(title() + " Slot", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);
        int selected = loadout == null
                ? (tree == TreeType.HOTM ? data.getActiveHotmSlot() : data.getActiveHotfSlot())
                : selected(data.getLoadouts()[loadout]);
        String[] names = tree == TreeType.HOTM ? data.getHotmNames() : data.getHotfNames();

        for (int slot = 0; slot < DatapointLoadouts.TREE_SLOT_COUNT; slot++) {
            int treeSlot = slot;
            boolean unlocked = slot < 2;
            boolean active = slot == selected;
            List<String> lore = new ArrayList<>(diagram(slot));
            lore.add("");
            if (!unlocked) {
                lore.add("§cUnlock more " + tree.name() + " §cSlots from");
                lore.add("§dElizabeth §cat the §bCommunity Center§c!");
                lore.add("");
                lore.add("§c§lLOCKED");
            } else if (loadout != null) {
                lore.add("§eClick to select!");
            } else if (active) {
                lore.add("§a§lSELECTED");
                lore.add("");
                lore.add("§eRight-click to rename!");
            } else {
                lore.add("§eLeft-click to select!");
                lore.add("§eRight-click to rename!");
            }
            Material material = !unlocked ? Material.RED_DYE : active && loadout == null ? Material.LIME_DYE : Material.GRAY_DYE;
            layout.slot(11 + slot, ItemStackCreator.getStack((active ? "§a" : "§c") + names[slot], material, 1, lore),
                    (click, c) -> handleClick((SkyBlockPlayer) c.player(), treeSlot, unlocked, click.click(), c));
        }

        layout.slot(30, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                "§7To " + (loadout == null ? "Loadouts" : LoadoutManager.data(player).getLoadouts()[loadout].getName())), (_, c) -> c.pop());
        if (loadout != null) {
            layout.slot(32, ItemStackCreator.getStack("§cClear Selection", Material.LAVA_BUCKET, 1,
                            "§7Clears your current selection for", "§7this component of your loadout.", "", "§eClick to clear!"),
                    (_, c) -> clear((SkyBlockPlayer) c.player()));
        }
    }

    private void handleClick(SkyBlockPlayer player, int slot, boolean unlocked, Click click, ViewContext ctx) {
        if (!unlocked) return;
        if (loadout != null) {
            DatapointLoadouts.Loadout selected = LoadoutManager.data(player).getLoadouts()[loadout];
            if (tree == TreeType.HOTM) selected.setHotmSlot(slot);
            else selected.setHotfSlot(slot);
            LoadoutManager.save(player);
            player.openView(new GUILoadoutEdit(loadout));
            return;
        }
        if (click instanceof Click.Right || click instanceof Click.RightShift) {
            rename(player, slot);
            return;
        }
        if (click instanceof Click.Left || click instanceof Click.LeftShift) {
            if (LoadoutManager.switchTree(player, tree, slot)) ctx.session(DefaultState.class).refresh();
        }
    }

    private void clear(SkyBlockPlayer player) {
        DatapointLoadouts.Loadout selected = LoadoutManager.data(player).getLoadouts()[loadout];
        if (tree == TreeType.HOTM) selected.setHotmSlot(-1);
        else selected.setHotfSlot(-1);
        LoadoutManager.save(player);
        player.openView(new GUILoadoutEdit(loadout));
    }

    private void rename(SkyBlockPlayer player, int slot) {
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);
        String[] names = tree == TreeType.HOTM ? data.getHotmNames() : data.getHotfNames();
        new HypixelSignGUI(player).open(new String[]{names[slot], ""}).thenAccept(name -> {
            if (name != null && !name.isBlank()) {
                names[slot] = name.trim();
                LoadoutManager.save(player);
            }
            player.openView(new GUITreeSlots(tree));
        });
    }

    private int selected(DatapointLoadouts.Loadout loadout) {
        return tree == TreeType.HOTM ? loadout.getHotmSlot() : loadout.getHotfSlot();
    }

    private List<String> diagram(int slot) {
        if (tree == TreeType.HOTM) return slot == 0 ? HOTM_TREE_ONE : HOTM_EMPTY;
        return slot == 0 ? HOTF_TREE_ONE : HOTF_EMPTY;
    }

    private String title() {
        return tree == TreeType.HOTM ? "Heart of the Mountain" : "Heart of the Forest";
    }
}
