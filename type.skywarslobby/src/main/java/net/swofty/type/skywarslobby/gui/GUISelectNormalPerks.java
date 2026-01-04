package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsUnlocks;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.perk.SkywarsPerk;
import net.swofty.type.skywarslobby.perk.SkywarsPerkRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for viewing and managing Normal mode perk slots.
 * Shows 6 perk slots + 3 global perks.
 */
public class GUISelectNormalPerks extends HypixelInventoryGUI {
    private static final String MODE = "NORMAL";
    private static final int[] PERK_SLOTS = {11, 12, 13, 14, 15, 16};

    public GUISelectNormalPerks() {
        super("Select Normal Perks", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        if (handler == null) return;

        DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                SkywarsDataHandler.Data.UNLOCKS,
                DatapointSkywarsUnlocks.class
        ).getValue();

        long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

        // Perk Slots label (slot 9)
        set(new GUIItem(9) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aPerk Slots",
                        Material.GOLD_BLOCK,
                        1,
                        "§7Your selected perks will be active",
                        "§7during your §aNormal SkyWars §7games."
                );
            }
        });

        // 6 Perk slots (slots 11-16)
        for (int i = 0; i < 6; i++) {
            final int slotIndex = i;
            final int guiSlot = PERK_SLOTS[i];

            set(new GUIClickableItem(guiSlot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    String perkId = unlocks.getPerkAtSlot(MODE, slotIndex);

                    if (perkId == null || perkId.isEmpty()) {
                        // Empty slot
                        return ItemStackCreator.getStack(
                                "§cEmpty",
                                Material.RED_STAINED_GLASS_PANE,
                                1,
                                "§8Perk Slot #" + (slotIndex + 1),
                                "",
                                "§eClick to select a perk!"
                        );
                    }

                    SkywarsPerk perk = SkywarsPerkRegistry.getPerk(perkId);
                    if (perk == null) {
                        return ItemStackCreator.getStack(
                                "§cEmpty",
                                Material.RED_STAINED_GLASS_PANE,
                                1,
                                "§8Perk Slot #" + (slotIndex + 1),
                                "",
                                "§eClick to select a perk!"
                        );
                    }

                    List<String> lore = new ArrayList<>();
                    lore.add("§8Perk Slot #" + (slotIndex + 1));
                    lore.add("");
                    lore.add("§7" + perk.getEffectDescription());
                    lore.add("");
                    lore.add("§7Rarity: " + perk.getRarity().getFormattedName());
                    lore.add("");
                    lore.add("§eLeft-click to replace!");
                    lore.add("§eRight-click to clear!");

                    return ItemStackCreator.getStack(
                            "§6" + perk.getName(),
                            perk.getIconMaterial(),
                            1,
                            lore
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    if (e.getClick() instanceof Click.Right) {
                        // Right-click: clear slot
                        unlocks.clearPerkSlot(MODE, slotIndex);
                        player.sendMessage("§7Cleared perk slot #" + (slotIndex + 1));
                        new GUISelectNormalPerks().open(player);
                    } else {
                        // Left-click: open perk selector for this slot
                        new GUISelectPerk(slotIndex).open(player);
                    }
                }
            });
        }

        // Close button (slot 17)
        set(GUIClickableItem.getCloseItem(17));

        // Global Perks label (slot 27)
        set(new GUIItem(27) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGlobal Perks",
                        Material.DIAMOND_BLOCK,
                        1,
                        "§7All players will have these perks",
                        "§7active during §aNormal SkyWars §7games."
                );
            }
        });

        // Global perks (slots 29-31)
        List<SkywarsPerk> globalPerks = SkywarsPerkRegistry.getGlobalPerks();
        int[] globalSlots = {29, 30, 31};
        for (int i = 0; i < Math.min(globalPerks.size(), 3); i++) {
            final SkywarsPerk perk = globalPerks.get(i);
            final int guiSlot = globalSlots[i];

            set(new GUIItem(guiSlot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8Global Perk");
                    lore.add("");
                    lore.add("§7" + perk.getEffectDescription());
                    lore.add("");
                    lore.add("§7Rarity: " + perk.getRarity().getFormattedName());

                    return ItemStackCreator.getStack(
                            "§6" + perk.getName(),
                            perk.getIconMaterial(),
                            1,
                            lore
                    );
                }
            });
        }

        // Go Back button (slot 48)
        set(GUIClickableItem.getGoBackItem(48, new GUIKitsPerks()));

        // Total Coins display (slot 49)
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§7Total Coins: §6" + String.format("%,d", coins),
                        Material.EMERALD,
                        1,
                        "§6https://store.hypixel.net"
                );
            }
        });

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
}
