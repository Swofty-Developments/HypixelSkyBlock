package net.swofty.type.skyblockgeneric.gui.inventories.abiphone;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;

import java.util.Locale;

public class GUIConfirmAbiphone extends HypixelInventoryGUI {

	private final AbiphoneNPC npc;
	private final Runnable onAccept;

	public GUIConfirmAbiphone(AbiphoneNPC npc, Runnable onAccept) {
		super(I18n.t("gui_abiphone.confirm.title"), InventoryType.CHEST_3_ROW);
		this.npc = npc;
		this.onAccept = onAccept;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		set(new GUIClickableItem(11) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				onAccept.run();
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return TranslatableItemStackCreator.getStack("gui_abiphone.confirm.confirm_button", Material.GREEN_TERRACOTTA, 1,
                    "gui_abiphone.confirm.confirm_button.lore", Component.text(npc.getName()));
			}
		});
		set(new GUIClickableItem(15) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.closeInventory();
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				Locale l = player.getLocale();
				return ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, I18n.string("gui_abiphone.confirm.cancel_button", l));
			}
		});
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
