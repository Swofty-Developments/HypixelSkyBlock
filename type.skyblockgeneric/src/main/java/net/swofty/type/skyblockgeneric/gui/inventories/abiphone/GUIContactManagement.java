package net.swofty.type.skyblockgeneric.gui.inventories.abiphone;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.List;

public class GUIContactManagement extends HypixelInventoryGUI {

	private final SkyBlockItem abiphone;
	private final AbiphoneNPC npc;

	public GUIContactManagement(SkyBlockItem abiphone, AbiphoneNPC npc) {
		super("Contact Management", InventoryType.CHEST_6_ROW);
		this.abiphone = abiphone;
		this.npc = npc;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);

		set(new GUIItem(4) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.updateLore(
						npc.getIcon().set(DataComponents.CUSTOM_NAME, Component.text("§f" + npc.getName())),
						List.of("§7" + npc.getDescription())
				);
			}
		});

		set(new GUIClickableItem(31) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				new GUIConfirmAbiphone(npc, () -> {
					abiphone.getAttributeHandler().removeAbiphoneNPC(npc);
					player.closeInventory();
					new GUIAbiphone(abiphone).open(player);
					player.sendMessage("§c✆ Removed " + npc.getName() + " §cfrom your contacts!");
				}).open(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§cRemove Contact", Material.FEATHER, 1, List.of("§7In case you're no longer friends, or", "§7whatever other reason.", " ", "§eClick to remove!"));
			}
		});

		GUIClickableItem.getGoBackItem(48, new GUIAbiphone(abiphone));
		GUIClickableItem.getCloseItem(49);
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}

