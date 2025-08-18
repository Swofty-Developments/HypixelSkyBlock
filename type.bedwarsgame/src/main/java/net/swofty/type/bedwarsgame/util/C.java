package net.swofty.type.bedwarsgame.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class C {

	public static Component noItalic(Component component) {
		return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
	}

	public static List<Component> noItalic(List<Component> components) {
		return components.stream()
				.map(C::noItalic)
				.toList();
	}

	public static List<Component> noItalic(Component... components) {
		return List.of(components).stream()
				.map(C::noItalic)
				.toList();
	}

}
