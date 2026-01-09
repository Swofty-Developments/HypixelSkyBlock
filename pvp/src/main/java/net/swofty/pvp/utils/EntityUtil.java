package net.swofty.pvp.utils;

import io.sentry.Sentry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;

import java.lang.reflect.Field;
import java.util.Objects;

public class EntityUtil {
	public static void spawnItemAtLocation(Entity entity, ItemStack itemStack, double up) {
		if (itemStack.isAir()) return;

		ItemEntity item = new ItemEntity(itemStack);
		item.setPickupDelay(10, TimeUnit.SERVER_TICK); // Default 0.5 seconds
		item.setInstance(Objects.requireNonNull(entity.getInstance()), entity.getPosition().add(0, up, 0));
	}

	public static Component getName(Entity entity) {
		HoverEvent<HoverEvent.ShowEntity> hoverEvent = HoverEvent.showEntity(entity.getEntityType().key(), entity.getUuid());
		var customName = entity.get(DataComponents.CUSTOM_NAME);
		if (customName != null) {
			return customName.hoverEvent(hoverEvent);
		} else if (entity instanceof Player) {
			return ((Player) entity).getName().hoverEvent(hoverEvent);
		} else {
			// Use entity type without underscores and starting with capital letter
			String name = entity.getEntityType().key().value().replace('_', ' ');
			name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			return Component.text(name).hoverEvent(hoverEvent);
		}
	}

	public static void setLastDamage(LivingEntity livingEntity, Damage lastDamage) {
		// Use reflection to set lastDamage field
		try {
			Field field = LivingEntity.class.getDeclaredField("lastDamage");
			field.setAccessible(true);
			field.set(livingEntity, lastDamage);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			Sentry.captureException(e);
		}
	}
}
