package net.swofty.types.generic.item;


import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigurableSkyBlockItem {
    private final static Map<String, ConfigurableSkyBlockItem> CACHED_ITEMS = new HashMap<>();

    @Getter
    private final String id;
    @Getter
    private final Material material;
    @Getter
    private final List<String> lore;
    @Getter
    private final ItemStatistics defaultStatistics;

    private final Map<Class<? extends SkyBlockItemComponent>, SkyBlockItemComponent> components = new HashMap<>();
    private final Map<Class<? extends SkyBlockItemComponent>, String> componentSources = new HashMap<>();
    private final List<Class<? extends SkyBlockItemComponent>> explicitComponents = new ArrayList<>();

    public ConfigurableSkyBlockItem(String id, Material material, List<String> lore,
                              Map<String, Double> statistics) {
        this.id = id;
        this.material = material;
        this.lore = lore;

        ItemStatistics.Builder builder = ItemStatistics.builder();
        statistics.forEach((stat, value) -> {
            builder.withBase(ItemStatistic.valueOf(stat.toUpperCase()), value);
        });
        this.defaultStatistics = builder.build();
    }

    public void addComponent(SkyBlockItemComponent component, boolean isExplicit) {
        String source = isExplicit ? "Explicit declaration" :
                "Inherited from " + component.getClass().getSimpleName();

        // If this is an explicit component, add it to our tracking list
        if (isExplicit) {
            explicitComponents.add(component.getClass());
        }

        // Check for conflicts with existing components
        addComponentWithConflictCheck(component.getClass(), component, source);

        // Add component to parent classes
        Class<?> parentClass = component.getClass().getSuperclass();
        while (parentClass != null && SkyBlockItemComponent.class.isAssignableFrom(parentClass)) {
            addComponentWithConflictCheck((Class<? extends SkyBlockItemComponent>) parentClass, component, source);
            parentClass = parentClass.getSuperclass();
        }

        // Only process inherited components if this isn't an explicit declaration that's
        // overriding an inherited component
        if (!isExplicit || !isInheritedComponentOverridden(component.getClass())) {
            // Add all inherited components
            for (SkyBlockItemComponent inheritedComponent : component.getInheritedComponents()) {
                // Skip if this component type was explicitly declared
                if (!explicitComponents.contains(inheritedComponent.getClass())) {
                    addComponent(inheritedComponent, false);
                }
            }
        }
    }

    private boolean isInheritedComponentOverridden(Class<? extends SkyBlockItemComponent> componentClass) {
        return explicitComponents.stream()
                .anyMatch(explicitClass -> {
                    if (explicitClass == componentClass) return false; // Same class isn't an override
                    return componentClass.isAssignableFrom(explicitClass); // Check if it's a parent class
                });
    }

    private void addComponentWithConflictCheck(Class<? extends SkyBlockItemComponent> componentClass,
                                               SkyBlockItemComponent component,
                                               String source) {
        if (components.containsKey(componentClass)) {
            String existingSource = componentSources.get(componentClass);

            // If the existing component was explicitly declared and this is an inherited one,
            // silently ignore the inherited one
            if (explicitComponents.contains(componentClass) && source.startsWith("Inherited")) {
                return;
            }

            // If this is an explicit declaration and the existing one was inherited,
            // override it
            if (source.equals("Explicit declaration") && existingSource.startsWith("Inherited")) {
                components.put(componentClass, component);
                componentSources.put(componentClass, source);
                return;
            }

            // If both are explicit declarations or both are inherited, that's an error
            throw new ComponentConflictException(String.format(
                    "Component conflict for %s in item %s:%n" +
                            "Existing component source: %s%n" +
                            "New component source: %s",
                    componentClass.getSimpleName(),
                    id,
                    existingSource,
                    source
            ));
        }

        components.put(componentClass, component);
        componentSources.put(componentClass, source);
    }

    public <T extends SkyBlockItemComponent> boolean hasComponent(Class<T> componentClass) {
        return components.containsKey(componentClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends SkyBlockItemComponent> T getComponent(Class<T> componentClass) {
        T component = (T) components.get(componentClass);
        if (component == null) {
            throw new ComponentNotFoundException(String.format(
                    "Component %s not found in item %s",
                    componentClass.getSimpleName(),
                    id
            ));
        }
        return component;
    }

    public void register() {
        CACHED_ITEMS.put(id, this);
    }

    public static class ComponentConflictException extends RuntimeException {
        public ComponentConflictException(String message) {
            super(message);
        }
    }

    public static class ComponentNotFoundException extends RuntimeException {
        public ComponentNotFoundException(String message) {
            super(message);
        }
    }

    public static @Nullable ConfigurableSkyBlockItem getFromID(String id) {
        if (CACHED_ITEMS.containsKey(id)) {
            return CACHED_ITEMS.get(id);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ConfigurableSkyBlockItem{" +
                "id='" + id + '\'' +
                ", material=" + material +
                ", components=" + components.keySet().stream().map(Class::getSimpleName).reduce((s, s2) -> s + ", " + s2).orElse("null") +
                '}';
    }
}
