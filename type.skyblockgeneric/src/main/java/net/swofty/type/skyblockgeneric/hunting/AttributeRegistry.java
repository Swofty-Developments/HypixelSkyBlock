package net.swofty.type.skyblockgeneric.hunting;

import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.region.RegionType;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static net.swofty.type.skyblockgeneric.hunting.AttributeDefinition.*;

public final class AttributeRegistry {
    private static final Map<AttributeId, AttributeDefinition> BY_ID = load();
    private static final List<AttributeDefinition> SORTED = BY_ID.values().stream()
            .sorted(Comparator.comparingInt((AttributeDefinition value) -> value.rarity().ordinal())
                    .thenComparingInt(AttributeDefinition::numericId)).toList();

    private AttributeRegistry() {
    }

    public static Collection<AttributeDefinition> values() {
        return SORTED;
    }

    public static AttributeDefinition get(String id) {
        if (id == null) return null;
        try {
            return BY_ID.get(AttributeId.parse(id));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static AttributeDefinition get(AttributeId id) {
        return BY_ID.get(id);
    }

    public static Optional<AttributeDefinition> findByShard(String shard) {
        if (shard == null) return Optional.empty();
        return SORTED.stream().filter(value -> value.shard().equalsIgnoreCase(shard) || value.shardName().equalsIgnoreCase(shard)).findFirst();
    }

    public static List<AttributeDefinition> search(String query) {
        String normalized = query == null ? "" : query.toLowerCase(Locale.ROOT);
        return SORTED.stream().filter(value -> String.join(" ", value.id().toString(), value.shard(), value.name(),
                value.family().name(), value.category().name(), value.skill().name()).toLowerCase(Locale.ROOT).contains(normalized)).toList();
    }

    @SuppressWarnings("unchecked")
    private static Map<AttributeId, AttributeDefinition> load() {
        Path path = Path.of("./configuration/skyblock/shards.yml");
        try (InputStream stream = Files.newInputStream(path)) {
            Map<String, Object> root = new Yaml().load(stream);
            if (root == null) {
                throw new IllegalStateException("Missing ./configuration/skyblock/shards.yml");
            }
            Map<AttributeId, AttributeDefinition> result = new LinkedHashMap<>();
            for (Map<String, Object> value : (List<Map<String, Object>>) root.getOrDefault("shards", List.of())) {
                AttributeId id = AttributeId.parse(text(value, "id"));
                List<AttributeEffect> effects = new ArrayList<>();
                for (Map<String, Object> effect : (List<Map<String, Object>>) value.getOrDefault("effects", List.of())) {
                    effects.add(new AttributeEffect(enumValue(EffectType.class, effect.get("type")),
                            enumValue(ItemStatistic.class, effect.get("statistic")), number(effect, "minimum"),
                            number(effect, "maximum"), enumValue(Condition.class, effect.getOrDefault("condition", "ALWAYS")),
                            enumValue(MobType.class, effect.get("mob_type")), Objects.toString(effect.get("key"), "")));
                }
                List<AttributeSource> sources = new ArrayList<>();
                for (Map<String, Object> source : (List<Map<String, Object>>) value.getOrDefault("sources", List.of())) {
                    sources.add(new AttributeSource(enumValue(SourceType.class, source.get("type")),
                            strings(source.get("mobs")), enums(RegionType.class, source.get("locations")),
                            enumValue(ServerType.class, source.get("island"))));
                }
                AttributeDefinition definition = new AttributeDefinition(id, text(value, "shard"), text(value, "name"),
                        enumValue(AttributeRarity.class, value.get("rarity")), enumValue(AttributeCategory.class, value.get("category")),
                        enumValue(AttributeFamily.class, value.get("family")), enumValue(AttributeSkill.class, value.get("skill")),
                        effects, sources, text(value, "description"), strings(value.get("huntInfo")));
                if (id.rarity() != definition.rarity())
                    throw new IllegalStateException("Rarity does not match id " + id);
                if (result.put(id, definition) != null) throw new IllegalStateException("Duplicate attribute id " + id);
            }
            return Map.copyOf(result);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read attributes file: " + path.toAbsolutePath(),
                    e
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Could not load typed attributes", exception);
        }
    }

    private static String text(Map<String, Object> map, String key) {
        return Objects.toString(map.get(key), "");
    }

    private static double number(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof Number n ? n.doubleValue() : 0;
    }

    private static List<String> strings(Object value) {
        return value instanceof List<?> list ? list.stream().map(Object::toString).toList() : List.of();
    }

    private static <E extends Enum<E>> List<E> enums(Class<E> type, Object value) {
        return value instanceof List<?> list ? list.stream().map(item -> enumValue(type, item)).filter(Objects::nonNull).toList() : List.of();
    }

    private static <E extends Enum<E>> E enumValue(Class<E> type, Object value) {
        if (value == null || value.toString().isBlank()) return null;
        try {
            return Enum.valueOf(type, value.toString().toUpperCase(Locale.ROOT).replace(' ', '_').replace('-', '_'));
        } catch (IllegalArgumentException ignored) {
            return type == AttributeFamily.class ? type.cast(AttributeFamily.OTHER) : null;
        }
    }
}
