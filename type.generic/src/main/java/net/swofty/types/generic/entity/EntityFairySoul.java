package net.swofty.types.generic.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.utility.ExtraItemTags;

import java.util.UUID;

public class EntityFairySoul extends EntityCreature {

    private static final String SKULL_TEXTURES = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk2OTIzYWQyNDczMTAwMDdmNmFlNWQzMjZkODQ3YWQ1Mzg2NGNmMTZjMzU2NWExODFkYzhlNmIyMGJlMjM4NyJ9fX0=";

    public FairySoul parent;

    public EntityFairySoul(FairySoul parent) {
        super(EntityType.ARMOR_STAND);
        this.parent = parent;
        setInvisible(true);
        ArmorStandMeta meta = (ArmorStandMeta) getEntityMeta();
        meta.setCustomNameVisible(false);
        meta.setHasNoGravity(true);
        meta.setNotifyAboutChanges(false);

        setHelmet(ItemStack.builder(Material.PLAYER_HEAD).meta(iMeta -> {
            iMeta.damage(3);
            iMeta.set(ExtraItemTags.SKULL_OWNER, new ExtraItemTags.SkullOwner(UUID.randomUUID(), null, new PlayerSkin(SKULL_TEXTURES, null)));
        }).build());
    }

    /**
     * Sets the location of the Fairy Soul and spawn the entity
     */
    public void spawn(Instance instance) {
        setInstance(instance, parent.getLocation().sub(0, 1.46875, 0).withYaw(118.125f));
    }
}
