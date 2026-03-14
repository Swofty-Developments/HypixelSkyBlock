package net.swofty.type.ravengardgeneric.texturepack.widgets;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackRenderer;

public record HudMapWidgetContext(
        HypixelPlayer player,
        Instance instance,
        Pos position,
        String shortServerName,
        String date,
        TexturePackRenderer renderer
) {
}
