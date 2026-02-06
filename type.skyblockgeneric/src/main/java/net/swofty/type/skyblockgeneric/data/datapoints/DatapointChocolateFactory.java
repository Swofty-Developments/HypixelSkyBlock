package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatapointChocolateFactory extends SkyBlockDatapoint<DatapointChocolateFactory.ChocolateFactoryData> {
    private static final Serializer<ChocolateFactoryData> serializer = new Serializer<>() {
        @Override
        public String serialize(ChocolateFactoryData value) {
            JSONObject json = new JSONObject();

            json.put("chocolate", value.getChocolate());
            json.put("chocolateAllTime", value.getChocolateAllTime());
            json.put("lastUpdated", value.getLastUpdated());
            json.put("partialChocolate", value.getPartialChocolate());

            // Upgrades
            json.put("timeTowerLevel", value.getTimeTowerLevel());
            json.put("timeTowerCharges", value.getTimeTowerCharges());
            json.put("timeTowerLastUsed", value.getTimeTowerLastUsed());
            json.put("timeTowerActiveUntil", value.getTimeTowerActiveUntil());
            json.put("rabbitBarnLevel", value.getRabbitBarnLevel());
            json.put("handBakedChocolateLevel", value.getHandBakedChocolateLevel());
            json.put("rabbitShrineLevel", value.getRabbitShrineLevel());
            json.put("coachJackrabbitLevel", value.getCoachJackrabbitLevel());
            json.put("employees", serializeEmployees(value.getEmployees()));

            // Production stats
            json.put("totalClicks", value.getTotalClicks());
            json.put("totalTimeTowerUsages", value.getTotalTimeTowerUsages());
            json.put("foundRabbits", serializeStringSet(value.getFoundRabbits()));

            // Shop stats
            json.put("totalChocolateSpent", value.getTotalChocolateSpent());

            // Hoppity Hunt
            json.put("claimedEggs", serializeStringSet(value.getClaimedEggs()));
            json.put("totalEggsFound", value.getTotalEggsFound());

            return json.toString();
        }

        @Override
        public ChocolateFactoryData deserialize(String json) {
            if (json == null || json.isEmpty()) {
                return new ChocolateFactoryData();
            }

            JSONObject jsonObject = new JSONObject(json);
            Map<String, EmployeeData> employees = deserializeEmployees(jsonObject.optJSONObject("employees"));
            Set<String> foundRabbits = deserializeStringSet(jsonObject.optJSONArray("foundRabbits"));
            Set<String> claimedEggs = deserializeStringSet(jsonObject.optJSONArray("claimedEggs"));

            return new ChocolateFactoryData(
                    jsonObject.optLong("chocolate", 0L),
                    jsonObject.optLong("chocolateAllTime", 0L),
                    jsonObject.optLong("lastUpdated", System.currentTimeMillis()),
                    jsonObject.optDouble("partialChocolate", 0.0),
                    jsonObject.optInt("timeTowerLevel", 0),
                    jsonObject.optInt("timeTowerCharges", 0),
                    jsonObject.optLong("timeTowerLastUsed", 0L),
                    jsonObject.optLong("timeTowerActiveUntil", 0L),
                    jsonObject.optInt("rabbitBarnLevel", 0),
                    jsonObject.optInt("handBakedChocolateLevel", 0),
                    jsonObject.optInt("rabbitShrineLevel", 0),
                    jsonObject.optInt("coachJackrabbitLevel", 0),
                    employees,
                    jsonObject.optLong("totalClicks", 0L),
                    jsonObject.optInt("totalTimeTowerUsages", 0),
                    foundRabbits,
                    jsonObject.optLong("totalChocolateSpent", 0L),
                    claimedEggs,
                    jsonObject.optInt("totalEggsFound", 0)
            );
        }

        @Override
        public ChocolateFactoryData clone(ChocolateFactoryData value) {
            Map<String, EmployeeData> clonedEmployees = cloneEmployees(value.getEmployees());

            return new ChocolateFactoryData(
                    value.getChocolate(),
                    value.getChocolateAllTime(),
                    value.getLastUpdated(),
                    value.getPartialChocolate(),
                    value.getTimeTowerLevel(),
                    value.getTimeTowerCharges(),
                    value.getTimeTowerLastUsed(),
                    value.getTimeTowerActiveUntil(),
                    value.getRabbitBarnLevel(),
                    value.getHandBakedChocolateLevel(),
                    value.getRabbitShrineLevel(),
                    value.getCoachJackrabbitLevel(),
                    clonedEmployees,
                    value.getTotalClicks(),
                    value.getTotalTimeTowerUsages(),
                    new HashSet<>(value.getFoundRabbits()),
                    value.getTotalChocolateSpent(),
                    new HashSet<>(value.getClaimedEggs()),
                    value.getTotalEggsFound()
            );
        }

        private JSONObject serializeEmployees(Map<String, EmployeeData> employees) {
            JSONObject employeesJson = new JSONObject();
            for (Map.Entry<String, EmployeeData> entry : employees.entrySet()) {
                JSONObject employeeJson = new JSONObject();
                EmployeeData employee = entry.getValue();
                employeeJson.put("level", employee.getLevel());
                employeeJson.put("baseProduction", employee.getBaseProduction());
                employeesJson.put(entry.getKey(), employeeJson);
            }
            return employeesJson;
        }

        private Map<String, EmployeeData> deserializeEmployees(JSONObject employeesJson) {
            Map<String, EmployeeData> employees = new HashMap<>();
            if (employeesJson == null) {
                return employees;
            }

            for (String key : employeesJson.keySet()) {
                JSONObject employeeJson = employeesJson.getJSONObject(key);
                employees.put(key, new EmployeeData(
                        employeeJson.optInt("level", 0),
                        employeeJson.optDouble("baseProduction", 0.0)
                ));
            }
            return employees;
        }

        private JSONArray serializeStringSet(Set<String> values) {
            JSONArray array = new JSONArray();
            for (String value : values) {
                array.put(value);
            }
            return array;
        }

        private Set<String> deserializeStringSet(JSONArray array) {
            Set<String> values = new HashSet<>();
            if (array == null) {
                return values;
            }

            for (int i = 0; i < array.length(); i++) {
                values.add(array.getString(i));
            }
            return values;
        }

        private Map<String, EmployeeData> cloneEmployees(Map<String, EmployeeData> employees) {
            Map<String, EmployeeData> clonedEmployees = new HashMap<>();
            for (Map.Entry<String, EmployeeData> entry : employees.entrySet()) {
                EmployeeData employee = entry.getValue();
                clonedEmployees.put(entry.getKey(), new EmployeeData(
                        employee.getLevel(),
                        employee.getBaseProduction()
                ));
            }
            return clonedEmployees;
        }
    };

    public DatapointChocolateFactory(String key, ChocolateFactoryData value, Serializer<ChocolateFactoryData> serializer) {
        super(key, value, serializer);
    }

    public DatapointChocolateFactory(String key, ChocolateFactoryData value) {
        super(key, value, serializer);
    }

    public DatapointChocolateFactory(String key) {
        super(key, new ChocolateFactoryData(), serializer);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChocolateFactoryData {
        private static final int BASE_CLICK_POWER = 1;
        private static final int BASE_RABBIT_SLOTS = 3;
        private static final double BASE_MULTIPLIER = 1.0;
        private static final double SHRINE_MULTIPLIER_PER_LEVEL = 0.1;
        private static final double TIME_TOWER_MULTIPLIER_PER_LEVEL = 0.1;
        private static final double COACH_MULTIPLIER_PER_LEVEL = 0.01;
        private static final long TIME_TOWER_DURATION_MS = 60L * 60L * 1000L;
        private static final double MILLIS_PER_SECOND = 1000.0;

        private long chocolate;
        private long chocolateAllTime;
        private long lastUpdated = System.currentTimeMillis();
        private double partialChocolate; // Accumulates fractional chocolate production

        // Upgrades
        private int timeTowerLevel;
        private int timeTowerCharges;
        private long timeTowerLastUsed;
        private long timeTowerActiveUntil;
        private int rabbitBarnLevel;
        private int handBakedChocolateLevel;
        private int rabbitShrineLevel;
        private int coachJackrabbitLevel;

        // Employees (rabbit name -> employee data)
        private Map<String, EmployeeData> employees = new HashMap<>();

        // Statistics
        private long totalClicks;
        private int totalTimeTowerUsages;

        // Collection
        private Set<String> foundRabbits = new HashSet<>();

        // Shop stats
        private long totalChocolateSpent;

        // Hoppity Hunt
        private Set<String> claimedEggs = new HashSet<>();
        private int totalEggsFound;

        /**
         * Adds chocolate and updates all-time total
         */
        public void addChocolate(long amount) {
            this.chocolate += amount;
            if (amount > 0) {
                this.chocolateAllTime += amount;
            }
        }

        /**
         * Removes chocolate (for purchases)
         */
        public boolean removeChocolate(long amount) {
            if (this.chocolate >= amount) {
                this.chocolate -= amount;
                return true;
            }
            return false;
        }

        /**
         * Gets the click power (chocolate per click)
         * Base is 1, increases with Hand-Baked Chocolate upgrade
         */
        public int getClickPower() {
            return BASE_CLICK_POWER + handBakedChocolateLevel;
        }

        /**
         * Gets the maximum number of rabbit slots based on Rabbit Barn level
         */
        public int getMaxRabbitSlots() {
            return BASE_RABBIT_SLOTS + rabbitBarnLevel;
        }

        /**
         * Gets the production multiplier from Rabbit Shrine
         * Base is 1.0, increases by 0.1 per level
         */
        public double getShrineMultiplier() {
            return BASE_MULTIPLIER + (rabbitShrineLevel * SHRINE_MULTIPLIER_PER_LEVEL);
        }

        /**
         * Gets the Time Tower multiplier when active
         * Base is 1.0, increases by 0.1 per level
         */
        public double getTimeTowerMultiplier() {
            if (System.currentTimeMillis() < timeTowerActiveUntil) {
                return BASE_MULTIPLIER + (timeTowerLevel * TIME_TOWER_MULTIPLIER_PER_LEVEL);
            }
            return BASE_MULTIPLIER;
        }

        /**
         * Checks if Time Tower is currently active
         */
        public boolean isTimeTowerActive() {
            return System.currentTimeMillis() < timeTowerActiveUntil;
        }

        /**
         * Activates the Time Tower if charges are available
         */
        public boolean activateTimeTower() {
            if (timeTowerCharges > 0 && !isTimeTowerActive()) {
                long now = System.currentTimeMillis();
                timeTowerCharges--;
                timeTowerLastUsed = now;
                timeTowerActiveUntil = now + TIME_TOWER_DURATION_MS; // 1 hour
                totalTimeTowerUsages++;
                return true;
            }
            return false;
        }

        /**
         * Gets the Coach Jackrabbit multiplier
         * Base is 1.0, increases by 0.01 per level
         */
        public double getCoachMultiplier() {
            return BASE_MULTIPLIER + (coachJackrabbitLevel * COACH_MULTIPLIER_PER_LEVEL);
        }

        /**
         * Calculates total chocolate production per second from all sources
         */
        public double getChocolatePerSecond() {
            double baseProduction = 0;

            // Sum production from all employees
            for (EmployeeData employee : employees.values()) {
                baseProduction += employee.getProductionPerSecond();
            }

            // Apply multipliers
            baseProduction *= getShrineMultiplier();
            baseProduction *= getTimeTowerMultiplier();
            baseProduction *= getCoachMultiplier();

            return baseProduction;
        }

        /**
         * Updates chocolate based on time passed since last update
         */
        public void updateChocolateFromProduction() {
            long now = System.currentTimeMillis();
            double secondsPassed = (now - lastUpdated) / MILLIS_PER_SECOND;

            if (secondsPassed > 0) {
                // Accumulate fractional production
                partialChocolate += getChocolatePerSecond() * secondsPassed;

                // Only add whole chocolate
                long wholeChocolate = (long) partialChocolate;
                if (wholeChocolate > 0) {
                    addChocolate(wholeChocolate);
                    partialChocolate -= wholeChocolate; // Keep the fractional part
                }

                lastUpdated = now;
            }
        }

        /**
         * Registers a click and adds chocolate
         */
        public void click() {
            addChocolate(getClickPower());
            totalClicks++;
        }

        /**
         * Hires or upgrades an employee
         */
        public void setEmployee(String name, int level, double baseProduction) {
            employees.put(name, new EmployeeData(level, baseProduction));
        }

        /**
         * Gets employee count
         */
        public int getEmployeeCount() {
            return employees.size();
        }

        /**
         * Gets prestige level based on all-time chocolate
         */
        public int getPrestigeLevel() {
            if (chocolateAllTime >= 1_000_000_000_000L) return 6; // 1T
            if (chocolateAllTime >= 10_000_000_000L) return 5;    // 10B
            if (chocolateAllTime >= 1_000_000_000L) return 4;     // 1B
            if (chocolateAllTime >= 100_000_000L) return 3;       // 100M
            if (chocolateAllTime >= 10_000_000L) return 2;        // 10M
            if (chocolateAllTime >= 1_000_000L) return 1;         // 1M
            return 0;
        }

        /**
         * Adds a rabbit to the found collection
         */
        public void addFoundRabbit(String rabbitName) {
            foundRabbits.add(rabbitName);
        }

        /**
         * Checks if a rabbit has been found
         */
        public boolean hasFoundRabbit(String rabbitName) {
            return foundRabbits.contains(rabbitName);
        }

        /**
         * Gets the count of found rabbits
         */
        public int getFoundRabbitCount() {
            return foundRabbits.size();
        }

        /**
         * Adds to total chocolate spent (for shop milestones tracking)
         */
        public void addChocolateSpent(long amount) {
            this.totalChocolateSpent += amount;
        }

        /**
         * Claims an egg location for the current hunt
         */
        public void claimEgg(String eggLocationId) {
            claimedEggs.add(eggLocationId);
            totalEggsFound++;
        }

        /**
         * Checks if an egg location has been claimed in the current hunt
         */
        public boolean hasClaimedEgg(String eggLocationId) {
            return claimedEggs.contains(eggLocationId);
        }

        /**
         * Resets claimed eggs for a new hunt
         */
        public void resetClaimedEggs() {
            claimedEggs.clear();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class EmployeeData {
        private int level;
        private double baseProduction;

        /**
         * Gets production per second for this employee
         * Production = baseProduction * level
         */
        public double getProductionPerSecond() {
            return baseProduction * level;
        }
    }
}
