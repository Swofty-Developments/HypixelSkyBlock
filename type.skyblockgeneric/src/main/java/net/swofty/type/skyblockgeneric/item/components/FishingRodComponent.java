package net.swofty.type.skyblockgeneric.item.components;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.swofty.type.skyblockgeneric.entity.FishingHook;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class FishingRodComponent extends SkyBlockItemComponent {

	public FishingRodComponent() {
		addInheritedComponent(new InteractableComponent(
				null,
				(player, item) -> {
					FishingHook hook = FishingHook.getFishingHookForOwner(player);
					if (hook != null) {
						hook.remove();
						player.playSound(
								Sound.sound()
										.type(Key.key("entity.fishing_bobber.retrieve"))
										.source(Sound.Source.NEUTRAL)
										.volume(1f)
										.pitch(0.8f) // the pitch is 0.8-1.2
										.build()
						);
					} else {
						new FishingHook(player).spawn(player.getInstance());
					}
				},
				null
		));
	}

}
