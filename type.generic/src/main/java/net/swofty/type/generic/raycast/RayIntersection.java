package net.swofty.type.generic.raycast;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

public record RayIntersection<T>(double distance, Point point, Vec normal, double exitT, Point exitPoint, Vec exitNormal,
                                 T object) implements Comparable<RayIntersection<?>> {

    private static boolean startsBefore(RayIntersection<?> left, RayIntersection<?> right) {
        return left.distance <= right.distance;
    }

    private static boolean endsAfter(RayIntersection<?> left, RayIntersection<?> right) {
        return left.exitT >= right.exitT;
    }

    @Override
    public int compareTo(RayIntersection<?> o) {
        int byEntry = Double.compare(distance, o.distance);
        if (byEntry != 0) return byEntry;
        return Double.compare(exitT, o.exitT);
    }

    /**
     * Creates a copy of this intersection with the specified hit object.
     *
     * @param object the new object
     * @param <R>    the type of the hit object
     * @return a new intersection
     */
    public <R> RayIntersection<R> withObject(R object) {
        return new RayIntersection<>(distance, point, normal, exitT, exitPoint, exitNormal, object);
    }

    /**
     * Returns whether an intersection overlaps with another.
     *
     * @param other the other intersection
     * @return whether the intersections overlap
     */
    public boolean overlaps(RayIntersection<?> other) {
        return distance <= other.exitT && other.distance <= exitT;
    }

    /**
     * Merges two intersections by making one out of the lowest distance and highest exitT from the intersections.
     *
     * @param other the other intersection
     * @return a potentially larger intersection with the same {@link #object} as this
     */
    public RayIntersection<T> merge(RayIntersection<?> other) {
        boolean thisStartsFirst = startsBefore(this, other);
        boolean thisEndsLast = endsAfter(this, other);
        return new RayIntersection<>(thisStartsFirst ? distance : other.distance, thisStartsFirst ? point : other.point, thisStartsFirst ? normal : other.normal, thisEndsLast ? exitT : other.exitT, thisEndsLast ? exitPoint : other.exitPoint, thisEndsLast ? exitNormal : other.exitNormal, object);
    }
}
