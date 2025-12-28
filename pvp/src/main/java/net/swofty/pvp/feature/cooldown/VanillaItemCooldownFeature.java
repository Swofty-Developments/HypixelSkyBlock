package net.swofty.pvp.feature.cooldown;

import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.SetCooldownPacket;
import net.minestom.server.tag.Tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Vanilla implementation of {@link ItemCooldownFeature}
 */
public class VanillaItemCooldownFeature implements ItemCooldownFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaItemCooldownFeature> DEFINED = new DefinedFeature<>(
			FeatureType.ITEM_COOLDOWN, configuration -> new VanillaItemCooldownFeature(),
			VanillaItemCooldownFeature::initPlayer
	);

	public static final Tag<Map<Material, Long>> COOLDOWN_END = Tag.Transient("cooldownEnd");

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
			Map<Material, Long> cooldown = player.getTag(COOLDOWN_END);
			if (cooldown == null || cooldown.isEmpty()) return;
			long time = System.currentTimeMillis();

			Iterator<Map.Entry<Material, Long>> iterator = cooldown.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Material, Long> entry = iterator.next();
				if (entry.getValue() <= time) {
					iterator.remove();
					sendCooldownPacket(player, entry.getKey(), 0);
				}
			}
		});

		node.addListener(PlayerUseItemEvent.class, event -> {
			if (hasCooldown(event.getPlayer(), event.getItemStack().material()))
				event.setCancelled(true);
		});
	}

	@Override
	public boolean hasCooldown(Player player, Material material) {
		Map<Material, Long> cooldown = player.getTag(COOLDOWN_END);
		return cooldown.containsKey(material) && cooldown.get(material) > System.currentTimeMillis();
	}

	@Override
	public void setCooldown(Player player, Material material, int ticks) {
		Map<Material, Long> cooldown = player.getTag(COOLDOWN_END);
		cooldown.put(material, System.currentTimeMillis() + (long) ticks * MinecraftServer.TICK_MS);
		sendCooldownPacket(player, material, ticks);
	}

	protected void sendCooldownPacket(Player player, Material material, int ticks) {
		player.getPlayerConnection().sendPacket(new SetCooldownPacket(material.key().asString(), ticks));
	}
}
