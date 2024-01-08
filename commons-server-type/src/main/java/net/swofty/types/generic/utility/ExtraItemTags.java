package net.swofty.types.generic.utility;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExtraItemTags {

    public static final Tag<SkullOwner> SKULL_OWNER = Tag.Structure("SkullOwner", SkullOwner.SERIALIZER);

    public record SkullOwner(@Nullable UUID uuid, @Nullable String name, @Nullable PlayerSkin skin) {
        public static final TagSerializer<SkullOwner> SERIALIZER = new TagSerializer<>() {
            private static final Tag<String> NAME_TAG = Tag.String("Name");
            private static final Tag<UUID> UUID_TAG = Tag.UUID("Id");
            private static final Tag<NBT> PROPERTIES_TAG = Tag.NBT("Properties");

            @Override
            public @Nullable SkullOwner read(@NotNull TagReadable reader) {
                String name = reader.getTag(NAME_TAG);
                if (name == null) return null;

                UUID uuid = reader.getTag(UUID_TAG);
                PlayerSkin skin = null;
                var props = (NBTCompound) reader.getTag(PROPERTIES_TAG);
                if (props != null) {
                    var textures = props.<NBTCompound>getList("textures");
                    if (textures != null && textures.getSize() > 0) {
                        var texture = textures.get(0);
                        var value = texture.getString("Value");
                        var signature = texture.getString("Signature");
                        skin = new PlayerSkin(value, signature);
                    }
                }

                return new SkullOwner(uuid, name, skin);
            }

            @Override
            public void write(@NotNull TagWritable writer, @NotNull SkullOwner value) {
                if (value.name() != null) writer.setTag(NAME_TAG, value.name());
                if (value.uuid() != null) writer.setTag(UUID_TAG, value.uuid());
                if (value.skin != null) writer.setTag(PROPERTIES_TAG, new NBTCompound(Map.of(
                        "textures", new NBTList<>(NBTType.TAG_Compound, List.of(
                                new NBTCompound(Map.of(
                                        "Value", new NBTString(value.skin.textures())
                                ))
                        ))
                )));
            }
        };
    }
}