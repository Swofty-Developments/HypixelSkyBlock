package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.user.HypixelPlayer;
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

public class GUIBestiaryMob extends HypixelInventoryGUI {

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

    private static final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    BestiaryData bestiaryData = new BestiaryData();
    BestiaryCategories category;
    BestiaryEntry bestiaryEntry;

    public GUIBestiaryMob(BestiaryCategories category, BestiaryEntry bestiaryEntry) {
        super(StringUtility.stripColor(category.getDisplayName() + " ➡ " + StringUtility.stripColor(bestiaryEntry.getName())), InventoryType.CHEST_6_ROW);
        this.category = category;
        this.bestiaryEntry = bestiaryEntry;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUIBestiaryIsland(category)));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ArrayList<String> lore = new ArrayList<>();
                BestiaryMob mob = bestiaryEntry.getMobs().getFirst();
                GUIMaterial guiMaterial = bestiaryEntry.getGuiMaterial();
                int kills = ((SkyBlockPlayer) getPlayer()).getBestiaryData().getAmount(bestiaryEntry.getMobs());
                int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);

                player.getBestiaryData().getMobDisplay(lore, kills, mob, bestiaryEntry);

                if (guiMaterial.hasTexture()) {
                    return ItemStackCreator.getStackHead("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                            guiMaterial.texture(), 1, lore);
                } else {
                    return ItemStackCreator.getStack("§a" + bestiaryEntry.getName() + " " + StringUtility.getAsRomanNumeral(tier),
                            guiMaterial.material(), 1, lore);
                }
            }
        });

        List<BestiaryMob> bestiaryMobs = bestiaryEntry.getMobs();
        int mobCount = bestiaryMobs.size();

        int[] chosenSlots = SLOTS.getOrDefault(mobCount, displaySlots);

        for (int i = 0; i < bestiaryMobs.size() && i < chosenSlots.length; i++) {
            BestiaryMob mob = bestiaryMobs.get(i);
            GUIMaterial guiMaterial = mob.getGuiMaterial();
            int slot = chosenSlots[i];

            set(new GUIItem(slot) {
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
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

                    lore.removeLast();

                    if (guiMaterial.hasTexture()) {
                        return ItemStackCreator.getStackHead("§8[§7Lv" + mob.getLevel() + "§8] §f" + mob.getDisplayName(), guiMaterial.texture(), 1, lore);
                    } else {
                        return ItemStackCreator.getStack("§8[§7Lv" + mob.getLevel() + "§8] §f" + mob.getDisplayName(), guiMaterial.material(), 1, lore);
                    }
                }
            });
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
