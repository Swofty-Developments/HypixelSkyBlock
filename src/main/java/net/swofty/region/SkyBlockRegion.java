package net.swofty.region;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.swofty.data.mongodb.RegionDatabase;

import java.util.*;

@Getter
public class SkyBlockRegion {
    private static final Map<String, SkyBlockRegion> REGION_CACHE = new HashMap<>();

    private final String name;
    private RegionDatabase regionDatabase;

    @Setter
    private Pos firstLocation;
    @Setter
    private Pos secondLocation;
    @Setter
    private RegionType type;

    public SkyBlockRegion(String name, Pos firstLocation, Pos secondLocation, RegionType type) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.regionDatabase = new RegionDatabase(name);
        REGION_CACHE.put(this.name, this);
    }

    public void save() {
        regionDatabase.insertOrUpdate("x1", firstLocation.x());
        regionDatabase.insertOrUpdate("y1", firstLocation.y());
        regionDatabase.insertOrUpdate("z1", firstLocation.z());

        regionDatabase.insertOrUpdate("x2", secondLocation.x());
        regionDatabase.insertOrUpdate("y2", secondLocation.y());
        regionDatabase.insertOrUpdate("z2", secondLocation.z());

        regionDatabase.insertOrUpdate("type", type.name());
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

    public static SkyBlockRegion getRegionOfPosition(Pos position) {
        List<SkyBlockRegion> possible = new ArrayList<>();
        for (SkyBlockRegion region : getRegions()) {
            if (region.insideRegion(position))
                possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return !possible.isEmpty() ? possible.get(0) : null;
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
        return  x >= (double) bounds.get(0) && x <= (double) bounds.get(1) &&
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
        for (SkyBlockRegion region : RegionDatabase.getAllRegions())
            REGION_CACHE.put(region.getName(), region);
    }
}
