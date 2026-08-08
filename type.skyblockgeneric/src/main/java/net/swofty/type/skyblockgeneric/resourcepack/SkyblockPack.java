package net.swofty.type.skyblockgeneric.resourcepack;

import net.swofty.commons.config.Settings;
import net.swofty.packer.HypixelPackBuilder;
import net.swofty.packer.packs.skyblock.SkyblockPackDefinition;
import net.swofty.type.generic.resourcepack.HypixelResourcePack;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;
import team.unnamed.creative.BuiltResourcePack;

import java.util.concurrent.TimeUnit;

public class SkyblockPack implements HypixelResourcePack {
    private static final SkyblockPackDefinition DEFINITION = SkyblockPackDefinition.INSTANCE;

    private final PackInfo defaultPack;
    private final HypixelSkyblockPackApi.Catalog officialPacks;

    private final String packUrl;
    private final String packHash;

    public SkyblockPack(String serverUrl, String hash) {
        this(new PackInfo(serverUrl + "/" + hash + ".zip", hash), null);
    }

    private SkyblockPack(PackInfo defaultPack, HypixelSkyblockPackApi.Catalog officialPacks) {
        this.defaultPack = defaultPack;
        this.officialPacks = officialPacks;
        this.packUrl = defaultPack.url();
        this.packHash = defaultPack.hash();
    }

    public static SkyblockPack fromConfig() {
        Settings.ResourcePackSettings settings = HypixelResourcePack.getConfigFor(DEFINITION.getPackName());

        if (settings.isUseHypixelApi()) {
            try {
                Logger.info("Loading the official Hypixel SkyBlock resource pack metadata...");
                HypixelSkyblockPackApi.Catalog officialPacks = HypixelSkyblockPackApi.fetch(settings.getHypixelApiUrl());
                HypixelSkyblockPackApi.Version latest = officialPacks.latest();
                PackInfo defaultPack = new PackInfo(latest.url(), latest.hash());
                Logger.info("Loaded official Hypixel SkyBlock resource pack format {} with hash {}",
                        latest.packFormat(), latest.hash());
                return new SkyblockPack(defaultPack, officialPacks);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while loading the official Hypixel SkyBlock resource pack", exception);
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to load the official Hypixel SkyBlock resource pack", exception);
            }
        }

        Logger.info("Building resource pack '{}' from {}...", DEFINITION.getPackName(), DEFINITION.getPackDirectory());
        HypixelPackBuilder builder = new HypixelPackBuilder(DEFINITION);
        BuiltResourcePack built = builder.build();
        Logger.info("Resource pack '{}' built. Hash: {}", DEFINITION.getPackName(), built.hash());

        return new SkyblockPack(settings.getServerUrl(), built.hash());
    }

    @Override
    public String getPackName() {
        return DEFINITION.getPackName();
    }

    @Override
    public String getPackUrl() {
        return packUrl;
    }

    @Override
    public String getPackHash() {
        return packHash;
    }

    @Override
    public PackInfo getPackFor(HypixelPlayer player) {
        if (officialPacks == null) {
            return defaultPack;
        }

        try {
            int protocolVersion = player.asProxyPlayer().getVersion().get(3, TimeUnit.SECONDS);
            HypixelSkyblockPackApi.Version version = officialPacks.forProtocol(protocolVersion);
            if (version == null) {
                Logger.warn("No official Hypixel SkyBlock resource pack is available for protocol version {}", protocolVersion);
                return null;
            }
            return new PackInfo(version.url(), version.hash());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            Logger.warn("Interrupted while resolving the official Hypixel SkyBlock resource pack for {}", player.getUsername());
        } catch (Exception exception) {
            Logger.warn("Failed to resolve the official Hypixel SkyBlock resource pack for {}, using the latest pack", player.getUsername());
        }
        return defaultPack;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void onPlayerJoin(HypixelPlayer player) {
    }

    @Override
    public void onPlayerQuit(HypixelPlayer player) {
    }
}
