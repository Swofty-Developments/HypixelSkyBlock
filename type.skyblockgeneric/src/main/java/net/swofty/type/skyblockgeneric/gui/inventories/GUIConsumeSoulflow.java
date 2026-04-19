package net.swofty.type.skyblockgeneric.gui.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.SoulflowComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Locale;

public class GUIConsumeSoulflow implements View<GUIConsumeSoulflow.State> {

	public record State(SkyBlockItem item) {
		public State {
			if (!item.hasComponent(SoulflowComponent.class)) {
				throw new IllegalArgumentException("Item does not have SoulflowComponent");
			}
		}
	}

	@Override
	public ViewConfiguration<State> configuration() {
		return ViewConfiguration.translatable("gui_misc.consume_soulflow.title", InventoryType.CHEST_4_ROW);
	}

	@Override
	public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
		Components.fill(layout);

		layout.slot(13, (s, viewCtx) -> {
					SkyBlockPlayer player = (SkyBlockPlayer) viewCtx.player();
					SkyBlockDataHandler data = player.getSkyblockDataHandler();
					int soulflow = data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).getValue();
					int itemSoulflow = s.item().getComponent(SoulflowComponent.class).getAmount();
					int addition = s.item().getAmount() * itemSoulflow;

                return TranslatableItemStackCreator.getStackHead("gui_misc.consume_soulflow.confirm_button",
							"94f0c693b85658b0bae792c9f9b717eb024ab8c4b349455648ea08358b50ddc4",
							1,
                    "gui_misc.consume_soulflow.confirm_button.lore",
                    Component.text(String.valueOf(soulflow)),
                    Component.text(String.valueOf(addition)));
				},
				(click, viewCtx) -> {
					SkyBlockPlayer player = (SkyBlockPlayer) viewCtx.player();
					SkyBlockDataHandler data = player.getSkyblockDataHandler();
					int soulflow = data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).getValue();

					int itemSoulflow = click.state().item().getComponent(SoulflowComponent.class).getAmount();
					int addition = click.state().item().getAmount() * itemSoulflow;

					data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).setValue(soulflow + addition);
					Locale l = player.getLocale();
                    player.sendMessage(I18n.t("gui_misc.consume_soulflow.consumed_message", Component.text(String.valueOf(addition)), Component.text(String.valueOf(soulflow + addition))));

					player.getInventory().setItemStack(player.getHeldSlot(), ItemStack.AIR);
					player.closeInventory();
				}
		);
		Components.close(layout, 31);
	}

	public static void open(SkyBlockPlayer player, SkyBlockItem item) {
		player.openView(new GUIConsumeSoulflow(), new State(item));
	}

}
