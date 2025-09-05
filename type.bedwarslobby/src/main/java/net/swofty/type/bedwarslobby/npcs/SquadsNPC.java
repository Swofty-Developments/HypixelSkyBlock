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

public class SquadsNPC extends HypixelNPC {
	private static List<UnderstandableProxyServer> cacheServers = new ArrayList<>();
	private static long lastCacheTime = 0;

	public SquadsNPC() {
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
						"§b4v4v4v4 §7[v1.9]",
						"§e" + commmaified + " Players",
				};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "tQBE08RCcC7KXz758VAUxJRwHRmITEHre3VmUcyxtmluwNzpAYhm/NcJXwhPrw3W+sY4gnOWbWc5deoc8tAiTT43tJWbqsOMUEZ2ECS1mBfpJVCpO8A/fboJF0T8WWkW3Mi3j607kY5sfzrVmn1jw9BoCit0SXXXpM9RLuglWkoXQ6i55pTJCcwyXx5FjjoucdvBwlmTTZ/HYCLaf75KeyPZrDmu7UfqWpE1aUmXVv3cvRmB80dCIQHh8WZX8UFHt9X/gOOwolczt8bT+AuOQMoJ4wZ415Iw07dOI19ZgPsn8csrKmmScub/4r1LbeKr3MnyHYdF4PSbCERWaDNZfX6CwDYqxQJufimnXBRaVl1oT7hmhB8ywy2/49LW5nqw4r7QT03XfqkcCEfEVyTnU5FfzukPTshtZWui6Z5Kvd8hF5OHM07SWf4xkioPPZJ1P40I80xVx88rnrsDWSWo8MiAJKsjvRus+wQPhysdR4P6ZjMql4kFhzmWagnGRAPlsKR77b3hdlmCv2S530nIgkN0FzyOlA4/XB9QFwESPL+j4HaAtzcJx5xvhpF69LYsdd8yvf5e6CS2SiZJLv+HnX6gA5DUXnVi4LbRxYu3zbrBUAVFJmsIX8G/IfB6YVRheDkOWNBmhN29bxjfjCDSVjfgoBIIZl00VhrjDq9kXWg=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxNjg2ODIzODA4MSwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUxMmFlYWZmMjJkYzYyODZiMDkxOGIyYmI4OGMzNWM4ZjcxZTk0MTMyMzIxOWI3NTZiODE2OGI3OWZjNDcyMCIKICAgIH0KICB9Cn0=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-2.5, 68, 5.5, 90, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent e) {
		new GUIPlay(GameType.FOUR_FOUR_FOUR_FOUR).open(e.player());
	}
}
