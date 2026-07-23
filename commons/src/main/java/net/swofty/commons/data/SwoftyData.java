package net.swofty.commons.data;

import net.swofty.DataAPI;
import net.swofty.api.DataAPIImpl;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.redis.RedisConnectionPool;
import net.swofty.data.format.JsonFormat;
import net.swofty.event.RedisPubSubHandler;
import net.swofty.lock.RedisDistributedLock;
import net.swofty.storage.RedisDataStorage;
import redis.clients.jedis.JedisPool;

public final class SwoftyData {
    private static JedisPool pool;
    private static RedisDistributedLock lock;
    private static DataAPI account;
    private static DataAPI profile;

    private SwoftyData() {}

    public static synchronized void bootstrap(String redisUri) {
        if (account != null) return;
        pool = RedisConnectionPool.connect(redisUri, RedisConnectionPool.Settings.standard());
        lock = new RedisDistributedLock(pool);
        account = new DataAPIImpl(new RedisDataStorage(pool, "hsb:acct"), new JsonFormat(),
                new RedisPubSubHandler(pool, "hsb-acct"), true, lock);
        profile = new DataAPIImpl(new RedisDataStorage(pool, "hsb:prof"), new JsonFormat(),
                new RedisPubSubHandler(pool, "hsb-prof"), true, lock);
    }

    private static void ensureBootstrapped() {
        if (account == null) bootstrap(ConfigProvider.settings().getRedisUri());
    }

    public static DataAPI account() {
        ensureBootstrapped();
        return account;
    }

    public static DataAPI profile() {
        ensureBootstrapped();
        return profile;
    }

    public static RedisDistributedLock lock() {
        ensureBootstrapped();
        return lock;
    }

    public static JedisPool jedisPool() {
        ensureBootstrapped();
        return pool;
    }
}
