package net.swofty.enchantment;

import lombok.Builder;
import lombok.Getter;
import net.swofty.utility.ItemGroups;

import java.util.List;

@Builder
@Getter
public class SkyBlockEnchantment {
    private final EnchantmentType type;
    private final int level;

    public String serialize() {
        return type.name() + ":" + level;
    }

    public static SkyBlockEnchantment deserialize(String string) {
        String[] split = string.split(":");
        return SkyBlockEnchantment.builder()
                .type(EnchantmentType.valueOf(split[0]))
                .level(Integer.parseInt(split[1]))
                .build();
    }

    public record ItemEnchantments(List<SkyBlockEnchantment> enchantments) {
        public void addEnchantment(SkyBlockEnchantment enchantment) {
            enchantments.add(enchantment);
        }
    }

    @Getter
    public enum EnchantmentType {
        SHARPNESS("§7Increases melee damage dealt by §a5%§7.",
                0,
                List.of(),
                List.of(new EnchantmentSource(SourceType.ENCHANTMENT_TABLE, 1, 5)),
                ItemGroups.SWORD, ItemGroups.FISHING_WEAPON, ItemGroups.LONG_SWORD, ItemGroups.GAUNTLET),
        ;

        private final String description;
        private final int requiredBookshelfPower;
        private final List<EnchantmentType> conflicts;
        private final List<EnchantmentSource> sources;
        private final List<ItemGroups> groups;

        EnchantmentType(String description, int requiredBookshelfPower, List<EnchantmentType> conflicts, List<EnchantmentSource> sources, ItemGroups... groups) {
            this.description = description;
            this.requiredBookshelfPower = requiredBookshelfPower;
            this.conflicts = conflicts;
            this.sources = sources;
            this.groups = List.of(groups);
        }

        public record EnchantmentSource(SourceType sourceType, int minLevel, int maxLevel) {}

        public enum SourceType {
            ENCHANTMENT_TABLE,
            BAZAAR,
        }
    }
}
