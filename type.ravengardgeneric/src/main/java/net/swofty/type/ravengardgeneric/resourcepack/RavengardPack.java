package net.swofty.type.ravengardgeneric.resourcepack;

import net.swofty.commons.config.Settings;
import net.swofty.packer.HypixelPackBuilder;
import net.swofty.packer.packs.ravengard.RavengardPackDefinition;
import net.swofty.type.generic.resourcepack.HypixelResourcePack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.hud.RavengardHud;
import org.tinylog.Logger;
import team.unnamed.creative.BuiltResourcePack;

public class RavengardPack implements HypixelResourcePack {
    private static final RavengardPackDefinition DEFINITION = RavengardPackDefinition.INSTANCE;

    private final String packUrl;
    private final String packHash;

    public RavengardPack(String serverUrl, String hash) {
        this.packHash = hash;
        this.packUrl = serverUrl + "/" + hash + ".zip";
    }

    public static RavengardPack fromConfig() {
        Settings.ResourcePackSettings settings = HypixelResourcePack.getConfigFor(DEFINITION.getPackName());

        try {
            Logger.info("Building resource pack '{}' from {}...", DEFINITION.getPackName(), DEFINITION.getPackDirectory());
            HypixelPackBuilder builder = new HypixelPackBuilder(DEFINITION);
            BuiltResourcePack built = builder.build();
            Logger.info("Resource pack '{}' built. Hash: {}", DEFINITION.getPackName(), built.hash());

            return new RavengardPack(settings.getServerUrl(), built.hash());
        } catch (Exception exception) {
            throw new IllegalStateException("Failed reading configuration/resourcepacks/ravengard-original.zip", exception);
        }
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
        RavengardHud.start();
    }

    @Override
    public void onPlayerJoin(HypixelPlayer player) {

    }

    @Override
    public void onPlayerQuit(HypixelPlayer player) {
        RavengardHud.detach(player);
    }
}
