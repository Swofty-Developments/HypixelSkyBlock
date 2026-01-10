package net.swofty.type.generic.gui.v2.context;

import net.minestom.server.inventory.Inventory;
import net.swofty.type.generic.gui.v2.*;
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
        return navigator().push(view);
    }

    public boolean pop() {
        return navigator().pop();
    }

    public <S> ViewSession<S> replace(View<S> view, S state) {
        return navigator().replace(view, state);
    }

    public <S> ViewSession<S> replace(View<S> view) {
        return navigator().replace(view);
    }

    public <S> ViewSession<S> pushShared(View<S> view, String contextId, S initialState) {
        return navigator().pushShared(view, contextId, initialState);
    }

    public <S> ViewSession<S> joinShared(View<S> view, String contextId) {
        return navigator().joinShared(view, contextId);
    }

    public boolean hasBackStack() {
        return navigator().hasStack();
    }

    public int stackDepth() {
        return navigator().depth();
    }

    public void backOrClose() {
        if (!pop()) {
            player.closeInventory();
        }
    }
}
