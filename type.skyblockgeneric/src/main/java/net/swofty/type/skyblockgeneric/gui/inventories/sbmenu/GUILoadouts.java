package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
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
import net.swofty.type.skyblockgeneric.loadout.LoadoutManager.TreeType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILoadouts implements StatefulView<GUILoadouts.LoadoutsState> {
    private static final int[] LOADOUT_SLOTS = {14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43};
    private static final String[] ARMOR_NAMES = {"Helmet", "Chestplate", "Leggings", "Boots"};
    private static final String[] EQUIPMENT_NAMES = {"Necklace", "Cloak", "Belt", "Gloves/Bracelet"};

    @Override
    public LoadoutsState initialState() {
        return new LoadoutsState(0);
    }

    @Override
    public ViewConfiguration<LoadoutsState> configuration() {
        return ViewConfiguration.withString((state, _) -> "(" + (state.page + 1) + "/3) Loadouts", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<LoadoutsState> layout, LoadoutsState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);

        layout.slot(9, treeSummary(data, TreeType.HOTF), (_, c) -> c.push(new GUITreeSlots(TreeType.HOTF)));
        layout.slot(18, treeSummary(data, TreeType.HOTM), (_, c) -> c.push(new GUITreeSlots(TreeType.HOTM)));
        for (int component = 0; component < 4; component++) {
            int piece = component;
            layout.slot(10 + component * 9, (s, c) -> currentEquipment((SkyBlockPlayer) c.player(), piece),
                    (_, c) -> c.push(new GUISkyBlockProfile()));
            layout.slot(11 + component * 9, (s, c) -> currentArmor((SkyBlockPlayer) c.player(), piece),
                    (_, c) -> c.push(new GUIWardrobe()));
        }
        layout.slot(21, (s, c) -> currentPet((SkyBlockPlayer) c.player()), (_, c) -> c.push(new GUIPets()));
        layout.slot(27, powerStone());
        layout.slot(36, statsTuning());

        int start = state.page * LOADOUT_SLOTS.length;
        int unlocked = LoadoutManager.unlockedLoadouts(player);
        for (int offset = 0; offset < LOADOUT_SLOTS.length; offset++) {
            int index = start + offset;
            if (index < unlocked) {
                loadout(layout, LOADOUT_SLOTS[offset], index);
            } else {
                locked(layout, LOADOUT_SLOTS[offset], index + 1);
            }
        }

        if (state.page > 0) {
            layout.slot(17, ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, "§ePage " + state.page),
                    (_, c) -> c.session(LoadoutsState.class).update(s -> new LoadoutsState(s.page - 1)));
        }
        if (state.page < 2) {
            layout.slot(44, ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, "§ePage " + (state.page + 2)),
                    (_, c) -> c.session(LoadoutsState.class).update(s -> new LoadoutsState(s.page + 1)));
        }
        layout.slot(48, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu"), (_, c) -> c.pop());
    }

    private static void loadout(ViewLayout<LoadoutsState> layout, int slot, int index) {
        layout.slot(slot,
                (s, c) -> icon((SkyBlockPlayer) c.player(), index),
                (click, c) -> {
                    if (click.click() instanceof Click.Right || click.click() instanceof Click.RightShift) {
                        c.push(new GUILoadoutEdit(index));
                    } else if (click.click() instanceof Click.Left || click.click() instanceof Click.LeftShift) {
                        LoadoutManager.equip((SkyBlockPlayer) c.player(), index);
                        c.session(LoadoutsState.class).refresh();
                    }
                });
    }

    private static void locked(ViewLayout<LoadoutsState> layout, int slot, int number) {
        layout.slot(slot, ItemStackCreator.getStack("§cLoadout " + number + " Locked", Material.RED_DYE, 1,
                "§7Unlock more slots from:", "§8▶ §aAccount Upgrades §8- §69 Slots", "",
                "§cUnlock more slots from §dElizabeth §cat", "§cthe §bCommunity Center"));
    }

    static ItemStack.Builder icon(SkyBlockPlayer player, int index) {
        DatapointLoadouts.LoadoutsData data = LoadoutManager.data(player);
        DatapointLoadouts.Loadout loadout = data.getLoadouts()[index];
        boolean equipped = data.getEquipped() == index;
        List<String> lore = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            lore.add("§7" + ARMOR_NAMES[i] + ": " + name(LoadoutManager.loadoutArmor(player, loadout, i), equipped));
        lore.add("");
        for (int i = 0; i < 4; i++) lore.add("§7" + EQUIPMENT_NAMES[i] + ": " + name(loadout.getEquipment()[i], false));
        lore.add("");
        lore.add("§7Pet: " + petName(player, loadout.getPetType()));
        lore.add("§7HOTM: " + treeName(loadout.getHotmSlot(), data.getHotmNames()));
        lore.add("§7HOTF: " + treeName(loadout.getHotfSlot(), data.getHotfNames()));
        lore.add("§7Power Stone: §8None");
        lore.add("§7Tuning Template Slot: §8None");
        lore.add("");
        if (!equipped && !loadout.isEmpty()) lore.add("§eLeft-click to equip!");
        lore.add("§eRight-click to edit");
        if (loadout.isEmpty()) {
            lore.add("");
            lore.add("§cYou must customize this loadout");
            lore.add("§cbefore you can equip it!");
        }
        SkyBlockItem helmet = LoadoutManager.loadoutArmor(player, loadout, 0);
        if (helmet == null || helmet.isNA()) {
            return ItemStackCreator.getStack("§a" + loadout.getName(), equipped ? Material.LIME_DYE : Material.GRAY_DYE, 1, lore);
        }
        List<Component> componentLore = lore.stream().<Component>map(
                line -> Component.text(line).decoration(TextDecoration.ITALIC, false)).toList();
        return PlayerItemUpdater.playerUpdate(player, helmet.getItemStack())
                .set(DataComponents.CUSTOM_NAME, Component.text("§a" + loadout.getName()).decoration(TextDecoration.ITALIC, false))
                .set(DataComponents.LORE, componentLore);
    }

    static String name(SkyBlockItem item, boolean equipped) {
        if (item == null || item.isNA()) return equipped ? "§8Empty" : "§8None";
        return item.getDisplayName();
    }

    private static String petName(SkyBlockPlayer player, String type) {
        if (type == null) return "§8None";
        try {
            SkyBlockItem pet = player.getPetData().getPet(net.swofty.commons.skyblock.item.ItemType.valueOf(type));
            return pet == null ? "§a" + type.replace('_', ' ') : pet.getDisplayName();
        } catch (IllegalArgumentException ignored) {
            return "§a" + type.replace('_', ' ');
        }
    }

    private static String treeName(int slot, String[] names) {
        return slot < 0 ? "§8None" : "§a" + names[slot];
    }

    private static ItemStack.Builder currentArmor(SkyBlockPlayer player, int component) {
        SkyBlockItem item = LoadoutManager.currentArmor(player, component);
        if (item != null) return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
        return ItemStackCreator.getStack("§7Empty " + ARMOR_NAMES[component] + " Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, "", "§eClick to select!");
    }

    private static ItemStack.Builder currentEquipment(SkyBlockPlayer player, int component) {
        SkyBlockItem item = LoadoutManager.currentEquipment(player, component);
        if (!item.isNA()) return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
        List<String> lore = new ArrayList<>();
        lore.add("§8> " + (component == 3 ? "Gloves" : EQUIPMENT_NAMES[component]));
        if (component == 3) lore.add("§8> Bracelet");
        lore.add("");
        lore.add("§eClick to select!");
        return ItemStackCreator.getStack("§7Empty Equipment Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, lore);
    }

    private static ItemStack.Builder currentPet(SkyBlockPlayer player) {
        SkyBlockItem pet = player.getPetData().getEnabledPet();
        return pet == null
                ? ItemStackCreator.getStack("§aPet", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, "§7Current: §8None")
                : PlayerItemUpdater.playerUpdate(player, pet.getItemStack());
    }

    private static ItemStack.Builder treeSummary(DatapointLoadouts.LoadoutsData data, TreeType tree) {
        boolean hotm = tree == TreeType.HOTM;
        int active = hotm ? data.getActiveHotmSlot() : data.getActiveHotfSlot();
        String[] names = hotm ? data.getHotmNames() : data.getHotfNames();
        String title = hotm ? "Heart of the Mountain" : "Heart of the Forest";
        String texture = hotm ? "86f06eaa3004aeed09b3d5b45d976de584e691c0e9cade133635de93d23b9edb"
                : "5ef539b165125cfa46b06ffb9659e7cf89084bbd3ede1b314edc8f443343d61c";
        return ItemStackCreator.getStackHead("§a" + title + " Slot", texture, 1,
                "§7Quickly swap between saved trees.", "", "§7Current: §a" + names[active], "",
                "§cSwapping trees has a 10m cooldown!", "", "§eClick to view!");
    }

    private static ItemStack.Builder powerStone() {
        return ItemStackCreator.getStack("§aPower Stone", Material.LAPIS_LAZULI, 1,
                "§7Choose your selected Power Stone.", "", "§7Current: §aInspired", "", "§7Stats:",
                "§c+31.02 Health", "§a+22.15 Defense", "§c+88.62 Strength", "§9+17.72 Crit Chance",
                "§9+66.46 Crit Damage", "§b+299.09 Intelligence", "", "§eClick to view!");
    }

    private static ItemStack.Builder statsTuning() {
        return ItemStackCreator.getStack("§aStats Tuning", Material.COMPARATOR, 1,
                "§7Optimize your build to your liking by using", "§eTuning Points§7.", "",
                "§7Every §610 MP §7grants §e1 Tuning Point§7.", "", "§7Magical Power: §6425",
                "§7Tuning Points: §e42", "", "§7Your tuning:", "§b+84 Intelligence", "", "§eClick to view!");
    }

    public record LoadoutsState(int page) {
    }
}
