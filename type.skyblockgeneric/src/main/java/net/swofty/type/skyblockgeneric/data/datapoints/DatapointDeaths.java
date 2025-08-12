package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.EntityDamage;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.SkyBlockDatapoint;
import net.swofty.type.generic.entity.mob.BestiaryMob;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointDeaths extends SkyBlockDatapoint<DatapointDeaths.PlayerDeaths> {

    public DatapointDeaths(String key, PlayerDeaths value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(PlayerDeaths value) {
                JSONObject jsonObject = new JSONObject(value.deaths);
                return jsonObject.toString();
            }

            @Override
            public PlayerDeaths deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                Map<String, Integer> deaths = new HashMap<>();

                for (String key : jsonObject.keySet()) {
                    deaths.put(key, jsonObject.getInt(key));
                }

                return new PlayerDeaths(deaths);
            }

            @Override
            public PlayerDeaths clone(PlayerDeaths value) {
                return new PlayerDeaths(value.deaths == null ? new HashMap<>() : new HashMap<>(value.deaths));
            }
        });
    }

    public DatapointDeaths(String key) {
        this(key, new PlayerDeaths());
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerDeaths {
        private Map<String, Integer> deaths = new HashMap<>();

        public PlayerDeaths(Map<String, Integer> mobs) {
            this.deaths = mobs;
        }

        private String getCauseAsString(Damage damage) {
            return switch (damage.getType().name()) {
                case "minecraft:mob_attack" -> ((BestiaryMob) ((EntityDamage) damage).getSource()).getMobID();
                default -> damage.getType().name();
            };
        }

        public void set(Damage deathCause, int amount) {
            deaths.put(getCauseAsString(deathCause), amount);
        }

        public void increase(Damage deathCause, Integer amount) {
            set(deathCause, getAmount(getCauseAsString(deathCause)) + amount);
        }

        public void decrease(Damage deathCause, Integer amount) {
            set(deathCause, getAmount(getCauseAsString(deathCause)) - amount);
        }

        public Integer getAmount(String cause) {
            return deaths.getOrDefault(cause, 0);
        }
    }
}
