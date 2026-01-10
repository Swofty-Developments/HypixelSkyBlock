package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIBestiaryIsland extends StatelessView {

    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    private final BestiaryCategories category;

    public GUIBestiaryIsland(BestiaryCategories category) {
        this.category = category;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Bestiary ➡ " + StringUtility.stripColor(category.getDisplayName()), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        BestiaryData bestiaryData = new BestiaryData();

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();

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
        });

        // Display all mobs
        BestiaryEntry[] bestiaryEntries = category.getEntries();
        for (int i = 0; i < DISPLAY_SLOTS.length && i < bestiaryEntries.length; i++) {
            BestiaryEntry bestiaryEntry = bestiaryEntries[i];
            BestiaryMob mob = bestiaryEntry.getMobs().getFirst();
            int slot = DISPLAY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                int kills = player.getBestiaryData().getAmount(bestiaryEntry.getMobs());

                if (kills > 0) {
                    int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);
                    ArrayList<String> lore = new ArrayList<>();
                    GUIMaterial guiMaterial = bestiaryEntry.getGuiMaterial();

                    player.getBestiaryData().getMobDisplay(lore, kills, mob, bestiaryEntry);
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getUsingGUIMaterial("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                            guiMaterial, 1, lore);
                } else {
                    return ItemStackCreator.getStack("§c" + bestiaryEntry.getName(), Material.GRAY_DYE, 1,
                            "§7Kill a mob belonging to this Family to",
                            "§7unlock it in your Bestiary!");
                }
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                int kills = player.getBestiaryData().getAmount(bestiaryEntry.getMobs());
                if (kills > 0) {
                    player.openView(new GUIBestiaryMob(category, bestiaryEntry));
                }
            });
        }
    }
}
