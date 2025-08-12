package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.item.handlers.anvilcombine.AnvilCombineHandler;
import net.swofty.type.generic.item.handlers.anvilcombine.AnvilCombineRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

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

    public boolean canApply(HypixelPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler(handlerId);
        return handler != null && handler.validateFunction().canApply(player, upgradeItem, sacrificeItem);
    }

    public int applyCostLevels(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, HypixelPlayer player) {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler(handlerId);
        return handler != null ? handler.costFunction().getCost(upgradeItem, sacrificeItem, player) : 0;
    }
}
