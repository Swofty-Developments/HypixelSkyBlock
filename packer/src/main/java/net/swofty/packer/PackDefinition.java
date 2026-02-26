package net.swofty.packer;

import team.unnamed.creative.font.FontProvider;

import java.util.List;
import java.util.Map;

public interface PackDefinition {
    String getPackName();
    String getPackDirectory();
    String getTexturesDirectory();

    List<FontProvider> getFontProviders();
    Map<String, String> getLangOverrides();
}
