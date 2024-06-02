package net.swofty.types.generic.entity;


import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;


public class DecorationEntityImpl extends EntityCreature {
    public DecorationEntityImpl(SkyBlockItem item , SkyBlockPlayer player) {
        super(EntityType.ARMOR_STAND);
        this.setInvisible(true);

        ArmorStandMeta armorStandMeta = (ArmorStandMeta) getEntityMeta();
        armorStandMeta.setCustomNameVisible(false);
        armorStandMeta.setHasNoGravity(true);
        armorStandMeta.setNotifyAboutChanges(false);

        SkullHead decorationHead = (SkullHead) item.getGenericInstance();

        setHelmet(
                ItemStackCreator.getStackHead(decorationHead.getSkullTexture(player , item)).build()
        );

    }

    public void spawn(Instance instance , Point pos) {
       this.setInstance(instance , pos.sub(0, 1.46875, 0));
    }
}
