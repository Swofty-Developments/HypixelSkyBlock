package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCOldShamanNyko extends HypixelNPC {

	public NPCOldShamanNyko() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Old Shaman Nyko"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "mlkwH7j+wvVdC2zn62BDU1TaJLXHZLXvdCySROKTRdGXFBv5+OeXC3ekxqTPdhCIHI1CwPu3CQYeZT2JdZVLF69+s6nKvXmke1EvnqD4vfxoeh2ohhRvDan0cAUW+uXuIhxAblfCPpJVGaQEbM+PUSDJQ1lpgcGsGBYBX69w0nBCNsfhs+BEUbVPQFIFF+ceu1iY5rFBmZUa17OZV6fXv2COBJykjyA+ghAsaBPXGVpJmUbcbyk9p0eMDUX0p+Crdr4EFUQHlHYL7SlMykLZ2y4yZS7zuN2nBl2F9Inxdk1Odp84kp626L6odKF6veC+DL0Lo0DJj3R144Z9Npun+gZsX45h32Qm1xB88p9tto+kQsyPnGCwdsUQN2tg8YbhCwFDmUhNtY75xlLTy5eE+4fTK2DDRqd8GKNKF7Xoy7fR2sUtL4TpxkM71i0ddcMTO5UEE6NcTj2f7Sv1xPyVKMqkd5BuXJf3Wa81STM44/jtVq5BdTda3MghKj7EGVKMf9onOabw+ivnNFHODEgWphZueZ2M7J2Oo1jp2YNXGwpkSLkFV4Qjhm4Glu4CbFQdeyXMVTPSV1oui/1KYvVTPKMpVzXg3Ckcl8CyptyC4+HWTMqQh+eCtoyeIAasv8fg298IUdkURnOdeDqV5EZ6v6WXaN5Jn24fB+vSgrJLxvM=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NzMwODI0NjQ3MTYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3YTUxNzg2NWI4MjBhNDQ1MWNkM2NjNjc2NWYzNzBmZDA1MjJiNjQ4OWM5Yzk0ZmIzNDVmZGVlMjY4OTQ1MWEifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-370.5, 84, -64.5, 45, 0);
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
