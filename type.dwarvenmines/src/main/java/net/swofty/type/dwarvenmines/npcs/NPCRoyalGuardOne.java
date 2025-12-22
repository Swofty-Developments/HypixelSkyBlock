package net.swofty.type.dwarvenmines.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class NPCRoyalGuardOne extends HypixelNPC {

	public NPCRoyalGuardOne() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§6Royal Guard", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "seuXIllWZjXjqV2JnR3IcWgjYKWb1wm0gK69ENtKNZZCcP0QKbqjHme1u+K/aIs+JIH0nSkdkj+5PP6/kbsRm64V789hHeDSYJ2aE8lcqamptdez63Ap//Nvv8ykDRPHTddvOlJ/zpeEOj2x2E0u4LtsyLaRlY24Ku8kkiy7L+EKZQ4Pl0mB2o2AtzZNECF1BYhhSguEzFVhu2emweFCD5yYcTcpyecnvKfedXy4uxaeTAls46rqCfGHJSLll6PatUwJ7ZcBQyN95RCR8I0+kdkkkFZ3v2nYllTLd3oJPpRD6GBkOA32uVFYEYXp4HAjN2odDZfPmw663jdVJtLvYIEc9FckQfY6uXjSt3V7/yzOrxP8iV22IMhXY1gaTjQFnoBFa/yRcVrbcJ1GIT3tJMYrB3hS8BnmR7jPQ05crUQjZHCqVzZEa74bM44eus3Db8sL6OALpYznP+u9Ss855AkjpIlwQw533qxMRZEffvnQ1wT556TeczvZDEb9zTszYqzCKt4uLG97k9aTYKpeJHIBbzCo9+xvGD0zzzTXZ7xKe4ZfpzKnFFjUrJRdqjddGRi5iV3WdHFLQ4VVcZyCqIhVNjt5bkCbObuqIdmhmCZNDvf8xmis1p890mLoTeln+Mu6/2NwgVXwuTtKgHnCGff4X7vdzepdomo0pZrgObk=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYwNzQ2NDk5NDA2MSwKICAicHJvZmlsZUlkIiA6ICJhMmY4MzQ1OTVjODk0YTI3YWRkMzA0OTcxNmNhOTEwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiUHVuY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU3YTQ2YzI1ODI2OGY0MmM4NTlkZDVjODAwOGE5N2M3MmI4ZDM3MGU5ODFjZTJkY2MwYzNmZDQ1MTYzOGVkNyIKICAgIH0KICB9Cn0=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-45.500, 200.000, -124.500, 90, 0);
			}

			@Override
			public boolean looking() {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.player();
		player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}