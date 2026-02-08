package net.swofty.packer;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HypixelPackBuilder {
    private static final int PACK_FORMAT = 75;

    private final PackDefinition definition;

    public HypixelPackBuilder(PackDefinition definition) {
        this.definition = definition;
    }

    public BuiltResourcePack build() {
        ResourcePack pack = MinecraftResourcePackReader.minecraft()
                .readFromDirectory(new File(definition.getPackDirectory()));
        pack.packMeta(PACK_FORMAT, "Hypixel");

        addCustomTextures(pack);
        addCustomFontProviders(pack);
        applyLangOverrides(pack);

        return MinecraftResourcePackWriter.minecraft().build(pack);
    }

    private void addCustomTextures(ResourcePack pack) {
        File texturesDirFile = new File(definition.getTexturesDirectory());
        if (!texturesDirFile.exists() || !texturesDirFile.isDirectory()) return;

        File[] files = texturesDirFile.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null) return;

        for (File textureFile : files) {
            pack.texture(Texture.texture(
                    Key.key("hypixel", textureFile.getName()),
                    Writable.file(textureFile)
            ));
        }
    }

    private void addCustomFontProviders(ResourcePack pack) {
        List<FontProvider> customProviders = definition.getFontProviders();
        if (customProviders.isEmpty()) return;

        Key defaultFontKey = Key.key("minecraft", "default");
        Font existingFont = pack.font(defaultFontKey);

        List<FontProvider> providers = new ArrayList<>();
        if (existingFont != null) {
            providers.addAll(existingFont.providers());
        }
        providers.addAll(customProviders);

        pack.font(Font.font(defaultFontKey, providers));
    }

    private void applyLangOverrides(ResourcePack pack) {
        Map<String, String> overrides = definition.getLangOverrides();
        if (overrides.isEmpty()) return;

        Key enUsKey = Key.key("minecraft", "en_us");
        Language existingLang = pack.language(enUsKey);

        Map<String, String> translations;
        if (existingLang != null) {
            translations = new HashMap<>(existingLang.translations());
        } else {
            translations = new HashMap<>();
        }

        translations.putAll(overrides);
        pack.language(Language.language(enUsKey, translations));
    }
}
