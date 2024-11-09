package net.swofty.types.generic.item;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyBlockItemComponent {
    private final List<SkyBlockItemComponent> inheritedComponents = new ArrayList<>();

    public SkyBlockItemComponent() {}

    protected void addInheritedComponent(SkyBlockItemComponent component) {
        inheritedComponents.add(component);
    }

    public List<SkyBlockItemComponent> getInheritedComponents() {
        return inheritedComponents;
    }
}
