package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.lore.LoreConfig;
import net.swofty.types.generic.item.handlers.lore.LoreRegistry;

import java.util.List;

@Getter
public class LoreUpdateComponent extends SkyBlockItemComponent {
    private final LoreConfig handler;
    private final boolean isAbsolute;

    public LoreUpdateComponent(String handlerId, boolean isAbsolute) {
        handler = LoreRegistry.getHandler(handlerId);
        this.isAbsolute = isAbsolute;
    }

    public LoreUpdateComponent(List<String> lore) {
        handler = new LoreConfig((item, player) -> lore, null);
        this.isAbsolute = false;
    }

    public LoreUpdateComponent(LoreConfig handler) {
        this.handler = handler;
        this.isAbsolute = true;
    }
}
