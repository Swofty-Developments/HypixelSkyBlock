package net.swofty.type.bedwarslobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.type.bedwarsgeneric.game.GameType;
import net.swofty.type.bedwarslobby.gui.GUIPlay;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCParameters;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class TriosNPC extends HypixelNPC {
	private static List<UnderstandableProxyServer> cacheServers = new ArrayList<>();
	private static long lastCacheTime = 0;

	public TriosNPC() {
		super(new NPCParameters() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				if (System.currentTimeMillis() - lastCacheTime > 5000) {
					ProxyInformation information = new ProxyInformation();
					List<UnderstandableProxyServer> servers = information.getAllServersInformation().join();
					cacheServers.clear();
					cacheServers.addAll(servers);
					lastCacheTime = System.currentTimeMillis();
				}

				int amountOnline = cacheServers.stream().map((server) -> {
					if (!(server.type() == ServerType.BEDWARS_GAME))
						return 0; // todo: actually check for bedwars players
					return server.players().size();
				}).reduce(0, Integer::sum);

				String commmaified = StringUtility.commaify(amountOnline);
				return new String[]{
						"§e§lCLICK TO PLAY",
						"§b3v3v3v3 §7[v1.9]",
						"§e" + commmaified + " Players",
				};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "qQLrYOBdPK4BONSKJVMn4QoIzvuDAs7FTx0+5JMExNNoTJpbtpB4t1loxNAhcCSBKu6tUMCLMNuAW1VM0cUyKd36PKWUW7rSWmxNfNk38FtsuA/5L0kbLhOfqbIqBybZNWz650XnuCj8w4alX6d4mmUESgxiuHy1EmvT2G/OocKabtP4QftLSimkMQY+f0WI6TwD5+wuINcfdW6LRJnJci0TvF4sru+i+WkOee0W5Bk09cizR2MOHRlGCFv9w2nScdKw5eg7ioL963IvlfL4cMz0Jah9GAdRxlie947Jxe+KGFyae4sikBbdjZCY9xB0/Hs3oCzoSFw0TAhmHQuXjnSqn8IINRR/Lw8nyePPXB5anC5J23MvMJGxaNSELwsiF9FrJHzlT65FrIDyFksf5+0t5nbqMpyxBWrRvUbQ0n1BUEplzXeDqzz0lawhHkxprmamjY7K2dAKSNRbHBOsCN4PoHkX6B/YGqYrYJQtsPRD5QV24kqFFSQipJwqQ0GZBvEmNOvhTEg6iz0Hz/aEVsGu2Ez1vKJ3Rld9Z5pmGWy/tJHvCOuspN134U4a5tKX/MDqN8Ny+qgdK8leAhImWB8Hovz1Fmwc7MJdAp6hEIVbjp6MUxP8I++2g/DMIDuSzH3uJwToZj0AYpKRqWyp5jaNJX2Gw8jNmVfrIczmyyw=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxNjg2ODI4NTY4MCwKICAicHJvZmlsZUlkIiA6ICI2MWVhMDkyM2FhNDQ0OTEwYmNlZjViZmQ2ZDNjMGQ1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVEYXJ0aEZhdGhlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMmJhZjVhYzRiMTc0NjUwM2Y0NmMwMTA4NTU0ZGFhZTkwMjA2ZjJjNjFjZGFhZTZlZTA4YmEyY2QwNmMxMjVjIgogICAgfQogIH0KfQ==";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-2.5, 68, -4.5, 90, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent e) {
		new GUIPlay(GameType.THREE_THREE_THREE_THREE).open(e.player());
	}
}
