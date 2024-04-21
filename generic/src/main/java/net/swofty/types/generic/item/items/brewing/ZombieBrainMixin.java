package net.swofty.types.generic.item.items.brewing;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class ZombieBrainMixin implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Mixins provide a buff that can be",
                "§7added to §cGod Potions§7 in a brewing",
                "§7stand and lasts for the full duration.",
                "",
                "§7Gain §c+10⫽ Ferocity§7!",
                "",
                "§7Duration: §a36h",
                "+§a36h§7 Default",
                "",
                "§7The duration of Mixins can be stacked!",
                "",
                "§eRight-click to consume!",
                "§8(Requires active Booster Cookie)",
                "",
                "§c☠§7 Requires §dZombie Slayer 8§7."
        ));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "dcedb2f4c97016cae7b89e4c6d6978d22ac3476c815c5a09a6792450dd918b6c";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Zombie Brain Mixin";
    }
}
