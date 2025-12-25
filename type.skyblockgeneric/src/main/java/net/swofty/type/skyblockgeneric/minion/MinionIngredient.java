package net.swofty.type.skyblockgeneric.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;

@AllArgsConstructor
@Getter
public class MinionIngredient {
    private ItemType item;
    private Integer amount;
}
