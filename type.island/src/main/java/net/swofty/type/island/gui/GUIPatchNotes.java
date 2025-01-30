package net.swofty.type.island.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUIPatchNotes extends SkyBlockAbstractInventory {
    private record PatchNote(String version, Material icon, String date, String discordUrl) {}
    private final List<PatchNote> patchNotes;

    public GUIPatchNotes() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Patch Notes")));

        this.patchNotes = new ArrayList<>();
        initializePatchNotes();
    }

    private void initializePatchNotes() {
        patchNotes.add(new PatchNote("1.1.5", Material.DIAMOND, "20th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1231214757114282065"));
        patchNotes.add(new PatchNote("1.1.4", Material.DISPENSER, "18th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1230477957764612146"));
        patchNotes.add(new PatchNote("1.1.3", Material.GOLD_INGOT, "15th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1229007700302495765"));
        patchNotes.add(new PatchNote("1.1.1", Material.HOPPER, "11th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1227909009336569906"));
        patchNotes.add(new PatchNote("1.1.0", Material.BLAZE_POWDER, "9th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1227143736669114459"));
        patchNotes.add(new PatchNote("1.0.3", Material.STICK, "8th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1226864370122887229"));
        patchNotes.add(new PatchNote("1.0.2", Material.BOOK, "6th April 2024",
                "https://discord.com/channels/830345347867476000/849739331278733332/1225968992909525056"));

        // Reverse to show newest first
        Collections.reverse(patchNotes);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Jerry the Assistant").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIJerry());
                    return true;
                })
                .build());

        // Add patch note entries
        for (int i = 0; i < patchNotes.size(); i++) {
            attachPatchNoteItem(16 - i, patchNotes.get(i));
        }
    }

    private void attachPatchNoteItem(int slot, PatchNote note) {
        attachItem(GUIItem.builder(slot)
                .item(ItemStackCreator.getStack(
                        "§aSkyBlock v" + note.version(),
                        note.icon(),
                        1,
                        "§7" + note.date(),
                        "",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage(Component.text("§fView Patch Notes §e§lCLICK HERE")
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, note.discordUrl())));
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}