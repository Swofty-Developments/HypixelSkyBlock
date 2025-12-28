package net.swofty.service.bazaar;

import net.swofty.commons.skyblock.bazaar.BuyOrderRefundTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

public class BazaarMarket {
    private static final BazaarMarket INSTANCE = new BazaarMarket();
    public static BazaarMarket get() { return INSTANCE; }

    private static final double TAX_RATE = 0.0125;
    private static final long EXPIRE_DAYS = 7;

    private final BazaarPropagator propagator = new BazaarPropagator();
    private final Map<String, PriorityQueue<LimitOrder>> buyBook = new ConcurrentHashMap<>();
    private final Map<String, PriorityQueue<LimitOrder>> sellBook = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Track order refund information
    private final Map<UUID, OrderRefundTracker> refundTrackers = new ConcurrentHashMap<>();

    private BazaarMarket() {
        scheduler.scheduleAtFixedRate(this::expireOldOrders, 1, 1, TimeUnit.HOURS);
    }

    public static class LimitOrder {
        public enum Side { BUY, SELL }
        public final UUID orderId;
        public final UUID owner;
        public final UUID profileUuid;
        public final Side side;
        public final double originalPrice;  // Original bid/ask price
        public double remaining;
        public final Instant ts;

        public LimitOrder(UUID owner, UUID profileUuid, Side side, double price, double qty) {
            this(UUID.randomUUID(), owner, profileUuid, side, price, qty, Instant.now());
        }

        LimitOrder(UUID id, UUID owner, UUID profileUuid, Side side, double price, double qty, Instant ts) {
            this.orderId = id;
            this.owner = owner;
            this.profileUuid = profileUuid;
            this.side = side;
            this.originalPrice = price;
            this.remaining = qty;
            this.ts = ts;
        }
    }

    // Track refunds for each buy order
    private static class OrderRefundTracker {
        public final UUID orderId;
        public final String itemName;
        public final UUID owner;
        public final UUID profileUuid;
        public final double originalPricePerUnit;
        public final double originalQuantity;
        public double totalSpent = 0.0;
        public double quantityFilled = 0.0;
        public double accumulatedRefunds = 0.0;

        public OrderRefundTracker(LimitOrder order, String itemName) {
            this.orderId = order.orderId;
            this.itemName = itemName;
            this.owner = order.owner;
            this.profileUuid = order.profileUuid;
            this.originalPricePerUnit = order.originalPrice;
            this.originalQuantity = order.remaining;
        }

        public void recordPartialFill(double quantity, double actualPrice) {
            quantityFilled += quantity;
            totalSpent += quantity * actualPrice;

            // Calculate price improvement refund
            double priceImprovement = (originalPricePerUnit - actualPrice) * quantity;
            if (priceImprovement > 0) {
                accumulatedRefunds += priceImprovement;
            }
        }

        public double getRemainingQuantity() {
            return originalQuantity - quantityFilled;
        }

        public double getExpiryRefund() {
            return getRemainingQuantity() * originalPricePerUnit;
        }

        public double getTotalRefundOwed() {
            return accumulatedRefunds + (getRemainingQuantity() > 0 ? getExpiryRefund() : 0);
        }
    }

    void injectLoadedOrder(String item, LimitOrder lo) {
        var book = (lo.side == LimitOrder.Side.BUY ? buyBook : sellBook)
                .computeIfAbsent(item, k -> newBook(lo.side));
        book.add(lo);

        // For buy orders, create/restore refund tracker
        if (lo.side == LimitOrder.Side.BUY) {
            refundTrackers.put(lo.orderId, new OrderRefundTracker(lo, item));
        }
    }

    public void submitBuy(String item, UUID buyer, UUID buyerProfile, double price, double qty) {
        var order = new LimitOrder(buyer, buyerProfile, LimitOrder.Side.BUY, price, qty);
        refundTrackers.put(order.orderId, new OrderRefundTracker(order, item));
        submitOrder(item, order);
    }

    public void submitSell(String item, UUID seller, UUID sellerProfile, double price, double qty) {
        submitOrder(item, new LimitOrder(seller, sellerProfile, LimitOrder.Side.SELL, price, qty));
    }

    public void submitDelete(UUID orderId, UUID player, UUID profile) {
        // Handle refund for cancelled buy order
        OrderRefundTracker tracker = refundTrackers.remove(orderId);
        if (tracker != null && tracker.accumulatedRefunds > 0) {
            // Issue refund for price improvements already accumulated
            var refundTx = new BuyOrderRefundTransaction(
                    orderId, tracker.itemName, tracker.owner, tracker.profileUuid,
                    tracker.accumulatedRefunds, "CANCELLED", Instant.now()
            );
            propagator.propagate(refundTx);
        }

        OrderRepository.delete(orderId);
        buyBook.values().forEach(q -> q.removeIf(o -> o.orderId.equals(orderId)));
        sellBook.values().forEach(q -> q.removeIf(o -> o.orderId.equals(orderId)));
    }

    private void submitOrder(String item, LimitOrder incoming) {
        var buys = buyBook.computeIfAbsent(item, k -> newBook(LimitOrder.Side.BUY));
        var sells = sellBook.computeIfAbsent(item, k -> newBook(LimitOrder.Side.SELL));

        OrderRepository.saveNew(incoming, item);
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
                ? Comparator.<LimitOrder>comparingDouble(o -> -o.originalPrice).thenComparing(o -> o.ts)
                : Comparator.<LimitOrder>comparingDouble(o -> o.originalPrice).thenComparing(o -> o.ts);
        return new PriorityQueue<>(cmp);
    }

    private void match(String item,
                       PriorityQueue<LimitOrder> buys,
                       PriorityQueue<LimitOrder> sells) {
        while (!buys.isEmpty() && !sells.isEmpty()) {
            // peek the top buy
            LimitOrder b = buys.peek();

            // 1) Pull off any sell orders from the same owner
            List<LimitOrder> skippedSells = new ArrayList<>();
            LimitOrder s = null;
            while (!sells.isEmpty()) {
                LimitOrder top = sells.peek();
                if (top.owner.equals(b.owner)) {
                    // same person → skip for now
                    skippedSells.add(sells.poll());
                } else {
                    s = top;
                    break;
                }
            }

            // 2) If no eligible sell left, or prices no longer cross, restore and stop
            if (s == null || b.originalPrice < s.originalPrice) {
                sells.addAll(skippedSells);
                break;
            }

            // 3) We’ve found a valid counter-party: remove that sell and put back the rest
            sells.poll();
            sells.addAll(skippedSells);

            // 4) Execute your normal matching logic
            double qty         = Math.min(b.remaining, s.remaining);
            double actualPrice = s.originalPrice;
            double gross       = actualPrice * qty;
            double tax         = gross * TAX_RATE;

            // Track partial fills/refunds for the buy side
            OrderRefundTracker tracker = refundTrackers.get(b.orderId);
            if (tracker != null) {
                tracker.recordPartialFill(qty, actualPrice);
            }

            System.out.println("Buy price was: " + b.originalPrice);
            System.out.println("Sell price was: " + s.originalPrice);
            System.out.println("Actual price was: " + actualPrice);
            double priceImprovement = (b.originalPrice - actualPrice) * qty;
            var tx = new SuccessfulBazaarTransaction(
                    item,
                    b.owner, b.profileUuid,
                    s.owner, s.profileUuid,
                    actualPrice, b.originalPrice,
                    qty, tax, priceImprovement,
                    b.orderId, s.orderId,
                    Instant.now()
            );
            propagator.propagate(tx);

            // Update quantities and repository
            b.remaining -= qty;
            s.remaining -= qty;

            if (b.remaining <= 0) {
                buys.poll();
                OrderRepository.delete(b.orderId);
                if (tracker != null && tracker.accumulatedRefunds > 0) {
                    var refundTx = new BuyOrderRefundTransaction(
                            b.orderId, item, b.owner, b.profileUuid,
                            tracker.accumulatedRefunds, "COMPLETED", Instant.now()
                    );
                    propagator.propagate(refundTx);
                    refundTrackers.remove(b.orderId);
                }
            } else {
                OrderRepository.updateRemaining(b.orderId, b.remaining);
            }

            if (s.remaining <= 0) {
                OrderRepository.delete(s.orderId);
            } else {
                OrderRepository.updateRemaining(s.orderId, s.remaining);
            }
        }
    }

    private void expireOldOrders() {
        Instant cutoff = Instant.now().minus(EXPIRE_DAYS, ChronoUnit.DAYS);
        expireSide(buyBook, cutoff);
        expireSide(sellBook, cutoff);
    }

    private void expireSide(Map<String, PriorityQueue<LimitOrder>> book, Instant cutoff) {
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

                if (o.side == LimitOrder.Side.BUY) {
                    // Handle buy order expiry with accumulated refunds
                    OrderRefundTracker tracker = refundTrackers.remove(o.orderId);
                    if (tracker != null) {
                        double totalRefund = tracker.getTotalRefundOwed();
                        if (totalRefund > 0) {
                            var refundTx = new BuyOrderRefundTransaction(
                                    o.orderId, item, o.owner, o.profileUuid,
                                    totalRefund, "EXPIRED", Instant.now()
                            );
                            propagator.propagate(refundTx);
                        }
                    }
                } else {
                    // Sell order expiry - return items
                    var evt = new OrderExpiredBazaarTransaction(
                            o.orderId, item, o.owner, o.profileUuid, o.side.name(),
                            o.originalPrice, o.remaining, Instant.now()
                    );
                    propagator.propagate(evt);
                }
            }
        }
    }
}