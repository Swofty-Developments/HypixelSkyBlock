package net.swofty.type.bedwarsgame.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;
import java.util.stream.Stream;

public class ComponentManipulator {

	public static Component noItalic(Component component) {
		return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
	}

	public static List<Component> noItalic(List<Component> components) {
		return components.stream()
				.map(ComponentManipulator::noItalic)
				.toList();
	}

	public static List<Component> noItalic(Component... components) {
		return Stream.of(components)
				.map(ComponentManipulator::noItalic)
				.toList();
	}

}
