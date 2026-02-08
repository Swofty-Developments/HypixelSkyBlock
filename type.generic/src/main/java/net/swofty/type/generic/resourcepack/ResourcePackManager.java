package net.swofty.type.generic.resourcepack;

import lombok.Getter;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.tinylog.Logger;

import java.net.URI;
import java.util.UUID;

public class ResourcePackManager {
    @Getter
    private static ResourcePackManager instance;

    @Getter
    private final HypixelResourcePack activePack;

    public ResourcePackManager(HypixelResourcePack pack) {
        this.activePack = pack;
        instance = this;
    }

    public void initialize() {
        activePack.initialize();
    }

    public void sendPack(Player player) {
        String packUrl = activePack.getPackUrl();
        String packHash = activePack.getPackHash();

        if (packUrl == null || packUrl.isEmpty() || packHash == null || packHash.isEmpty()) {
            Logger.warn("Resource pack URL or hash not configured, skipping pack send for " + player.getUsername());
            return;
        }

        ResourcePackInfo info = ResourcePackInfo.resourcePackInfo(
                UUID.nameUUIDFromBytes(packHash.getBytes()),
                URI.create(packUrl),
                packHash
        );

        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(info)
                .replace(true)
                .required(activePack.isRequired())
                .prompt(Component.text("§aThis resource pack is required to play on Hypixel."))
                .build();

        player.sendResourcePacks(request);
    }
}
