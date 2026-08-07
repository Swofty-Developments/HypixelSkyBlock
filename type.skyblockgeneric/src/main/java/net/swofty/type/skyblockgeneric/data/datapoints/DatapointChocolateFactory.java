package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.rabbits.ChocolateRabbit;
import net.swofty.type.skyblockgeneric.rabbits.HoppityEggType;
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
            json.put("timeTowerLevel", value.getTimeTowerLevel());
            json.put("timeTowerCharges", value.getTimeTowerCharges());
            json.put("timeTowerLastUsed", value.getTimeTowerLastUsed());
            json.put("timeTowerActiveUntil", value.getTimeTowerActiveUntil());
            json.put("rabbitBarnLevel", value.getRabbitBarnLevel());
            json.put("handBakedChocolateLevel", value.getHandBakedChocolateLevel());
            json.put("rabbitShrineLevel", value.getRabbitShrineLevel());
            json.put("coachJackrabbitLevel", value.getCoachJackrabbitLevel());
            json.put("employees", serializeEmployees(value.getEmployees()));
            json.put("totalClicks", value.getTotalClicks());
            json.put("totalTimeTowerUsages", value.getTotalTimeTowerUsages());
            json.put("foundRabbits", serializeEnumSet(value.getFoundRabbits()));
            json.put("totalChocolateSpent", value.getTotalChocolateSpent());
            json.put("claimedEggs", serializeEnumSet(value.getClaimedEggs()));
            json.put("totalEggsFound", value.getTotalEggsFound());
            return json.toString();
        }

        @Override
        public ChocolateFactoryData deserialize(String json) {
            if (json == null || json.isEmpty()) {
                return new ChocolateFactoryData();
            }

            JSONObject jsonObject = new JSONObject(json);
            Map<EmployeeType, EmployeeData> employees = deserializeEmployees(jsonObject.optJSONObject("employees"));
            Set<ChocolateRabbit> foundRabbits = deserializeEnumSet(jsonObject.optJSONArray("foundRabbits"), ChocolateRabbit.class);
            Set<HoppityEggType> claimedEggs = deserializeEnumSet(jsonObject.optJSONArray("claimedEggs"), HoppityEggType.class);

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
            Map<EmployeeType, EmployeeData> clonedEmployees = cloneEmployees(value.getEmployees());

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

        private JSONObject serializeEmployees(Map<EmployeeType, EmployeeData> employees) {
            JSONObject employeesJson = new JSONObject();
            for (Map.Entry<EmployeeType, EmployeeData> entry : employees.entrySet()) {
                JSONObject employeeJson = new JSONObject();
                EmployeeData employee = entry.getValue();
                employeeJson.put("level", employee.getLevel());
                employeeJson.put("baseProduction", employee.getBaseProduction());
                employeesJson.put(entry.getKey().name(), employeeJson);
            }
            return employeesJson;
        }

        private Map<EmployeeType, EmployeeData> deserializeEmployees(JSONObject employeesJson) {
            Map<EmployeeType, EmployeeData> employees = new HashMap<>();
            if (employeesJson == null) {
                return employees;
            }

            for (String key : employeesJson.keySet()) {
                JSONObject employeeJson = employeesJson.getJSONObject(key);
                EmployeeType.fromName(key).ifPresent(type -> employees.put(type, new EmployeeData(
                        employeeJson.optInt("level", 0),
                        employeeJson.optDouble("baseProduction", type.getBaseProductionPerLevel())
                )));
            }
            return employees;
        }

        private JSONArray serializeEnumSet(Set<? extends Enum<?>> values) {
            JSONArray array = new JSONArray();
            for (Enum<?> value : values) {
                array.put(value.name());
            }
            return array;
        }

        private <E extends Enum<E>> Set<E> deserializeEnumSet(JSONArray array, Class<E> type) {
            Set<E> values = new HashSet<>();
            if (array == null) {
                return values;
            }

            for (int i = 0; i < array.length(); i++) {
                try {
                    values.add(Enum.valueOf(type, array.getString(i)));
                } catch (IllegalArgumentException ignored) {
                    // Ignore values removed or renamed in a later version.
                }
            }
            return values;
        }

        private Map<EmployeeType, EmployeeData> cloneEmployees(Map<EmployeeType, EmployeeData> employees) {
            Map<EmployeeType, EmployeeData> clonedEmployees = new HashMap<>();
            for (Map.Entry<EmployeeType, EmployeeData> entry : employees.entrySet()) {
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
        private double partialChocolate; // accumulates fractional chocolate production
        private int timeTowerLevel;
        private int timeTowerCharges;
        private long timeTowerLastUsed;
        private long timeTowerActiveUntil;
        private int rabbitBarnLevel;
        private int handBakedChocolateLevel;
        private int rabbitShrineLevel;
        private int coachJackrabbitLevel;
        private Map<EmployeeType, EmployeeData> employees = new HashMap<>();
        private long totalClicks;
        private int totalTimeTowerUsages;
        private Set<ChocolateRabbit> foundRabbits = new HashSet<>();
        private long totalChocolateSpent;
        private Set<HoppityEggType> claimedEggs = new HashSet<>();
        private int totalEggsFound;

        public void addChocolate(long amount) {
            this.chocolate += amount;
            if (amount > 0) {
                this.chocolateAllTime += amount;
            }
        }

        public boolean removeChocolate(long amount) {
            if (this.chocolate >= amount) {
                this.chocolate -= amount;
                return true;
            }
            return false;
        }

        public int getClickPower() {
            return BASE_CLICK_POWER + handBakedChocolateLevel;
        }

        public int getMaxRabbitSlots() {
            return BASE_RABBIT_SLOTS + rabbitBarnLevel;
        }

        public double getShrineMultiplier() {
            return BASE_MULTIPLIER + (rabbitShrineLevel * SHRINE_MULTIPLIER_PER_LEVEL);
        }

        public double getTimeTowerMultiplier() {
            if (System.currentTimeMillis() < timeTowerActiveUntil) {
                return BASE_MULTIPLIER + (timeTowerLevel * TIME_TOWER_MULTIPLIER_PER_LEVEL);
            }
            return BASE_MULTIPLIER;
        }

        public boolean isTimeTowerActive() {
            return System.currentTimeMillis() < timeTowerActiveUntil;
        }

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

        public double getCoachMultiplier() {
            return BASE_MULTIPLIER + (coachJackrabbitLevel * COACH_MULTIPLIER_PER_LEVEL);
        }

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

        public void click() {
            addChocolate(getClickPower());
            totalClicks++;
        }

        public void setEmployee(EmployeeType type, int level) {
            employees.put(type, new EmployeeData(level, type.getBaseProductionPerLevel()));
        }

        public int getEmployeeCount() {
            return employees.size();
        }

        public int getPrestigeLevel() {
            if (chocolateAllTime >= 1_000_000_000_000L) return 6; // 1T
            if (chocolateAllTime >= 10_000_000_000L) return 5;    // 10B
            if (chocolateAllTime >= 1_000_000_000L) return 4;     // 1B
            if (chocolateAllTime >= 100_000_000L) return 3;       // 100M
            if (chocolateAllTime >= 10_000_000L) return 2;        // 10M
            if (chocolateAllTime >= 1_000_000L) return 1;         // 1M
            return 0;
        }

        public void addFoundRabbit(ChocolateRabbit rabbit) {
            foundRabbits.add(rabbit);
        }

        public boolean hasFoundRabbit(ChocolateRabbit rabbit) {
            return foundRabbits.contains(rabbit);
        }

        public int getFoundRabbitCount() {
            return foundRabbits.size();
        }

        public void addChocolateSpent(long amount) {
            this.totalChocolateSpent += amount;
        }

        public void claimEgg(HoppityEggType egg) {
            claimedEggs.add(egg);
            totalEggsFound++;
        }

        public boolean hasClaimedEgg(HoppityEggType egg) {
            return claimedEggs.contains(egg);
        }

        public void resetClaimedEggs() {
            claimedEggs.clear();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum EmployeeType {
        RABBIT_BRO(1, "Rabbit Bro", 1.0),
        RABBIT_COUSIN(2, "Rabbit Cousin", 2.0),
        RABBIT_SIS(3, "Rabbit Sis", 3.0),
        RABBIT_DADDY(4, "Rabbit Daddy", 4.0),
        RABBIT_GRANNY(5, "Rabbit Granny", 5.0),
        RABBIT_UNCLE(6, "Rabbit Uncle", 6.0),
        RABBIT_DOG(7, "Rabbit Dog", 7.0);

        private final int index;
        private final String name;
        private final double baseProductionPerLevel;

        public static java.util.Optional<EmployeeType> fromName(String name) {
            return java.util.Arrays.stream(values())
                    .filter(type -> type.name().equals(name) || type.name.equals(name))
                    .findFirst();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    // TODO: record
    public static class EmployeeData {
        private int level;
        private double baseProduction;

        public double getProductionPerSecond() {
            return baseProduction * level;
        }
    }
}
