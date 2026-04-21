package net.swofty.type.generic.collectibles;

import java.util.*;

public abstract class CollectibleCatalog {

    private final Map<String, CollectibleDefinition> byId = new LinkedHashMap<>();
    private final Map<CollectibleCategory, List<CollectibleDefinition>> byCategory = new EnumMap<>(CollectibleCategory.class);
    private final Set<CollectibleCategory> knownCategories = EnumSet.noneOf(CollectibleCategory.class);

    protected void clear() {
        byId.clear();
        byCategory.clear();
        knownCategories.clear();
    }

    protected void registerCategory(CollectibleCategory category) {
        knownCategories.add(category);
        byCategory.computeIfAbsent(category, ignored -> new ArrayList<>());
    }

    protected void register(CollectibleDefinition definition) {
        if (byId.containsKey(definition.id())) {
            throw new IllegalArgumentException("Duplicate collectible id: " + definition.id());
        }

        registerCategory(definition.category());
        byId.put(definition.id(), definition);
        byCategory.get(definition.category()).add(definition);
    }

    protected void sortAll() {
        Comparator<CollectibleDefinition> comparator = Comparator
            .comparingInt(CollectibleDefinition::sortIndex)
            .thenComparing(CollectibleDefinition::id);

        for (List<CollectibleDefinition> definitions : byCategory.values()) {
            definitions.sort(comparator);
        }
    }

    public Optional<CollectibleDefinition> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public List<CollectibleDefinition> getByCategory(CollectibleCategory category) {
        return List.copyOf(byCategory.getOrDefault(category, List.of()));
    }

    public Collection<CollectibleDefinition> getAll() {
        return List.copyOf(byId.values());
    }

    public Set<CollectibleCategory> getKnownCategories() {
        return EnumSet.copyOf(knownCategories);
    }
}
