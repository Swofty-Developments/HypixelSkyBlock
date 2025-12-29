package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCKelly extends HypixelNPC {

	public NPCKelly() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Kelly", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "Pi7jmNymIAKdg4Z80WSEuhjifAL8q7GSXY131I/bb6Aec5NSUknOcSbCva6iXGn1Ke5AMDLBlyUsP45rgxwObRqjN04JxNvbp37dwXc9PeerOtbN+8pN4NXkMg0xFx1qcttxrSwdSmVgQ0W0wDqDvX5VxAhVNiC8zzv6Dj/8YjdkF6DKr7A+kH69CpgNrlPXChbyMXuHnx0AVFPfcMazb0K8nzRvuQy5RT5mBvgPee3nKvc8OvVHL6RQjXpAKnDv4Oo+yDE1ipAvl98eUmIczM2yeSqXX+JHGOJKEUAlbjED7SZSDbx2njzwTDYeiBRPnWki/wWGvsnYcLhiMrTQPb6ZMk7VNAqAmug3VfTsY+1tutJ6+C6oYnIvfoG1WHNbUHOLbfd2ijqwE/bihIE7PGL3W1PDuAqW8lyvACXHNCKy0Wp2iopixiDK2mXmMxaXIq7m4u6r/5GfP0AAd75BAx+mSk8pPb0rHlKqJuDVoxM4fBL2SBDU+mx0zDVLJE/pJsN97Bphs0XRs8W/Jf67notKp/iviiiJQA4owNe0G/ckpiFEFW4Qiob7AyOrY3ECjTgivZpM6hw0NOhYZ1Mh780Szgd7WrwxVAUrnPjKkkZZns/IiuRrzYLu49g22NhADNedMpaEW35V13tvvsDdEGkMehbA4UxszBIS9mMj7fA=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTcyOTAyNDE4ODkyMywKICAicHJvZmlsZUlkIiA6ICJjNmViMzdjNmE4YjM0MDI3OGJjN2FmZGE3ZjMxOWJmMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJFbFJleUNhbGFiYXphbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZWIzZjllMmFjMGRkNWY5NzRlMGFlNzEwZWU3NGYxZDhlOTZlODY2YTUzMGIwZjRiNDE3N2I5ZTYzNWYzNGQ3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-350.500, 94.000, 33.500, -180, 0);
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
