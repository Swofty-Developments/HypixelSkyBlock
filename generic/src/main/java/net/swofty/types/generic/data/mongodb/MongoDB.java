package net.swofty.types.generic.data.mongodb;

public interface MongoDB {
    void set(String key, Object value);

    Object get(String key, Object def);

    void insertOrUpdate(String key, Object value);

    boolean remove(String id);

    boolean exists();
}
