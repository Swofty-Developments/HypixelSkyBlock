package net.swofty.type.generic.gui.v2;

public interface StatefulView<S> extends View<S> {
    S initialState();
}
