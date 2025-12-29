package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCRyan extends HypixelNPC {

	public NPCRyan() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Ryan"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "cKGU6Ze2Y0I19H7/PrT/OdOjFADX9rg7dRgOMg95ugPfz4jSNmbIIan/i9C3+YaNN+QM3g2wAA/cFInXIK9CIisQ9C5ObDCT2h4ZhTueWSkygd7Vdm/34FyISNyu7maHRSJHg6HhcVaAVGZVyF9s9Tbmvgu3rX59kG1R+L65ArbPSDKlRLlyZYW0ut+F16WkjDjgT9qTiRvDGXWFBhFVaQ70Qr+68oWo3Q57+Ml2NGmY3AE9229uds17XuVD4O3ct09iwiXAtPUD9eHHdXW4oErtPCH/KoQ2mWG+JZwDf/bPvD2TZy0yKfVAJBxlmym4Ik5UOD88BpYATY+gPPm9k7xab3gVvVUY6YiRoev0NswOpYWwkowaY7sGlbDoFkNctTvUmKrO/o5RgT/N3M7tUX3PSaaupB/iqffV8b21d+V49YVQk1pOdNp6okIo/KNRSQImZZkGNzPEK6rJ6OQSgohHxcn/lDTW45ApKaD6gdkLsI/wtXeMR/9R3Nn3nJwMOvusit7m6zC2CRCOr4q4txin8sTOIgmZCCsrDhdAJOO4cYhYb/lREbjTe4FfYWMHq/WU0RFQzDk8eNVmYMEsVUQ7j7J977EfEy1vWJwbWTl++/IASPQiyrz9Cm1ys82H1NIPbguFKVcw61toiA0WdtwzMVqWYllj9O2TT9LXAck=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NzE4ODIwNjYxNzUsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxNzZjYjEzYTQ0MzYzMThhZTRjMmJjMzFkZjA5ZDU1NjE5MmRhZWJiNzBhM2IyODAyMGY3YWI5Y2ExNzA5YmIifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-364.500, 102.500, -90.500, -146, 20);
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
