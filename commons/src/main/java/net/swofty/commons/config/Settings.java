package net.swofty.commons.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@Getter
@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class Settings {

	private String hostName = "0.0.0.0";
	private long transferTimeout = 800;

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

	@Comment("Resource pack settings keyed by pack name (e.g. testingpack, bedwarspack)")
	private java.util.Map<String, ResourcePackSettings> resourcePacks = new java.util.HashMap<>();

	@Getter
	@ConfigSerializable
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class LimboSettings {
		private String hostName = "127.0.0.1";
		private int port = 65535;
	}

	@Getter
	@ConfigSerializable
	@NoArgsConstructor
	public static class ResourcePackSettings {
		@Comment("Base URL of the pack server (e.g. http://0.0.0.0:7270)")
		private String serverUrl = "http://127.0.0.1:7270";
	}

	@Getter
	@ConfigSerializable
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class IntegrationSettings {
		@Comment("Whether to enable Spark for performance monitoring")
		private boolean spark = false;

		@Comment("Whether to enable anti-cheat measures")
		private boolean anticheat = false;

		@Comment("Whether to enable ViaVersion for supporting multiple Minecraft versions. This may cause issues of any kind")
		private boolean viaVersion = false;

		private String sentryDsn = "";
	}

}
