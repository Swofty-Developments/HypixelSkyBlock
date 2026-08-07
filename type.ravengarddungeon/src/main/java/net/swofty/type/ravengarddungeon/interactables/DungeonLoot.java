package net.swofty.type.ravengarddungeon.interactables;

import net.minestom.server.item.ItemStack;
import net.swofty.type.ravengardgeneric.item.RavengardItem;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;

import java.util.List;
import java.util.Random;

public final class DungeonLoot {
    private DungeonLoot() {
    }

    public static ItemStack roll(Random random, double distanceFromCenter) {
        List<RavengardItemType> pool = RavengardItemRegistry.all().stream()
                .filter(type -> !type.getId().endsWith("_SLOT"))
                .toList();
        if (pool.isEmpty()) {
            return ItemStack.AIR;
        }
        double rareChance = distanceFromCenter > 220 ? 0.75
                : distanceFromCenter > 120 ? 0.55 : 0.3;
        boolean wantUncommon = random.nextDouble() < rareChance;
        List<RavengardItemType> tiered = pool.stream()
                .filter(type -> wantUncommon
                        ? !type.getRarity().name().equals("COMMON")
                        : type.getRarity().name().equals("COMMON"))
                .toList();
        List<RavengardItemType> source = tiered.isEmpty() ? pool : tiered;
        return RavengardItem.of(source.get(random.nextInt(source.size())).getId());
    }
}
