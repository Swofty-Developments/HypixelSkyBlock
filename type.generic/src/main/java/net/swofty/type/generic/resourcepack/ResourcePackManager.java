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
        sendPackBlocking(player, 0);
    }

    /**
     * Pushes the pack and, when {@code timeoutSeconds > 0}, waits for the client's
     * terminal pack status. The pack must finish applying during the configuration
     * phase, before any world data streams: a mid-game resource reload leaves
     * already-received chunk sections permanently unrendered on the client.
     */
    public void sendPackBlocking(Player player, int timeoutSeconds) {
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

        java.util.concurrent.CompletableFuture<Void> resolved = new java.util.concurrent.CompletableFuture<>();
        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(info)
                .replace(true)
                .required(activePack.isRequired())
                .prompt(Component.text("§aThis resource pack is required to play on Hypixel."))
                .callback((packId, status, audience) -> {
                    if (status.intermediate()) return;
                    resolved.complete(null);
                })
                .build();

        player.sendResourcePacks(request);

        if (timeoutSeconds > 0) {
            try {
                resolved.get(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS);
            } catch (Exception exception) {
                Logger.warn("Resource pack for {} did not resolve within {}s, continuing",
                        player.getUsername(), timeoutSeconds);
            }
        }
    }
}
