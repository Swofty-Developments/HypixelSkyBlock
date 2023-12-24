package net.swofty.enchantment;

import lombok.Builder;
import lombok.Getter;
import net.swofty.utility.ItemGroups;
import net.swofty.utility.StringUtility;

import java.util.List;
import java.util.function.Function;

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
                (level) -> switch (level) {
                    case 1 -> 9;
                    case 2 -> 14;
                    case 3 -> 18;
                    case 4 -> 23;
                    case 5 -> 27;
                    case 6 -> 91;
                    case 7 -> 179;
                    default -> throw new IllegalStateException("Unexpected value: " + level);
                }, ItemGroups.SWORD, ItemGroups.FISHING_WEAPON, ItemGroups.LONG_SWORD, ItemGroups.GAUNTLET),
        EFFICIENCY("§7Increases how quickly your tool breaks blocks.",
                0,
                List.of(),
                List.of(new EnchantmentSource(SourceType.ENCHANTMENT_TABLE, 1, 5),
                        new EnchantmentSource(SourceType.REDSTONE_COLLECTION, 4, 5)),
                (level) -> switch (level) {
                    case 1 -> 9;
                    case 2 -> 14;
                    case 3 -> 18;
                    case 4 -> 23;
                    case 5 -> 27;
                    default -> throw new IllegalStateException("Unexpected value: " + level);
                }, ItemGroups.TOOLS),
        ;

        private final String description;
        private final int requiredBookshelfPower;
        private final List<EnchantmentType> conflicts;
        private final List<EnchantmentSource> sources;
        private final Function<Integer, Integer> applyCostCalculation;
        private final List<ItemGroups> groups;

        EnchantmentType(String description, int requiredBookshelfPower, List<EnchantmentType> conflicts, List<EnchantmentSource> sources, Function<Integer, Integer> applyCostCalculation, ItemGroups... groups) {
            this.description = description;
            this.requiredBookshelfPower = requiredBookshelfPower;
            this.conflicts = conflicts;
            this.sources = sources;
            this.applyCostCalculation = applyCostCalculation;
            this.groups = List.of(groups);
        }

        public record EnchantmentSource(SourceType sourceType, int minLevel, int maxLevel) {}

        public enum SourceType {
            ENCHANTMENT_TABLE,
            REDSTONE_COLLECTION,
            BAZAAR,
            ;

            @Override
            public String toString() {
                return StringUtility.toNormalCase(name()
                        .replace("[", "")
                        .replace("]", "")
                        .replace("_", " "));
            }
        }
    }
}
