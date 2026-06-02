package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.function.Supplier;

public class ItemLuckyReward extends LuckyReward {
    private final Supplier<ItemStack> itemSupplier;

    public ItemLuckyReward(String name, ItemStack item) {
        this(name, () -> item);
    }

    public ItemLuckyReward(String name, Supplier<ItemStack> itemSupplier) {
        super(name);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        give(player, itemSupplier.get());
    }
}
