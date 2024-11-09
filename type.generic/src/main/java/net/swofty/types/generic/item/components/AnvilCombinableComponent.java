package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.anvilcombine.AnvilCombineHandler;
import net.swofty.types.generic.item.handlers.anvilcombine.AnvilCombineRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class AnvilCombinableComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public AnvilCombinableComponent(String handlerId) {
        this.handlerId = handlerId;
        addInheritedComponent(new ExtraUnderNameComponent("Combinable in Anvil"));
    }

    public void apply(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler(handlerId);
        if (handler != null) {
            handler.combineFunction().apply(upgradeItem, sacrificeItem);
        }
    }

    public boolean canApply(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler(handlerId);
        return handler != null && handler.validateFunction().canApply(player, upgradeItem, sacrificeItem);
    }

    public int applyCostLevels(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler(handlerId);
        return handler != null ? handler.costFunction().getCost(upgradeItem, sacrificeItem, player) : 0;
    }
}
