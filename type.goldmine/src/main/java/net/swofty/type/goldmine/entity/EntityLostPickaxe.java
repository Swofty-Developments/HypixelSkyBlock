package net.swofty.type.goldmine.entity;

import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.joml.Quaternionf;

public class EntityLostPickaxe extends LivingEntity {

	private static final float width = 0.55f;
	private static final float height = 0.8f;

	public EntityLostPickaxe() {
		super(EntityType.ITEM_DISPLAY);
		editEntityMeta(ItemDisplayMeta.class, meta -> {
			meta.setHasNoGravity(true);
			meta.setItemStack(ItemStack.builder(Material.IRON_PICKAXE).set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).build());

			Quaternionf q = new Quaternionf()
					.rotateZ((float) Math.toRadians(-55));

			meta.setRightRotation(new float[]{
					q.x, q.y, q.z, q.w
			});
			meta.setLeftRotation(new float[] {0f, 0f, 0f, 1f});
			meta.setScale(new Vec(0.9, 0.9, 0.9));
		});
	}

	@Override
	public void spawn() {
		super.spawn();
		InteractionEntity interactionEntity = new InteractionEntity(width, height, (p, event) -> {
			SkyBlockPlayer player = (SkyBlockPlayer) p;
			boolean hasFound = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_FOUND_LAZY_MINER_PICKAXE);
			if (hasFound) {
				player.sendMessage("§cYou have already picked that up!");
				return;
			}
			player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_FOUND_LAZY_MINER_PICKAXE, true);
			player.sendMessage("§aYou have found the Lazy Miner's Pickaxe!");
		});
		interactionEntity.setInstance(getInstance(), getPosition().add(0, -0.4, 0));
	}
}
