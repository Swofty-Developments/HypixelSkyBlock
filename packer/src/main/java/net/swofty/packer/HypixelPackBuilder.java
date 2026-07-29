package net.swofty.packer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.intellij.lang.annotations.Subst;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.metadata.pack.FormatVersion;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HypixelPackBuilder {
    private static final FormatVersion FORMAT_VERSION = FormatVersion.of(FormatVersion.FORMAT_26_1);

    private final PackDefinition definition;

    public HypixelPackBuilder(PackDefinition definition) {
        this.definition = definition;
    }

    public BuiltResourcePack build() {
        File packDirectory = new File(definition.getPackDirectory()).getAbsoluteFile();
        if (!packDirectory.isDirectory()) {
            throw new IllegalStateException("Pack directory does not exist: " + packDirectory.getPath());
        }

        ResourcePack pack = MinecraftResourcePackReader.minecraft()
                .readFromDirectory(packDirectory);
        pack.packMeta(PackMeta.of(PackFormat.format(FORMAT_VERSION, FORMAT_VERSION), Component.text("Hypixel")));

        addCustomTextures(pack);
        addCustomFontProviders(pack);
        applyLangOverrides(pack);

        return MinecraftResourcePackWriter.minecraft().build(pack);
    }

    private void addCustomTextures(ResourcePack pack) {
        File texturesDirFile = new File(definition.getTexturesDirectory());
        if (!texturesDirFile.exists() || !texturesDirFile.isDirectory()) return;

        File[] files = texturesDirFile.listFiles((_, name) -> name.endsWith(".png"));
        if (files == null) return;
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File textureFile : files) {
            if (!textureFile.exists() || !textureFile.isFile()) continue;
            @Subst("fallback") String name = textureFile.getName();
            pack.texture(Texture.texture(
                Key.key("hypixel", name),
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
            translations = new TreeMap<>(existingLang.translations());
        } else {
            translations = new TreeMap<>();
        }

        translations.putAll(overrides);
        pack.language(Language.language(enUsKey, new LinkedHashMap<>(translations)));
    }

}
