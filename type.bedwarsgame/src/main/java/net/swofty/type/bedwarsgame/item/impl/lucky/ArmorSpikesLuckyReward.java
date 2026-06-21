package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class ArmorSpikesLuckyReward extends LuckyReward {
    public ArmorSpikesLuckyReward() {
        super("Armor Spikes");
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        if (!player.getHelmet().isAir()) {
            player.setHelmet(player.getHelmet().with(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.THORNS, 1)));
        }
        if (!player.getChestplate().isAir()) {
            player.setChestplate(player.getChestplate().with(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.THORNS, 1)));
        }
        if (!player.getLeggings().isAir()) {
            player.setLeggings(player.getLeggings().with(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.THORNS, 1)));
        }
        if (!player.getBoots().isAir()) {
            player.setBoots(player.getBoots().with(DataComponents.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.THORNS, 1)));
        }
    }
}
