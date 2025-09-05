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

public class DoublesNPC extends HypixelNPC {
	private static List<UnderstandableProxyServer> cacheServers = new ArrayList<>();
	private static long lastCacheTime = 0;

	public DoublesNPC() {
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
						"§bDoubles §7[v1.9]",
						"§e" + commmaified + " Players",
				};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "t7pfpi1gLZ2miRowaYjxF+4qedigXeBj7dRwGdDDcf1H9Ibg0JsVEOWjndm05vbc/rFlhXQi0+tqA1+gdIi2orQhJ0OPtQX9TTRkUqChlFFOQtOUndWzt+sXQFlpQ0bT1vjC/U7Vkmud976lLizfGBhTCApE5pn4DKSkuzIfgjvJlmQ5hFQDRt0r8EqqbJy8zlI6VfV71e3e0DF3KiommmuD5AMM7HfJBVUU0PuqaAYGguHJrkhTi9yHueZ/oJEdX4mKHZVKDLt0dbOFltXg7cSfCylxAq9u89OlV1TJSBDrhp7/I9q288+Snz+ghG6kjGJ5Ad4NI4YJgegaxoyrhompqJNFu73UOsN/AAtmerfjoZu4RBEECHP7jicgqxks2U75lO5DrKVNdxtlpJuCHvH6uEatg3AKbnEOy0UQZ1qQQn3rTfw0C60QyDgdoSChsuYoe3japPsr4jzi71xn4jyJBuG6M2Vco2YCRRamHl6w0yPg1CjtyXjdjsuQHZQU5oAeLEOaleKoMCJMfrcEpaM0L2Dgnhxp+pj2K0WXyMbR3H8VjZHDB/WyF/tSo+1sA8Pvk+MLUzoZjyO05khFsRgCtRRhl5vwX2K87GcF5/qNkxTxm/EyzdLBQ9UGws1Y4AkBzmUcBX9KFSN5Moz4B5aNcjcSt/0bdRIM4d/VQow=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxNjg2ODA5MzcxMiwKICAicHJvZmlsZUlkIiA6ICI5MzZmMTA3MTEzOGM0YjMyYTg0OGY2NmE5Nzc2NDJhMiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDAwMDAwMDAwMDAwMDB4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I3NGI0YmZkYzUzZWYyYTkyNmE3ZmVkM2Y4M2RkN2UyYTEzNWFjZDgxYzI2ODJjNTk1YzRjZDA0MTc2YWQ0ZTQiCiAgICB9CiAgfQp9";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-3.5, 68, -8.5, 90, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent e) {
		new GUIPlay(GameType.DOUBLES).open(e.player());
	}
}
