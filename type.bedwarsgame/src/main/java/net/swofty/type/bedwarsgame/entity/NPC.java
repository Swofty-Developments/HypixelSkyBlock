package net.swofty.type.bedwarsgame.entity;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.entity.VillagerType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.minestom.server.tag.Tag;

public class NPC extends EntityCreature {

    @Getter
    private final String name;

    public NPC(String name, NPCType type) {
        super(EntityType.VILLAGER);
        this.name = name;

        editEntityMeta(VillagerMeta.class, (meta) -> {
            meta.setVillagerData(new VillagerMeta.VillagerData(
                    VillagerType.PLAINS,
                    VillagerProfession.CLERIC,
                    VillagerMeta.Level.APPRENTICE
            ));
        });

        setCustomName(Component.text(name));
        setCustomNameVisible(true);
        setNoGravity(true);
        setTag(Tag.String("type"), type.name().toLowerCase());
    }

    public enum NPCType {
        SHOP,
        TEAM
    }

}
