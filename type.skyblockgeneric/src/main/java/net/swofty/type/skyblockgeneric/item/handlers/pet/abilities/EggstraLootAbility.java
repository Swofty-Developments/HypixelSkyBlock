package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.KillEventPetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.swofty.commons.StringUtility.decimalify;

public class EggstraLootAbility implements PetAbility, KillEventPetAbility {
    private static final RarityValue<Double> CHANCE_PER_LEVEL = new RarityValue<>(0.0, 0.0, 0.8, 1.0, 1.0, 0.0);
    private static final Set<EntityType> ANIMALS = Set.of(
            EntityType.CHICKEN, EntityType.COW, EntityType.SHEEP,
            EntityType.PIG, EntityType.RABBIT
    );

    @Override
    public String getName() {
        return "Eggstra Loot";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double chance = CHANCE_PER_LEVEL.getForRarity(rarity) * level;

        return Arrays.asList(
                "§7Chickens always drop an Egg when killed.",
                "§7Grants a §a" + decimalify(chance, 1) + "% §7chance for animals",
                "§7to drop an additional item."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        return ItemStatistics.empty();
    }

    @Override
    public void onPlayerKilledMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob) {
        EntityType entityType = mob.getEntityType();

        if (entityType == EntityType.CHICKEN) {
            SkyBlockItem eggItem = new SkyBlockItem(ItemStack.of(Material.EGG));
            dropItemForPlayer(player, eggItem, 1, mob);
        }

        if (ANIMALS.contains(entityType)) {
            Rarity rarity = pet.getAttributeHandler().getRarity();
            int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
            double chance = CHANCE_PER_LEVEL.getForRarity(rarity) * level;

            if (Math.random() * 100 < chance) {
                SkyBlockLootTable lootTable = mob.getLootTable();
                if (lootTable != null) {
                    Map<ItemType, SkyBlockLootTable.LootRecord> extraDrops = lootTable.runChances(player);
                    for (ItemType itemType : extraDrops.keySet()) {
                        SkyBlockLootTable.LootRecord record = extraDrops.get(itemType);
                        if (SkyBlockLootTable.LootRecord.isNone(record)) continue;
                        SkyBlockItem item = new SkyBlockItem(itemType, record.getAmount());
                        dropItemForPlayer(player, item, record.getAmount(), mob);
                    }
                }
            }
        }
    }

    // same as SkyBlockMob.java:261-277
    private void dropItemForPlayer(SkyBlockPlayer player, SkyBlockItem item, int amount, SkyBlockMob mob) {
        ItemType type = item.getAttributeHandler().getPotentialType();
        if (type != null && player.canInsertItemIntoSacks(type, amount)) {
            player.getSackItems().increase(type, amount);
        } else if (player.getSkyBlockExperience().getLevel().asInt() >= 6) {
            player.addAndUpdateItem(item);
        } else {
            DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(item, player);
            droppedItem.setInstance(mob.getInstance(), mob.getPosition().add(0, 0.5, 0));
        }
    }
}
