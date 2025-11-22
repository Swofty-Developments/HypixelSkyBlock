package net.swofty.pvp.damage.combat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.EndCombatEventPacket;
import net.minestom.server.network.packet.server.play.EnterCombatEventPacket;
import net.swofty.pvp.damage.DamageTypeInfo;
import net.swofty.pvp.feature.fall.FallFeature;
import net.swofty.pvp.feature.state.PlayerStateFeature;
import net.swofty.pvp.utils.EntityUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CombatManager {
	private static final Component BAD_RESPAWN_POINT_MESSAGE = Component.text("[")
			.append(Component.translatable("death.attack.badRespawnPoint.link")
					.clickEvent(ClickEvent.openUrl("https://bugs.mojang.com/browse/MCPE-28723"))
					.hoverEvent(HoverEvent.showText(Component.text("MCPE-28723"))))
			.append(Component.text("]"));

	private final List<CombatEntry> entries = new ArrayList<>();
	private final Player player;
	private int lastDamagedBy = -1;
	private long lastDamageTime;
	private long combatStartTime;
	private long combatEndTime;
	private boolean inCombat;
	private boolean takingDamage;

	public CombatManager(Player player) {
		this.player = player;
	}

	public @Nullable String getFallLocation(PlayerStateFeature playerStateFeature) {
		Block lastClimbedBlock = playerStateFeature.getLastClimbedBlock(player);
		if (lastClimbedBlock == null) {
			//TODO check for water at feet
			return null;
		}

		if (lastClimbedBlock.compare(Block.LADDER) || lastClimbedBlock.compare(Block.ACACIA_TRAPDOOR)
				|| lastClimbedBlock.compare(Block.BIRCH_TRAPDOOR) || lastClimbedBlock.compare(Block.CRIMSON_TRAPDOOR)
				|| lastClimbedBlock.compare(Block.IRON_TRAPDOOR) || lastClimbedBlock.compare(Block.DARK_OAK_TRAPDOOR)
				|| lastClimbedBlock.compare(Block.JUNGLE_TRAPDOOR) || lastClimbedBlock.compare(Block.OAK_TRAPDOOR)
				|| lastClimbedBlock.compare(Block.SPRUCE_TRAPDOOR) || lastClimbedBlock.compare(Block.WARPED_TRAPDOOR)) {
			return "ladder";
		}

		if (lastClimbedBlock.compare(Block.VINE)) {
			return "vines";
		}

		if (lastClimbedBlock.compare(Block.WEEPING_VINES) || lastClimbedBlock.compare(Block.WEEPING_VINES_PLANT)) {
			return "weeping_vines";
		}

		if (lastClimbedBlock.compare(Block.TWISTING_VINES) || lastClimbedBlock.compare(Block.TWISTING_VINES_PLANT)) {
			return "twisting_vines";
		}

		if (lastClimbedBlock.compare(Block.SCAFFOLDING)) {
			return "scaffolding";
		}

		return "other_climbable";
	}

	public void recordDamage(int attackerId, Damage damage,
							 FallFeature fallFeature, PlayerStateFeature playerStateFeature) {
		recheckStatus();

		CombatEntry entry = new CombatEntry(damage, getFallLocation(playerStateFeature), fallFeature.getFallDistance(player));
		entries.add(entry);

		lastDamagedBy = attackerId;
		lastDamageTime = System.currentTimeMillis();
		takingDamage = true;

		if (entry.isCombat() && !inCombat && !player.isDead()) {
			inCombat = true;
			combatStartTime = System.currentTimeMillis();
			combatEndTime = combatStartTime;

			onEnterCombat();
		}
	}

	public Component getDeathMessage() {
		if (entries.isEmpty()) {
			return Component.translatable("death.attack.generic", getEntityName());
		}

		CombatEntry heaviestFall = null;
		CombatEntry lastEntry = entries.get(entries.size() - 1);
		DamageTypeInfo lastInfo = DamageTypeInfo.of(lastEntry.damage().getType());

		boolean fall = false;
		if (lastInfo.fall()) {
			heaviestFall = getHeaviestFall();
			fall = heaviestFall != null;
		}

		if (!fall) return getAttackDeathMessage(lastEntry.damage());

		DamageTypeInfo heaviestFallInfo = DamageTypeInfo.of(heaviestFall.damage().getType());
		if (heaviestFallInfo.fall() || heaviestFallInfo.outOfWorld()) {
			return Component.translatable("death.fell.accident." + heaviestFall.getMessageFallLocation(), getEntityName());
		}

		Entity firstAttacker = heaviestFall.getAttacker();
		Entity lastAttacker = lastEntry.getAttacker();

		if (firstAttacker != null && firstAttacker != lastAttacker) {
			ItemStack weapon = firstAttacker instanceof LivingEntity ? ((LivingEntity) firstAttacker).getItemInMainHand() : ItemStack.AIR;
			if (!weapon.isAir() && weapon.has(DataComponents.CUSTOM_NAME)) {
				return Component.translatable("death.fell.assist.item", getEntityName(), EntityUtil.getName(firstAttacker), weapon.get(DataComponents.CUSTOM_NAME));
			} else {
				return Component.translatable("death.fell.assist", getEntityName(), EntityUtil.getName(firstAttacker));
			}
		} else if (lastAttacker != null) {
			ItemStack weapon = lastAttacker instanceof LivingEntity ? ((LivingEntity) lastAttacker).getItemInMainHand() : ItemStack.AIR;
			if (!weapon.isAir() && weapon.has(DataComponents.CUSTOM_NAME)) {
				return Component.translatable("death.fell.finish.item", getEntityName(), EntityUtil.getName(lastAttacker), weapon.get(DataComponents.CUSTOM_NAME));
			} else {
				return Component.translatable("death.fell.finish", getEntityName(), EntityUtil.getName(lastAttacker));
			}
		} else {
			return Component.translatable("death.fell.killer", getEntityName());
		}
	}

	private Component getAttackDeathMessage(@NotNull Damage damage) {
		if (damage.getType() == DamageType.BAD_RESPAWN_POINT) {
			return Component.translatable("death.attack.badRespawnPoint.message", player.getName(), BAD_RESPAWN_POINT_MESSAGE);
		}

		DamageType damageType = MinecraftServer.getDamageTypeRegistry().get(damage.getType());
		if (damageType == null) return Component.empty();
		String id = "death.attack." + damageType.messageId();

		Entity source = damage.getSource();
		Entity attacker = damage.getAttacker();

		if (source != null) {
			Component ownerName = attacker == null ? EntityUtil.getName(source) : EntityUtil.getName(attacker);
			ItemStack weapon = source instanceof LivingEntity living ? living.getItemInMainHand() : ItemStack.AIR;
			if (!weapon.isAir() && weapon.has(DataComponents.CUSTOM_NAME)) {
				return Component.translatable(id + ".item", EntityUtil.getName(player), ownerName, weapon.get(DataComponents.CUSTOM_NAME));
			} else {
				return Component.translatable(id, EntityUtil.getName(player), ownerName);
			}
		} else {
			LivingEntity killer = getKillCredit();
			if (killer == null) {
				return Component.translatable(id, EntityUtil.getName(player));
			} else {
				return Component.translatable(id + ".player", EntityUtil.getName(player),
						EntityUtil.getName(killer));
			}
		}
	}

	private @Nullable LivingEntity getKillCredit() {
		LivingEntity killer = getKiller();
		if (killer != null) return killer;

		if (lastDamagedBy != -1) {
			Entity entity = player.getInstance().getEntityById(lastDamagedBy);
			if (entity instanceof LivingEntity living) return living;
		}

		return null;
	}

	private @Nullable LivingEntity getKiller() {
		LivingEntity entity = null;
		Player player = null;
		float livingDamage = 0.0F;
		float playerDamage = 0.0F;

		for (CombatEntry entry : entries) {
			Entity attacker = entry.getAttacker();
			if (attacker instanceof Player && (player == null || entry.damage().getAmount() > playerDamage)) {
				player = (Player) attacker;
				playerDamage = entry.damage().getAmount();
			} else if (attacker instanceof LivingEntity && (entity == null || entry.damage().getAmount() <= livingDamage)) {
				entity = (LivingEntity) attacker;
				livingDamage = entry.damage().getAmount();
			}
		}

		if (player != null && playerDamage >= livingDamage / 3.0F) {
			return player;
		}

		return entity;
	}

	public @Nullable CombatEntry getHeaviestFall() {
		CombatEntry mostDamageEntry = null;
		CombatEntry highestFallEntry = null;
		float mostDamage = 0.0F;
		double highestFall = 0.0F;

		for (int i = 0; i < entries.size(); i++) {
			CombatEntry entry = entries.get(i);
			DamageTypeInfo info = DamageTypeInfo.of(entry.damage().getType());

			if ((info.fall() || info.outOfWorld())
					&& entry.getFallDistance() > 0.0 && (mostDamageEntry == null || entry.getFallDistance() > highestFall)) {
				if (i > 0) {
					mostDamageEntry = entries.get(i - 1);
				} else {
					mostDamageEntry = entry;
				}

				highestFall = entry.getFallDistance();
			}

			if (entry.fallLocation() != null && (highestFallEntry == null || entry.damage().getAmount() > mostDamage)) {
				highestFallEntry = entry;
				mostDamage = entry.damage().getAmount();
			}
		}

		if (highestFall > 5.0 && mostDamageEntry != null) {
			return mostDamageEntry;
		} else if (mostDamage > 5.0F) {
			return highestFallEntry;
		} else {
			return null;
		}
	}

	public long getCombatDuration() {
		return inCombat ? System.currentTimeMillis() - combatStartTime : combatEndTime - combatStartTime;
	}

	public void tick() {
		if (player.isDead() || player.getAliveTicks() % 20 == 0)
			recheckStatus();

		if (lastDamagedBy != -1) {
			Entity lastDamager = player.getInstance().getEntityById(lastDamagedBy);
			if (lastDamager instanceof LivingEntity living && living.isDead()) {
				lastDamagedBy = -1;
			} else if (System.currentTimeMillis() - lastDamageTime > 5000) {
				// After 5 seconds of no attack the last damaged by does not count anymore
				lastDamagedBy = -1;
			}
		}
	}

	public void recheckStatus() {
		// Check if combat should end
		int idleMillis = inCombat ? 300 * MinecraftServer.TICK_MS : 100 * MinecraftServer.TICK_MS;
		if (takingDamage && (player.isDead() || System.currentTimeMillis() - lastDamageTime > idleMillis)) {
			reset();
			combatEndTime = System.currentTimeMillis();
		}
	}

	public void reset() {
		boolean wasInCombat = inCombat;
		takingDamage = false;
		inCombat = false;

		if (wasInCombat) {
			onLeaveCombat();
		}

		entries.clear();
	}

	public Component getEntityName() {
		return EntityUtil.getName(player);
	}

	private void onEnterCombat() {
		player.getPlayerConnection().sendPacket(new EnterCombatEventPacket());
	}

	private void onLeaveCombat() {
		int duration = (int) (getCombatDuration() / MinecraftServer.TICK_MS);
		player.getPlayerConnection().sendPacket(new EndCombatEventPacket(duration));
	}

	public List<CombatEntry> getEntries() {
		return entries;
	}

	public Player getPlayer() {
		return player;
	}

	public long getLastDamageTime() {
		return lastDamageTime;
	}

	public long getCombatStartTime() {
		return combatStartTime;
	}

	public long getCombatEndTime() {
		return combatEndTime;
	}

	public boolean isInCombat() {
		return inCombat;
	}

	public boolean isTakingDamage() {
		return takingDamage;
	}
}