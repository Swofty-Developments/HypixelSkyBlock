package net.swofty.type.hub.runes;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;

public class RuneEntityImpl extends LivingEntity {
    public RuneEntityImpl(Pos position, boolean isHead) {
        super(EntityType.ARMOR_STAND);

        setAutoViewable(true);
        setAutoViewEntities(true);

        ArmorStandMeta meta = (ArmorStandMeta) this.entityMeta;
        meta.setHasNoBasePlate(true);
        meta.setHasNoGravity(true);
        meta.setHasArms(false);

        setInvisible(true);

        if (isHead) {
            meta.setSmall(true);
            ((ArmorStandMeta) this.entityMeta).setHeadRotation(new Vec( -90, 0.4, 0));
            setHelmet(ItemStackCreator.getStackHead("5c540298a017b25f9cfae9281fe5b585d770db1852b73804d1bb7c7ee53733a4").build());
        } else {
            setHelmet(ItemStack.builder(Material.STONE_SLAB).build());
        }

        setInstance(SkyBlockConst.getInstanceContainer(), position);
    }
}
