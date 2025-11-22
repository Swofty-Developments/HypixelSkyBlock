package net.swofty.type.generic.user;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.commons.MinecraftVersion;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

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

	public static String getDisplayName(UUID uuid) {
		if (HypixelGenericLoader.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid))) {
			return HypixelGenericLoader.getLoadedPlayers().stream().filter(player -> player.getUuid().equals(uuid)).findFirst().get().getFullDisplayName();
		} else {
			// Fallback for offline name display: use Hypixel account data (rank + ign)
			HypixelDataHandler account = HypixelDataHandler.getUser(uuid);
			return account.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() +
					account.get(HypixelDataHandler.Data.IGN, DatapointString.class).getValue();
		}
	}

	public static String getRawName(UUID uuid) {
		if (HypixelGenericLoader.getLoadedPlayers().stream().anyMatch(player -> player.getUuid().equals(uuid))) {
			return HypixelGenericLoader.getLoadedPlayers().stream().filter(player -> player.getUuid().equals(uuid)).findFirst().get().getUsername();
		} else {
			HypixelDataHandler account = HypixelDataHandler.getUser(uuid);
			return account.get(HypixelDataHandler.Data.IGN, DatapointString.class).getValue();
		}
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

	public ProxyPlayer asProxyPlayer() {
		return new ProxyPlayer(this);
	}

	public void setReadyForEvents() {
		this.readyForEvents = true;
	}

	public LogHandler getLogHandler() {
		return new LogHandler(this);
	}

	public DatapointToggles.Toggles getToggles() {
		return getDataHandler().get(HypixelDataHandler.Data.TOGGLES, DatapointToggles.class).getValue();
	}

	public String getFullDisplayName() {
		Rank rank = getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();
		return rank.getPrefix() + getUsername();
	}

	public AntiCheatHandler getAntiCheatHandler() {
		return new AntiCheatHandler(this);
	}

	public PlayerSkin getPlayerSkin() {
		String texture = getDataHandler().get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
		String signature = getDataHandler().get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();
		return new PlayerSkin(texture, signature);
	}

	public void sendTo(ServerType type) {
		sendTo(type, false);
	}

	public void sendTo(ServerType type, boolean force) {
		ProxyPlayer player = asProxyPlayer();

		if (type == HypixelConst.getTypeLoader().getType() && !force) {
			this.teleport(HypixelConst.getTypeLoader().getLoaderValues().spawnPosition().apply(this.getOriginServer()));
			return;
		}

		HypixelConst.getTypeLoader().getTablistManager().nullifyCache(this);

        /*showTitle(Title.title(
                Component.text(SkyBlockTexture.FULL_SCREEN_BLACK.toString()),
                Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofMillis(300), Duration.ZERO)
        ));*/

		player.transferTo(type);
	}

	public void sendMini(String message) {
		sendMessage(MiniMessage.miniMessage().deserialize(message));
	}
}
