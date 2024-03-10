package net.swofty.packer;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum SkyBlockTexture {
    FULL_SCREEN_BLACK("\uE000", IntendedLocation.TITLE, 200, 600),
    VILLAGER_SPEAK_OUTLINE("\uE001", IntendedLocation.TITLE, -10, 20),
    HUD_BACK_PLATE("\uE002", IntendedLocation.ACTIONBAR, -25, 10),
    ;

    @Getter
    public final String unicode;
    private final IntendedLocation intendedLocation;
    public final int ascent;
    public final int height;

    SkyBlockTexture(String unicode, IntendedLocation intendedLocation, int ascent, int height) {
        this.unicode = unicode;
        this.intendedLocation = intendedLocation;
        this.ascent = ascent;
        this.height = height;
    }

    @Override
    public String toString() {
        return unicode;
    }

    public static JSONObject updateTextures(String defaultJson) {
        JSONObject json = new JSONObject(defaultJson);

        List<JSONObject> providerList = new ArrayList<>();

        json.getJSONArray("providers").forEach(provider -> {
            JSONObject providerJson = (JSONObject) provider;
            providerList.add(providerJson);
        });

        for (SkyBlockTexture texture : values()) {
            JSONObject textureJson = new JSONObject();
            textureJson.put("type", "bitmap");
            textureJson.put("file", "skyblock:" + texture.name().toLowerCase() + ".png");
            textureJson.put("height", texture.height);
            textureJson.put("ascent", texture.ascent);

            StringBuilder unicode = new StringBuilder();
            unicode.append("\\u");
            unicode.append(Integer.toHexString(texture.unicode.codePointAt(0)).toUpperCase());

            // Convert unicode to traditional backslashXXXX format
            textureJson.put("chars", new String[] {
                    unicode.toString()
            });

            providerList.add(textureJson);
        }


        JSONObject providerJson = new JSONObject();
        providerJson.put("providers", new JSONArray(providerList.toArray()));

        return providerJson;
    }

    enum IntendedLocation {
        TITLE,
        BOSSBAR,
        ACTIONBAR
    }
}
