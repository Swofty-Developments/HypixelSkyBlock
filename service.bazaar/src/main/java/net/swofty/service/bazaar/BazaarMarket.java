package net.swofty.service.bazaar;

import net.swofty.commons.bazaar.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

public class BazaarMarket {
    private static final BazaarMarket INSTANCE = new BazaarMarket();
    public  static BazaarMarket get() { return INSTANCE; }

    // 1.25% tax by default
    private static final double TAX_RATE = 0.0125;
    private static final long   EXPIRE_DAYS = 7;

    private final BazaarPropagator propagator = new BazaarPropagator();

    // buy: highest first; sell: lowest first
    private final Map<String, PriorityQueue<LimitOrder>> buyBook  = new ConcurrentHashMap<>();
    private final Map<String, PriorityQueue<LimitOrder>> sellBook = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private BazaarMarket() {
        // load persisted orders
        OrderRepository.loadAll();

        // schedule expiration check
        scheduler.scheduleAtFixedRate(this::expireOldOrders,
                1, 1, TimeUnit.HOURS);
    }

    public static class LimitOrder {
        public enum Side { BUY, SELL }
        public final UUID    orderId;
        public final UUID    owner;
        public final Side    side;
        public final double  price;
        public double        remaining;
        public final Instant ts;

        // for new orders:
        public LimitOrder(UUID owner, Side side, double price, double qty) {
            this(UUID.randomUUID(), owner, side, price, qty, Instant.now());
        }
        // for DB‐loaded orders:
        LimitOrder(UUID id, UUID owner, Side side, double price, double qty, Instant ts) {
            this.orderId   = id;
            this.owner     = owner;
            this.side      = side;
            this.price     = price;
            this.remaining = qty;
            this.ts        = ts;
        }
    }

    /** called during loadAll() to inject orders from DB */
    void injectLoadedOrder(String item, LimitOrder lo) {
        var book = (lo.side == LimitOrder.Side.BUY ? buyBook : sellBook)
                .computeIfAbsent(item, k -> newBook(lo.side));
        book.add(lo);
    }

    public void submitBuy(String item, UUID buyer, double price, double qty) {
        submitOrder(item, new LimitOrder(buyer, LimitOrder.Side.BUY, price, qty));
    }
    public void submitSell(String item, UUID seller, double price, double qty) {
        submitOrder(item, new LimitOrder(seller, LimitOrder.Side.SELL, price, qty));
    }

    private void submitOrder(String item, LimitOrder incoming) {
        var buys  = buyBook.computeIfAbsent(item, k -> newBook(LimitOrder.Side.BUY));
        var sells = sellBook.computeIfAbsent(item, k -> newBook(LimitOrder.Side.SELL));

        // persist new
        OrderRepository.saveNew(incoming, item);

        // enqueue then match
        (incoming.side == LimitOrder.Side.BUY ? buys : sells).add(incoming);
        match(item, buys, sells);
    }

    public List<LimitOrder> getBuyOrders(String item) {
        var buyBook = this.buyBook.get(item);
        return buyBook != null ? new ArrayList<>(buyBook) : new ArrayList<>();
    }

    public List<LimitOrder> getSellOrders(String item) {
        var sellBook = this.sellBook.get(item);
        return sellBook != null ? new ArrayList<>(sellBook) : new ArrayList<>();
    }

    private PriorityQueue<LimitOrder> newBook(LimitOrder.Side side) {
        Comparator<LimitOrder> cmp = side == LimitOrder.Side.BUY
                ? Comparator.<LimitOrder>comparingDouble(o -> -o.price)
                .thenComparing(o -> o.ts)
                : Comparator.<LimitOrder>comparingDouble(o -> o.price)
                .thenComparing(o -> o.ts);
        return new PriorityQueue<>(cmp);
    }

    /** Core matching + tax + DB updates + emit SuccessfulBazaarTransaction */
    private void match(String item,
                       PriorityQueue<LimitOrder> buys,
                       PriorityQueue<LimitOrder> sells) {
        while (!buys.isEmpty() && !sells.isEmpty()) {
            LimitOrder b = buys.peek();
            LimitOrder s = sells.peek();
            if (b.price < s.price) break;

            double qty   = Math.min(b.remaining, s.remaining);
            double price = s.price;
            double gross = price * qty;
            double tax   = gross * TAX_RATE;

            // emit
            var tx = new SuccessfulBazaarTransaction(
                    item, b.owner, s.owner, price, qty, tax, Instant.now()
            );
            propagator.propagate(tx);

            // decrement
            b.remaining -= qty;
            s.remaining -= qty;

            // update or delete in DB
            if (b.remaining <= 0) {
                buys.poll();
                OrderRepository.delete(b.orderId);
            } else {
                OrderRepository.updateRemaining(b.orderId, b.remaining);
            }
            if (s.remaining <= 0) {
                sells.poll();
                OrderRepository.delete(s.orderId);
            } else {
                OrderRepository.updateRemaining(s.orderId, s.remaining);
            }
        }
    }

    /** scan all books for orders older than 7 days */
    private void expireOldOrders() {
        Instant cutoff = Instant.now().minus(EXPIRE_DAYS, ChronoUnit.DAYS);
        expireSide(buyBook,  cutoff);
        expireSide(sellBook, cutoff);
    }

    private void expireSide(Map<String,PriorityQueue<LimitOrder>> book, Instant cutoff) {
        for (var entry : book.entrySet()) {
            String item = entry.getKey();
            var pq = entry.getValue();

            List<LimitOrder> toExpire = new ArrayList<>();
            for (var o : pq) {
                if (o.ts.isBefore(cutoff)) toExpire.add(o);
            }
            for (var o : toExpire) {
                pq.remove(o);
                OrderRepository.delete(o.orderId);

                var evt = new OrderExpiredBazaarTransaction(
                        o.orderId, item, o.owner, o.side.name(), o.remaining, Instant.now()
                );
                propagator.propagate(evt);
            }
        }
    }
}