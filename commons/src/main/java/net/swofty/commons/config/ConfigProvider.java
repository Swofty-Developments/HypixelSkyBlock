package net.swofty.commons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigProvider {

    @Getter
    @Setter
    @Accessors(fluent = true)
    private static Settings settings;

    static {
        try {
            Logger.info("Loading config...");

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(Path.of("./configuration/config.yml"))
                    .nodeStyle(NodeStyle.BLOCK)
                    .build();

            CommentedConfigurationNode root = loader.load();
            CommentedConfigurationNode defaults = loader.createNode();
            defaults.set(Settings.class, new Settings());
            root.mergeFrom(defaults);

            Settings loaded = root.get(Settings.class);
            if (loaded == null) {
                loaded = new Settings();
            }

            loader.save(root);
            settings(loaded);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

}
