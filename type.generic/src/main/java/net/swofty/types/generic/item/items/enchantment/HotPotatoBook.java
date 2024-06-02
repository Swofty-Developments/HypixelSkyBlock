package net.swofty.types.generic.item.items.enchantment;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class HotPotatoBook implements AnvilCombinable, Enchanted, Sellable, TrackedUniqueItem {
    @Override
    public void apply(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        HotPotatoable upgrade = (HotPotatoable) upgradeItem.getGenericInstance();
        HotPotatoable.PotatoType potatoType = upgrade.getHotPotatoType();

        ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
        upgradeData.addAmount(1);
        upgradeData.setPotatoType(potatoType);
        upgradeItem.getAttributeHandler().setHotPotatoBookData(upgradeData);
    }

    @Override
    public boolean canApply(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        if (upgradeItem.getGenericInstance() instanceof HotPotatoable) {
            ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
            return upgradeData.getAmount() < 10;
        }
        return false;
    }

    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = HotPotatoable.PotatoType.allLores();
        lore.add("§7This item can be applied to an item up to");
        lore.add("§a10 §7times!");

        return lore;
    }

    @Override
    public double getSellValue() {
        return 38400;
    }
}
