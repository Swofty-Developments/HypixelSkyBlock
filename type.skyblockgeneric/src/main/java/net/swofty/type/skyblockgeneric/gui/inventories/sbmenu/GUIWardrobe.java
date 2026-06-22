package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointWardrobe;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.wardrobe.WardrobeService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GUIWardrobe implements StatefulView<GUIWardrobe.WardrobeState> {
    private static final Material[] EMPTY = {
        Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE,
        Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
        Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
        Material.BLUE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
        Material.PURPLE_STAINED_GLASS_PANE
    };

    @Override
    public WardrobeState initialState() {
        return new WardrobeState(0);
    }

    @Override
    public ViewConfiguration<WardrobeState> configuration() {
        return ViewConfiguration.withString((state, _) -> "Wardrobe (" + (state.page + 1) + "/3)", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<WardrobeState> layout, WardrobeState state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointWardrobe.WardrobeData data = data(player);
        int start = state.page * 9;

        for (int column = 0; column < 9; column++) {
            int setIndex = start + column;
            boolean unlocked = WardrobeService.isUnlocked(setIndex, player.getRank(), data);
            DatapointWardrobe.ArmorSet set = data.getSets()[setIndex];
            for (int piece = 0; piece < 4; piece++) {
                int guiSlot = piece * 9 + column;
                if (!unlocked) {
                    layout.slot(guiSlot, ItemStackCreator.getStack("§7Slot " + (setIndex + 1) + ": §cLocked",
                        Material.BLACK_STAINED_GLASS_PANE, 1, "",
                        "§7Unlock more slots from:",
                        "§8▶ §aAccount Upgrades §8- §69 Slots",
                        "",
                        "§cUnlock more slots from §dElizabeth §cat",
                        "§cthe §bCommunity Center"));
                    continue;
                }
                SkyBlockItem item = set.getPieces()[piece];
                layout.editable(guiSlot, (s, c) -> item == null || item.isNA()
                        ? ItemStack.AIR.builder()
                        : PlayerItemUpdater.playerUpdate((SkyBlockPlayer) c.player(), item.getItemStack()),
                    (_, _, _, _) -> {
                    });
            }

            int controlSlot = 36 + column;
            if (!unlocked) {
                layout.slot(controlSlot, ItemStackCreator.getStack("§7Slot " + (setIndex + 1) + ": §cLocked",
                    Material.RED_DYE, 1,
                    "§7This wardrobe slot is locked and",
                    "§7cannot be used.",
                    "",
                    "§7Unlock more slots from:",
                    "§8▶ §aAccount Upgrades §8- §69 Slots",
                    "",
                    "§cUnlock more slots from §dElizabeth §cat",
                    "§cthe §bCommunity Center"));
            } else {
                layout.slot(controlSlot, (s, c) -> control(setIndex, set, data), (_, c) -> {
                    savePage((SkyBlockPlayer) c.player(), c, state.page);
                    toggle((SkyBlockPlayer) c.player(), setIndex);
                    c.session(WardrobeState.class).refresh();
                });
            }
        }

        if (state.page > 0) {
            layout.slot(45, ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, "§ePage " + state.page),
                (_, c) -> {
                    savePage((SkyBlockPlayer) c.player(), c, state.page);
                    c.session(WardrobeState.class).update(s -> new WardrobeState(s.page - 1));
                });
        }
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);
        if (state.page < 2) {
            layout.slot(53, ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, "§ePage " + (state.page + 2)),
                (_, c) -> {
                    savePage((SkyBlockPlayer) c.player(), c, state.page);
                    c.session(WardrobeState.class).update(s -> new WardrobeState(s.page + 1));
                });
        }
        layout.allowHotkey(true);
    }

    @Override
    public boolean onBottomClick(ClickContext<WardrobeState> click, ViewContext ctx) {
        int slot = click.slot();
        if (slot < 0 || slot >= 36) return true;
        SkyBlockItem cursor = new SkyBlockItem(ctx.player().getInventory().getCursorItem());
        if (WardrobeService.accepts(slot / 9, cursor)) return true;
        ctx.player().sendMessage("§cThat item does not fit in this Wardrobe slot!");
        return false;
    }

    @Override
    public void onClose(WardrobeState state, ViewContext ctx, ViewSession.CloseReason reason) {
        savePage((SkyBlockPlayer) ctx.player(), ctx, state.page);
    }

    private void savePage(SkyBlockPlayer player, ViewContext ctx, int page) {
        DatapointWardrobe.WardrobeData data = data(player);
        for (int column = 0; column < 9; column++) {
            int setIndex = page * 9 + column;
            if (!WardrobeService.isUnlocked(setIndex, player.getRank(), data) || data.getEquippedSlot() == setIndex)
                continue;
            for (int piece = 0; piece < 4; piece++) {
                SkyBlockItem item = new SkyBlockItem(ctx.inventory().getItemStack(piece * 9 + column));
                data.getSets()[setIndex].getPieces()[piece] = item.isNA() ? null : item;
            }
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class).setValue(data);
    }

    private void toggle(SkyBlockPlayer player, int slot) {
        DatapointWardrobe.WardrobeData data = data(player);
        DatapointWardrobe.ArmorSet target = data.getSets()[slot];
        SkyBlockItem[] worn = player.getArmor();
        if (data.getEquippedSlot() == slot) {
            target.setPieces(worn);
            setArmor(player, new SkyBlockItem[4]);
            data.setEquippedSlot(-1);
        } else {
            SkyBlockItem[] stored = target.getPieces().clone();
            target.setPieces(worn);
            setArmor(player, stored);
            data.setEquippedSlot(slot);
            if (target.isComplete() && target.getFirstWorn() == 0) target.setFirstWorn(System.currentTimeMillis());
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class).setValue(data);
    }

    private void setArmor(SkyBlockPlayer player, SkyBlockItem[] pieces) {
        player.setHelmet(stack(pieces[0]));
        player.setChestplate(stack(pieces[1]));
        player.setLeggings(stack(pieces[2]));
        player.setBoots(stack(pieces[3]));
    }

    private ItemStack stack(SkyBlockItem item) {
        return item == null || item.isNA() ? ItemStack.AIR : item.getItemStack();
    }

    private ItemStack.Builder control(int index, DatapointWardrobe.ArmorSet set, DatapointWardrobe.WardrobeData data) {
        if (data.getEquippedSlot() == index) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aEquipped", Material.LIME_DYE, 1,
                "§7This wardrobe slot contains your", "§7current armor set.", "", "§eClick to unequip!");
        }
        if (set.isEmpty()) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §cEmpty", Material.GRAY_DYE, 1,
                "§7This wardrobe slot contains no armor.");
        }
        if (set.getFirstWorn() > 0) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aReady", Material.PINK_DYE, 1,
                "§7This wardrobe slot is ready.", "", "§bFull Set First Worn",
                "§7" + new SimpleDateFormat("MMM d, yyyy").format(new Date(set.getFirstWorn())), "", "§eClick to equip!");
        }
        return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aReady", Material.PINK_DYE, 1,
            "§7This wardrobe slot is ready.", "", "§eClick to equip!");
    }

    private DatapointWardrobe.WardrobeData data(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class).getValue();
    }

    private static String pieceName(int piece) {
        return switch (piece) {
            case 0 -> "Helmet";
            case 1 -> "Chestplate";
            case 2 -> "Leggings";
            default -> "Boots";
        };
    }

    public record WardrobeState(int page) {
    }
}
