package net.swofty.type.skyblockgeneric.abiphone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
@AllArgsConstructor
public abstract class AbiphoneNPC {
	private final String id;
	private final String name;
	private final String description;

	public abstract void onCall(HypixelPlayer player);

	public abstract ItemStack.Builder getIcon();

}
