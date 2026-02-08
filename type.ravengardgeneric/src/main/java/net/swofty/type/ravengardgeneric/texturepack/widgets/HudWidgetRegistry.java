package net.swofty.type.ravengardgeneric.texturepack.widgets;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class HudWidgetRegistry {
    private final Map<String, HudMapWidget> widgets = new LinkedHashMap<>();

    public synchronized void register(HudMapWidget widget) {
        widgets.put(widget.id(), widget);
    }

    public synchronized void register(HudWidgetType type, HudMapWidget widget) {
        widgets.put(type.id(), widget);
    }

    public synchronized HudMapWidget unregister(String widgetId) {
        return widgets.remove(widgetId);
    }

    public synchronized HudMapWidget unregister(HudWidgetType type) {
        return widgets.remove(type.id());
    }

    public synchronized HudMapWidget get(String widgetId) {
        return widgets.get(widgetId);
    }

    public synchronized HudMapWidget get(HudWidgetType type) {
        return widgets.get(type.id());
    }

    public synchronized Collection<HudMapWidget> all() {
        return Collections.unmodifiableCollection(widgets.values());
    }
}
