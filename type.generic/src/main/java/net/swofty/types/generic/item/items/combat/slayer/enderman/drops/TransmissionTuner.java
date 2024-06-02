package net.swofty.types.generic.item.items.combat.slayer.enderman.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class TransmissionTuner implements CustomSkyBlockItem, SkullHead, Sellable, ExtraUnderNameDisplay, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 45000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "52626178b84dbd394cf2409467210282e521032176db21b281fdc8ae9cbe2b2f";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7When applied to an item offering",
                "§7a Transmission ability, its",
                "§7ability range is increased by",
                "§7§d+1§7. Can be applied up to §a4",
                "§a§7times!"));
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Combinable in Anvil";
    }
}
