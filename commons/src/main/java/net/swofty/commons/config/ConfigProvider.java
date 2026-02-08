package net.swofty.commons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
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

    static YamlConfigurationLoader createLoader(final Path source) {
        final ObjectMapper.Factory customFactory = ObjectMapper.factoryBuilder()
                .build();

        return YamlConfigurationLoader.builder()
                .path(source)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(opts -> opts.serializers(build -> build.registerAnnotatedObjects(customFactory)))
                .build();
    }

    static {
        try {
            Logger.info("Loading config...");

            YamlConfigurationLoader loader = createLoader(Path.of("./configuration/config.yml"));

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
