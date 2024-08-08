package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.types.generic.item.ItemTypeLinker;

@AllArgsConstructor
@Getter
public class MinionIngredient {
    private ItemTypeLinker item;
    private Integer amount;
}
