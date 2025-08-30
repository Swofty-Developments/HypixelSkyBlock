package net.swofty.type.generic.gui.inventory.item;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

public record GUIMaterial(Material material, @Nullable String texture) {
    public GUIMaterial(Material material) {
        this(material, null);
    }

    public GUIMaterial(@Nullable String texture) {
        this(Material.PLAYER_HEAD, texture);
    }

    public boolean hasTexture() {
        return texture != null && !texture.isEmpty();
    }
}
