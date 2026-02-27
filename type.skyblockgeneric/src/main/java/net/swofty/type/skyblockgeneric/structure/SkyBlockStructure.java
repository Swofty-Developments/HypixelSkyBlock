package net.swofty.type.skyblockgeneric.structure;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.entity.hologram.ServerHolograms;

import java.util.List;

@Getter
@Setter
public abstract class SkyBlockStructure {
    private int rotation;
    private int x;
    private int y;
    private int z;

    public SkyBlockStructure(int rotation, int x, int y, int z) {
        this.rotation = rotation;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public abstract void setBlocks(Instance instance);

    public abstract List<StructureHologram> getHolograms();

    public void build(SharedInstance instance) {
        setBlocks(instance);
        getHolograms().forEach(hologram -> {
            ServerHolograms.addExternalHologram(hologram.getHologram(this, instance));
        });
    }

    protected void set(Instance instance, int x, int y, int z, Block block) {
        instance.setBlock(
                rotateValue(this.x, x, CoordinateType.X),
                this.y + y,
                rotateValue(this.z, z, CoordinateType.Z),
                block);
    }

    protected void get(Instance instance, int x, int y, int z) {
        instance.getBlock(
                rotateValue(this.x, x, CoordinateType.X),
                this.y + y,
                rotateValue(this.z, z, CoordinateType.Z));
    }

    protected void fill(Instance instance, int x1, int y1, int z1, int x2, int y2, int z2, Block block) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    set(instance, x, y, z, block);
    }

    public int rotateValue(int value, int difference, CoordinateType type) {
        switch (type) {
            case X:
                switch (rotation) {
                    case 0, 1 -> { // North, South rotation doesn't affect x movement
                        return value + difference;
                    }
                    case 2, 3 -> { // West rotation, x becomes negative
                        return value - difference;
                    }
                }
            case Z:
                switch (rotation) {
                    case 0, 3 -> { // North rotation, z becomes negative
                        return value - difference;
                    }
                    case 1, 2 -> { // East, West rotation doesn't affect z movement
                        return value + difference;
                    }
                }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public record StructureHologram(String[] text, int x, int y, int z) {
        public ServerHolograms.ExternalHologram getHologram(SkyBlockStructure structure, SharedInstance instance) {

            return ServerHolograms.ExternalHologram.builder()
                    .instance(instance)
                    .pos(new net.minestom.server.coordinate.Pos(
                            structure.rotateValue(structure.getX(), x, CoordinateType.X) + 0.5,
                            structure.getY() + y + 0.5,
                            structure.rotateValue(structure.getZ(), z, CoordinateType.Z) + 0.5
                    ))
                    .text(text)
                    .build();
        }
    }

    public enum CoordinateType {
        X, Z
    }
}
