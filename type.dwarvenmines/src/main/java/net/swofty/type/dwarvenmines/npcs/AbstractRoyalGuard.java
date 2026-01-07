package net.swofty.type.dwarvenmines.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.function.Function;
import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;
import org.jspecify.annotations.NonNull;

public class AbstractRoyalGuard extends HypixelNPC {

	public AbstractRoyalGuard(Function<HypixelPlayer, String> signature, Function<HypixelPlayer, String> texture, Function<HypixelPlayer, Pos> position) {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§6Royal Guard", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return signature.apply(player);
			}

			@Override
			public String texture(HypixelPlayer player) {
				return texture.apply(player);
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return position.apply(player);
			}

			@Override
			public @NonNull String chatName() {
				return "§6Royal Guard";
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.player();
		if (isInDialogue(player)) return;
		setDialogue(player, String.valueOf((int) (Math.random() * 5 + 1)));
	}

	@Override
	protected DialogueSet[] dialogues(HypixelPlayer player) {
		return Stream.of(
				DialogueSet.builder()
						.key("1")
						.lines(new String[]{
								"§fThere are no miners like the dwarves."
						})
						.build(),
				DialogueSet.builder()
						.key("2")
						.lines(new String[]{
								"§fSTAND BACK!",
						})
						.build(),
				DialogueSet.builder()
						.key("3")
						.lines(new String[]{
								"§fGreat treasures lie ahead, but at what cost?"
						})
						.build(),
				DialogueSet.builder()
						.key("4")
						.lines(new String[]{
								"§fBehold a fellow holder of Heart of the Mountain!"
						})
						.build(),
				DialogueSet.builder()
						.key("5")
						.lines(new String[]{
								"§fWatch yourself, traveller. There are creatures about."
						})
						.build()
		).toArray(DialogueSet[]::new);
	}
}