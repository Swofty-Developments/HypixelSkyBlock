package net.swofty.types.generic.entity;

import lombok.Getter;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.LeatherArmorMeta;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MinionSkinItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.extension.extensions.MinionSkinExtension;
import net.swofty.types.generic.utility.MathUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class MinionEntityImpl extends LivingEntity {
    @Getter
    private static final List<MinionEntityImpl> activeMinions = Collections.synchronizedList(new ArrayList<>());

    private final SkyBlockMinion minion;
    private IslandMinionData.IslandMinion islandMinion;

    public MinionEntityImpl(IslandMinionData.IslandMinion islandMinion, SkyBlockMinion minion) {
        super(EntityType.ARMOR_STAND);

        this.hasCollision = false;
        this.hasPhysics = false;

        this.islandMinion = islandMinion;
        this.minion = minion;

        setAutoViewable(true);
        setAutoViewEntities(true);

        ArmorStandMeta meta = (ArmorStandMeta) this.entityMeta;
        meta.setSmall(true);
        meta.setHasNoBasePlate(true);
        meta.setHasNoGravity(true);
        meta.setHasArms(true);

        setInvisible(false);

        updateMinionDisplay(islandMinion);
    }

    public void updateMinionDisplay(IslandMinionData.IslandMinion updatedMinion) {
        islandMinion = updatedMinion;

        int tier = islandMinion.getTier();
        SkyBlockMinion.MinionTier minionTier = minion.getTiers().get(tier - 1);

        MinionSkinExtension skinExtension = (MinionSkinExtension) updatedMinion.getExtensionData().getOfType(MinionSkinExtension.class);

        if (skinExtension.getItemTypePassedIn() != null) {
            MinionSkinItem skinItem = (MinionSkinItem) new SkyBlockItem(skinExtension.getItemTypePassedIn()).getGenericInstance();

            setHelmet(skinItem.getHelmet());
            setBoots(skinItem.getBoots());
            setLeggings(skinItem.getLeggings());
            setChestplate(skinItem.getChestplate());
        } else {
            setHelmet(ItemStackCreator.getStackHead(minionTier.texture()).build());
            setBoots(ItemStack.builder(Material.LEATHER_BOOTS).meta(bootMeta -> {
                LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(bootMeta.tagHandler());
                leatherMeta.color(minion.getBootColour());
            }).build());
            setLeggings(ItemStack.builder(Material.LEATHER_LEGGINGS).meta(leggingsMeta -> {
                LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(leggingsMeta.tagHandler());
                leatherMeta.color(minion.getLeggingsColour());
            }).build());
            setChestplate(ItemStack.builder(Material.LEATHER_CHESTPLATE).meta(chestplateMeta -> {
                LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(chestplateMeta.tagHandler());
                leatherMeta.color(minion.getChestplateColour());
            }).build());
        }

        setItemInMainHand(ItemStack.builder(minionTier.heldItem()).build());
    }

    @Override
    public void spawn() {
        activeMinions.add(this);
    }

    @Override
    public void remove() {
        super.remove();
        activeMinions.remove(this);
    }

    public void swingAnimation() {
        ArmorStandMeta meta = (ArmorStandMeta) this.entityMeta;

        for (int rotation = 0; rotation < 90; rotation = rotation + 9) {
            int delay = rotation / 9;
            int inverseDelay = 11 - delay;

            int finalRotation = rotation;
            MathUtility.delay(() -> {
                meta.setRightArmRotation(new Vec(360 - finalRotation,0, 0));
            }, delay + 1);

            MathUtility.delay(() -> {
                meta.setRightArmRotation(new Vec(360 - finalRotation,0, 0));
            }, 10 + inverseDelay);
        }
    }

    public void placeAnimation() {
        ArmorStandMeta meta = (ArmorStandMeta) this.entityMeta;

        for (int rotation = 0; rotation < 90; rotation = rotation + 9) {
            int delay = rotation / 9;
            int inverseDelay = 11 - delay;

            int finalRotation = rotation;
            MathUtility.delay(() -> {
                meta.setRightArmRotation(new Vec(360 - finalRotation,0, 0));
                meta.setLeftArmRotation(new Vec(360 - finalRotation,0, 0));
            }, delay + 1);

            MathUtility.delay(() -> {
                meta.setRightArmRotation(new Vec(360 - finalRotation,0, 0));
                meta.setLeftArmRotation(new Vec(360 - finalRotation,0, 0));
            }, 10 + inverseDelay);
        }
    }
}
