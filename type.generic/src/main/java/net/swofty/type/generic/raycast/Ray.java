package net.swofty.type.generic.raycast;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.collision.Shape;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.EntityTracker;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public record Ray(Point start, Vec direction, double distance, Vec inverse) {
    private static final double AXIS_EPSILON = 1.0E-9;

    private static final Vec NEGATIVE_UNIT_X = new Vec(-1, 0, 0);
    private static final Vec POSITIVE_UNIT_X = new Vec(1, 0, 0);
    private static final Vec NEGATIVE_UNIT_Y = new Vec(0, -1, 0);
    private static final Vec POSITIVE_UNIT_Y = new Vec(0, 1, 0);
    private static final Vec NEGATIVE_UNIT_Z = new Vec(0, 0, -1);
    private static final Vec POSITIVE_UNIT_Z = new Vec(0, 0, 1);

    /**
     * Constructs a ray.
     *
     * @param origin the start point
     * @param vector the ray's path, which can have any nonzero length
     */
    public Ray(Point origin, Vec vector) {
        Check.argCondition(vector.isZero(), "Ray may not have zero length");
        Vec normalized = vector.normalize();
        this(origin, normalized, vector.length(), Vec.ONE.div(normalized));
    }

    /**
     * Check if this ray hits some shape.
     *
     * @param shape  the shape to check against
     * @param offset an offset to shift the shape by, e.g. for block hitboxes
     * @param <S>    any Shape
     * @return an {@link RayIntersection} if one is found between this ray and the shape, and null otherwise
     */
    public <S extends Shape> @Nullable RayIntersection<S> cast(S shape, Point offset) {
        Vec shapeMin = shape.relativeStart().asVec().add(offset.asVec());
        Vec shapeMax = shape.relativeEnd().asVec().add(offset.asVec());

        AxisInterval x = axisInterval(start.x(), direction.x(), shapeMin.x(), shapeMax.x(), NEGATIVE_UNIT_X, POSITIVE_UNIT_X);
        if (x == null) return null;
        AxisInterval y = axisInterval(start.y(), direction.y(), shapeMin.y(), shapeMax.y(), NEGATIVE_UNIT_Y, POSITIVE_UNIT_Y);
        if (y == null) return null;
        AxisInterval z = axisInterval(start.z(), direction.z(), shapeMin.z(), shapeMax.z(), NEGATIVE_UNIT_Z, POSITIVE_UNIT_Z);
        if (z == null) return null;

        double entryT = x.entryT;
        Vec entryNormal = x.entryNormal;
        if (y.entryT > entryT) {
            entryT = y.entryT;
            entryNormal = y.entryNormal;
        }
        if (z.entryT > entryT) {
            entryT = z.entryT;
            entryNormal = z.entryNormal;
        }

        double exitT = x.exitT;
        Vec exitNormal = x.exitNormal;
        if (y.exitT < exitT) {
            exitT = y.exitT;
            exitNormal = y.exitNormal;
        }
        if (z.exitT < exitT) {
            exitT = z.exitT;
            exitNormal = z.exitNormal;
        }

        if (exitT < entryT || exitT < 0 || entryT > distance) {
            return null;
        }

        return new RayIntersection<>(entryT, start.add(direction.mul(entryT)), entryNormal, exitT, start.add(direction.mul(exitT)), exitNormal, shape);
    }

    private static @Nullable AxisInterval axisInterval(double originAxis, double directionAxis, double minAxis, double maxAxis, Vec minNormal, Vec maxNormal) {
        if (Math.abs(directionAxis) <= AXIS_EPSILON) {
            if (originAxis < minAxis || originAxis > maxAxis) return null;
            return new AxisInterval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Vec.ZERO, Vec.ZERO);
        }

        double entryT = (minAxis - originAxis) / directionAxis;
        double exitT = (maxAxis - originAxis) / directionAxis;
        Vec entryNormal = minNormal;
        Vec exitNormal = maxNormal;

        if (entryT > exitT) {
            double swapT = entryT;
            entryT = exitT;
            exitT = swapT;

            Vec swapNormal = entryNormal;
            entryNormal = exitNormal;
            exitNormal = swapNormal;
        }

        return new AxisInterval(entryT, exitT, entryNormal, exitNormal);
    }

    private record AxisInterval(double entryT, double exitT, Vec entryNormal, Vec exitNormal) {
    }

    /**
     * Check if this ray hits some shape.
     * <p>
     * If you're checking an {@link Entity}, use {@link Ray#cast(Shape, Point)} with its position.
     *
     * @param shape the shape to check against
     * @param <S>   any Shape - for example, a {@link BoundingBox}
     * @return an {@link RayIntersection} if one is found between this ray and the shape, and null otherwise
     */
    public <S extends Shape> @Nullable RayIntersection<S> cast(S shape) {
        return cast(shape, Vec.ZERO);
    }

    /**
     * Get an <b>unordered</b> list of collisions with shapes.
     * <p>
     * If you need to know which collisions happened first, use {@link #castSorted(Collection)} or {@link Collections#min(Collection)}.
     *
     * @param shapes the shapes to check against
     * @param <S>    any Shape - for example, an {@link net.minestom.server.entity.Entity} or {@link BoundingBox}
     * @return a list of results, possibly empty
     */
    public <S extends Shape> List<RayIntersection<S>> cast(Collection<? extends S> shapes) {
        ArrayList<RayIntersection<S>> result = new ArrayList<>(shapes.size());
        for (S e : shapes) {
            RayIntersection<S> r = cast(e);
            if (r != null) result.add(r);
        }
        return result;
    }

    /**
     * Get an ordered list of collisions with shapes, starting with the closest to the ray start.
     *
     * @param shapes the shapes to check against
     * @param <S>    any Shape - for example, a {@link BoundingBox}
     * @return a list of results, possibly empty
     */
    public <S extends Shape> List<RayIntersection<S>> castSorted(Collection<? extends S> shapes) {
        ArrayList<RayIntersection<S>> result = new ArrayList<>(shapes.size());
        for (S e : shapes) {
            RayIntersection<S> r = cast(e);
            if (r != null) result.add(r);
        }
        result.sort(null);
        return result;
    }

    /**
     * Get the closest collision to the ray's start.
     *
     * @param shapes the shapes to check against
     * @param <S>    any Shape - for example, a{@link BoundingBox}
     * @return the closest result or null if there is none
     */
    public <S extends Shape> @Nullable RayIntersection<S> findFirst(Collection<? extends S> shapes) {
        RayIntersection<S> best = null;
        double bestT = distance();
        for (S e : shapes) {
            RayIntersection<S> r = cast(e);
            if (r != null && r.distance() <= bestT) {
                best = r;
                bestT = r.distance();
            }
        }
        return best;
    }

    /**
     * Get an <b>unordered</b> list of collisions with entities.
     * <p>
     * If you need to know which collisions happened first, use {@link #entitiesSorted(Collection)} or {@link Collections#min(Collection)}.
     *
     * @param entities the entities to check against
     * @param <E>      any Entity - if you're using {@link net.minestom.server.instance.EntityTracker}, you might use {@link net.minestom.server.entity.Player}
     * @return a list of results, possibly empty
     */
    public <E extends Entity> List<RayIntersection<E>> entities(Collection<? extends E> entities) {
        ArrayList<RayIntersection<E>> result = new ArrayList<>(entities.size());
        for (E e : entities) {
            RayIntersection<E> r = cast(e, e.getPosition());
            if (r != null) result.add(r);
        }
        return result;
    }

    /**
     * Get an ordered list of collisions with entities, starting with the closest to the ray start.
     *
     * @param entities the entities to check against
     * @param <E>      any Entity - if you're using {@link net.minestom.server.instance.EntityTracker}, you might use {@link net.minestom.server.entity.Player}
     * @return a list of results, possibly empty
     */
    public <E extends Entity> List<RayIntersection<E>> entitiesSorted(Collection<? extends E> entities) {
        ArrayList<RayIntersection<E>> result = new ArrayList<>(entities.size());
        for (E e : entities) {
            RayIntersection<E> r = cast(e, e.getPosition());
            if (r != null) result.add(r);
        }
        result.sort(null);
        return result;
    }

    /**
     * Get the closest entity collision to the ray's start.
     *
     * @param entities the entities to check against
     * @param <E>      any Entity - if you're using {@link EntityTracker}, you might use {@link net.minestom.server.entity.Player}
     * @return the closest result or null if there is none
     */
    public <E extends Entity> @Nullable RayIntersection<E> firstEntity(Collection<? extends E> entities) {
        RayIntersection<E> best = null;
        double bestT = distance();
        for (E e : entities) {
            RayIntersection<E> r = cast(e, e.getPosition());
            if (r != null && r.distance() <= bestT) {
                best = r;
                bestT = r.distance();
            }
        }
        return best;
    }

    /**
     * Gets a {@link BlockIterator} along this ray.
     *
     * @return a {@link BlockIterator}
     */
    public BlockIterator blockIterator() {
        return new BlockIterator(start.asVec(), direction, 0, distance);
    }

    /**
     * Gets a {@link RayBlockFinder} along this ray.
     * <p>
     * This is useful if you need only the first hit point, for instance, as it does not perform merging.
     *
     * @param blockGetter the provider for blocks, such as an {@link net.minestom.server.instance.Instance} or {@link net.minestom.server.instance.Chunk}
     * @return a {@link RayBlockFinder}
     */
    public RayBlockFinder findBlocks(Block.Getter blockGetter) {
        return new RayBlockFinder(this, blockIterator(), blockGetter, RayBlockFinder.SOLID_BLOCK_HITBOXES);
    }

    /**
     * Gets a {@link RayBlockFinder} along this ray.
     * <p>
     * This is useful if you need only the first hit point, for instance, as it does not perform merging.
     *
     * @param blockGetter  the provider for blocks, such as an {@link net.minestom.server.instance.Instance} or {@link net.minestom.server.instance.Chunk}
     * @param hitboxGetter a function that gets bounding boxes from a block
     *                     <p>
     *                     {@link RayBlockFinder} provides some options, and {@link RayBlockFinder#SOLID_BLOCK_HITBOXES} is the default.
     * @return a {@link RayBlockFinder}
     */
    public RayBlockFinder findBlocks(Block.Getter blockGetter, Function<Block, Collection<BoundingBox>> hitboxGetter) {
        return new RayBlockFinder(this, blockIterator(), blockGetter, hitboxGetter);
    }

    /**
     * Gets a {@link RayBlockQueue} along this ray.
     * <p>
     * These can perform merging. They are useful if you need exit points from blocks.
     *
     * @param blockGetter the provider for blocks, such as an {@link net.minestom.server.instance.Instance} or {@link net.minestom.server.instance.Chunk}
     * @return a {@link RayBlockQueue}
     */
    public RayBlockQueue blockQueue(Block.Getter blockGetter) {
        return new RayBlockQueue(findBlocks(blockGetter));
    }

    /**
     * Gets a {@link RayBlockQueue} along this ray.
     * <p>
     * These can perform merging. They are useful if you need exit points from blocks.
     *
     * @param blockGetter  the provider for blocks, such as an {@link net.minestom.server.instance.Instance} or {@link net.minestom.server.instance.Chunk}
     * @param hitboxGetter a function that gets bounding boxes from a block
     *                     <p>
     *                     {@link RayBlockFinder} provides some options, and {@link RayBlockFinder#SOLID_BLOCK_HITBOXES} is the default.
     * @return a {@link RayBlockQueue}
     */
    public RayBlockQueue blockQueue(Block.Getter blockGetter, Function<Block, Collection<BoundingBox>> hitboxGetter) {
        return new RayBlockQueue(findBlocks(blockGetter, hitboxGetter));
    }

    /**
     * Gets the end point of this ray with some data that may or may not be useful.
     *
     * @return the end point as a result
     */
    public RayIntersection<Ray> endPoint() {
        Point endPoint = start.add(direction.mul(distance));
        return new RayIntersection<>(distance, endPoint, direction.neg(), distance, endPoint, direction, this);
    }
}