package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.item.ItemType;

@AllArgsConstructor
@Getter
public class MinionIngredient {
    private ItemType item;
    private Integer amount;
}
