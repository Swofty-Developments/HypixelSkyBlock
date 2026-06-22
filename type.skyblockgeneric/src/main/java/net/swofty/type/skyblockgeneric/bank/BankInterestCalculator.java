package net.swofty.type.skyblockgeneric.bank;

import java.util.List;

public final class BankInterestCalculator {

    private record Tranche(double lower, double upper, double rate, BankAccountTier requiredTier) {
    }

    private static final List<Tranche> TRANCHES = List.of(
        new Tranche(0, 10_000_000, 0.02, BankAccountTier.STARTER),
        new Tranche(10_000_000, 15_000_000, 0.01, BankAccountTier.STARTER),
        new Tranche(15_000_000, 20_000_000, 0.01, BankAccountTier.GOLD),
        new Tranche(20_000_000, 30_000_000, 0.005, BankAccountTier.DELUXE),
        new Tranche(30_000_000, 50_000_000, 0.002, BankAccountTier.SUPER_DELUXE),
        new Tranche(50_000_000, 160_000_000, 0.001, BankAccountTier.PREMIER),
        new Tranche(160_000_000, 5_160_000_000D, 0.0001, BankAccountTier.LUXURIOUS),
        new Tranche(5_160_000_000D, 55_160_000_000D, 0.00001, BankAccountTier.PALATIAL)
    );

    public static double calculate(double balance, BankAccountTier tier, int museumMilestone) {
        double base = 0;
        for (Tranche tranche : TRANCHES) {
            if (tier.ordinal() < tranche.requiredTier.ordinal() || balance <= tranche.lower) continue;
            base += (Math.min(balance, tranche.upper) - tranche.lower) * tranche.rate;
        }
        double multiplier = 1D + Math.clamp(museumMilestone, 0, 30) * 0.02D;
        return Math.min(base * multiplier, tier.getBaseMaximumInterest() * multiplier);
    }
}
