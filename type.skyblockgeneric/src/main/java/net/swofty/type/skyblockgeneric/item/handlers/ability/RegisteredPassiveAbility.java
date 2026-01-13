package net.swofty.type.skyblockgeneric.item.handlers.ability;

import lombok.Getter;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AbilityComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
public class RegisteredPassiveAbility extends RegisteredAbility {

	private final List<Action<?>> passiveAction;

	public RegisteredPassiveAbility(String id, String name, BiFunction<SkyBlockPlayer, SkyBlockItem, String> description, List<Action<?>> action) {
		super(id, name, description, AbilityActivation.NEVER, 0, new NoAbilityCost(), ((_, _, _, _) -> {
            return false;
        }));
		this.passiveAction = action;
	}

	public static final class Action<E extends Event> {
		private final Consumer<E> action;
		private final Class<E> eventClass;
		private final EventNode<?> eventNode;
		private final Predicate<E> filter;

		public static <E extends Event> Predicate<E> createDefaultCondition(String abilityId) {
			return event -> {
				try {
					Method getPlayer = event.getClass().getMethod("getPlayer");
					Object playerObj = getPlayer.invoke(event);
					if (!(playerObj instanceof SkyBlockPlayer player)) {
						return false;
					}
					SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
					return item.hasComponent(AbilityComponent.class) &&
							item.getComponent(AbilityComponent.class).getAbilityIds().contains(abilityId);
				} catch (Exception e) {
					Logger.warn("Failed to evaluate default condition for Action", e);
					return false;
				}
			};
		}

		public Action(Class<E> eventClass, EventNodes eventNode, Consumer<E> action, Predicate<E> filter) {
			this.action = action;
			this.eventClass = eventClass;
			this.eventNode = eventNode.eventNode;
			this.filter = filter;
		}

		public void register() {
			@SuppressWarnings("unchecked")
			EventNode<E> castedEventNode = (EventNode<E>) eventNode;
			castedEventNode.addListener(eventClass, event -> {
				try {
					if (filter.test(event)) {
						action.accept(event);
					}
				} catch (Exception e) {
					Logger.error("Error executing passive ability event action for event " + eventClass.getSimpleName(), e);
				}
			});
		}
	}

}