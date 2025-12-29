package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCMolbert extends HypixelNPC {

	public NPCMolbert() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Molbert", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "W0e+/EVqkTyw3gQZgRQbswV8EA1K/YE557v6VpVu/a0B7XzcMMtS8aUyzsljpBiXM3pjPo36D4Wjq45AB/Ij8GaCV2M491lTk0OLzZnRYvGOZ0HvT8K0TdWkOZQtFfh45IX97yvrnfSx1P5XYvJxXSBCKFn6lG9CpYT4Oy5RQG2ARiBkwq3NNGbgJFEfgMQl5KyszgyWCvyXsEQJo9UVYvlGLgrMML+/bORdDSzlnm3R8/tQtCbCfKfRNKlNiv03c02vDGrWDdCB6JYXd+58V5uRwP4NrzSer6GTd59sHLNx1lWMvvag/5m+1A+XFaBTs6wK9m6PB5AVEm9ROhIHjKRpuDjUBHnKnc/docuV+JKlqu3z1BQ/nUccznZmOvKJ8eewtawtfd4/HalVsIPrE5f4nUoH7C1t50xO20fZ3Z7B7hurdSwJEuP1vV1AxhM21Y8ux42yQOqQ5l0pYeIPJTIs99MUaYJYqdWJll1Mp3AHuNelDbTVBvqzt0wtFvN5I5ULoeJrbV7t1PmCUJIP6pcyg9wGPRBW5ZauR2sp8TRNhrZOCRTiaWST+XbVnw7HkprqaoHqutxyascDQej38AknySBbMzcCZYS8CEwQYfMTbW7E/6SO5USapx0K29t+nh1ZOvfx2mI8hFW/qH5HFP2FhyZ1dSISfpWDCq7LWUg=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTU5MDQ1OTI1MTYwOCwKICAicHJvZmlsZUlkIiA6ICI3M2ZkNzU2NWJkZTY0MGQzYWE4MGUxMWUwMTMwMjc3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJHYUJySWVMVnR6IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyMDc4M2UyNzQzYjg5NzZiMDBmODI3ZWUyODM2NzVkNzkxMTE0MGM5ODI4NDIzOWY2YmMxYzhkZDFkMjdjODEiCiAgICB9CiAgfQp9";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-447.500, 120.000, -63.500, 45, 0);
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
