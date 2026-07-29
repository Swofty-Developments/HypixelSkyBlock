package net.swofty.pvp.feature.cooldown;

import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.UseCooldown;
import net.minestom.server.network.packet.server.play.SetCooldownPacket;
import net.minestom.server.tag.Tag;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Vanilla implementation of {@link ItemCooldownFeature}
 */
public class VanillaItemCooldownFeature implements ItemCooldownFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaItemCooldownFeature> DEFINED = new DefinedFeature<>(
		FeatureType.ITEM_COOLDOWN, _ -> new VanillaItemCooldownFeature(),
			VanillaItemCooldownFeature::initPlayer
	);

	public static final Tag<Map<String, Long>> COOLDOWN_END = Tag.Transient("cooldownEnd");

	private static void initPlayer(Player player, boolean firstInit) {
		player.setTag(COOLDOWN_END, new HashMap<>());
	}

	@Override
	public int getPriority() {
		// Needs to stop every item usage event
		return -5;
	}

	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getPlayer();
			Map<String, Long> cooldown = player.getTag(COOLDOWN_END);
			if (cooldown == null || cooldown.isEmpty()) return;
			long time = System.currentTimeMillis();

			Iterator<Map.Entry<String, Long>> iterator = cooldown.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, Long> entry = iterator.next();
				if (entry.getValue() <= time) {
					iterator.remove();
					sendCooldownPacket(player, entry.getKey(), 0);
				}
			}
		});

		node.addListener(PlayerUseItemEvent.class, event -> {
			ItemStack stack = event.getItemStack();
			UseCooldown useCooldown = stack.get(DataComponents.USE_COOLDOWN);

			String cooldownGroup = useCooldown != null && useCooldown.cooldownGroup() != null
				? useCooldown.cooldownGroup()
				: stack.material().name();

			if (hasCooldown(event.getPlayer(), cooldownGroup))
				event.setCancelled(true);
		});
	}

	@Override
	public boolean hasCooldown(Player player, String cooldownGroup) {
		Map<String, Long> cooldown = player.getTag(COOLDOWN_END);
		return cooldown.containsKey(cooldownGroup) && cooldown.get(cooldownGroup) > System.currentTimeMillis();
	}

	@Override
	public boolean hasCooldown(Player player, ItemStack itemStack) {
		UseCooldown useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
		String cooldownGroup = useCooldown != null && useCooldown.cooldownGroup() != null
			? useCooldown.cooldownGroup()
			: itemStack.material().name();
		return hasCooldown(player, cooldownGroup);
	}

	@Override
	public void setCooldown(Player player, String cooldownGroup, int ticks) {
		Map<String, Long> cooldown = player.getTag(COOLDOWN_END);
		cooldown.put(cooldownGroup, System.currentTimeMillis() + (long) ticks * MinecraftServer.TICK_MS);
		sendCooldownPacket(player, cooldownGroup, ticks);
	}

	@Override
	public void setCooldown(Player player, ItemStack itemStack, int ticks) {
		UseCooldown useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
		String cooldownGroup = useCooldown != null && useCooldown.cooldownGroup() != null
			? useCooldown.cooldownGroup()
			: itemStack.material().name();
		setCooldown(player, cooldownGroup, ticks);
	}

	@Override
	public void setCooldown(Player player, ItemStack itemStack) {
		UseCooldown useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
		int ticks = useCooldown != null ? (int) useCooldown.seconds() * 20 : 0;
		setCooldown(player, itemStack, ticks);
	}

	protected void sendCooldownPacket(Player player, String cooldownGroup, int ticks) {
		player.getPlayerConnection().sendPacket(new SetCooldownPacket(cooldownGroup, ticks));
	}
}
