package net.swofty.commons.skyblock.item.updater;

import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

import java.util.Map;
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
    OFF_HAND((entry) -> entry.getKey().getItemInOffHand(), (player, entry) -> player.setItemInOffHand(entry.getKey()), true),
    HELMET((entry) -> entry.getKey().getHelmet(), (player, entry) -> player.setHelmet(entry.getKey()), true),
    CHESTPLATE((entry) -> entry.getKey().getChestplate(), (player, entry) -> player.setChestplate(entry.getKey()), true),
    LEGGINGS((entry) -> entry.getKey().getLeggings(), (player, entry) -> player.setLeggings(entry.getKey()), true),
    BOOTS((entry) -> entry.getKey().getBoots(), (player, entry) -> player.setBoots(entry.getKey()), true),
    INVENTORY_SLOT((entry) -> entry.getKey().getInventory().getItemStack((Integer) entry.getValue()),
            (player, entry) -> player.getInventory().setItemStack((Integer) entry.getValue(), entry.getKey()),
            false);

    private final Function<Map.Entry<SkyBlockPlayer, Object>, ItemStack> retriever;
    private final BiConsumer<SkyBlockPlayer, Map.Entry<ItemStack, Object>> setter;
    private final boolean loop;
    private Object data;

    PlayerItemOrigin(Function<Map.Entry<SkyBlockPlayer, Object>, ItemStack> retriever,
                     BiConsumer<SkyBlockPlayer, Map.Entry<ItemStack, Object>> setter, boolean loop) {
        this.retriever = retriever;
        this.setter = setter;
        this.loop = loop;
        this.data = new Object();
    }

    public void setData(Object data) {
        this.data = data;
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
}
