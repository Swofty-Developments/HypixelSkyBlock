package net.swofty.type.dwarvenmines.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.dwarvenmines.gui.GUICommisions;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

public class EmissaryWilson extends HypixelNPC {

	public EmissaryWilson() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§6Emissary Wilson", "§e§lCLICK"};
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxMDIyNTExNDI0MiwKICAicHJvZmlsZUlkIiA6ICI2MTZiODhkNDMwNzM0ZTM3OWM3NDc1ODdlZTJkNzlmZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfX25vdGFodW1hbl9fIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2UxMTcyYjE3ZWIzMzE4Yjg0MWZhOTRiZTBiOGZiZDBiN2NkZGI1YWRkMWQyYjM1YWQ1ZTZhZGM4YWI3ZDUyMDYiCiAgICB9CiAgfQp9";
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "OfpJzikHSkXmvhaThpA3qdkGkN+zn0pcq6Mf9pSoskz6KVDMhpIpZIyxWjyeugUWmYkp7tkweKCahCtmqoykAltkuuucBHJvQ9j7CxA3PyXQceLvhmEynmQoRB2qHPJTJeR2BXVBTt0VQsv3x64TiEL9U4C0MNg8gBY13z6RALf6FKqzTRImqAlx/9D7M4fhT0fnFAwbzbCPmTlz5zfHKd1L6cPfu/KJKvJLXI9aoM9AgWjARcP3lKRXmgbv431+B0a2ICqngKohVXEWqHLcGI9jK/N1KDEZcy4MNrQ49WJrPn2tnAEqeRmcvab4sjyMqwzrKaLyo71aEAXPFm9UbhZ2d0rknYOo7gxzIirVQMnJR9HxbBslrlZuUwHDOsb4GPwzHUYnL1zZiTRpnspkaADE4vLlt6cyozBCkZI2WZNd30P/15GCLUk0XsETMYlfPsXiQ/CUw8ikZRtP1x1xhWNltQCUcpnbrbJHKZblW/zfXWfEBgCC/kod46VJHHAW+m6PPJZBgceGQrf49MuUbSHfTWk7LHqiDnoahV9rQyb3l1BTUvRgJCdbg9kpYutkMEe8jSPm9xylG1O2ap09cXjqCwhpXiqG0c2jqf4gXEoYEqAPZYJxPQP26Ji2Pk7b/xS5ix6YubDxiQXWLA++LCch6xMwL2pyPxqfDnO4Qk0=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(181, 150, 31);
			}

			@Override
			public boolean looking() {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		new GUICommisions().open(event.player());
	}
}
