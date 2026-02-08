package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.type.game.replay.recordable.bedwars.RecordableBedDestruction;
import net.swofty.type.game.replay.recordable.bedwars.RecordableFinalKill;
import net.swofty.type.game.replay.recordable.bedwars.RecordableGeneratorUpgrade;
import net.swofty.type.game.replay.recordable.bedwars.RecordableTeamElimination;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum RecordableType {
	// Block events (0-19)
	BLOCK_CHANGE(0, RecordableBlockChange::new),
	BLOCK_BREAK_ANIMATION(1, RecordableBlockBreakAnimation::new),

	// Entity events (20-49),
	ENTITY_SPAWN(20, RecordableEntitySpawn::new),
	ENTITY_DESPAWN(21, RecordableEntityDespawn::new),
	ENTITY_LOCATIONS(22, RecordableEntityLocations::new),
	ENTITY_METADATA(24, RecordableEntityMetadata::new),
	ENTITY_EQUIPMENT(25, RecordableEntityEquipment::new),
	ENTITY_MOUNT(26, RecordableEntityMount::new),
	ENTITY_ANIMATION(27, RecordableEntityAnimation::new),
	ENTITY_EFFECT(30, RecordableEntityEffect::new),
	ENTITY_EFFECT_REMOVE(31, RecordableEntityEffectRemove::new),

	// Player-specific events (50-79)
	PLAYER_ARM_SWING(50, RecordablePlayerArmSwing::new),
	PLAYER_SNEAK(51, RecordablePlayerSneak::new),
	PLAYER_SPRINT(52, RecordablePlayerSprint::new),
	PLAYER_CHAT(53, RecordablePlayerChat::new),
	PLAYER_DEATH(54, RecordablePlayerDeath::new),
	PLAYER_RESPAWN(55, RecordablePlayerRespawn::new),
	PLAYER_GAMEMODE(56, RecordablePlayerGamemode::new),
	PLAYER_HAND_ITEM(57, RecordablePlayerHandItem::new),
	PLAYER_SKIN(58, RecordablePlayerSkin::new),
	PLAYER_DISPLAY_NAME(59, RecordablePlayerDisplayName::new),
	PLAYER_HEALTH(60, RecordablePlayerHealth::new),

	// World events (80-99)
	PARTICLE(80, RecordableParticle::new),
	SOUND(81, RecordableSound::new),
	EXPLOSION(82, RecordableExplosion::new),

	// Item events (100-109)
	DROPPED_ITEM(100, RecordableDroppedItem::new),
	ITEM_PICKUP(101, RecordableItemPickup::new),

	// Display events (110-119)
	DYNAMIC_TEXT_DISPLAY(110, RecordableDynamicTextDisplay::new),
	TEXT_DISPLAY_UPDATE(111, RecordableTextDisplayUpdate::new),

	// BedWars
	BEDWARS_BED_DESTRUCTION(120, RecordableBedDestruction::new),
	BEDWARS_FINAL_KILL(121, RecordableFinalKill::new),
	BEDWARS_TEAM_ELIMINATION(122, RecordableTeamElimination::new),
	BEDWARS_EVENT_CONTINUE(123, RecordableGeneratorUpgrade::new),

	// NPC events (140-149)
	NPC_DISPLAY_NAME(140, RecordableNpcDisplayName::new),
	NPC_TEXT_LINE(141, RecordableNpcTextLine::new),

	// Composite events (150+)
	BATCH(150, RecordableBatch::new)
	;

	private static final Map<Integer, RecordableType> BY_ID = new HashMap<>();

	static {
		for (RecordableType type : values()) {
			BY_ID.put(type.id, type);
		}
	}

	@Getter
	private final int id;
	private final Supplier<Recordable> factory;

	RecordableType(int id, Supplier<Recordable> factory) {
		this.id = id;
		this.factory = factory;
	}

	public Recordable create() {
		Recordable recordable = factory.get();
		if (recordable == null) {
			throw new UnsupportedOperationException("Recordable type " + name() + " has no factory");
		}
		return recordable;
	}

	public Recordable createAndRead(ReplayDataReader reader) throws IOException {
		Recordable recordable = create();
		recordable.read(reader);
		return recordable;
	}

	public static RecordableType byId(int id) {
		return BY_ID.get(id);
	}

	public static void registerFactory(RecordableType type, Supplier<Recordable> factory) {
		// This allows game modules to register their own recordable implementations
		// For types like BED_DESTRUCTION that are game-specific
	}
}
