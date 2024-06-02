package net.swofty.types.generic.entity.mob;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.entity.mob.mobs.MobSheep;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MobRegistry {
    private static List<MobRegistry> REGISTERED_MOBS = new ArrayList<>(Arrays.asList(
            new MobRegistry(EntityType.SHEEP, MobSheep.class)
    ));

    private final EntityType entityType;
    private final Class<? extends SkyBlockMob> clazz;
    private final SkyBlockMob mobCache;

    public MobRegistry(EntityType entityType, Class<? extends SkyBlockMob> clazz) {
        this.entityType = entityType;
        this.clazz = clazz;
        this.mobCache = asMob();
    }

    public SkyBlockMob asMob() {
        try {
            return clazz.getDeclaredConstructor(EntityType.class).newInstance(entityType);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void registerExtraMob(EntityType entityType, Class<? extends SkyBlockMob> clazz) {
        REGISTERED_MOBS.add(new MobRegistry(entityType, clazz));
    }

    public static List<MobRegistry> getMobsToRegionPopulate() {
        return REGISTERED_MOBS.stream().filter(mobRegistry -> mobRegistry.getMobCache() instanceof RegionPopulator).toList();
    }

    public static MobRegistry getFromMob(SkyBlockMob mob) {
        return REGISTERED_MOBS.stream().filter(mobRegistry -> mobRegistry.getClazz() == mob.getClass()).findFirst().orElse(null);
    }
}
