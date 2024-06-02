package net.swofty.types.generic.enchantment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

/**
 * The source of the enchant, for the enchanting guide
 */
@RequiredArgsConstructor
@Getter
public class EnchantmentSource {

    public final String source;
    public final int minLevel, maxLevel;
    @Nullable
    public SourceType sourceType;

    public EnchantmentSource(SourceType sourceType, int minLevel, int maxLevel) {
        this(sourceType.toString(), minLevel, maxLevel);
        this.sourceType = sourceType;
    }
  
		/* It's here you want to use it later, or just delete it and keep using the string constructor
        public EnchantmentSource(Collection collection, int minLevel, int maxLevel) {
            this(collection.getName()+" Collection", minLevel, maxLevel);
        } */

    @Override
    public String toString() {
        String levelString = minLevel == maxLevel
                ? StringUtility.getAsRomanNumeral(minLevel)
                : StringUtility.getAsRomanNumeral(minLevel) + "-" + StringUtility.getAsRomanNumeral(maxLevel);
        return " §7- " + source + " §7(§a" + levelString + "§7)";
    }

    /**
     * The most common ways to get enchants used in {@link EnchantmentSource}
     */
    public enum SourceType {
        ENCHANTMENT_TABLE,
        BAZAAR,
        FISHING,
        EXPERIMENTS,
        DARK_AUCTION,
        COMMUNITY_SHOP,
        SKYMART,
        CATACOMBS,
        KUUDRA,
        SEASON_OF_JERRY,
        SCORPIUS;

        @Override
        public String toString() {
            return StringUtility.toNormalCase(name()
                    .replace("[", "")
                    .replace("]", ""));
        }
    }

}