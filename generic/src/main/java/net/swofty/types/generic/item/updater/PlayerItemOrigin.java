package net.swofty.types.generic.item.updater;

import lombok.NonNull;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum PlayerItemOrigin {
    MAIN_HAND((entry) -> {
        SkyBlockPlayer player = entry.getKey();
        // We don't want to update the item of a player who is in a ShopGUI
        if (player.getOpenInventory() == null)
            return entry.getKey().getItemInMainHand();
        return null;
    }, (player, entry) -> player.setItemInMainHand(entry.getKey()), true),
    HELMET((entry) -> entry.getKey().getHelmet(), (player, entry) -> player.setHelmet(entry.getKey()), true),
    CHESTPLATE((entry) -> entry.getKey().getChestplate(), (player, entry) -> player.setChestplate(entry.getKey()), true),
    LEGGINGS((entry) -> entry.getKey().getLeggings(), (player, entry) -> player.setLeggings(entry.getKey()), true),
    BOOTS((entry) -> entry.getKey().getBoots(), (player, entry) -> player.setBoots(entry.getKey()), true),
    INVENTORY_SLOT((entry) -> entry.getKey().getInventory().getItemStack((Integer) entry.getValue()), (player, entry) -> {
        if ((Integer) entry.getValue() == 8) return; // Do not override skyblock menu
        player.getInventory().setItemStack((Integer) entry.getValue(), entry.getKey());
    }, false);

    private final static Map<UUID, OriginCache> cache = new HashMap<>();

    private final Function<Map.Entry<SkyBlockPlayer, Object>, ItemStack> retriever;
    private final BiConsumer<SkyBlockPlayer, Map.Entry<ItemStack, Object>> setter;
    private final boolean loop;
    @Setter
    private Object data;

    PlayerItemOrigin(Function<Map.Entry<SkyBlockPlayer, Object>, ItemStack> retriever,
                     BiConsumer<SkyBlockPlayer, Map.Entry<ItemStack, Object>> setter, boolean loop) {
        this.retriever = retriever;
        this.setter = setter;
        this.loop = loop;
        this.data = new Object();
    }

    public ItemStack getStack(SkyBlockPlayer player) {
        return retriever.apply(Map.entry(player, data));
    }

    public void setStack(SkyBlockPlayer player, ItemStack stack) {
        setter.accept(player, Map.entry(stack, data));
    }

    public Boolean shouldBeLooped() {
        return loop;
    }

    public static OriginCache getFromCache(UUID uuid) {
        if (!cache.containsKey(uuid))
            cache.put(uuid, new OriginCache(new HashMap<>()));
        return cache.get(uuid);
    }

    public static void setCache(UUID uuid, OriginCache cache) {
        PlayerItemOrigin.cache.put(uuid, cache);
    }

    public static void clearCache(UUID uuid) {
        cache.remove(uuid);
    }

    public record OriginCache(HashMap<PlayerItemOrigin, SkyBlockItem> cache) {
        public @NonNull SkyBlockItem get(PlayerItemOrigin origin) {
            if (!cache.containsKey(origin))
                return new SkyBlockItem(ItemStack.AIR);
            return cache.get(origin);
        }

        public void put(PlayerItemOrigin origin, SkyBlockItem item) {
            cache.put(origin, item);
        }
    }
}
