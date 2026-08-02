package net.swofty.type.ravengardgeneric.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The rotating stock. Every fifteen minute cycle each shop rolls a fresh shelf from its pool,
 * seeded by the cycle number so the roll is stable within the cycle, and purchases run the
 * shelf entry down until it greys out in place the way the captures show. Quantities per entry
 * are not observable from captures; one each matches how quickly the live shelves sell out.
 */
public final class RavengardShopStock {
    public static final long CYCLE_MILLIS = 15 * 60 * 1000;
    private static final int STOCK_PER_ENTRY = 1;

    private static final Map<String, Shelf> SHELVES = new ConcurrentHashMap<>();

    private RavengardShopStock() {
    }

    public static long cycle() {
        return System.currentTimeMillis() / CYCLE_MILLIS;
    }

    public static long untilRefreshMillis() {
        return CYCLE_MILLIS - (System.currentTimeMillis() % CYCLE_MILLIS);
    }

    public static String refreshText() {
        long millis = untilRefreshMillis();
        return (millis / 60000) + "m " + (millis / 1000 % 60) + "s";
    }

    public static synchronized Shelf shelf(RavengardShop shop) {
        long current = cycle();
        Shelf shelf = SHELVES.get(shop.id());
        if (shelf == null || shelf.cycle() != current) {
            shelf = roll(shop, current);
            SHELVES.put(shop.id(), shelf);
        }
        return shelf;
    }

    private static Shelf roll(RavengardShop shop, long cycle) {
        List<RavengardShop.PoolEntry> pool = new ArrayList<>(shop.pool());
        Collections.shuffle(pool, new Random(Objects.hash(shop.id(), cycle)));

        List<Entry> entries = new ArrayList<>();
        List<Integer> slots = shop.shelfSlots();
        for (int index = 0; index < slots.size() && !pool.isEmpty(); index++) {
            // pools smaller than the shelf repeat, the way the alchemist's five slots carry
            // duplicates of its three items
            RavengardShop.PoolEntry picked = pool.get(index % pool.size());
            entries.add(new Entry(slots.get(index), picked.item(), picked.price(), STOCK_PER_ENTRY));
        }
        return new Shelf(cycle, List.copyOf(entries));
    }

    public record Shelf(long cycle, List<Entry> entries) {
        public Entry at(int slot) {
            for (Entry entry : entries) {
                if (entry.slot() == slot) {
                    return entry;
                }
            }
            return null;
        }
    }

    public static final class Entry {
        private final int slot;
        private final String item;
        private final int price;
        private int remaining;

        Entry(int slot, String item, int price, int remaining) {
            this.slot = slot;
            this.item = item;
            this.price = price;
            this.remaining = remaining;
        }

        public int slot() {
            return slot;
        }

        public String item() {
            return item;
        }

        public int price() {
            return price;
        }

        public synchronized boolean inStock() {
            return remaining > 0;
        }

        public synchronized boolean take() {
            if (remaining <= 0) {
                return false;
            }
            remaining--;
            return true;
        }
    }
}
