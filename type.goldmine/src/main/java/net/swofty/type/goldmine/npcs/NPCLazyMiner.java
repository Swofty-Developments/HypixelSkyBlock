package net.swofty.type.goldmine.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCParameters;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class NPCLazyMiner extends HypixelNPC {

	public NPCLazyMiner() {
		super(new NPCParameters() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Lazy Miner", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "DEPpEYXbbm6SQmo0h40eZFei7e+qtBqzZXDcRrPI5L2Yb4AFHQtalJrqqR4YlOv9NtOuBg+MDIMna+KFBSldUA0fMklitAk3GRl7Y4mWpAQM9BuOGGuWnqf2wVci5hZSiMtL2pSC4ogln0urAdm5qsB224RfbfnpiEWrm8BTHFXfqaMkEZNah+dxeszTMl1TytWYePAFtolMw/MdyuTGqApSpgk9nWoPaUAgGVWEo0mlrflEdmX0DkR98BKpqeg1naN8C+SbleTBJe8ZYqKRNMqD5bIJWc6J6cjGEeb7TbR+ZMipk3cMRu43HrcRdPAlmxihezTlxeDi1RoeObDSZXNSGtMhDxEWU1bOBAgohT8qM2x9AChAGL2kphq3EJplMlw45a9KbZj+/R68QmcEPY+TNo0BPshFoj8sh+RxZPdrirjM76G6TyVYJNIScu0JQ74WqYh/hpjc75rOldORlS9Aat5IXsDnrjCYi4l37DH2bBB6C+gk5BgGqnce/1QF0VESfJWtmS9gHSPBNZHa9ave2rM0ou2hpEw1LgW27hGWqZfGg0sfU9deQvLYyiRFsAXww22L5xmNJnOmvn3L1lDdGNpT63qXknp9NvAABOCPpBV0zeSCmHlDYbAN19sEkthnnIGk1BwZhsaZizf09gGAlMdLgcGYQKzfJJTODWo=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NjAxOTM2MzQxNTQsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Q5M2Q5NjM2YWE5NTU2YWNmNGJlMmNjNThkOTNmZjBiNTMyZTY4MDk4ZjgwYTdkYzE1MWEwZjFkMTZiOGU1ZjgifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-11.50, 78.00, -337.50, 40, 0);
			}

			@Override
			public boolean looking() {
				return true;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.player();
		player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));

	}
}