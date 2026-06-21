package net.swofty.service.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MongoDB {
    @NotNull
    MongoDB connect(@NotNull String connectionString);

    void set(@NotNull String key, @Nullable Object value);

    @Nullable
    Object get(@NotNull String key, @Nullable Object def);

    void insertOrUpdate(@NotNull String key, @Nullable Object value);

    boolean remove(@NotNull String id);

    boolean exists();
}