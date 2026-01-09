package net.swofty.type.generic.gui.v2.context;

import net.minestom.server.inventory.Inventory;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewNavigator;
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

    public ViewNavigator navigator() {
        return ViewNavigator.get(player);
    }

    public <S> ViewSession<S> push(View<S> view, S state) {
        return navigator().push(view, state);
    }

    public <S> ViewSession<S> push(View<S> view) {
        return navigator().push(view, null);
    }

    public boolean pop() {
        return navigator().pop();
    }

    public <S> ViewSession<S> replace(View<S> view, S state) {
        return navigator().replace(view, state);
    }

    public boolean viewStack() {
        return navigator().hasStack();
    }
}
