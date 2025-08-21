package net.swofty.pvp.entity.projectile;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.projectile.ArrowMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.CustomPotionEffect;
import net.swofty.pvp.feature.effect.EffectFeature;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class Arrow extends AbstractArrow {
	public static final ItemStack DEFAULT_ARROW = ItemStack.of(Material.ARROW);

	private final EffectFeature effectFeature;

	private ItemStack itemStack = DEFAULT_ARROW;

	public Arrow(@Nullable Entity shooter, EffectFeature effectFeature, EnchantmentFeature enchantmentFeature) {
		super(shooter, EntityType.ARROW, enchantmentFeature);
		this.effectFeature = effectFeature;
	}

	@Override
	public void update(long time) {
		super.update(time);
		if (onGround && stuckTime >= 600 && (!itemStack.has(DataComponents.POTION_CONTENTS)
				|| !Objects.equals(itemStack.get(DataComponents.POTION_CONTENTS), PotionContents.EMPTY))) {
			triggerStatus((byte) 0);
			itemStack = DEFAULT_ARROW;
		}
	}

	@Override
	protected ItemStack getPickupItem() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		updateColor();
	}

	@Override
	protected void onHurt(LivingEntity entity) {
		effectFeature.addArrowEffects(entity, this);
	}

	private void updateColor() {
		PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);
		if (potionContents == null || potionContents.equals(PotionContents.EMPTY)) {
			setColor(-1);
			return;
		}

		setColor(effectFeature.getPotionColor(potionContents));
	}

	private void setColor(int color) {
		((ArrowMeta) getEntityMeta()).setColor(color);
	}

	public @NotNull PotionContents getPotion() {
		return itemStack.get(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
	}

	public void setPotion(@NotNull PotionContents potion) {
		if (itemStack.material() != Material.TIPPED_ARROW)
			itemStack = ItemStack.of(Material.TIPPED_ARROW);
		itemStack = itemStack.with(DataComponents.POTION_CONTENTS, potion);
		updateColor();
	}

	public void addArrowEffect(CustomPotionEffect effect) {
		itemStack = itemStack.with(DataComponents.POTION_CONTENTS, (UnaryOperator<PotionContents>) potionContents -> {
			List<CustomPotionEffect> list = new ArrayList<>(potionContents.customEffects());
			list.add(effect);
			return new PotionContents(potionContents.potion(), potionContents.customColor(), list);
		});
	}
}