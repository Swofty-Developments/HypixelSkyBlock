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

public class FourFourNPC extends HypixelNPC {
	private static List<UnderstandableProxyServer> cacheServers = new ArrayList<>();
	private static long lastCacheTime = 0;

	public FourFourNPC() {
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
						"§b4v4 §7[v1.9]",
						"§e" + commmaified + " Players",
				};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "NYN80kE4rhej50fuuWnlv0SsxFpTPzLkZ8LqtN2VLwltVNijjA5tH2LR9iD0CFp7nszVxoXCull59qzzGdjV/QoCFcvPpYmAQH223/7NzCAPBqr0nZpQFWYX5jO/eZC64yGVHZL8k0P9J+JQmfHQuDYw9bpaWKmxWrNsEmulgyk1QRbxBxMlrIQbB931hNDe+Wb5jhwaNHe642i9Q3VV2YMxNqmE+mt1UZPEv0a7S8dVveUIO1CkfqXtuMEN5WUb7qBZDIS+Gyioggloiz+pwOdVrrc/AnCYXD31fHejVuDIi5N4fas/JDDlYMGriIxeMQ2d1EDkRKT0/e0s3eMnItBCF6W3zQYVshghuc+lJEwgnTjqzK+0AEnBpSgNwCFfB+PSFirBOVCOT/4SCS4/IMuhybfXHgiGOulaj8B7G4V1RydzMskBhdtGNTGMes813oAGhZ4H8Ja4iqcoM27utIw+T9YiL81ECLXWaPqDaSFGcTT/0PILKwArARATXMmvRndTbL8XtyrfUwnHMgWVtnRs8mNeGDZVcGSGQoWZqaWySnsMMI5/BKLTdtOqz10UiN+x4JfNAZWRc/WvdzrcP15M4GYLuJGnNxitiQk3AX24BMzM9cymPJOW9HF4S8bhnIFZsCOdrCSxtLTgLK2oIIkuEKy69swLJ/feFII/6x4=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTc1NTE4MjU4ODY1OSwKICAicHJvZmlsZUlkIiA6ICJmNTBjOGRkN2FiN2Y0ZmUyYWI4ZGI1M2NjYzRiYWQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYWNoYWRvVF9UIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVkZDc2OWQ3M2UyOTY2Y2Y1ZGY3Nzc2ZmE0NWM2ZThhMWMwZTBlMWM5OWExNzQzNDJkZTEwNzVjYWU2ZjM0ZGIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-3.5, 68, 9.5, 90, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent e) {
		new GUIPlay(GameType.FOUR_FOUR).open(e.player());
	}
}
