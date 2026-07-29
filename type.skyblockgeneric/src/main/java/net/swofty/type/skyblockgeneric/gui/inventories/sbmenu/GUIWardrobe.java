package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
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
        Components.fill(layout);

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
                if (data.getEquippedSlot() == setIndex) {
                    int pieceIndex = piece;
                    layout.slot(guiSlot, (s, c) -> {
                            SkyBlockItem worn = ((SkyBlockPlayer) c.player()).getArmor()[pieceIndex];
                            return worn == null || worn.isNA()
                                ? ItemStack.AIR.builder()
                                : PlayerItemUpdater.playerUpdate((SkyBlockPlayer) c.player(), worn.getItemStack());
                        },
                        (_, c) -> c.player().sendMessage("§cYou cannot modify your equipped armor set!"));
                } else if (item == null || item.isNA()) {
                    int pieceIndex = piece;
                    layout.slot(guiSlot, ItemStackCreator.getStack(
                            "§aSlot " + (setIndex + 1) + " " + pieceName(piece),
                            EMPTY[column],
                            1,
                            // TODO: use the StringUtility 40 character limit per line system after this so it looks exactly like Hypixel does
                            (piece < 2 ? "§7Place a " : "§7Place a pair of ") + pieceName(piece).toLowerCase() + (piece < 2 ? " here to add it to the" : " here to add"),
                            (piece < 2 ? "§7armor set" : "§7them to the armor set")),
                        (_, c) -> placeStoredPiece((SkyBlockPlayer) c.player(), setIndex, pieceIndex, c));
                } else {
                    int pieceIndex = piece;
                    layout.slot(guiSlot,
                        (s, c) -> PlayerItemUpdater.playerUpdate((SkyBlockPlayer) c.player(), item.getItemStack()),
                        (click, c) -> handleStoredPiece(
                            (SkyBlockPlayer) c.player(), setIndex, pieceIndex, click.click(), c));
                }
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
        if (!(click.click() instanceof Click.LeftShift) && !(click.click() instanceof Click.RightShift)) {
            return true;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        ItemStack clickedStack = player.getInventory().getItemStack(click.slot());
        SkyBlockItem clickedItem = new SkyBlockItem(clickedStack);

        if (clickedItem.isNA()) {
            return false;
        }

        int piece = armorPiece(clickedItem);
        if (piece == -1) {
            player.sendMessage("§cOnly armor can be placed in the Wardrobe!");
            return false;
        }

        DatapointWardrobe.WardrobeData data = data(player);
        int targetSet = findClosestAvailableSet(player, data, click.state().page, piece);

        if (targetSet == -1) {
            player.sendMessage("§cThere are no available Wardrobe slots for that item!");
            return false;
        }

        ItemStack one = clickedStack.withAmount(1);
        SkyBlockItem stored = new SkyBlockItem(one);

        data.getSets()[targetSet].getPieces()[piece] = stored;

        player.getInventory().setItemStack(click.slot(),
            clickedStack.amount() == 1 ? ItemStack.AIR : clickedStack.withAmount(clickedStack.amount() - 1)
        );

        int pageStart = click.state().page * 9;
        if (targetSet >= pageStart && targetSet < pageStart + 9) {
            int column = targetSet - pageStart;
            int guiSlot = piece * 9 + column;

            ctx.inventory().setItemStack(guiSlot,
                PlayerItemUpdater.playerUpdate(player, one).build()
            );
        }

        save(player);
        ctx.session(WardrobeState.class).refresh();
        return false;
    }

    private int findClosestAvailableSet(SkyBlockPlayer player, DatapointWardrobe.WardrobeData data,
                                        int page, int piece) {
        int pageStart = page * 9;
        for (int distance = 0; distance < data.getSets().length; distance++) {
            int setIndex = (pageStart + distance) % data.getSets().length;
            if (!WardrobeService.isUnlocked(setIndex, player.getRank(), data)
                || data.getEquippedSlot() == setIndex) {
                continue;
            }

            SkyBlockItem stored = data.getSets()[setIndex].getPieces()[piece];
            if (stored == null || stored.isNA()) {
                return setIndex;
            }
        }
        return -1;
    }

    private int armorPiece(SkyBlockItem item) {
        for (int piece = 0; piece < 4; piece++) {
            if (WardrobeService.accepts(piece, item)) {
                return piece;
            }
        }
        return -1;
    }

    @Override
    public void onClose(WardrobeState state, ViewContext ctx, ViewSession.CloseReason reason) {
        savePage((SkyBlockPlayer) ctx.player(), ctx, state.page);
    }

    private void savePage(SkyBlockPlayer player, ViewContext ctx, int page) {
        DatapointWardrobe.WardrobeData data = data(player);
        for (int column = 0; column < 9; column++) {
            int setIndex = page * 9 + column;
            if (!WardrobeService.isUnlocked(setIndex, player.getRank(), data)) continue;
            for (int piece = 0; piece < 4; piece++) {
                SkyBlockItem item = new SkyBlockItem(ctx.inventory().getItemStack(piece * 9 + column));
                if (data.getEquippedSlot() == setIndex) {
                    setArmorPiece(player, piece, item);
                } else if (!isPanel(item)) {
                    data.getSets()[setIndex].getPieces()[piece] = item.isNA() ? null : item;
                }
            }
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class).setValue(data);
    }

    private void toggle(SkyBlockPlayer player, int slot) {
        DatapointWardrobe.WardrobeData data = data(player);
        DatapointWardrobe.ArmorSet target = data.getSets()[slot];
        if (data.getEquippedSlot() != slot && target.isEmpty()) {
            player.sendMessage("§cYou cannot equip an empty wardrobe slot!");
            return;
        }
        SkyBlockItem[] worn = player.getArmor();
        if (data.getEquippedSlot() == slot) {
            target.setPieces(worn);
            setArmor(player, new SkyBlockItem[4]);
            data.setEquippedSlot(-1);
        } else {
            SkyBlockItem[] stored = target.getPieces().clone();
            boolean storedComplete = target.isComplete();

            target.setPieces(worn);
            setArmor(player, stored);
            data.setEquippedSlot(slot);

            if (storedComplete && target.getFirstWorn() == 0) {
                target.setFirstWorn(System.currentTimeMillis());
            }
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class).setValue(data);
    }

    private void setArmor(SkyBlockPlayer player, SkyBlockItem[] pieces) {
        player.setHelmet(stack(pieces[0]));
        player.setChestplate(stack(pieces[1]));
        player.setLeggings(stack(pieces[2]));
        player.setBoots(stack(pieces[3]));
    }

    private void handleStoredPiece(SkyBlockPlayer player, int setIndex, int piece, Click click, ViewContext ctx) {
        DatapointWardrobe.WardrobeData data = data(player);
        SkyBlockItem stored = data.getSets()[setIndex].getPieces()[piece];
        if (stored == null || stored.isNA()) {
            ctx.session(WardrobeState.class).refresh();
            return;
        }

        if (click instanceof Click.LeftShift || click instanceof Click.RightShift) {
            player.addAndUpdateItem(stored);
            data.getSets()[setIndex].getPieces()[piece] = null;
            save(player);
            ctx.session(WardrobeState.class).refresh();
            return;
        }

        if (!(click instanceof Click.Left) && !(click instanceof Click.Right)) {
            return;
        }

        ItemStack cursor = player.getInventory().getCursorItem();
        if (cursor.isAir()) {
            player.getInventory().setCursorItem(stored.getItemStack());
            data.getSets()[setIndex].getPieces()[piece] = null;
            save(player);
            ctx.session(WardrobeState.class).refresh();
            return;
        }

        SkyBlockItem cursorItem = new SkyBlockItem(cursor);
        if (!WardrobeService.accepts(piece, cursorItem)) {
            player.sendMessage("§cThat item does not fit in this Wardrobe slot!");
            return;
        }

        data.getSets()[setIndex].getPieces()[piece] = new SkyBlockItem(cursor.withAmount(1));
        if (cursor.amount() == 1) {
            player.getInventory().setCursorItem(stored.getItemStack());
        } else {
            player.getInventory().setCursorItem(cursor.withAmount(cursor.amount() - 1));
            player.addAndUpdateItem(stored);
        }

        save(player);
        ctx.session(WardrobeState.class).refresh();
    }

    private void placeStoredPiece(SkyBlockPlayer player, int setIndex, int piece, ViewContext ctx) {
        ItemStack cursor = player.getInventory().getCursorItem();
        SkyBlockItem item = new SkyBlockItem(cursor);
        if (item.isNA()) return;

        if (!WardrobeService.accepts(piece, item)) {
            player.sendMessage("§cThat item does not fit in this Wardrobe slot!");
            return;
        }

        ItemStack one = cursor.withAmount(1);
        SkyBlockItem stored = new SkyBlockItem(one);

        data(player).getSets()[setIndex].getPieces()[piece] = stored;

        int guiSlot = piece * 9 + (setIndex % 9);

        ctx.inventory().setItemStack(guiSlot,
            PlayerItemUpdater.playerUpdate(player, one).build()
        );

        int remaining = cursor.amount() - 1;
        player.getInventory().setCursorItem(
            remaining <= 0 ? ItemStack.AIR : cursor.withAmount(remaining)
        );

        save(player);
        ctx.session(WardrobeState.class).refresh();
    }

    private void setArmorPiece(SkyBlockPlayer player, int piece, SkyBlockItem item) {
        ItemStack stack = stack(item);
        switch (piece) {
            case 0 -> player.setHelmet(stack);
            case 1 -> player.setChestplate(stack);
            case 2 -> player.setLeggings(stack);
            case 3 -> player.setBoots(stack);
            default -> throw new IllegalArgumentException("Invalid armor piece: " + piece);
        }
    }

    private boolean isPanel(SkyBlockItem item) {
        if (item == null || item.isNA()) return false;
        for (Material material : EMPTY) {
            if (item.getMaterial() == material) return true;
        }
        return false;
    }

    private void save(SkyBlockPlayer player) {
        player.getSkyblockDataHandler()
            .get(SkyBlockDataHandler.Data.WARDROBE, DatapointWardrobe.class)
            .setValue(data(player));
    }

    private ItemStack stack(SkyBlockItem item) {
        return item == null || item.isNA() ? ItemStack.AIR : item.getItemStack();
    }

    private ItemStack.Builder control(int index, DatapointWardrobe.ArmorSet set, DatapointWardrobe.WardrobeData data) {
        if (data.getEquippedSlot() == index) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aEquipped", Material.LIME_DYE, 1,
                "§7This wardrobe slot contains your", "§7current armor set.", "", "§eClick to unequip this armor set");
        }
        if (set.isEmpty()) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §cEmpty", Material.GRAY_DYE, 1,
                "§7This wardrobe slot contains no", "§7armor");
        }
        if (set.getFirstWorn() > 0) {
            return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aReady", Material.PINK_DYE, 1,
                "§7This wardrobe slot is ready to be", "§7equipped.", "", "§bFull Set First Worn",
                "§7" + new SimpleDateFormat("MMM d, yyyy").format(new Date(set.getFirstWorn())), "", "§eClick to equip this armor set");
        }
        return ItemStackCreator.getStack("§7Slot " + (index + 1) + ": §aReady", Material.PINK_DYE, 1,
            "§7This wardrobe slot is ready to be", "§7equipped.", "", "§eClick to equip this armor set");
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
