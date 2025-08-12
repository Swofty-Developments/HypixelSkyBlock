package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.bestiary.BestiaryData;
import net.swofty.type.generic.entity.mob.BestiaryMob;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIBestiaryIsland extends HypixelInventoryGUI {

    private static final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    BestiaryData bestiaryData = new BestiaryData();
    BestiaryCategories category;

    public GUIBestiaryIsland(BestiaryCategories category) {
        super("Bestiary ➡ " + StringUtility.stripColor(category.getDisplayName()), InventoryType.CHEST_6_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUIBestiary()));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                HypixelPlayer player = (HypixelPlayer) p; 

                BestiaryEntry[] entries = category.getEntries();
                int total = entries.length;
                int found = 0;
                int completed = 0;

                for (BestiaryEntry entry : entries) {
                    int kills = player.getBestiaryData().getAmount(entry.getMobs());
                    if (kills > 0) {
                        found++;

                        BestiaryMob mob = entry.getMobs().getFirst();
                        int mobKills = player.getBestiaryData().getAmount(mob);
                        int tier = bestiaryData.getCurrentBestiaryTier(mob, mobKills);
                        if (tier == mob.getMaxBestiaryTier()) completed++;
                    }
                }

                List<String> lore = new ArrayList<>();
                lore.add("§7View all of the mobs that you've");
                lore.add("§7found and killed on " + category.getDisplayName() + "§7.");
                lore.add("");

                // Families Found
                int foundPercent = (int) ((double) found / total * 100);
                String foundColor = foundPercent == 100 ? "§a" : "§e";
                lore.add("§7Families Found: " + foundColor + foundPercent + "%");

                String baseBar = "─────────────────";
                int barLength = baseBar.length();
                int filled = (int) Math.round(((double) found / total) * barLength);
                String filledBar = "§3§m" + baseBar.substring(0, Math.min(filled, barLength));
                String unfilledBar = "§f§m" + baseBar.substring(Math.min(filled, barLength));

                lore.add(filledBar + unfilledBar + "§r §b" +
                        StringUtility.commaify(found) + "§3/§b" + StringUtility.shortenNumber(total));
                lore.add("");

                // Families Completed
                int completedPercent = (int) ((double) completed / total * 100);
                String completedColor = completedPercent == 100 ? "§a" : "§e";
                lore.add("§7Families Completed: " + completedColor + completedPercent + "%");

                int completedFilled = (int) Math.round(((double) completed / total) * barLength);
                String completedBar = "§3§m" + baseBar.substring(0, Math.min(completedFilled, barLength));
                String completedUnfilled = "§f§m" + baseBar.substring(Math.min(completedFilled, barLength));

                lore.add(completedBar + completedUnfilled + "§r §b" +
                        StringUtility.commaify(completed) + "§3/§b" + StringUtility.shortenNumber(total));

                return ItemStackCreator.getStackHead(
                        category.getDisplayName(),
                        "c9c8881e42915a9d29bb61a16fb26d059913204d265df5b439b3d792acd56",
                        1,
                        lore
                );
            }
        });

        BestiaryEntry[] bestiaryEntries = category.getEntries();
        int index = 0;
        for (int slot : displaySlots) {
            if (index >= bestiaryEntries.length) break;
            BestiaryEntry bestiaryEntry = bestiaryEntries[index];
            BestiaryMob mob = bestiaryEntry.getMobs().getFirst();
            int kills = getPlayer().getBestiaryData().getAmount(bestiaryEntry.getMobs());
            int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);
            if (kills > 0) {
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                HypixelPlayer player = (HypixelPlayer) p; 
                        new GUIBestiaryMob(category, bestiaryEntry).open(player);
                    }

                    public ItemStack.Builder getItem(HypixelPlayer p) {
                HypixelPlayer player = (HypixelPlayer) p; 
                        ArrayList<String> lore = new ArrayList<>();

                        player.getBestiaryData().getMobDisplay(lore, kills, mob, bestiaryEntry);

                        lore.add("§eClick to view!");

                        if (bestiaryEntry.getMaterial() == Material.PLAYER_HEAD) {
                            return ItemStackCreator.getStackHead("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                                    bestiaryEntry.getTexture(), 1, lore);
                        } else {
                            return ItemStackCreator.getStack("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                                    bestiaryEntry.getMaterial(), 1, lore);
                        }
                    }
                });
            } else {
                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                HypixelPlayer player = (HypixelPlayer) p; 
                        return ItemStackCreator.getStack("§c" + bestiaryEntry.getName(), Material.GRAY_DYE, 1,
                                "§7Kill a mob belonging to this Family to",
                                "§7unlock it in your Bestiary!");
                    }
                });
            }
            index++;
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
