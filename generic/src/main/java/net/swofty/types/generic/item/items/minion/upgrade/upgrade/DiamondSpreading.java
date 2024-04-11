package net.swofty.types.generic.item.items.minion.upgrade.upgrade;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionUpgradeItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class DiamondSpreading implements CustomSkyBlockItem, MinionUpgradeItem, Enchanted, Sellable {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7This item can be used as a minion",
                "§7upgrade. Place it in any minion and it",
                "§7will occasionally generate Diamonds!"
        );
    }

    @Override
    public double getSellValue() {
        return 640;
    }
}
