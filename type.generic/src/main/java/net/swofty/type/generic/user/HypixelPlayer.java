package net.swofty.type.generic.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.commons.MinecraftVersion;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HypixelPlayer extends Player {
    public long joined;
    @Setter
    @Getter
    private ServerType originServer = ServerType.SKYBLOCK_HUB;
    @Setter
    @Getter
    private MinecraftVersion version = MinecraftVersion.MINECRAFT_1_20_3;
    @Getter
    private boolean readyForEvents = false;
    @Getter
    private PlayerHookManager hookManager;

    public HypixelPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);

        joined = System.currentTimeMillis();
        hookManager = new PlayerHookManager(this, new HashMap<>());
    }

    public HypixelDataHandler getDataHandler() {
        return HypixelDataHandler.getUser(this.getUuid());
    }

    public Rank getRank() {
        return getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();
    }

    public DatapointChatType.ChatType getChatType() {
        return getDataHandler().get(HypixelDataHandler.Data.CHAT_TYPE, DatapointChatType.class).getValue();
    }

    public void setReadyForEvents() {
        this.readyForEvents = true;
    }

    public LogHandler getLogHandler() {
        return new LogHandler(this);
    }
}
