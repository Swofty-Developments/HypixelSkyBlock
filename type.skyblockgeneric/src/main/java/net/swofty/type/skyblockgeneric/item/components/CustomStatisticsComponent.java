package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.customstatistics.CustomStatisticsConfig;
import net.swofty.type.skyblockgeneric.item.handlers.customstatistics.CustomStatisticsRegistry;

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
