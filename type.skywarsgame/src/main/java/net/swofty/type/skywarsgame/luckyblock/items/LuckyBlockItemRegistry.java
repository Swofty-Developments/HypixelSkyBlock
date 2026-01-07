package net.swofty.type.skywarsgame.luckyblock.items;

import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LuckyBlockItemRegistry {

    public static final Tag<String> LUCKY_BLOCK_ITEM_TAG = Tag.String("lucky_block_item_id");

    private static final Map<String, LuckyBlockItem> ITEMS = new HashMap<>();
    private static final Map<String, LuckyBlockArmor> ARMOR_ITEMS = new HashMap<>();
    private static final Map<String, LuckyBlockWeapon> WEAPON_ITEMS = new HashMap<>();
    private static final Map<String, LuckyBlockConsumable> CONSUMABLE_ITEMS = new HashMap<>();

    public static void register(LuckyBlockItem item) {
        String id = item.getId();
        if (ITEMS.containsKey(id)) {
            Logger.warn("Duplicate lucky block item registration: {}", id);
            return;
        }

        ITEMS.put(id, item);

        if (item instanceof LuckyBlockArmor armor) {
            ARMOR_ITEMS.put(id, armor);
        }
        if (item instanceof LuckyBlockWeapon weapon) {
            WEAPON_ITEMS.put(id, weapon);
        }
        if (item instanceof LuckyBlockConsumable consumable) {
            CONSUMABLE_ITEMS.put(id, consumable);
        }

        Logger.debug("Registered lucky block item: {}", id);
    }

    @Nullable
    public static LuckyBlockItem getItem(String id) {
        return ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockArmor getArmor(String id) {
        return ARMOR_ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockWeapon getWeapon(String id) {
        return WEAPON_ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockConsumable getConsumable(String id) {
        return CONSUMABLE_ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockItem fromItemStack(ItemStack itemStack) {
        String id = itemStack.getTag(LUCKY_BLOCK_ITEM_TAG);
        if (id == null) {
            return null;
        }
        return ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockArmor armorFromItemStack(ItemStack itemStack) {
        String id = itemStack.getTag(LUCKY_BLOCK_ITEM_TAG);
        if (id == null) {
            return null;
        }
        return ARMOR_ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockWeapon weaponFromItemStack(ItemStack itemStack) {
        String id = itemStack.getTag(LUCKY_BLOCK_ITEM_TAG);
        if (id == null) {
            return null;
        }
        return WEAPON_ITEMS.get(id);
    }

    @Nullable
    public static LuckyBlockConsumable consumableFromItemStack(ItemStack itemStack) {
        String id = itemStack.getTag(LUCKY_BLOCK_ITEM_TAG);
        if (id == null) {
            return null;
        }
        return CONSUMABLE_ITEMS.get(id);
    }

    public static boolean isLuckyBlockItem(ItemStack itemStack) {
        return itemStack.hasTag(LUCKY_BLOCK_ITEM_TAG);
    }

    public static Collection<LuckyBlockItem> getAllItems() {
        return ITEMS.values();
    }

    public static Collection<LuckyBlockArmor> getAllArmor() {
        return ARMOR_ITEMS.values();
    }

    public static Collection<LuckyBlockWeapon> getAllWeapons() {
        return WEAPON_ITEMS.values();
    }

    public static Collection<LuckyBlockConsumable> getAllConsumables() {
        return CONSUMABLE_ITEMS.values();
    }

    public static Collection<LuckyBlockItem> getTickableItems() {
        return ITEMS.values().stream()
                .filter(LuckyBlockItem::hasTickEffect)
                .toList();
    }

    public static Collection<LuckyBlockItem> getHitEffectItems() {
        return ITEMS.values().stream()
                .filter(LuckyBlockItem::hasHitEffect)
                .toList();
    }

    public static void clear() {
        ITEMS.clear();
        ARMOR_ITEMS.clear();
        WEAPON_ITEMS.clear();
        CONSUMABLE_ITEMS.clear();
    }

    public static int size() {
        return ITEMS.size();
    }
}
