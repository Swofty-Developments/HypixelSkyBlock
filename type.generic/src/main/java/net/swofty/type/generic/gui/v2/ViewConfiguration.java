package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.function.BiFunction;

@Getter
public class ViewConfiguration<S> {

    private final BiFunction<S, ViewContext, Component> titleFunction;
    private final InventoryType inventoryType;

    public ViewConfiguration(Component title, InventoryType type) {
        this.titleFunction = (v, c) -> title;
        this.inventoryType = type;
    }

    public ViewConfiguration(String title, InventoryType type) {
        this.titleFunction = (v, c) -> Component.text(title);
        this.inventoryType = type;
    }

    public ViewConfiguration(Title<S> title, InventoryType type) {
        this.titleFunction = title::getTitle;
        this.inventoryType = type;
    }

    public ViewConfiguration(StringTitle<S> title, InventoryType type) {
        this.titleFunction = (v, c) -> Component.text(title.getTitle(v, c));
        this.inventoryType = type;
    }

    public static <S> ViewConfiguration<S> withTitle(Title<S> title, InventoryType type) {
        return new ViewConfiguration<>(title, type);
    }

    public static <S> ViewConfiguration<S> withString(StringTitle<S> title, InventoryType type) {
        return new ViewConfiguration<>(title, type);
    }

    @FunctionalInterface
    public interface Title<S> {
        Component getTitle(S state, ViewContext ctx);
    }

    @FunctionalInterface
    public interface StringTitle<S> {
        String getTitle(S state, ViewContext ctx);
    }
}
