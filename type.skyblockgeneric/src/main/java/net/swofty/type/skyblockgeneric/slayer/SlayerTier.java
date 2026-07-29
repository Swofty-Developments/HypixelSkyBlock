package net.swofty.type.skyblockgeneric.slayer;

public enum SlayerTier {
    I(1, "I"),
    II(2, "II"),
    III(3, "III"),
    IV(4, "IV"),
    V(5, "V");

    private final int number;
    private final String numeral;

    SlayerTier(int number, String numeral) {
        this.number = number;
        this.numeral = numeral;
    }

    public int number() {
        return number;
    }

    public String numeral() {
        return numeral;
    }
}
