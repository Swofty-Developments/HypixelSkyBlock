package net.swofty.service.store;

import net.swofty.commons.protocol.objects.proxy.to.StorePurchaseFulfillmentProtocol;
import net.swofty.commons.redis.RedisClient;
import org.tinylog.Logger;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PurchaseFulfillmentWorker {
    private static final StorePurchaseFulfillmentProtocol PROTOCOL = new StorePurchaseFulfillmentProtocol();
    private static final int BATCH_SIZE = 50;
    private static final Duration LEASE_DURATION = Duration.ofSeconds(45);

    private final StoreDatabase database;
    private final String workerId;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "store-fulfillment-scanner");
        thread.setDaemon(true);
        return thread;
    });
    private final ExecutorService fulfillmentExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicBoolean scanning = new AtomicBoolean(false);

    public PurchaseFulfillmentWorker(StoreDatabase database) {
        this.database = database;
        this.workerId = resolveWorkerId();
    }

    public void start() {
        scheduler.scheduleWithFixedDelay(this::scan, 0, 2, TimeUnit.SECONDS);
        Logger.info("Store fulfillment worker {} started", workerId);
    }

    private void scan() {
        if (!scanning.compareAndSet(false, true)) return;
        try {
            var purchases = database.claimDuePurchases(workerId, BATCH_SIZE, LEASE_DURATION);
            for (StorePurchase purchase : purchases) {
                fulfillmentExecutor.submit(() -> fulfill(purchase));
            }
        } catch (Exception exception) {
            Logger.error(exception, "Store fulfillment scan failed");
        } finally {
            scanning.set(false);
        }
    }

    private void fulfill(StorePurchase purchase) {
        try {
            StorePurchaseFulfillmentProtocol.Response response = RedisClient.requestProxy(
                    PROTOCOL,
                    new StorePurchaseFulfillmentProtocol.Request(
                            purchase.id(),
                            purchase.playerUuid(),
                            purchase.playerName(),
                            purchase.productId(),
                            purchase.productName(),
                            purchase.paidAt().toEpochMilli(),
                            purchase.entitlements().stream().map(StoreEntitlement::toProtocol).toList()
                    )
            ).orTimeout(15, TimeUnit.SECONDS).join();

            if (!response.success()) {
                throw new IllegalStateException(response.error() != null ? response.error() : "Proxy rejected purchase");
            }

            database.markFulfilled(purchase);
            Logger.info("Fulfilled store purchase {} for {}", purchase.id(), purchase.playerUuid());
        } catch (Exception exception) {
            database.markFailed(purchase, unwrap(exception));
            Logger.warn("Store purchase {} will retry: {}", purchase.id(), unwrap(exception).getMessage());
        }
    }

    private Throwable unwrap(Throwable throwable) {
        if (throwable.getCause() != null) return throwable.getCause();
        return throwable;
    }

    private String resolveWorkerId() {
        try {
            return InetAddress.getLocalHost().getHostName() + ":" + ManagementFactory.getRuntimeMXBean().getName();
        } catch (Exception ignored) {
            return "store-service:" + UUID.randomUUID();
        }
    }
}
