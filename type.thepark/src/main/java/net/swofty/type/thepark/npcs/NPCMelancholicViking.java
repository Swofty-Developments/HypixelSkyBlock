package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCMelancholicViking extends HypixelNPC {

	public NPCMelancholicViking() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§aMelancholic Viking", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "LNUoJj3H66O7noZiY2VAq/XAuD03DCaLMtu7K8ieIvHszL3iWoD8GSb3wmx0p5JHyC1IaTuuJncHi1ncHACzG64s5QlMPdtQxY7Cwy1DgE8l7v0eVRWPrLOgp1hEeakvU4pxsZEym62T56Z0TVHv+L4DD5B96JEaxvQBK9vFqSea5+N9Pe/4DoZb3jxjHpVicKkPi496C+IG9Av/0F33Oz74PdlyHnNiOnRwCrQQIrpC//2ZNlHq3RNLOqQS9h+VFAGmoUISMb5TO2xXHLnI4Db1IcD1GvsuZl7hOuKY6OA3QwlYyXxf/T34MVWeCEPGjS/MUsgS28XcWVpSPc4hCz9JUgAEqhR3nYxSSUlG63bYJCZLObP0xWSVjNEgPKp02qIRiJdC/0s1vC4beaohfhlePPTbEC9M6117p5fXL/xCvhIcv6FpXo4Vp53mKjXSooI/R+zro66jAMddhcNnj7C26q2jdjm69QjdN5/dpfRXSJsX0MOxlpasTYN/nr9uIx01JYnN8TQyrjqn9TWZr/UckfOp8MIHmUaOb71I33nG6U9+JHNTGDKcgN1CAv2Yzbai5WjdJRSSMWaGSZt8vJT+Q4sUdseS4sVNATaw+MqkcDX1YXwYs13FpoO0enbJHe/yZ04oSgmRcTUGjPy2QJ7cvTyzOTElYhXNAALBnns=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NzIzMTkwMDQ5OTQsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NkNTEwNDNkNmExOGZjMDE0ZjI2NGIxZTdkN2ZjMTgzM2FmYThjZjZjYTI5ZjJiOGY2OTFmZGY1NGRhZmVmZGEifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-335.500, 92.500, 73.500, -180, 0);
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
