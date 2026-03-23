package net.swofty.commons.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class Settings {

    @Comment("The host name or IP address to bind the server to")
    private String hostName = "0.0.0.0";

    @Comment("The host or IP address this server should advertise back to the proxy. Leave blank to auto-detect")
    private String advertisedHost = "";

    @Comment("The port this server should advertise back to the proxy")
    private int advertisedPort = 25565;

    @Comment("The MongoDB connection URI")
    private String mongodb = "mongodb://localhost";

    @Comment("The Redis connection URI")
    private String redisUri = "redis://localhost:6379";

    @Comment("The secret key used to authenticate with Velocity proxy")
    private String velocitySecret = "ixmSUgWOgvs7";

    private boolean requireAuth = false;

    @Comment("Whether to enable sandbox features (such as editing items)")
    private boolean sandbox = false;

    @Comment("Integrations with services")
    private IntegrationSettings integrations = new IntegrationSettings();

    @Comment("Settings related to configuration of Limbo server connections")
    private LimboSettings limbo = new LimboSettings();

    @Comment("Management endpoint settings used for Kubernetes probes and metrics scraping")
    private ManagementSettings management = new ManagementSettings();

    @Comment("Resource pack settings keyed by pack name (e.g. testingpack, bedwarspack)")
    private Map<String, ResourcePackSettings> resourcePacks = new HashMap<>();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LimboSettings {
        private String hostName = "127.0.0.1";
        private int port = 65535;
    }

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ManagementSettings {
        @Comment("Whether to expose HTTP management endpoints such as /healthz, /readyz and /metrics")
        private boolean enabled = true;

        @Comment("The host name or IP address to bind management endpoints to")
        private String hostName = "0.0.0.0";

        @Comment("The TCP port to bind management endpoints to")
        private int port = 9090;
    }

    @Getter
    @Configuration
    @NoArgsConstructor
    public static class ResourcePackSettings {
        @Comment("Base URL of the pack server (e.g. http://0.0.0.0:7270)")
        private String serverUrl = "http://127.0.0.1:7270";
    }

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class IntegrationSettings {
        @Comment("Whether to enable Spark for performance monitoring")
        private boolean spark = false;

        @Comment("Whether to enable anti-cheat measures")
        private boolean anticheat = false;

        @Comment("Whether to enable ViaVersion for supporting multiple Minecraft versions. This may cause issues of any kind")
        private boolean viaVersion = false;

        @Comment("The DSN to use for Sentry error tracking. If empty, Sentry will be disabled")
        private String sentryDsn = "";
    }

}
