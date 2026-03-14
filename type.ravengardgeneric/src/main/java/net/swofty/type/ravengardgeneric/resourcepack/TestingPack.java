package net.swofty.type.ravengardgeneric.resourcepack;

import lombok.Getter;
import net.swofty.commons.config.Settings;
import net.swofty.packer.HypixelPackBuilder;
import net.swofty.packer.packs.TestingPackDefinition;
import net.swofty.type.generic.resourcepack.HypixelResourcePack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackManager;
import org.tinylog.Logger;
import team.unnamed.creative.BuiltResourcePack;

public class TestingPack implements HypixelResourcePack {
    private static final TestingPackDefinition DEFINITION = TestingPackDefinition.INSTANCE;

    @Getter
    private final TexturePackManager texturePackManager = new TexturePackManager();

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
        texturePackManager.start();
    }

    @Override
    public void onPlayerJoin(HypixelPlayer player) {
        texturePackManager.enableFor(player);
    }

    @Override
    public void onPlayerQuit(HypixelPlayer player) {
        texturePackManager.disableFor(player);
    }
}
