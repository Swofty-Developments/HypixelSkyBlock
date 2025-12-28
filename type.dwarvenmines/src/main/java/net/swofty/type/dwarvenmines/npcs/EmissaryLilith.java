package net.swofty.type.dwarvenmines.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.dwarvenmines.gui.GUICommisions;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class EmissaryLilith extends HypixelNPC {

	public EmissaryLilith() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§6Emissary Lilith", "§e§lCLICK"};
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTYxMDIyNTE0NzkwMSwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhkZmFiMmE5M2U1YjUwMzAwMDNjZGIxZTNhYjgyYzExNWFlMWZjYTc1YjQ1ZDc1NmUyNzRlMGFmMmFmNWY1MCIKICAgIH0KICB9Cn0=";
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "mjzfJ9D2gyxpHM1h60M6fnrwXkNkeik76dt7LWoGoME4+16PFkj+dMnmKoQm8eLs/6PRoMbWp6UATEpT7fsEcR7sKr/5EWdkcxpuBNgOlf4K2kREiFokE4Ri1cvBsM5YrfMFLlw417CmLmmrauSNuCOm1oUjWLzP/JexiRxmQ4yeWrg4TEmN1rE9F4RNaWQ0jKCZwSIf8DCbqx2w/kGA5prWrArgS2tIZiYCo1ydgNEag4nbZyny6siGyx817VjFt+UNIXqrwQ2PHRjpLyTlFUq6x8HXgyw2ZfHT4DqlH5pGdaE97dvUWuPt0rAU8iN3v1zxiegsCIjZ8xnuGFcgXMsHibLkJbBREdVnSd9LjJbGdD/jEQjCi+iquIs+ozwzhxN2emXZsJnrdbfFf0DD6pg7guUDhdVFZOA+b52hCB4e/N7Inmr9bE/KGsDnIEGfTk9YqUK9hznmIgcE8nAzzwl1SSFmZF8NQWOoZ5BirJztTD+Qfg+7hOzz3STrtIM5xVD1HrYpYHEFHV6DDri0thnHRvuQwllSXkr15nymnQPrCL+LEkRyg6n1iofHcVQGLTPXyJJ0mMZ7Z4W2uQePyeQMaFansZKk4NBaq4Juoz4TmvLIoSzgFjFaENQhHPccsyaSPWPi9iQ1nTjpNlUwvsjAfFgw/29/G1efPEI2sRg=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(58, 198, -8, -180, 0);
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
