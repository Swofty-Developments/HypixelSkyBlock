package net.swofty.type.generic.raycast;

import net.minestom.server.instance.block.Block;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiPredicate;

public final class RayBlockQueue extends ArrayDeque<RayIntersection<Block>> {

    public static final BiPredicate<RayIntersection<Block>, RayIntersection<Block>> SAME_BLOCK_TYPE = (i1, i2) -> i1.object().compare(i2.object());

    private final Iterator<Collection<RayIntersection<Block>>> refiller;

    /**
     * Create a queue with the specified refiller.
     *
     * @param refiller something that can provide collisions in steps
     */
    public RayBlockQueue(Iterator<Collection<RayIntersection<Block>>> refiller) {
        this.refiller = refiller;
    }

    /**
     * Refill this queue with zero or more results.
     *
     * @return number of entries added
     */
    public int refill() {
        Collection<RayIntersection<Block>> nextBatch = pollNextBatch();
        if (nextBatch == null) {
            return 0;
        }

        addAll(nextBatch);
        return nextBatch.size();
    }

    private Collection<RayIntersection<Block>> pollNextBatch() {
        if (!refiller.hasNext()) {
            return null;
        }
        return refiller.next();
    }

    /**
     * Keep refilling until something is added or the refiller cannot add anything more.
     *
     * @return number of entries added, zero if the refiller does not have a next element
     */
    public int refillSome() {
        while (true) {
            int result = refill();
            if (result > 0) return result;
            if (!refiller.hasNext()) return 0;
        }
    }

    /**
     * Keep refilling until the refiller does not have a next element.
     *
     * @return number of entries added
     */
    public int refillAll() {
        int added = 0;
        int next;
        while ((next = refill()) > 0 || refiller.hasNext()) {
            added += next;
        }
        return added;
    }

    /**
     * If the first and second elements exist and can merge, merge them, otherwise do nothing.
     *
     * @param predicate a predicate for merging
     * @return whether elements were merged
     */
    public boolean merge(BiPredicate<RayIntersection<Block>, RayIntersection<Block>> predicate) {
        if (size() < 2) {
            return false;
        }

        RayIntersection<Block> first = pollFirst();
        RayIntersection<Block> second = pollFirst();
        if (first == null || second == null) {
            if (second != null) addFirst(second);
            if (first != null) addFirst(first);
            return false;
        }

        if (!first.overlaps(second) || !predicate.test(first, second)) {
            addFirst(second);
            addFirst(first);
            return false;
        }

        addFirst(first.merge(second));
        return true;
    }

    /**
     * If the first and second elements exist and can merge, merge them, otherwise do nothing.
     *
     * @return whether elements were merged
     */
    public boolean merge() {
        return merge((_, _) -> true);
    }

    /**
     * Merges for as long as possible.
     *
     * @param predicate a predicate for merging
     * @return number of times merged
     */
    public int mergeAll(BiPredicate<RayIntersection<Block>, RayIntersection<Block>> predicate) {
        int mergedCount = 0;
        while (merge(predicate)) {
            mergedCount++;
        }
        return mergedCount;
    }

    /**
     * Merges for as long as possible.
     *
     * @return number of times merged
     */
    public int mergeAll() {
        int mergedCount = 0;
        while (merge()) {
            mergedCount++;
        }
        return mergedCount;
    }
}
