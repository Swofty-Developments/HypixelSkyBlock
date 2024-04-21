package net.swofty.types.generic.region;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.mongodb.RegionDatabase;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
public class SkyBlockRegion {
    private static final Map<String, SkyBlockRegion> REGION_CACHE = new HashMap<>();

    private final String name;
    private final RegionDatabase regionDatabase;

    private Pos firstLocation;
    private Pos secondLocation;
    private RegionType type;
    private ServerType serverType;

    public SkyBlockRegion(String name, Pos firstLocation, Pos secondLocation, RegionType type, ServerType serverType) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.regionDatabase = new RegionDatabase(name);
        this.serverType = serverType;
    }

    public void save() {
        regionDatabase.insertOrUpdate("x1", firstLocation.blockX());
        regionDatabase.insertOrUpdate("y1", firstLocation.blockY());
        regionDatabase.insertOrUpdate("z1", firstLocation.blockZ());

        regionDatabase.insertOrUpdate("x2", secondLocation.blockX());
        regionDatabase.insertOrUpdate("y2", secondLocation.blockY());
        regionDatabase.insertOrUpdate("z2", secondLocation.blockZ());

        regionDatabase.insertOrUpdate("type", type.name());
        regionDatabase.insertOrUpdate("serverType", serverType.name());

        REGION_CACHE.put(name, this);
    }

    public Pos getRandomPosition() {
        List<Integer> bounds = getBounds();
        int x = new Random().nextInt(bounds.get(1) - bounds.get(0)) + bounds.get(0);
        int y = new Random().nextInt(bounds.get(3) - bounds.get(2)) + bounds.get(2);
        int z = new Random().nextInt(bounds.get(5) - bounds.get(4)) + bounds.get(4);
        return new Pos(x, y, z);
    }

    /**
     * Gets a random position for an entity to spawn in the region
     * @param instance The instance to check if the position is valid in
     * @return A random position in the region, or null if there is no valid position
     * @see SkyBlockMob#spawn()
     * @see SkyBlockMob#setInstance(Instance, Pos)
     */
    public @Nullable Pos getRandomPositionForEntity(Instance instance) {
        // Checks if there is a 2 block gap up the Y level and if the block below is solid

        int tries = 0;
        while (true) {
            tries++;
            Pos randomPosition = getRandomPosition();
            Pos blockAbove = randomPosition.add(0, 1, 0);
            Pos blockBelow = randomPosition.sub(0, 1, 0);

            if (tries > 5) {
                return null;
            }

            if (instance.isChunkLoaded(randomPosition)
                    && instance.getBlock(randomPosition).isAir()
                    && instance.getBlock(blockAbove).isAir()
                    && !instance.getBlock(blockBelow).isAir())
                return randomPosition;
        }
    }

    public void delete() {
        REGION_CACHE.remove(name);
        regionDatabase.remove(name);
    }

    public static List<SkyBlockRegion> getRegions() {
        return new ArrayList<>(REGION_CACHE.values());
    }

    public static SkyBlockRegion getFromID(String id) {
        if (REGION_CACHE.containsKey(id.toLowerCase()))
            return REGION_CACHE.get(id.toLowerCase());
        return null;
    }

    public static SkyBlockRegion getRegionOfEntity(Entity entity) {
        return getRegionOfPosition(entity.getPosition());
    }

    public static List<SkyBlockMob> getMobsInRegion(RegionType region) {
        List<SkyBlockMob> mobs = new ArrayList<>();
        for (SkyBlockMob mob : SkyBlockMob.getMobs()) {
            SkyBlockRegion regionOfEntity = getRegionOfEntity(mob);

            if (regionOfEntity != null && regionOfEntity.getType() == region)
                mobs.add(mob);
        }
        return mobs;
    }

    public static SkyBlockRegion getRegionOfPosition(Point point) {
        return getRegionOfPosition(Pos.fromPoint(point));
    }

    public static SkyBlockRegion getRandomRegionOfType(RegionType type) {
        List<SkyBlockRegion> regions = new ArrayList<>();
        for (SkyBlockRegion region : getRegions()) {
            if (region.getType() == type)
                regions.add(region);
        }
        if (regions.isEmpty()) return null;
        double random = Math.random();
        int index = (int) (random * regions.size());
        return regions.get(index);
    }

    public static SkyBlockRegion getRegionOfPosition(Pos position) {
        List<SkyBlockRegion> possible = new ArrayList<>();
        for (SkyBlockRegion region : getRegions()) {
            if (region.insideRegion(position))
                possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return !possible.isEmpty() ? possible.getFirst() : null;
    }

    public boolean insideRegion(Entity entity) {
        return insideRegion(entity.getPosition());
    }

    public boolean insideRegion(Pos location) {
        List<Integer> bounds = getBounds();
        double x = location.x();
        double y = location.y();
        double z = location.z();
        if (firstLocation == null)
            return false;
        return x >= (double) bounds.get(0) && x <= (double) bounds.get(1) &&
                y >= (double) bounds.get(2) && y <= (double) bounds.get(3) &&
                z >= (double) bounds.get(4) && z <= (double) bounds.get(5);
    }

    public List<Integer> getBounds() {
        int sx = Math.min(firstLocation.blockX(), secondLocation.blockX()),
                ex = Math.max(firstLocation.blockX(), secondLocation.blockX()),
                sy = Math.min(firstLocation.blockY(), secondLocation.blockY()),
                ey = Math.max(firstLocation.blockY(), secondLocation.blockY()),
                sz = Math.min(firstLocation.blockZ(), secondLocation.blockZ()),
                ez = Math.max(firstLocation.blockZ(), secondLocation.blockZ());
        return Arrays.asList(sx, ex, sy, ey, sz, ez);
    }

    public static void cacheRegions() {
        for (SkyBlockRegion region : RegionDatabase.getAllRegions()) {
            if (region.getType() == null || region.getType() == RegionType.PRIVATE_ISLAND) {
                region.delete();
            } else {
                ServerType typeOfRegion = region.getServerType();
                if (typeOfRegion != SkyBlockConst.getTypeLoader().getType())
                    continue;

                REGION_CACHE.put(region.getName(), region);
            }
        }
        REGION_CACHE.put("island", new SkyBlockRegion("island",
                new Pos(0, 0, 0),
                new Pos(0, 0, 0),
                RegionType.PRIVATE_ISLAND,
                ServerType.ISLAND));
    }

    public static SkyBlockRegion getIslandRegion() {
        return REGION_CACHE.get("island");
    }
}
