package net.swofty.velocity.data;

public interface MongoDB {
    MongoDB connect(String connectionString);

    void set(String key, Object value);

    Object get(String key, Object def);

    void insertOrUpdate(String key, Object value);

    boolean remove(String id);

    boolean exists();
}
