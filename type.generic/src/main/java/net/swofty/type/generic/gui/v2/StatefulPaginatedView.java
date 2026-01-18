package net.swofty.type.generic.gui.v2;

public abstract class StatefulPaginatedView<T, S extends PaginatedView.PaginatedState<T>> extends PaginatedView<T, S> {
    public abstract S initialState();
}
