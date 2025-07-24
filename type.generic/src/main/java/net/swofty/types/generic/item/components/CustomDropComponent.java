package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class CustomDropComponent extends SkyBlockItemComponent {
    private final List<DropRule> rules;
    private static final Random RANDOM = new Random();

    public CustomDropComponent(List<DropRule> rules) {
        this.rules = rules;
    }

    /**
     * Simulates what drops would be given when this item is broken
     * @param brokenItem The item being broken
     * @param player The player breaking the item (can be null for non-player breaks)
     * @param brokenWith The tool used to break the item (can be null)
     * @param region The region where the item is being broken (can be null)
     * @param isOnIsland Whether the break is happening on a player's island
     * @return List of items that would drop
     */
    public static List<SkyBlockItem> simulateDrop(SkyBlockItem brokenItem, SkyBlockPlayer player,
                                                  SkyBlockItem brokenWith, SkyBlockRegion region,
                                                  boolean isOnIsland) {
        List<SkyBlockItem> drops = new ArrayList<>();

        if (!brokenItem.hasComponent(CustomDropComponent.class)) {
            return drops; // No custom drops
        }

        CustomDropComponent component = brokenItem.getComponent(CustomDropComponent.class);

        // Find the first matching rule
        for (DropRule rule : component.getRules()) {
            if (rule.matches(brokenWith, region, isOnIsland)) {
                // Process all drops for this rule
                for (Drop drop : rule.drops()) {
                    if (RANDOM.nextDouble() <= drop.chance()) {
                        int amount = calculateAmount(drop.amount());
                        if (amount > 0) {
                            SkyBlockItem dropItem = new SkyBlockItem(drop.item(), amount);
                            drops.add(dropItem);
                        }
                    }
                }
                break; // Only process the first matching rule
            }
        }

        // No rules matched - return default drop (1 of the original material)
        ItemType originalType = brokenItem.getAttributeHandler().getPotentialType();
        if (originalType != null) {
            drops.add(new SkyBlockItem(originalType, 1));
        }

        return drops;
    }

    private static int calculateAmount(String amountStr) {
        if (amountStr.contains("-")) {
            String[] parts = amountStr.split("-");
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());
            return RANDOM.nextInt(max - min + 1) + min;
        } else {
            return Integer.parseInt(amountStr.trim());
        }
    }

    public record DropRule(
            DropConditions conditions,
            List<Drop> drops
    ) {
        public boolean matches(SkyBlockItem brokenWith, SkyBlockRegion region, boolean isOnIsland) {
            return conditions.matches(brokenWith, region, isOnIsland);
        }
    }

    public record DropConditions(
            Boolean silkTouch,
            ItemType brokenWith,
            String brokenWithNot, // For negation like "!SHEARS"
            LocationType locationType,
            String region,
            String regionNot // For negation like "!DWARVEN_MINES"
    ) {
        public boolean matches(SkyBlockItem tool, SkyBlockRegion currentRegion, boolean isOnIsland) {
            // Check silk touch condition
            if (silkTouch != null) {
                boolean hasSilkTouch = tool != null && hasSilkTouch(tool);
                if (silkTouch != hasSilkTouch) {
                    return false;
                }
            }

            // Check tool type condition
            if (brokenWith != null) {
                if (tool == null) return false;
                ItemType toolType = getToolType(tool);
                if (toolType != brokenWith) return false;
            }

            // Check negated tool type condition
            if (brokenWithNot != null) {
                String negatedType = brokenWithNot.startsWith("!") ?
                        brokenWithNot.substring(1) : brokenWithNot;
                if (tool != null) {
                    ItemType toolType = getToolType(tool);
                    if (toolType != null && toolType.name().equals(negatedType)) {
                        return false;
                    }
                }
            }

            // Check location type condition
            if (locationType != null) {
                boolean matchesLocationType = switch (locationType) {
                    case ISLAND -> isOnIsland;
                    case NON_ISLAND -> !isOnIsland;
                };
                if (!matchesLocationType) return false;
            }

            // Check region condition
            if (region != null && currentRegion != null) {
                if (!currentRegion.getName().equals(region)) {
                    return false;
                }
            }

            // Check negated region condition
            if (regionNot != null && currentRegion != null) {
                String negatedRegion = regionNot.startsWith("!") ?
                        regionNot.substring(1) : regionNot;
                return !currentRegion.getName().equals(negatedRegion);
            }

            return true;
        }

        private boolean hasSilkTouch(SkyBlockItem tool) {
            return tool.getAttributeHandler().getEnchantment(EnchantmentType.SILK_TOUCH) != null;
        }

        private ItemType getToolType(SkyBlockItem tool) {
            return tool.getAttributeHandler().getPotentialType();
        }
    }

    public record Drop(
            ItemType item,
            double chance,
            String amount
    ) {}

    public enum LocationType {
        ISLAND,
        NON_ISLAND
    }

    public static class Builder {
        private final List<DropRule> rules = new ArrayList<>();

        public Builder addRule(DropConditions conditions, List<Drop> drops) {
            rules.add(new DropRule(conditions, drops));
            return this;
        }

        public Builder addSimpleRule(ItemType dropItem, double chance, String amount) {
            DropConditions conditions = new DropConditions(null, null, null, null, null, null);
            List<Drop> drops = List.of(new Drop(dropItem, chance, amount));
            return addRule(conditions, drops);
        }

        public CustomDropComponent build() {
            return new CustomDropComponent(rules);
        }
    }

    public static DropConditions conditions() {
        return new DropConditions(null, null, null, null, null, null);
    }

    public static DropConditions silkTouch(boolean silkTouch) {
        return new DropConditions(silkTouch, null, null, null, null, null);
    }

    public static DropConditions brokenWith(ItemType tool) {
        return new DropConditions(null, tool, null, null, null, null);
    }

    public static DropConditions notBrokenWith(String tool) {
        return new DropConditions(null, null, "!" + tool, null, null, null);
    }

    public static DropConditions location(LocationType locationType) {
        return new DropConditions(null, null, null, locationType, null, null);
    }

    public static DropConditions region(String region) {
        return new DropConditions(null, null, null, null, region, null);
    }

    public static DropConditions notRegion(String region) {
        return new DropConditions(null, null, null, null, null, "!" + region);
    }

    public static Drop drop(ItemType item, double chance, String amount) {
        return new Drop(item, chance, amount);
    }

    public static Drop drop(ItemType item, double chance, int amount) {
        return new Drop(item, chance, String.valueOf(amount));
    }

    public static CustomDropComponent.DropConditions parseDropConditions(Map<String, Object> conditionsConfig) {
        if (conditionsConfig == null) {
            return new CustomDropComponent.DropConditions(null, null, null, null, null, null);
        }

        Boolean silkTouch = null;
        if (conditionsConfig.containsKey("silk_touch")) {
            silkTouch = (Boolean) conditionsConfig.get("silk_touch");
        }

        ItemType brokenWith = null;
        if (conditionsConfig.containsKey("broken_with")) {
            String brokenWithStr = (String) conditionsConfig.get("broken_with");
            try {
                brokenWith = ItemType.valueOf(brokenWithStr);
            } catch (IllegalArgumentException e) {
                Logger.error("Invalid ItemType for broken_with: " + brokenWithStr);
            }
        }

        String brokenWithNot = null;
        if (conditionsConfig.containsKey("broken_with_not")) {
            brokenWithNot = (String) conditionsConfig.get("broken_with_not");
        }

        CustomDropComponent.LocationType locationType = null;
        if (conditionsConfig.containsKey("location_type")) {
            String locationTypeStr = (String) conditionsConfig.get("location_type");
            locationType = CustomDropComponent.LocationType.valueOf(locationTypeStr);
        }

        String region = null;
        if (conditionsConfig.containsKey("region")) {
            region = (String) conditionsConfig.get("region");
        }

        String regionNot = null;
        if (conditionsConfig.containsKey("region_not")) {
            regionNot = (String) conditionsConfig.get("region_not");
        }

        return new CustomDropComponent.DropConditions(
                silkTouch, brokenWith, brokenWithNot, locationType, region, regionNot
        );
    }
}