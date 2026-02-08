package net.swofty.packer.packs;

import java.util.HashMap;
import java.util.Map;

public enum TestingLangModifier {
    LOADING_TERRAIN("multiplayer.downloadingTerrain", "\uE000ffffffffffffffffffffffffffffffff\uE000"),
    RECONFIGURING_1("connect.reconfiging", "\uE000ffffffffffffffffffffffffffffffff\uE000"),
    RECONFIGURING_2("connect.reconfiguring", "\uE000fffffffffffffffffffffffffff\uE000"),
    CONNECT_JOINING("connect.joining", "\uE000fffffffffffffffffffffffffff\uE000"),
    ;

    private final String key;
    private final String value;

    TestingLangModifier(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Map<String, String> getOverrides() {
        Map<String, String> overrides = new HashMap<>();
        for (TestingLangModifier modifier : values()) {
            overrides.put(modifier.key, modifier.value);
        }
        return overrides;
    }
}
