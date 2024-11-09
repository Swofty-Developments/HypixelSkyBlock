package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.customstatistics.CustomStatisticsConfig;
import net.swofty.types.generic.item.handlers.customstatistics.CustomStatisticsRegistry;

@Getter
public class CustomStatisticsComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public CustomStatisticsComponent(String handlerId) {
        this.handlerId = handlerId;
    }

    public CustomStatisticsConfig getConfig() {
        return CustomStatisticsRegistry.getHandler(handlerId);
    }
}
