package net.swofty.pvp.enums;

import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;

public enum ToolMaterial {
	WOOD(0, 2.0F, 0.0F, Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_HOE),
	STONE(1, 4.0F, 1.0F, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_HOE),
	IRON(2, 6.0F, 2.0F, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_HOE),
	DIAMOND(3, 8.0F, 3.0F, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_HOE),
	GOLD(0, 12.0F, 0.0F, Material.GOLDEN_SWORD, Material.GOLDEN_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_HOE),
	NETHERITE(4, 9.0F, 4.0F, Material.NETHERITE_SWORD, Material.NETHERITE_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_HOE);
	
	private final int miningLevel;
	private final float miningSpeed;
	private final float attackDamage;
	private final Material[] items;
	
	ToolMaterial(int miningLevel, float miningSpeed, float attackDamage, Material... items) {
		this.miningLevel = miningLevel;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.items = items;
	}
	
	public float getMiningSpeed() {
		return this.miningSpeed;
	}
	
	public float getAttackDamage() {
		return this.attackDamage;
	}
	
	public int getMiningLevel() {
		return this.miningLevel;
	}
	
	private static final Map<Material, ToolMaterial> MATERIAL_TO_TOOL_MATERIAL = new HashMap<>();
	
	public static ToolMaterial fromMaterial(Material material) {
		return MATERIAL_TO_TOOL_MATERIAL.get(material);
	}
	
	static {
		for (ToolMaterial toolMaterial : values()) {
			for (Material material : toolMaterial.items) {
				MATERIAL_TO_TOOL_MATERIAL.put(material, toolMaterial);
			}
		}
	}
}
