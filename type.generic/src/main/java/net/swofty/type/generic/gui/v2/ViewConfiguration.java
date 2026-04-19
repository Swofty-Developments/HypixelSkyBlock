package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;

import java.util.function.BiFunction;

@Getter
public class ViewConfiguration<S> {

    private final BiFunction<S, ViewContext, Component> titleFunction;
    private final InventoryType inventoryType;

    public ViewConfiguration(Component title, InventoryType type) {
        this.titleFunction = (_, _) -> title;
        this.inventoryType = type;
    }

    public ViewConfiguration(String title, InventoryType type) {
        this.titleFunction = (_, _) -> Component.text(title);
        this.inventoryType = type;
    }

    public ViewConfiguration(Title<S> title, InventoryType type) {
        this.titleFunction = title::getTitle;
        this.inventoryType = type;
    }

    public ViewConfiguration(StringTitle<S> title, InventoryType type) {
        this.titleFunction = (s, c) -> Component.text(title.getTitle(s, c));
        this.inventoryType = type;
    }

    public static <S> ViewConfiguration<S> withTitle(Title<S> title, InventoryType type) {
        return new ViewConfiguration<>(title, type);
    }

    public static <S> ViewConfiguration<S> withString(StringTitle<S> title, InventoryType type) {
        return new ViewConfiguration<>(title, type);
    }

    public static <S> ViewConfiguration<S> translatable(String titleKey, InventoryType type) {
        return new ViewConfiguration<>((Title<S>) (_, _) -> I18n.t(titleKey), type);
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
