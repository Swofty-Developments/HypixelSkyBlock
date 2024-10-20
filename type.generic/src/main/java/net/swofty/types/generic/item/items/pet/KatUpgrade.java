package net.swofty.types.generic.item.items.pet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.ItemTypeLinker;

@RequiredArgsConstructor
@Getter
public class KatUpgrade {
    private final Long time;
    private final Integer coins;
    @Setter
    private ItemTypeLinker item;
    private Integer amount;

    public KatUpgrade(Long time, Integer coins, ItemTypeLinker item, Integer amount) {
        this.time = time;
        this.coins = coins;
        this.item = item;
        this.amount = amount;
    }

    public static KatUpgrade OnlyCoins(Long time, Integer coins) {
        return new KatUpgrade(time, coins);
    }
    public static KatUpgrade WithItem(Long time, Integer coins, ItemTypeLinker item, Integer amount) {
        return new KatUpgrade(time, coins, item, amount);
    }
}
