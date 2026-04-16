package net.swofty.type.generic.achievement;

public record AchievementTier(int tier, int goal, int points) {
    public String getRomanNumeral() {
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
