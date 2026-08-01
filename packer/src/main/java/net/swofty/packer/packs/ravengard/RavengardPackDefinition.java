package net.swofty.packer.packs.ravengard;

import net.swofty.packer.PackDefinition;
import team.unnamed.creative.font.FontProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RavengardPackDefinition implements PackDefinition {
    public static final RavengardPackDefinition INSTANCE = new RavengardPackDefinition();

    @Override
    public String getPackName() {
        return "testingpack";
    }

    @Override
    public String getPackDirectory() {
        return "configuration/resourcepacks/ravengard";
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
