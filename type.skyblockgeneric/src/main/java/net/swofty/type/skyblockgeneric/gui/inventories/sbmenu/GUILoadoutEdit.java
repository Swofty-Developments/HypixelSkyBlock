package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointLoadouts;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager;
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager.TreeType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILoadoutEdit extends StatelessView {
    private static final int[] ARMOR_SLOTS = {11, 20, 29, 38};
    private static final int[] EQUIPMENT_SLOTS = {10, 19, 28, 37};
    private static final String[] ARMOR_NAMES = {"Helmet", "Chestplate", "Leggings", "Boots"};
    private static final String[] EQUIPMENT_NAMES = {"Necklace", "Cloak", "Belt", "Gloves/Bracelet"};
    private final int index;

    public GUILoadoutEdit(int index) {
        this.index = index;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString((_, ctx) -> LoadoutManager.data((SkyBlockPlayer) ctx.player()).getLoadouts()[index].getName(), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointLoadouts.Loadout loadout = LoadoutManager.data(player).getLoadouts()[index];

        for (int component = 0; component < 4; component++) {
            int piece = component;
            layout.slot(ARMOR_SLOTS[component], (s, c) -> armorItem((SkyBlockPlayer) c.player(), piece),
                    (_, c) -> c.push(new GUIWardrobe(index)));
            layout.slot(EQUIPMENT_SLOTS[component], (s, c) -> equipmentItem((SkyBlockPlayer) c.player(), piece),
                    (_, c) -> c.push(new GUIEquipmentSets(index, piece)));
        }

        layout.slot(21, (s, c) -> petItem((SkyBlockPlayer) c.player()), (_, c) -> c.push(new GUILoadoutPetSelection(index)));
        layout.slot(23, treeItem(loadout, TreeType.HOTM), (_, c) -> c.push(new GUITreeSlots(TreeType.HOTM, index)));
        layout.slot(24, treeItem(loadout, TreeType.HOTF), (_, c) -> c.push(new GUITreeSlots(TreeType.HOTF, index)));
        layout.slot(32, ItemStackCreator.getStackHead("§aPower Stone",
                "71e1f6162db42245639609f728a4e134ed7bd7de3c15a7792d219a6e2a9db", 1,
                "§7Select a Power Stone to use in this", "§7loadout!", "", "§7Current: §8None", "", "§eLeft-click to change!"));
        layout.slot(33, ItemStackCreator.getStack("§aStats Tuning Slot", Material.COMPARATOR, 1,
                "§7Select a Stats Tuning template slot", "§7to use in this loadout!", "", "§7Current: §8None", "", "§eLeft-click to change!"));
        layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Loadouts"), (_, c) -> c.pop());
        layout.slot(50, ItemStackCreator.getStack("§cClear", Material.LAVA_BUCKET, 1,
                        "§7Clear all settings in this loadout,", "§7restoring it back to default.", "", "§eClick to clear!"),
                (_, c) -> clear((SkyBlockPlayer) c.player(), c));
        layout.slot(51, ItemStackCreator.getStack("§aRename Loadout", Material.NAME_TAG, 1,
                "§7Want to feel a more personal", "§7connection with your loadout slot?", "§7Give it a name!", "",
                "§7Current Name: §a" + loadout.getName(), "", "§eClick to rename!"), (_, c) -> rename((SkyBlockPlayer) c.player()));
    }

    private ItemStack.Builder armorItem(SkyBlockPlayer player, int component) {
        DatapointLoadouts.Loadout loadout = LoadoutManager.data(player).getLoadouts()[index];
        SkyBlockItem item = LoadoutManager.loadoutArmor(player, loadout, component);
        if (item != null && !item.isNA()) return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
        return ItemStackCreator.getStack("§7Empty " + ARMOR_NAMES[component] + " Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1,
                "", "§7No armor selected for this loadout.", "", "§eLeft-click to change!");
    }

    private ItemStack.Builder equipmentItem(SkyBlockPlayer player, int component) {
        SkyBlockItem item = LoadoutManager.data(player).getLoadouts()[index].getEquipment()[component];
        if (item != null && !item.isNA()) return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
        List<String> lore = new ArrayList<>();
        lore.add("§8> " + (component == 3 ? "Gloves" : EQUIPMENT_NAMES[component]));
        if (component == 3) lore.add("§8> Bracelet");
        lore.add("");
        lore.add("§7No equipment selected for this");
        lore.add("§7loadout.");
        lore.add("");
        lore.add("§eLeft-click to change!");
        return ItemStackCreator.getStack("§7Empty Equipment " + (component + 1) + " Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, lore);
    }

    private ItemStack.Builder petItem(SkyBlockPlayer player) {
        String petType = LoadoutManager.data(player).getLoadouts()[index].getPetType();
        if (petType != null) {
            try {
                SkyBlockItem pet = player.getPetData().getPet(net.swofty.commons.skyblock.item.ItemType.valueOf(petType));
                if (pet != null) return PlayerItemUpdater.playerUpdate(player, pet.getItemStack());
            } catch (IllegalArgumentException ignored) {
            }
        }
        return ItemStackCreator.getStack("§aPet", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1,
                "§7Select a pet to use in this loadout!", "", "§7Current: §8None", "", "§eLeft-click to change!");
    }

    private ItemStack.Builder treeItem(DatapointLoadouts.Loadout loadout, TreeType tree) {
        boolean hotm = tree == TreeType.HOTM;
        int selected = hotm ? loadout.getHotmSlot() : loadout.getHotfSlot();
        String title = hotm ? "Heart of the Mountain" : "Heart of the Forest";
        String texture = hotm ? "86f06eaa3004aeed09b3d5b45d976de584e691c0e9cade133635de93d23b9edb"
                : "5ef539b165125cfa46b06ffb9659e7cf89084bbd3ede1b314edc8f443343d61c";
        return ItemStackCreator.getStackHead("§a" + title + " Slot", texture, 1,
                hotm ? "§7Select a Heart of the Mountain to" : "§7Select a Heart of the Forest to use",
                hotm ? "§7use in this loadout!" : "§7in this loadout!", "",
                "§7Selected: " + (selected < 0 ? "§8None" : "§a" + title + " " + (selected + 1)), "",
                "§cSwapping trees has a 10m cooldown!", "", "§eLeft-click to change!");
    }

    private void clear(SkyBlockPlayer player, ViewContext ctx) {
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);
        data.getLoadouts()[index] = new DatapointLoadouts.Loadout("Loadout " + (index + 1));
        if (data.getEquipped() == index) data.setEquipped(-1);
        LoadoutManager.save(player);
        ctx.session(DefaultState.class).refresh();
    }

    private void rename(SkyBlockPlayer player) {
        DatapointLoadouts.Loadout loadout = LoadoutManager.data(player).getLoadouts()[index];
        new HypixelSignGUI(player).open(new String[]{loadout.getName(), ""}).thenAccept(name -> {
            if (name != null && !name.isBlank()) {
                loadout.setName(name.trim());
                LoadoutManager.save(player);
            }
            player.openView(new GUILoadoutEdit(index));
        });
    }
}
