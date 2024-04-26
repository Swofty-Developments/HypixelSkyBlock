package net.swofty.types.generic.item.items.combat.slayer.blaze.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class ManaDisintegrator implements CustomSkyBlockItem, SkullHead, ExtraUnderNameDisplay, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "61565c1c265f50398c0646a9b7d64289522c0b089091ba5a04f1d18baa860d85";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Combinable in Anvil";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Applies §9ᛃ §7on wands or deployables,",
                "§7reducing their mana cost by §b1%§7. Can",
                "§7be applied up to §a10 §7times."));
    }
}
