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
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.List;
import java.util.Map;

public class GUIContactManagement extends HypixelInventoryGUI {

	private final SkyBlockItem abiphone;
	private final AbiphoneNPC npc;

	public GUIContactManagement(SkyBlockItem abiphone, AbiphoneNPC npc) {
		super(I18n.string("gui_abiphone.management.title"), InventoryType.CHEST_6_ROW);
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
					player.sendMessage(I18n.string("gui_abiphone.management.removed_message", Map.of("npc_name", npc.getName())));
				}).open(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack(I18n.string("gui_abiphone.management.remove_contact"), Material.FEATHER, 1,
						I18n.lore("gui_abiphone.management.remove_contact.lore"));
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

