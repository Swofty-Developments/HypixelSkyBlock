package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BestiaryCategories {
    YOUR_ISLAND("§aYour island", BestiaryCategory.PRIVATE_ISLAND.values()),
    HUB("§aHub", BestiaryCategory.HUB.values()),
    DEEP_CAVERNS("§bDeep Caverns", BestiaryCategory.DEEP_CAVERNS.values()),
    SPIDERS_DEN("§4Spider's Den", BestiaryCategory.SPIDERS_DEN.values()),
    THE_END("§5The End", BestiaryCategory.THE_END.values()),
    THE_PARK("§2The Park", BestiaryCategory.THE_PARK.values()),
    CRIMSON_ISLE("§cCrimson Isle", BestiaryCategory.CRIMSON_ISLE.values()),
    ;

    private final String displayName;
    private final BestiaryEntry[] entries;

    BestiaryCategories(String displayName, BestiaryEntry[] entries) {
        this.displayName = displayName;
        this.entries = entries;
    }

    public List<BestiaryMob> getAllMobs() {
        return Arrays.stream(entries)
                .flatMap(entry -> entry.getMobs().stream())
                .toList();
    }
}
