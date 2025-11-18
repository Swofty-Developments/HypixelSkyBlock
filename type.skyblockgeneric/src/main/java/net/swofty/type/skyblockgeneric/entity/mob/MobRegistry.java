package net.swofty.type.skyblockgeneric.entity.mob;

import lombok.Getter;
import org.tinylog.Logger;
import net.minestom.server.entity.EntityType;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.entity.mob.impl.RegionPopulator;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import org.tinylog.Logger;
import java.util.ArrayList;
import org.tinylog.Logger;
import java.util.Arrays;
import org.tinylog.Logger;
import java.util.List;
import org.tinylog.Logger;

@Getter
public class MobRegistry {
    public static final List<MobRegistry> REGISTERED_MOBS = new ArrayList<>(Arrays.asList());

    private final EntityType entityType;
    private final Class<? extends SkyBlockMob> clazz;
    private final SkyBlockMob mobCache;

    public MobRegistry(Class<? extends SkyBlockMob> clazz) {
        this.clazz = clazz;
        this.mobCache = asMob();
        this.entityType = mobCache.getEntityType();
    }

    public SkyBlockMob asMob() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            Logger.error(e, "Failed to register mob in MobRegistry");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void registerExtraMob(Class<? extends SkyBlockMob> clazz) {
        REGISTERED_MOBS.add(new MobRegistry(clazz));
    }

    public static List<MobRegistry> getMobsToRegionPopulate() {
        return REGISTERED_MOBS.stream().filter(mobRegistry -> mobRegistry.getMobCache() instanceof RegionPopulator).toList();
    }

    public static MobRegistry getFromMob(SkyBlockMob mob) {
        return REGISTERED_MOBS.stream().filter(mobRegistry -> mobRegistry.getClazz() == mob.getClass()).findFirst().orElse(null);
    }

    public static BestiaryMob getMobById(String id) {
        for (MobRegistry registry : REGISTERED_MOBS) {
            if (registry.getMobCache() instanceof BestiaryMob mob) {
                String mobName = mob.getMobID();
                if (mobName.equals(id)) {
                    return mob;
                }
            }
        }
        return null;
    }
}
