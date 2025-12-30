package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIConsumeSoulflow;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class SoulflowComponent extends SkyBlockItemComponent {

	@Getter
	private final int amount;

	public SoulflowComponent(int amount) {
		addInheritedComponent(
				new InteractableComponent((player, item) -> {
					new GUIConsumeSoulflow(item).open(player);
				}, null, null)
		);
		this.amount = amount;
	}

}
