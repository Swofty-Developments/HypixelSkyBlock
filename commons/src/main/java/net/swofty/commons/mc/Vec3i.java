package net.swofty.commons.mc;


public record Vec3i(int x, int y, int z) {

    public static Vec3i ZERO = new Vec3i(0, 0, 0);

    public Vec3i sub(int x, int y, int z) {
        return new Vec3i(this.x - x, this.y - y, this.z - z);
    }

    public Vec3i sub(int value) {
        return sub(value, value, value);
    }

    public Vec3i sub(Vec3i other) {
        return sub(other.x, other.y, other.z);
    }

    public Vec3i add(int x, int y, int z) {
        return new Vec3i(this.x + x, this.y + y, this.z + z);
    }

    public Vec3i add(int value) {
        return add(value, value, value);
    }

    public Vec3i add(Vec3i other) {
        return add(other.x, other.y, other.z);
    }

    public HypixelPosition asHypixelPosition() {
        return new HypixelPosition(x, y, z);
    }

}
