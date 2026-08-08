package net.swofty.type.generic.resourcepack;

import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.config.Settings;
import net.swofty.type.generic.user.HypixelPlayer;

public interface HypixelResourcePack {
    record PackInfo(String url, String hash) {
    }

    String getPackName();
    String getPackUrl();
    String getPackHash();

    default PackInfo getPackFor(HypixelPlayer player) {
        return new PackInfo(getPackUrl(), getPackHash());
    }

    boolean isRequired();

    void initialize();
    void onPlayerJoin(HypixelPlayer player);
    void onPlayerQuit(HypixelPlayer player);

    static Settings.ResourcePackSettings getConfigFor(String packName) {
        return ConfigProvider.settings().getResourcePacks()
                .getOrDefault(packName, new Settings.ResourcePackSettings());
    }
}
