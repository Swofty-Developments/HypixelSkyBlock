package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointLoadouts;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIEquipmentSets implements StatefulView<GUIEquipmentSets.EquipmentState> {
    private static final Material[] COLORS = {
            Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE
    };
    private static final String[] COMPONENTS = {"Necklace", "Cloak", "Belt", "Gloves/Bracelet"};
    private final int loadout;
    private final int component;

    public GUIEquipmentSets(int loadout, int component) {
        this.loadout = loadout;
        this.component = component;
    }

    @Override
    public EquipmentState initialState() {
        return new EquipmentState(0);
    }

    @Override
    public ViewConfiguration<EquipmentState> configuration() {
        return ViewConfiguration.withString((state, _) -> "(" + (state.page + 1) + "/2) Equipment Sets", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<EquipmentState> layout, EquipmentState state, ViewContext ctx) {
        Components.close(layout, 49);
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        int start = state.page * 9;
        int unlocked = LoadoutManager.unlockedEquipmentSets(player);

        for (int column = 0; column < 9; column++) {
            int setIndex = start + column;
            boolean available = setIndex < unlocked;
            DatapointLoadouts.EquipmentSet set = LoadoutManager.data(player).getEquipmentSets()[setIndex];
            for (int row = 0; row < 4; row++) {
                int guiSlot = row * 9 + column;
                int equipmentComponent = row;
                if (!available) {
                    layout.slot(guiSlot, ItemStackCreator.getStack("§7Slot " + (setIndex + 1) + ": §cLocked",
                            Material.BLACK_STAINED_GLASS_PANE, 1, "§7Unlock this equipment set from", "§dElizabeth §7at the §bCommunity Center§7."));
                } else {
                    layout.slot(guiSlot, (s, c) -> equipmentPiece((SkyBlockPlayer) c.player(), setIndex, equipmentComponent),
                            (_, c) -> editPiece((SkyBlockPlayer) c.player(), setIndex, equipmentComponent, c));
                }
            }
            if (available) {
                layout.slot(36 + column, ItemStackCreator.getStack("Slot " + (setIndex + 1) + ":§a Ready", Material.GRAY_DYE, 1,
                                "§7This slot is ready to be selected.", "", "§eClick to equip to loadout!"),
                        (_, c) -> select((SkyBlockPlayer) c.player(), setIndex));
            } else {
                layout.slot(36 + column, ItemStackCreator.getStack("§7Slot " + (setIndex + 1) + ": §cLocked", Material.RED_DYE, 1,
                        "§7This equipment set is locked.", "", "§cUnlock more slots from §dElizabeth §cat", "§cthe §bCommunity Center"));
            }
        }

        if (state.page > 0) {
            layout.slot(45, ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, "§ePage 1"),
                    (_, c) -> c.session(EquipmentState.class).update(s -> new EquipmentState(0)));
        }
        if (state.page < 1) {
            layout.slot(53, ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, "§ePage 2"),
                    (_, c) -> c.session(EquipmentState.class).update(s -> new EquipmentState(1)));
        }
        layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Loadout " + (loadout + 1)), (_, c) -> c.pop());
        layout.slot(50, ItemStackCreator.getStack("§cClear Selection", Material.LAVA_BUCKET, 1,
                        "§7Clears your current selection for", "§7this component of your loadout.", "", "§eClick to clear!"),
                (_, c) -> clear((SkyBlockPlayer) c.player()));
    }

    private ItemStack.Builder equipmentPiece(SkyBlockPlayer player, int setIndex, int equipmentComponent) {
        SkyBlockItem item = LoadoutManager.data(player).getEquipmentSets()[setIndex].getPieces()[equipmentComponent];
        if (item != null && !item.isNA()) return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
        String[] lore = switch (equipmentComponent) {
            case 0 -> new String[]{"§7Place a necklace here to add it to", "§7this set."};
            case 1 -> new String[]{"§7Place a cloak here to add it to this", "§7set."};
            case 2 -> new String[]{"§7Place a belt here to add it to this set."};
            default -> new String[]{"§7Place a pair of gloves or a bracelet", "§7here to add it to this set."};
        };
        return ItemStackCreator.getStack("§aSlot " + (setIndex + 1) + " " + COMPONENTS[equipmentComponent],
                COLORS[setIndex % 9], 1, lore);
    }

    private void editPiece(SkyBlockPlayer player, int setIndex, int equipmentComponent, ViewContext ctx) {
        DatapointLoadouts.EquipmentSet set = LoadoutManager.data(player).getEquipmentSets()[setIndex];
        SkyBlockItem stored = set.getPieces()[equipmentComponent];
        ItemStack cursor = player.getInventory().getCursorItem();
        if (cursor.isAir()) {
            if (stored == null || stored.isNA()) return;
            player.getInventory().setCursorItem(stored.getItemStack());
            set.getPieces()[equipmentComponent] = null;
        } else {
            SkyBlockItem cursorItem = new SkyBlockItem(cursor);
            if (!LoadoutManager.acceptsEquipment(equipmentComponent, cursorItem)) {
                player.sendMessage("§cThat item does not fit in this equipment slot!");
                return;
            }
            set.getPieces()[equipmentComponent] = new SkyBlockItem(cursor.withAmount(1));
            player.getInventory().setCursorItem(stored == null || stored.isNA() ? ItemStack.AIR : stored.getItemStack());
            if (cursor.amount() > 1) player.addAndUpdateItem(cursor.withAmount(cursor.amount() - 1));
        }
        LoadoutManager.save(player);
        ctx.session(EquipmentState.class).refresh();
    }

    private void select(SkyBlockPlayer player, int setIndex) {
        SkyBlockItem selected = LoadoutManager.data(player).getEquipmentSets()[setIndex].getPieces()[component];
        LoadoutManager.data(player).getLoadouts()[loadout].getEquipment()[component] = copy(selected);
        LoadoutManager.save(player);
        player.openView(new GUILoadoutEdit(loadout));
    }

    private void clear(SkyBlockPlayer player) {
        LoadoutManager.data(player).getLoadouts()[loadout].getEquipment()[component] = null;
        LoadoutManager.save(player);
        player.openView(new GUILoadoutEdit(loadout));
    }

    private static SkyBlockItem copy(SkyBlockItem item) {
        return item == null || item.isNA() ? null : new SkyBlockItem(item.toUnderstandable());
    }

    public record EquipmentState(int page) {
    }
}
