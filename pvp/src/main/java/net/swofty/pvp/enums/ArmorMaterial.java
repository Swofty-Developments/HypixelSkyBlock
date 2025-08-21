package net.swofty.pvp.enums;

import net.kyori.adventure.key.Key;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.swofty.pvp.utils.CombatVersion;
import net.swofty.pvp.utils.ModifierId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum ArmorMaterial {
	LEATHER(new int[]{1, 2, 3, 1}, new int[]{1, 3, 2, 1}, SoundEvent.ITEM_ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET),
	CHAIN(new int[]{1, 4, 5, 2}, new int[]{2, 5, 4, 1}, SoundEvent.ITEM_ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET),
	IRON(new int[]{2, 5, 6, 2}, new int[]{2, 6, 5, 2}, SoundEvent.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET),
	GOLD(new int[]{1, 3, 5, 2}, new int[]{2, 5, 3, 1}, SoundEvent.ITEM_ARMOR_EQUIP_GOLD, 0.0F, 0.0F, Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET),
	DIAMOND(new int[]{3, 6, 8, 3}, new int[]{3, 8, 6, 3}, SoundEvent.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET),
	TURTLE(new int[]{2, 5, 6, 2}, new int[]{2, 6, 5, 2}, SoundEvent.ITEM_ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, Material.TURTLE_HELMET),
	NETHERITE(new int[]{3, 6, 8, 3}, new int[]{3, 8, 6, 3}, SoundEvent.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET);

	private static final Map<Material, ArmorMaterial> MATERIAL_TO_ARMOR_MATERIAL = new HashMap<>();

	static {
		for (ArmorMaterial armorMaterial : values()) {
			for (Material material : armorMaterial.items) {
				MATERIAL_TO_ARMOR_MATERIAL.put(material, armorMaterial);
			}
		}
	}

	private final int[] protectionAmounts;
	private final int[] legacyProtectionAmounts;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Material[] items;

	ArmorMaterial(int[] protectionAmounts, int[] legacyProtectionAmounts, SoundEvent equipSound, float toughness, float knockbackResistance, Material... items) {
		this.protectionAmounts = protectionAmounts;
		this.legacyProtectionAmounts = legacyProtectionAmounts;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.items = items;
	}

	public static void updateEquipmentAttributes(LivingEntity entity, ItemStack oldStack, ItemStack newStack,
												 EquipmentSlot slot, CombatVersion version) {
		ArmorMaterial oldMaterial = fromMaterial(oldStack.material());
		ArmorMaterial newMaterial = fromMaterial(newStack.material());

		Key modifierId = getModifierId(slot);

		// Remove attributes from previous armor
		if (oldMaterial != null && hasDefaultAttributes(oldStack)) {
			if (slot == getRequiredSlot(oldStack.material())) {
				entity.getAttribute(Attribute.ARMOR).removeModifier(modifierId);
				entity.getAttribute(Attribute.ARMOR_TOUGHNESS).removeModifier(modifierId);
				entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE).removeModifier(modifierId);
			}
		}

		// Add attributes from new armor
		if (newMaterial != null && hasDefaultAttributes(newStack)) {
			if (slot == getRequiredSlot(newStack.material())) {
				entity.getAttribute(Attribute.ARMOR).addModifier(new AttributeModifier(modifierId, newMaterial.getProtectionAmount(slot, version), AttributeOperation.ADD_VALUE));
				entity.getAttribute(Attribute.ARMOR_TOUGHNESS).addModifier(new AttributeModifier(modifierId, newMaterial.getToughness(), AttributeOperation.ADD_VALUE));
				if (newMaterial.getKnockbackResistance() > 0) {
					entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier(modifierId, newMaterial.getKnockbackResistance(), AttributeOperation.ADD_VALUE));
				}
			}
		}
	}

	private static boolean hasDefaultAttributes(ItemStack stack) {
		// When modifiers tag is not empty, default modifiers are not
		return !stack.has(DataComponents.ATTRIBUTE_MODIFIERS)
				|| Objects.requireNonNull(stack.get(DataComponents.ATTRIBUTE_MODIFIERS)).modifiers().isEmpty();
	}

	public static EquipmentSlot getRequiredSlot(Material material) {
		EquipmentSlot slot = material.registry().equipmentSlot();
		return slot == null ? EquipmentSlot.HELMET : slot;
	}

	public static ArmorMaterial fromMaterial(Material material) {
		return MATERIAL_TO_ARMOR_MATERIAL.get(material);
	}

	public static Key getModifierId(EquipmentSlot slot) {
		return ModifierId.ARMOR_MODIFIERS[slot.ordinal() - 2];
	}

	public int getProtectionAmount(EquipmentSlot slot, CombatVersion version) {
		int id;
		switch (slot) {
			case HELMET -> id = 3;
			case CHESTPLATE -> id = 2;
			case LEGGINGS -> id = 1;
			case BOOTS -> id = 0;
			default -> {
				return 0;
			}
		}

		return version.legacy() ? this.legacyProtectionAmounts[id] : this.protectionAmounts[id];
	}

	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}