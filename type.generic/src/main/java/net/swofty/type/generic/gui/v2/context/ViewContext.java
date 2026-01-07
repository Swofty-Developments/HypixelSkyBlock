package net.swofty.type.generic.gui.v2.context;

import net.minestom.server.inventory.Inventory;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.user.HypixelPlayer;

public record ViewContext(
    HypixelPlayer player,
    Inventory inventory,
    ViewSession<?> session
) {
    @SuppressWarnings("unchecked")
    public <S> ViewSession<S> session(Class<S> stateType) {
        return (ViewSession<S>) session;
    }
}
