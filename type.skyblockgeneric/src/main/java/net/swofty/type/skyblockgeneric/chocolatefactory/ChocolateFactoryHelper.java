package net.swofty.type.skyblockgeneric.chocolatefactory;

import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Helper class for Chocolate Factory operations.
 * Provides utility methods for production calculation, formatting, and player interactions.
 */
public class ChocolateFactoryHelper {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.0");

    /**
     * Gets the chocolate factory data for a player
     */
    public static DatapointChocolateFactory.ChocolateFactoryData getData(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class)
                .getValue();
    }

    /**
     * Gets the chocolate factory datapoint for a player
     */
    public static DatapointChocolateFactory getDatapoint(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class);
    }

    /**
     * Updates chocolate production for the player based on time elapsed
     */
    public static void updateProduction(SkyBlockPlayer player) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();
        data.updateChocolateFromProduction();
        datapoint.setValue(data);
    }

    /**
     * Handles a click on the chocolate cookie
     */
    public static void handleClick(SkyBlockPlayer player) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();
        data.click();
        datapoint.setValue(data);
    }

    /**
     * Formats chocolate amount for display with commas (e.g., 1,000 not 1k)
     */
    public static String formatChocolate(long amount) {
        return NUMBER_FORMAT.format(amount);
    }

    /**
     * Formats production per second for display
     */
    public static String formatProductionPerSecond(double production) {
        return DECIMAL_FORMAT.format(production) + "/s";
    }

    /**
     * Formats production per hour for display
     */
    public static String formatProductionPerHour(double productionPerSecond) {
        double perHour = productionPerSecond * 3600;
        return formatChocolate((long) perHour) + "/h";
    }

    /**
     * Gets the upgrade cost for Hand-Baked Chocolate
     * Level 1→2: 500, Level 2→3: 1000, etc.
     * Internal level 0 = display level 1, so cost = 500 * (internalLevel + 1)
     * Max internal level is 9 (display level 10)
     */
    public static long getHandBakedChocolateCost(int currentLevel) {
        if (currentLevel >= 9) return 0; // Already maxed at level 10
        return 500L * (currentLevel + 1);
    }

    /**
     * Gets the upgrade cost for Rabbit Barn
     * Level 1 is unlocked by default (free)
     * Level 2 costs 5,000 Chocolate
     * Subsequent levels: floor(5000 × 1.05^(L-2)) where L is target level
     */
    public static long getRabbitBarnCost(int currentLevel) {
        // Level 1 is free (unlocked by default)
        if (currentLevel == 0) {
            return 0;
        }
        // Cost formula: 5000 × 1.05^(currentLevel-1), rounded
        return Math.round(5000 * Math.pow(1.05, currentLevel - 1));
    }

    /**
     * Gets the upgrade cost for Rabbit Shrine
     * Max level is 20, providing 40% extra chance for higher rarity rabbits
     */
    public static long getRabbitShrineCost(int currentLevel) {
        // Cost to upgrade TO level (currentLevel + 1)
        return switch (currentLevel) {
            case 0 -> 10_000_000L;   // To level 1
            case 1 -> 20_000_000L;   // To level 2
            case 2 -> 30_000_000L;   // To level 3
            case 3 -> 40_000_000L;   // To level 4
            case 4 -> 60_000_000L;   // To level 5
            case 5 -> 80_000_000L;   // To level 6
            case 6 -> 100_000_000L;  // To level 7
            case 7 -> 120_000_000L;  // To level 8
            case 8 -> 150_000_000L;  // To level 9
            case 9 -> 200_000_000L;  // To level 10
            case 10 -> 250_000_000L; // To level 11
            case 11 -> 300_000_000L; // To level 12
            case 12 -> 350_000_000L; // To level 13
            case 13 -> 400_000_000L; // To level 14
            case 14 -> 450_000_000L; // To level 15
            case 15 -> 500_000_000L; // To level 16
            case 16 -> 550_000_000L; // To level 17
            case 17 -> 600_000_000L; // To level 18
            case 18 -> 650_000_000L; // To level 19
            case 19 -> 700_000_000L; // To level 20
            default -> 0L;           // Already maxed
        };
    }

    /**
     * Gets the upgrade cost for Time Tower based on prestige level
     * Level 1 is unlocked by default (free)
     */
    public static long getTimeTowerCost(int currentLevel, int prestigeLevel) {
        // Level 1 is free (unlocked by default)
        if (currentLevel == 0) {
            return 0;
        }

        // Base cost depends on prestige (Factory II = prestige 1, Factory III = prestige 2, etc.)
        long baseCost = switch (prestigeLevel) {
            case 1 -> 5_500_000L;  // Factory II
            case 2 -> 6_000_000L;  // Factory III
            case 3 -> 6_500_000L;  // Factory IV
            case 4 -> 7_000_000L;  // Factory V
            default -> 7_500_000L; // Factory VI+
        };

        // Multiplier for each level (upgrading TO level currentLevel+1)
        int multiplier = switch (currentLevel) {
            case 1 -> 1;   // To level 2
            case 2 -> 2;   // To level 3
            case 3 -> 3;   // To level 4
            case 4 -> 4;   // To level 5
            case 5 -> 6;   // To level 6
            case 6 -> 8;   // To level 7
            case 7 -> 10;  // To level 8
            case 8 -> 12;  // To level 9
            case 9 -> 14;  // To level 10
            case 10 -> 16; // To level 11
            case 11 -> 20; // To level 12
            case 12 -> 24; // To level 13
            case 13 -> 30; // To level 14
            case 14 -> 40; // To level 15
            default -> 40;
        };

        return baseCost * multiplier;
    }

    /**
     * Gets the upgrade cost for Time Tower (legacy, uses minimum prestige)
     */
    public static long getTimeTowerCost(int currentLevel) {
        return getTimeTowerCost(currentLevel, 1);
    }

    /**
     * Gets the upgrade cost for Coach Jackrabbit
     * Max level is 20, providing +0.2x CpS multiplier
     */
    public static long getCoachJackrabbitCost(int currentLevel) {
        // Cost to upgrade TO level (currentLevel + 1)
        return switch (currentLevel) {
            case 0 -> 2_200_000L;    // To level 1
            case 1 -> 3_900_000L;    // To level 2
            case 2 -> 5_300_000L;    // To level 3
            case 3 -> 7_200_000L;    // To level 4
            case 4 -> 9_700_000L;    // To level 5
            case 5 -> 13_000_000L;   // To level 6
            case 6 -> 18_000_000L;   // To level 7
            case 7 -> 24_000_000L;   // To level 8
            case 8 -> 32_000_000L;   // To level 9
            case 9 -> 43_000_000L;   // To level 10
            case 10 -> 59_000_000L;  // To level 11
            case 11 -> 79_000_000L;  // To level 12
            case 12 -> 110_000_000L; // To level 13
            case 13 -> 140_000_000L; // To level 14
            case 14 -> 190_000_000L; // To level 15
            case 15 -> 260_000_000L; // To level 16
            case 16 -> 350_000_000L; // To level 17
            case 17 -> 480_000_000L; // To level 18
            case 18 -> 650_000_000L; // To level 19
            case 19 -> 870_000_000L; // To level 20
            default -> 0L;           // Already maxed
        };
    }

    /**
     * Gets the employee index (1-7) for cost calculation
     */
    public static int getEmployeeIndex(String employeeName) {
        return switch (employeeName) {
            case "Rabbit Bro" -> 1;
            case "Rabbit Cousin" -> 2;
            case "Rabbit Sis" -> 3;
            case "Rabbit Daddy" -> 4;
            case "Rabbit Granny" -> 5;
            case "Rabbit Uncle" -> 6;
            case "Rabbit Dog" -> 7;
            default -> 1;
        };
    }

    /**
     * Gets the cost to hire/upgrade an employee at a specific level
     * For Rabbit Bro levels 1-10: (30 + 20 × CF) × multiplier
     * For all other cases: base_cost × 1.05^L where base_cost = (216 + 144 × CF) × i²
     */
    public static long getEmployeeCost(String employeeName, int targetLevel, int chocolateFactoryLevel) {
        int employeeIndex = getEmployeeIndex(employeeName);
        int cf = chocolateFactoryLevel; // CF level 1-6

        // Rabbit Bro's first 10 levels use special formula
        if (employeeName.equals("Rabbit Bro") && targetLevel <= 10) {
            double multiplier = getRabbitBroMultiplier(targetLevel);
            return (long) ((30 + 20 * cf) * multiplier);
        }

        // All other cases: base_cost × 1.05^L
        double baseCost = (216 + 144.0 * cf) * (employeeIndex * employeeIndex);
        return (long) (baseCost * Math.pow(1.05, targetLevel));
    }

    /**
     * Gets the cost to hire/upgrade an employee (uses player's CF level)
     */
    public static long getEmployeeCost(SkyBlockPlayer player, String employeeName, int targetLevel) {
        int cfLevel = getData(player).getPrestigeLevel() + 1; // Prestige 0 = CF1, etc.
        return getEmployeeCost(employeeName, targetLevel, cfLevel);
    }

    /**
     * Legacy method - assumes CF level 1
     */
    public static long getEmployeeCost(String employeeName, int targetLevel) {
        return getEmployeeCost(employeeName, targetLevel, 1);
    }

    /**
     * Gets the multiplier for Rabbit Bro's first 10 levels
     */
    private static double getRabbitBroMultiplier(int level) {
        return switch (level) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 8;
            case 6 -> 9;
            case 7 -> 9.5;
            case 8 -> 10;
            case 9 -> 10.5;
            case 10 -> 11;
            default -> 11;
        };
    }

    /**
     * Gets the base production per level for an employee type
     * This is the chocolate per second gained per employee level
     */
    public static double getEmployeeBaseProduction(String employeeName) {
        return switch (employeeName) {
            case "Rabbit Bro" -> 1.0;      // +1/level
            case "Rabbit Cousin" -> 2.0;   // +2/level
            case "Rabbit Sis" -> 3.0;      // +3/level
            case "Rabbit Daddy" -> 4.0;    // +4/level
            case "Rabbit Granny" -> 5.0;   // +5/level
            case "Rabbit Uncle" -> 6.0;    // +6/level
            case "Rabbit Dog" -> 7.0;      // +7/level
            default -> 1.0;
        };
    }

    /**
     * Gets the employee that must be at level 20 to unlock the given employee
     * Returns null if no prerequisite (Rabbit Bro)
     */
    public static String getEmployeePrerequisite(String employeeName) {
        return switch (employeeName) {
            case "Rabbit Bro" -> null;           // No prerequisite
            case "Rabbit Cousin" -> "Rabbit Bro";
            case "Rabbit Sis" -> "Rabbit Cousin";
            case "Rabbit Daddy" -> "Rabbit Sis";
            case "Rabbit Granny" -> "Rabbit Daddy";
            case "Rabbit Uncle" -> "Rabbit Granny";
            case "Rabbit Dog" -> "Rabbit Uncle";
            default -> null;
        };
    }

    /**
     * Checks if an employee is unlocked for a player
     */
    public static boolean isEmployeeUnlocked(SkyBlockPlayer player, String employeeName) {
        String prerequisite = getEmployeePrerequisite(employeeName);
        if (prerequisite == null) return true; // No prerequisite

        DatapointChocolateFactory.ChocolateFactoryData data = getData(player);
        DatapointChocolateFactory.EmployeeData prereqEmployee = data.getEmployees().get(prerequisite);
        return prereqEmployee != null && prereqEmployee.getLevel() >= 20;
    }

    /**
     * Tries to purchase an upgrade
     * @return true if purchase was successful
     */
    public static boolean purchaseUpgrade(SkyBlockPlayer player, UpgradeType type) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();

        long cost = switch (type) {
            case HAND_BAKED_CHOCOLATE -> getHandBakedChocolateCost(data.getHandBakedChocolateLevel());
            case RABBIT_BARN -> getRabbitBarnCost(data.getRabbitBarnLevel());
            case RABBIT_SHRINE -> getRabbitShrineCost(data.getRabbitShrineLevel());
            case TIME_TOWER -> getTimeTowerCost(data.getTimeTowerLevel(), data.getPrestigeLevel());
            case COACH_JACKRABBIT -> getCoachJackrabbitCost(data.getCoachJackrabbitLevel());
        };

        if (data.removeChocolate(cost)) {
            switch (type) {
                case HAND_BAKED_CHOCOLATE -> data.setHandBakedChocolateLevel(data.getHandBakedChocolateLevel() + 1);
                case RABBIT_BARN -> data.setRabbitBarnLevel(data.getRabbitBarnLevel() + 1);
                case RABBIT_SHRINE -> data.setRabbitShrineLevel(data.getRabbitShrineLevel() + 1);
                case TIME_TOWER -> data.setTimeTowerLevel(data.getTimeTowerLevel() + 1);
                case COACH_JACKRABBIT -> data.setCoachJackrabbitLevel(data.getCoachJackrabbitLevel() + 1);
            }
            datapoint.setValue(data);
            return true;
        }
        return false;
    }

    /**
     * Tries to hire or upgrade an employee
     * @return true if purchase was successful
     */
    public static boolean hireOrUpgradeEmployee(SkyBlockPlayer player, String employeeName) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();

        DatapointChocolateFactory.EmployeeData existing = data.getEmployees().get(employeeName);
        int targetLevel = existing != null ? existing.getLevel() + 1 : 1;

        // Check if employee is unlocked (prerequisite employee at level 20)
        if (!isEmployeeUnlocked(player, employeeName)) {
            return false;
        }

        long cost = getEmployeeCost(player, employeeName, targetLevel);

        if (data.removeChocolate(cost)) {
            double baseProduction = getEmployeeBaseProduction(employeeName);
            data.setEmployee(employeeName, targetLevel, baseProduction);
            datapoint.setValue(data);
            return true;
        }
        return false;
    }

    /**
     * Activates the Time Tower for the player
     * @return true if activation was successful
     */
    public static boolean activateTimeTower(SkyBlockPlayer player) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();

        if (data.activateTimeTower()) {
            datapoint.setValue(data);
            return true;
        }
        return false;
    }

    /**
     * Adds a Time Tower charge to the player
     */
    public static void addTimeTowerCharge(SkyBlockPlayer player) {
        DatapointChocolateFactory datapoint = getDatapoint(player);
        DatapointChocolateFactory.ChocolateFactoryData data = datapoint.getValue();
        data.setTimeTowerCharges(data.getTimeTowerCharges() + 1);
        datapoint.setValue(data);
    }

    /**
     * Gets the prestige rank name based on level
     */
    public static String getPrestigeRankName(int level) {
        return switch (level) {
            case 0 -> "Newcomer";
            case 1 -> "Apprentice";
            case 2 -> "Worker";
            case 3 -> "Journeyman";
            case 4 -> "Expert";
            case 5 -> "Master";
            case 6 -> "Grandmaster";
            default -> "Unknown";
        };
    }

    /**
     * Gets the prestige rank color based on level
     */
    public static String getPrestigeRankColor(int level) {
        return switch (level) {
            case 0 -> "§7";
            case 1 -> "§a";
            case 2 -> "§9";
            case 3 -> "§5";
            case 4 -> "§6";
            case 5 -> "§d";
            case 6 -> "§b";
            default -> "§f";
        };
    }

    public enum UpgradeType {
        HAND_BAKED_CHOCOLATE,
        RABBIT_BARN,
        RABBIT_SHRINE,
        TIME_TOWER,
        COACH_JACKRABBIT
    }
}
