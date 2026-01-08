package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public record ViewComponent<S>(
		int slot,
		BiFunction<S, ViewContext, ItemStack.Builder> render,
		BiConsumer<ClickContext<S>, ViewContext> onClick,
		SlotBehavior behavior,
		SlotChangeHandler<S> changeHandler
) {
	public ViewComponent(int slot, BiFunction<S, ViewContext, ItemStack.Builder> render,
						 BiConsumer<ClickContext<S>, ViewContext> onClick) {
		this(slot, render, onClick, SlotBehavior.UI, null);
	}

	public static <S> ViewComponent<S> staticItem(int slot, ItemStack.Builder item) {
		return new ViewComponent<>(slot, (_, _) -> item, (_, _) -> {
		}, SlotBehavior.UI, null);
	}

	public static <S> ViewComponent<S> clickable(
			int slot,
			BiFunction<S, ViewContext, ItemStack.Builder> render,
			BiConsumer<ClickContext<S>, ViewContext> onClick
	) {
		return new ViewComponent<>(slot, render, onClick, SlotBehavior.UI, null);
	}

	public static <S> ViewComponent<S> editable(
			int slot,
			BiFunction<S, ViewContext, ItemStack.Builder> initialRender,
			SlotChangeHandler<S> changeHandler
	) {
		return new ViewComponent<>(slot, initialRender, (_, _) -> {
		}, SlotBehavior.EDITABLE, changeHandler);
	}

	public static <S> ViewComponent<S> editable(int slot, ItemStack.Builder initialItem, SlotChangeHandler<S> changeHandler) {
		return new ViewComponent<>(slot, (_, _) -> initialItem, (_, _) -> {
		}, SlotBehavior.EDITABLE, changeHandler);
	}

	public static <S> ViewComponent<S> editable(int slot, SlotChangeHandler<S> changeHandler) {
		return editable(slot, ItemStack.AIR.builder(), changeHandler);
	}
}
