package net.swofty.type.ravengardgeneric.resourcepack;

import lombok.Getter;
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
            java.nio.file.Path original = java.nio.file.Path.of("configuration/resourcepacks/ravengard-original.zip");
            byte[] data = java.nio.file.Files.readAllBytes(original);
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            StringBuilder hash = new StringBuilder();
            for (byte b : digest.digest(data)) {
                hash.append(String.format("%02x", b));
            }
            Logger.info("Serving Hypixel's untouched Ravengard pack, hash: {}", hash);
            return new RavengardPack(settings.getServerUrl(), hash.toString());
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
