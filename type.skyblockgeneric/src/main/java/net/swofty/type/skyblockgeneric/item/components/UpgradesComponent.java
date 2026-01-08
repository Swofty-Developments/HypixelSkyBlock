package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.List;

@Getter
public class UpgradesComponent extends SkyBlockItemComponent {
    private final List<UpgradeLevel> levels;

    public UpgradesComponent(List<UpgradeLevel> levels) {
        this.levels = levels;
    }

    public record UpgradeLevel(
            int level,
            List<UpgradeRequirement> requirements
    ) {}

    public record UpgradeRequirement(
            RequirementType type,
            Object requirementData
    ) {
        public enum RequirementType {
            ESSENCE,
            ITEM
        }

        public record EssenceRequirement(
                String essence,
                int amount
        ) {}

        public record ItemRequirement(
                ItemType item,
                int amount
        ) {}
    }
}