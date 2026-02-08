package net.swofty.type.prototypelobby.resourcepack;

import lombok.Getter;
import net.swofty.commons.config.Settings;
import net.swofty.packer.HypixelPackBuilder;
import net.swofty.packer.packs.TestingPackDefinition;
import net.swofty.type.generic.resourcepack.HypixelResourcePack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.prototypelobby.minimap.MinimapManager;
import org.tinylog.Logger;
import team.unnamed.creative.BuiltResourcePack;

public class TestingPack implements HypixelResourcePack {
    private static final TestingPackDefinition DEFINITION = TestingPackDefinition.INSTANCE;

    @Getter
    private final MinimapManager minimapManager = new MinimapManager();

    private final String packUrl;
    private final String packHash;

    public TestingPack(String serverUrl, String hash) {
        this.packHash = hash;
        this.packUrl = serverUrl + "/" + hash + ".zip";
    }

    public static TestingPack fromConfig() {
        Settings.ResourcePackSettings settings = HypixelResourcePack.getConfigFor(DEFINITION.getPackName());

        Logger.info("Building resource pack '{}' from {}...", DEFINITION.getPackName(), DEFINITION.getPackDirectory());
        HypixelPackBuilder builder = new HypixelPackBuilder(DEFINITION);
        BuiltResourcePack built = builder.build();
        Logger.info("Resource pack '{}' built. Hash: {}", DEFINITION.getPackName(), built.hash());

        return new TestingPack(settings.getServerUrl(), built.hash());
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
    public boolean isRequired() {
        return false;
    }

    @Override
    public void initialize() {
        minimapManager.start();
    }

    @Override
    public void onPlayerJoin(HypixelPlayer player) {
    }

    @Override
    public void onPlayerQuit(HypixelPlayer player) {
        minimapManager.disableFor(player);
    }
}
