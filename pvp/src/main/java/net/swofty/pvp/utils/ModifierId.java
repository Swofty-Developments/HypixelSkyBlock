package net.swofty.pvp.utils;

import net.kyori.adventure.key.Key;

public class ModifierId {
	public static final Key ATTACK_DAMAGE_MODIFIER_ID = Key.key("minecraft:base_attack_damage");
	public static final Key ATTACK_SPEED_MODIFIER_ID = Key.key("minecraft:base_attack_speed");
	
	public static final Key[] ARMOR_MODIFIERS = new Key[]{
			Key.key("minecraft:armor.boots"),
			Key.key("minecraft:armor.leggings"),
			Key.key("minecraft:armor.chestplate"),
			Key.key("minecraft:armor.helmet"),
	};
}
