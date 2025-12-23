package net.swofty.type.generic.entity.npc;

import lombok.Builder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NPCOption {
	public static final Map<HypixelPlayer, OptionData> options = new HashMap<>();

	public static void sendOption(HypixelPlayer player, String id, List<Option> message) {
		Component optionMessage = Component.empty();
		for (Option option : message) {
			optionMessage = optionMessage.append(Component.text("[" + option.name() + "]", Style.style().color(option.color()).decoration(TextDecoration.BOLD, true).build())
					.clickEvent(ClickEvent.runCommand("/selectnpcoption " + id + " " + option.key())));
			if (message.indexOf(option) != message.size() - 1) {
				optionMessage = optionMessage.appendSpace();
			}
		}
		player.sendMessage(optionMessage);
		options.put(player, new OptionData(id, message));
		MathUtility.delay(
				() -> options.remove(player),
				20 * 60 * 5 // 5 minutes
		);
	}

	public record OptionData(String npcId, List<Option> options) {
	}

	@Builder
	public record Option(
			String key,
			NamedTextColor color,
			String name,
			Consumer<HypixelPlayer> action) {
	}

}
