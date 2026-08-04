package net.swofty.type.ravengardgeneric.item.components;

import lombok.Getter;
import net.swofty.type.ravengardgeneric.item.RavengardItemComponent;

import java.util.Map;

/** An empty styled slot, like the unreleased necklace slot. */
@Getter
public class PlaceholderSlotComponent implements RavengardItemComponent {
    private String loreOne;
    private String loreTwo;

    @Override
    public String id() {
        return "PLACEHOLDER_SLOT";
    }

    @Override
    public void configure(Map<String, Object> config) {
        this.loreOne = String.valueOf(config.getOrDefault("lore_one", ""));
        this.loreTwo = String.valueOf(config.getOrDefault("lore_two", ""));
    }
}
