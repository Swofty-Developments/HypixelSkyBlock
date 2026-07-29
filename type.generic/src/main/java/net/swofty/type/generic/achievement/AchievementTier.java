package net.swofty.type.generic.achievement;

public record AchievementTier(int tier, int goal, int points) {
    public String getRomanNumeral() {
        return toRomanNumeral(tier);
    }

    /**
     * Tier-friendly Roman numeral converter. Tiers in this codebase don't
     * exceed V in practice, so the higher-order rules of
     * {@link net.swofty.commons.StringUtility#getAsRomanNumeral} aren't
     * needed — and a value outside 1-5 should fall back to its decimal form
     * (which the StringUtility variant doesn't do).
     */
    public static String toRomanNumeral(int tier) {
        return switch (tier) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(tier);
        };
    }
}
