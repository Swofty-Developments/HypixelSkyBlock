package net.swofty.packer.packs;

import net.swofty.packer.PackDefinition;
import team.unnamed.creative.font.FontProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestingPackDefinition implements PackDefinition {
    public static final TestingPackDefinition INSTANCE = new TestingPackDefinition();

    @Override
    public String getPackName() {
        return "testingpack";
    }

    @Override
    public String getPackDirectory() {
        return "configuration/resourcepacks/testingpack";
    }

    @Override
    public String getTexturesDirectory() {
        return "configuration/resourcepacks/testingpack_textures";
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
