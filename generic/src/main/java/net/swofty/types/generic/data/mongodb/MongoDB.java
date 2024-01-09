package net.swofty.types.generic.data.mongodb;

public interface MongoDB {
    MongoDB connect(String connectionString);

    void set(String key, Object value);

    Object get(String key, Object def);

    String getString(String key, String def);

    default void insertOrUpdate(String key, Object value) {
    }

    boolean remove(String id);

    boolean exists();
}
