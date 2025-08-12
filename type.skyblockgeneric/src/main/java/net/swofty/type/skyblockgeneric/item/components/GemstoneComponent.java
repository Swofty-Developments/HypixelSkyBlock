package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.gems.Gemstone;
import net.swofty.type.generic.item.SkyBlockItemComponent;

import java.util.List;

@Getter
public class GemstoneComponent extends SkyBlockItemComponent {
    private final List<GemstoneSlot> slots;

    public GemstoneComponent(List<String> gemstoneConfigs) {
        this.slots = gemstoneConfigs.stream()
                .map(config -> {
                    String[] parts = config.split(":");
                    return new GemstoneSlot(
                            Gemstone.Slots.valueOf(parts[0]),
                            Integer.parseInt(parts[1])
                    );
                })
                .toList();
    }

    public record GemstoneSlot(Gemstone.Slots slot, int unlockPrice) {}
}