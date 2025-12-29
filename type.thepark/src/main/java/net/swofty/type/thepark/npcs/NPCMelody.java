package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCMelody extends HypixelNPC {

	public NPCMelody() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Melody §d♫", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "l3+nGUvRiH//fkK8SwTwQFjXyqt+kNd5n4MCXSmtxoQTeUGZpa4dR5wwxpyYue5TacFcSVEXZaAq4Y2Uu8KMb9JYlVrL4W8O7Sl//jik9hd3DyOHhHd9oLyt1x1GvU1hiUmcXX74ma6YERznSPXepDMiEX0lsx3+JTHbpQoIqpuuqcl0wgRT9hrKPYGE32s3DCV7MIpepD2z/hmnXCCSRBjjPfL8qflCB3eAFUBw767vfbt1J8f290Hvb5CgL4AgZehplAXmqQM4j8Rd/8DAv+nCny5OrC2f2Xj3dxsYXcJzxUcBvPmPBrYrT8/uF7jBia4rW7bU6kGzy5IzR6pqOJmQnU3Frrwi/3dJnCJ3iFVllPOiWqCYC4AVOy0+0cAVlZMSWtLIg+ZCXNc3Ah5/eoBxIBOtH2pA1iYO2P3FqW4g2jn0JH+4ck1GvuFywj6XgHvkmnGo5pZsudnGdHt0a4RECWA+lrbPfoFmQND0Q4B2pqLcCXufF3k0Q8471XNJVzMPgnWpTJ5p3wS16xZZ0ETm9kEnETs6506hcig/JyVgZsPxMVctsu5LfNp8jhXVhlWRfuC+X6MNURmqVBGv14Erc29952dhf1LwYna5+Tq8LdIWMZ7KHgpg2L8P503HvsY34O/TOdGZ855ZEgrj5FTgBdJYY+oJXCMmtALPeJA=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NzI0OTg3MzU2MjMsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRkMjM4Y2NjODE4YWIwYzBmZGViZDA3ZDRhNTZjMzUzZmY1ZWI1NDFjODVkMzg2MzYyMTYxNzgxZDFkZDc0Y2MiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-411.500, 109.000, 71.500, 169, 0);
			}

			@Override
			public boolean looking() {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {

	}
}
