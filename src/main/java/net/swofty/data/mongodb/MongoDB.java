package net.swofty.data.mongodb;

import java.util.List;

public interface MongoDB {
    MongoDB connect(String connectionString);

    void set(String key, Object value);

    Object get(String key, Object def);

    String getString(String key, String def);

    default void insertOrUpdate(String key, Object value) { }

    int getInt(String key, int def);

    long getLong(String key, long def);

    boolean getBoolean(String key, boolean def);

    <T> List<T> getList(String key, Class<T> t);

    boolean remove(String id);

    boolean exists();
}
