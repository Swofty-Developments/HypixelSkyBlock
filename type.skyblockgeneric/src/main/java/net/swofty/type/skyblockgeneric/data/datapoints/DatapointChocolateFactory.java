package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatapointChocolateFactory extends SkyBlockDatapoint<DatapointChocolateFactory.ChocolateFactoryData> {
    private static final Serializer<ChocolateFactoryData> serializer = new Serializer<>() {
        @Override
        public String serialize(ChocolateFactoryData value) {
            JSONObject json = new JSONObject();

            json.put("chocolate", value.chocolate);
            json.put("chocolateAllTime", value.chocolateAllTime);
            json.put("lastUpdated", value.lastUpdated);
            json.put("partialChocolate", value.partialChocolate);

            // Upgrades
            json.put("timeTowerLevel", value.timeTowerLevel);
            json.put("timeTowerCharges", value.timeTowerCharges);
            json.put("timeTowerLastUsed", value.timeTowerLastUsed);
            json.put("timeTowerActiveUntil", value.timeTowerActiveUntil);
            json.put("rabbitBarnLevel", value.rabbitBarnLevel);
            json.put("handBakedChocolateLevel", value.handBakedChocolateLevel);
            json.put("rabbitShrineLevel", value.rabbitShrineLevel);
            json.put("coachJackrabbitLevel", value.coachJackrabbitLevel);

            // Employees - store as JSON object
            JSONObject employeesJson = new JSONObject();
            for (Map.Entry<String, EmployeeData> entry : value.employees.entrySet()) {
                JSONObject employeeJson = new JSONObject();
                employeeJson.put("level", entry.getValue().level);
                employeeJson.put("baseProduction", entry.getValue().baseProduction);
                employeesJson.put(entry.getKey(), employeeJson);
            }
            json.put("employees", employeesJson);

            // Production stats
            json.put("totalClicks", value.totalClicks);
            json.put("totalTimeTowerUsages", value.totalTimeTowerUsages);

            // Found rabbits
            JSONArray foundRabbitsArray = new JSONArray();
            for (String rabbit : value.foundRabbits) {
                foundRabbitsArray.put(rabbit);
            }
            json.put("foundRabbits", foundRabbitsArray);

            // Shop stats
            json.put("totalChocolateSpent", value.totalChocolateSpent);

            return json.toString();
        }

        @Override
        public ChocolateFactoryData deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);

            Map<String, EmployeeData> employees = new HashMap<>();
            if (jsonObject.has("employees")) {
                JSONObject employeesJson = jsonObject.getJSONObject("employees");
                for (String key : employeesJson.keySet()) {
                    JSONObject employeeJson = employeesJson.getJSONObject(key);
                    employees.put(key, new EmployeeData(
                            employeeJson.getInt("level"),
                            employeeJson.getDouble("baseProduction")
                    ));
                }
            }

            Set<String> foundRabbits = new HashSet<>();
            if (jsonObject.has("foundRabbits")) {
                JSONArray foundRabbitsArray = jsonObject.getJSONArray("foundRabbits");
                for (int i = 0; i < foundRabbitsArray.length(); i++) {
                    foundRabbits.add(foundRabbitsArray.getString(i));
                }
            }

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
                    jsonObject.optLong("totalChocolateSpent", 0L)
            );
        }

        @Override
        public ChocolateFactoryData clone(ChocolateFactoryData value) {
            Map<String, EmployeeData> clonedEmployees = new HashMap<>();
            for (Map.Entry<String, EmployeeData> entry : value.employees.entrySet()) {
                clonedEmployees.put(entry.getKey(), new EmployeeData(
                        entry.getValue().level,
                        entry.getValue().baseProduction
                ));
            }

            return new ChocolateFactoryData(
                    value.chocolate,
                    value.chocolateAllTime,
                    value.lastUpdated,
                    value.partialChocolate,
                    value.timeTowerLevel,
                    value.timeTowerCharges,
                    value.timeTowerLastUsed,
                    value.timeTowerActiveUntil,
                    value.rabbitBarnLevel,
                    value.handBakedChocolateLevel,
                    value.rabbitShrineLevel,
                    value.coachJackrabbitLevel,
                    clonedEmployees,
                    value.totalClicks,
                    value.totalTimeTowerUsages,
                    new HashSet<>(value.foundRabbits),
                    value.totalChocolateSpent
            );
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
            return 1 + handBakedChocolateLevel;
        }

        /**
         * Gets the maximum number of rabbit slots based on Rabbit Barn level
         */
        public int getMaxRabbitSlots() {
            return 3 + rabbitBarnLevel;
        }

        /**
         * Gets the production multiplier from Rabbit Shrine
         * Base is 1.0, increases by 0.1 per level
         */
        public double getShrineMultiplier() {
            return 1.0 + (rabbitShrineLevel * 0.1);
        }

        /**
         * Gets the Time Tower multiplier when active
         * Base is 1.0, increases by 0.1 per level
         */
        public double getTimeTowerMultiplier() {
            if (System.currentTimeMillis() < timeTowerActiveUntil) {
                return 1.0 + (timeTowerLevel * 0.1);
            }
            return 1.0;
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
                timeTowerCharges--;
                timeTowerLastUsed = System.currentTimeMillis();
                timeTowerActiveUntil = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour
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
            return 1.0 + (coachJackrabbitLevel * 0.01);
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
            double secondsPassed = (now - lastUpdated) / 1000.0;

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
    }

    @AllArgsConstructor
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
