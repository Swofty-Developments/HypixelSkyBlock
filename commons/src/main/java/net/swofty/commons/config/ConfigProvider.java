package net.swofty.commons.config;

import de.exlll.configlib.ConfigurationProperties;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigProvider {

    final static ConfigurationProperties.EnvVarResolutionConfiguration envResolution = ConfigurationProperties.EnvVarResolutionConfiguration
        .resolveEnvVarsWithPrefix("HYPIXEL_", false);

    @NotNull
    static final YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
        .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
        .charset(StandardCharsets.UTF_8)
        .setEnvVarResolutionConfiguration(envResolution)
        .build();

    @Getter
    @Setter
    @Accessors(fluent = true)
    private static Settings settings;

    static {
        Logger.info("Loading config...");
        settings(
            YamlConfigurations.update(
                Path.of("./configuration/config.yml"),
                Settings.class,
                properties
            )
        );
    }

}
