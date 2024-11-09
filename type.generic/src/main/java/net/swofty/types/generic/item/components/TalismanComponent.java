package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItemComponent;

import java.util.List;

public class TalismanComponent extends SkyBlockItemComponent {
    @Getter
    private final List<String> talismanDisplay;
    @Getter
    private final ItemStatistics statistics;

    public TalismanComponent(List<String> talismanDisplay) {
        this.talismanDisplay = talismanDisplay;
        this.statistics = ItemStatistics.empty();
        addInheritedComponent(new ExtraRarityComponent("ACCESSORY"));
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new AccessoryComponent());
    }
}