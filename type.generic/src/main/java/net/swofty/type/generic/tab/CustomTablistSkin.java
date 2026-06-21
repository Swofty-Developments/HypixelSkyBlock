package net.swofty.type.generic.tab;

import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

public class CustomTablistSkin implements TablistSkin {
    private final String texture;
    private final String signature;

    public CustomTablistSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public CustomTablistSkin(@NotNull PlayerSkin skin) {
        this.texture = skin.textures();
        this.signature = skin.signature();
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public String getSignature() {
        return signature;
    }
}
