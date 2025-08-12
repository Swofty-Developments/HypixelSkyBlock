package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.item.handlers.lore.LoreConfig;
import net.swofty.type.generic.item.handlers.lore.LoreRegistry;

import java.util.List;

@Getter
public class LoreUpdateComponent extends SkyBlockItemComponent {
    private final LoreConfig handler;
    private final boolean isAbsolute;

    public LoreUpdateComponent(String handlerId, boolean isAbsolute) {
        handler = LoreRegistry.getHandler(handlerId);
        this.isAbsolute = isAbsolute;
    }

    public LoreUpdateComponent(List<String> lore, boolean isAbsolute) {
        handler = new LoreConfig((item, player) -> lore, null);
        this.isAbsolute = isAbsolute;
    }

    public LoreUpdateComponent(LoreConfig handler, boolean isAbsolute) {
        this.handler = handler;
        this.isAbsolute = isAbsolute;
    }
}
