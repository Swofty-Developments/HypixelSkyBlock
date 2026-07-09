package net.swofty.packer.packs.skyblock;

import net.swofty.packer.PackDefinition;
import net.swofty.packer.packs.ravengard.TestingLangModifier;
import net.swofty.packer.packs.ravengard.TestingTexture;
import team.unnamed.creative.font.FontProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkyblockPackDefinition implements PackDefinition {
    public static final SkyblockPackDefinition INSTANCE = new SkyblockPackDefinition();

    @Override
    public String getPackName() {
        return "skyblockpack";
    }

    @Override
    public String getPackDirectory() {
        return "configuration/resourcepacks/skyblockpack";
    }

    @Override
    public String getTexturesDirectory() {
        return "configuration/resourcepacks/skyblockpack_textures";
    }

    @Override
    public List<FontProvider> getFontProviders() {
        List<FontProvider> providers = new ArrayList<>();
        File texturesDir = new File(getTexturesDirectory());

        for (TestingTexture texture : TestingTexture.values()) {
            if (!texturesDir.exists()) break;
            File textureFile = new File(texturesDir, texture.name().toLowerCase() + ".png");
            if (textureFile.exists()) {
                providers.add(texture.toFontProvider());
            }
        }

        return providers;
    }

    @Override
    public Map<String, String> getLangOverrides() {
        return TestingLangModifier.getOverrides();
    }
}
