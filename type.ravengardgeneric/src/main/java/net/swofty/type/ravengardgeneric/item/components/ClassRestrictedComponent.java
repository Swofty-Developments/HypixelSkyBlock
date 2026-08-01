package net.swofty.type.ravengardgeneric.item.components;

import lombok.Getter;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.item.RavengardItemComponent;

import java.util.Map;

/** Restricts an item to one class, so kits and drops can be filtered per class. */
@Getter
public class ClassRestrictedComponent implements RavengardItemComponent {
    private RavengardClass restrictedTo;

    @Override
    public String id() {
        return "CLASS_RESTRICTED";
    }

    @Override
    public void configure(Map<String, Object> config) {
        Object value = config.get("class");
        this.restrictedTo = value == null ? null : RavengardClass.fromKey(value.toString());
    }
}
