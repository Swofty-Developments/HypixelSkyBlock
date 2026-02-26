package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIBestiaryMob extends StatelessView {

    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            0, new int[]{},
            1, new int[]{22},
            2, new int[]{21, 23},
            3, new int[]{20, 22, 24},
            4, new int[]{19, 21, 23, 25},
            5, new int[]{20, 21, 22, 23, 24},
            6, new int[]{21, 22, 23, 30, 31, 32},
            7, new int[]{19, 20, 21, 22, 23, 24, 25}
    ));

    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    private final BestiaryCategories category;
    private final BestiaryEntry bestiaryEntry;

    public GUIBestiaryMob(BestiaryCategories category, BestiaryEntry bestiaryEntry) {
        this.category = category;
        this.bestiaryEntry = bestiaryEntry;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(StringUtility.stripColor(category.getDisplayName()) + " ➡ " + StringUtility.stripColor(bestiaryEntry.getName()), InventoryType.CHEST_6_ROW);
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
            ArrayList<String> lore = new ArrayList<>();
            BestiaryMob mob = bestiaryEntry.getMobs().getFirst();
            GUIMaterial guiMaterial = bestiaryEntry.getGuiMaterial();
            int kills = player.getBestiaryData().getAmount(bestiaryEntry.getMobs());
            int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);

            player.getBestiaryData().getMobDisplay(lore, kills, mob, bestiaryEntry);

            return ItemStackCreator.getUsingGUIMaterial("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                    guiMaterial, 1, lore);
        });

        List<BestiaryMob> bestiaryMobs = bestiaryEntry.getMobs();
        int mobCount = bestiaryMobs.size();
        int[] chosenSlots = SLOTS.getOrDefault(mobCount, DISPLAY_SLOTS);

        for (int i = 0; i < bestiaryMobs.size() && i < chosenSlots.length; i++) {
            BestiaryMob mob = bestiaryMobs.get(i);
            GUIMaterial guiMaterial = mob.getGuiMaterial();
            int slot = chosenSlots[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                ArrayList<String> lore = new ArrayList<>();
                int kills = player.getBestiaryData().getAmount(mob);
                int deaths = player.getDeathData().getAmount(mob.getMobID());
                OtherLoot otherLoot = mob.getOtherLoot();

                List<SkyBlockLootTable.LootRecord> commonLoot = new ArrayList<>();
                List<SkyBlockLootTable.LootRecord> uncommonLoot = new ArrayList<>();
                List<SkyBlockLootTable.LootRecord> rareLoot = new ArrayList<>();
                List<SkyBlockLootTable.LootRecord> legendaryLoot = new ArrayList<>();
                List<SkyBlockLootTable.LootRecord> rngesusLoot = new ArrayList<>();

                List<SkyBlockLootTable.LootRecord> lootRecords = mob.getLootTable().getLootTable();

                for (SkyBlockLootTable.LootRecord lootRecord : lootRecords) {
                    double chance = lootRecord.getChancePercent();
                    if (chance <= 0.01) rngesusLoot.add(lootRecord);
                    else if (chance <= 0.1) legendaryLoot.add(lootRecord);
                    else if (chance <= 1) rareLoot.add(lootRecord);
                    else if (chance <= 30) uncommonLoot.add(lootRecord);
                    else commonLoot.add(lootRecord);
                }

                List<MobType> mobtypes = mob.getMobTypes();

                if (mobtypes.size() == 1) {
                    lore.add("§7Mob Type: " + mobtypes.getFirst().getFullDisplayName());
                    lore.add("");
                } else if (mobtypes.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (MobType mobType : mobtypes) {
                        sb.append(mobType.getFullDisplayName());
                        sb.append("§7, ");
                    }
                    sb.delete(sb.chars().sum() - 3, sb.chars().sum());

                    lore.add("§7Mob Types: " + sb);
                    lore.add("");
                }

                lore.add("§7Mob Stats:");
                lore.add("§7Health: " + ItemStatistic.HEALTH.getDisplayColor() + Math.round(mob.getBaseStatistics().getOverall(ItemStatistic.HEALTH).floatValue()) + ItemStatistic.HEALTH.getSymbol());
                lore.add("§7Damage: " + ItemStatistic.DAMAGE.getDisplayColor() + Math.round(mob.getBaseStatistics().getOverall(ItemStatistic.DAMAGE).floatValue()) + ItemStatistic.DAMAGE.getSymbol());
                lore.add("§7Coins per Kill: §6" + otherLoot.getCoinAmount());
                lore.add("§7" + mob.getSkillCategory().asCategory().getName() + " Exp: §3" + otherLoot.getSkillXPAmount());
                lore.add("§7XP Orbs: §3" + otherLoot.getXpOrbAmount());
                lore.add("");
                lore.add("§7Kills: §a" + kills);
                lore.add("§7Deaths: §a" + deaths);
                lore.add("");

                if (!commonLoot.isEmpty()) {
                    lore.add(Rarity.COMMON.getColor() + "Common Loot");
                    for (SkyBlockLootTable.LootRecord lootRecord : commonLoot) {
                        lore.add(" §8■ §f" + lootRecord.getItemType().getDisplayName());
                    }
                    lore.add("");
                }
                if (!uncommonLoot.isEmpty()) {
                    lore.add(Rarity.UNCOMMON.getColor() + "Uncommon Loot");
                    for (SkyBlockLootTable.LootRecord lootRecord : uncommonLoot) {
                        lore.add(" §8■ §f" + lootRecord.getItemType().getDisplayName() + " §8(§a" + lootRecord.getChancePercent() + "%§8)");
                    }
                    lore.add("");
                }
                if (!rareLoot.isEmpty()) {
                    lore.add(Rarity.RARE.getColor() + "Rare Loot");
                    for (SkyBlockLootTable.LootRecord lootRecord : rareLoot) {
                        lore.add(" §8■ §f" + lootRecord.getItemType().getDisplayName() + " §8(§a" + lootRecord.getChancePercent() + "%§8)");
                    }
                    lore.add("");
                }
                if (!legendaryLoot.isEmpty()) {
                    lore.add(Rarity.LEGENDARY.getColor() + "Legendary Loot");
                    for (SkyBlockLootTable.LootRecord lootRecord : legendaryLoot) {
                        lore.add(" §8■ §f" + lootRecord.getItemType().getDisplayName() + " §8(§a" + lootRecord.getChancePercent() + "%§8)");
                    }
                    lore.add("");
                }
                if (!rngesusLoot.isEmpty()) {
                    lore.add("§dRNGesus Loot");
                    for (SkyBlockLootTable.LootRecord lootRecord : rngesusLoot) {
                        lore.add(" §8■ §f" + lootRecord.getItemType().getDisplayName() + " §8(§a" + lootRecord.getChancePercent() + "%§8)");
                    }
                    lore.add("");
                }

                if (!lore.isEmpty()) lore.removeLast();

                return ItemStackCreator.getUsingGUIMaterial("§8[§7Lv" + mob.getLevel() + "§8] §f" + mob.getDisplayName(), guiMaterial, 1, lore);
            });
        }
    }
}
