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

public class SoloNPC extends HypixelNPC {
	private static List<UnderstandableProxyServer> cacheServers = new ArrayList<>();
	private static long lastCacheTime = 0;

	public SoloNPC() {
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
						"§bSolo §7[v1.9]",
						"§e" + commmaified + " Players",
				};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "IV5B6+49GEZKV6oFKFFhTxH9mQ6HBHgdRhxJF8GNdwvnxKoMHWa19IpD7/YhLe8UIblB1TohopvMNUx1f9BIg2QJWkmqcaXVph+HjBokS6NlQ9iX8l0OSgMDH+sR2eZly09vUd0eqy9Ba9pI4GfiZMHWajDDn5ziLAM3RuUGIMKrG5woxKm/8Hrj0Jc1+b88hQN2YCWwelmnY2difn+jz6+h62Z+CVIHFOVmKql0xJG4aLT9fFLGgcTJhYcisP0wU8ml0SHTkJ7EARn5xEMyd5kybZ7h4uf8AwsDbZ5hxTvjljMDOIaFeRLO4EsczBNy1EzSEYc0NS7rMU4AURutdZ2/lOraY4PEUHbMEEmM8em0yGX2qPHkF0jXwcjS2ER7qWUblCyA+3BYXRSdozK793NKAgrFgOpQDvzSyHQL26eeTcXKG7cd1l0KyrKJxWzJ0RY8IzO7CooTs2Rryl16T4PffC3zzK1bANuLG5996Pd4rovMWavmnOMKnCBwYLfXeXhAZ246YhFzTOPI0m38wKi6Spo6SHXZ5AShLcAcac844nrAsgYdL/8bqavDbOW3uxvVej+98vUMwvPAH2fO9y3COqPuAAST3gjvLVt7Xk3wecuzf2tb1zaSWNa0ke0sjMgCitcPz+KG48JK4PcZ8GkGCIPRkyG3sZxUnPo61qI=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxNjg2ODA2NzY1NiwKICAicHJvZmlsZUlkIiA6ICJiYjdjY2E3MTA0MzQ0NDEyOGQzMDg5ZTEzYmRmYWI1OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXVyZW5jaW8zMDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUwZmYwMDcyNTE2ODBjNjNjYTJkM2E2YjY1ZWM1N2YzMWQ4ZjgzZGYxMjc0MWZiMGEzMzlhMWZlMmIwOTRhOSIKICAgIH0KICB9Cn0=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-5.5, 68, -12.5, 90, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent e) {
		new GUIPlay(GameType.SOLO).open(e.player());
	}
}
