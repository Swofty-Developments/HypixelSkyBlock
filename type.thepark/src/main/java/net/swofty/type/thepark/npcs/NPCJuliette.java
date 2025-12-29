package net.swofty.type.thepark.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCJuliette extends HypixelNPC {

	public NPCJuliette() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Juliette"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "KfBsGKY8f6EkYsyjmC8Gcneb4/kmLK9Ui2imeRb43xlF1U+gatchbLsTfywMQlcD9hBxg/zU83oyfqtz1sQl9e/x2k9lU3ANl3zLfJ4sZrwmeKGnsBRdHRF2mC6skTIVYj1QF1AfAhHNdzuYKnx3A+oh559rXcPk8PAMYjo1FqOUA3nbYRzzG87w+oBVh5jP6h/p1o5b7viEab0ASfokEPFSCSCu/aq9vw7bxYYXzmCyHztDFaAHeVlwD8uK3zlrUYDiUsE1Ajw8w99Is1W8iWTWRV2iQ6YvJzsQH4LQ+eDjBoYHp5Nf8sgfaAlEf3Q3IvRCxfyt6PmFqCzBgHHvDWmw6xpF8rO00hHEB/Yda2i6zLwsyvsuXdYlQQp16Pq8EUSQAB41pK2fvr0vcdiieVkaGViQEkAYf3Ku2Wp6/iViq3HSVEPi7krPiZroaoVx05DIaZkuneRdXAtOGqJf6MGK2oLlIOgt/Pa/8BWGUTziNWvsdNkjfMZnyNDz/rN2Ky7LSeiXEr1YXBriRF0rYhArSS+VMVn2fH1rKilrwhfDW0GB+E483th1skxvqxHg4PdN1uW6FY90k9q49sPUYbOxqXIC8zO7XGyF9TMSpwzo67ZS2ux0sFXGJfe5Z4k/3YOhWaQTQ52ZfzJhJKN0G74Fj717JPeZdzATEt+sFrs=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NzI4NzgzNDY2MDIsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYyMWFhYmVkM2JhOGI4OGI4ZGUzNDNiMjY3YmM0OWIyZGYwNDY4YzBiYTRmMjBmZWI5ODUyYjc0YzIyZjMwMjAifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-415.500, 130.000, -121.500, 14, 0);
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
