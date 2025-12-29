package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCVanessa extends HypixelNPC {

	public NPCVanessa() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Vanessa"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "bWMvruKbW5+PPvFHcKTot9f0nvTjaoJAIcW5BZhO2BH/J8HoIWPU1BFNwqkYK9yBS/9KfB6dW8UEmAxEP/tCtb8uoZC9ePnNDXa3hMgGliEDOdXyUjIHBKg15tqa8qLMsEj3W0mVS7Oou0TKkczZayywuwbJKGEmpdhg+DIiDs4QC7pBNaoI5aM5HzKk6IKmqRtu6pEQ8b5MNm8+uwfNQCv28FMNSqx8FG92i2uNxRHcAtlPk0SIrgyXdWSkQjXdF1BOyWaNg86R+UFDbLUFQ0vstuNl5cwqwSzhQUerccSJve8Zs6y48JGp9GjFjxOMKBQYdiIXcUpeRt4mVYZMbrQPch4Hky0frJpKF+totgLXejyeqzPzZPcJcgRR4+An2fitp9sDY9p0ltIR7BReS7mBZNxzRL1KtKiUyZ+jkFRJFTSopPUmb2gh34dIc8QdpkGJT5/GS5KNghMSm8OI+ytcIQnNorBshCRJIViqlZ0yiG3RXCKrGFySrc98StpTQ2l9MWihqGmCzEPRRpH4UUZdjaXE5+0UMwwt42quchCfN8k7uhiiV2pTgx9Sb61vEnqBWVadcOs86I9wF1PksAz2PkDP5+ir1NBuZWq/Lupm3RJD7i4YsDiS5MWXPMFRN1EVFQV7ibYmLJrG724i5ddAvXdiDGYtSXsprGZ8nR4=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTcwNzE0MzE3MTA2NiwKICAicHJvZmlsZUlkIiA6ICIwOGQ4Nzg3ZDFiYzY0MWE2YWNlNzdjMzliNzVmODZiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYWdpa2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDllMjk3OWM4ZTJjNTg4OThjMjc3NGIzNjY5ZWE0YTFiMjk3NjdmY2RjMmRkNTkzN2VjMGNmMGRhM2E4ZmI2NyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-304.5, 76, -79.5, 0, 0);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {

	}
}
