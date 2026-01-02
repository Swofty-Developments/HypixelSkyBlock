package net.swofty.type.skyblockgeneric.item.handlers.ability;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.block.BlockFace;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.function.BiFunction;

@Getter
public class RegisteredPassiveAbility extends RegisteredAbility {

	private final Action<?> passiveAction;

	public RegisteredPassiveAbility(String id, String name, BiFunction<SkyBlockPlayer, SkyBlockItem, String> description, Action<?> action) {
		super(id, name, description, AbilityActivation.NEVER, 0, new NoAbilityCost(), ((_, _, _, _) -> {}));
		this.passiveAction = action;
	}

	@FunctionalInterface
	public interface HypixelEventAction<E> {
		void execute(E event) throws Exception;
	}

	public static final class Action<E extends Event> implements HypixelEventClass {
		private final HypixelEventAction<E> action;
		private final Class<E> eventClass;

		public Action(Class<E> eventClass, HypixelEventAction<E> action) {
			this.action = action;
			this.eventClass = eventClass;
		}

		@SneakyThrows
		@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
		public void run(Event event) {
			if (eventClass.isInstance(event)) {
				action.execute(eventClass.cast(event));
			}
		}
	}

}