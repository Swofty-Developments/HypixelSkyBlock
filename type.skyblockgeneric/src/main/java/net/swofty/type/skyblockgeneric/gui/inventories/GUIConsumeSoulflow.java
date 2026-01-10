package net.swofty.type.skyblockgeneric.gui.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.SoulflowComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

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
		return new ViewConfiguration<>("Consume Soulflow", InventoryType.CHEST_4_ROW);
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

					return ItemStackCreator.getStackHead(
							"§aConsume Soulflow?",
							"94f0c693b85658b0bae792c9f9b717eb024ab8c4b349455648ea08358b50ddc4",
							1,
							"§7Takes all the §3⸎ Soulflow §7items in",
							"§7your inventory and internalizes them",
							"§7to be ready for use.",
							"",
							"§7Internalized: §3" + soulflow + "⸎",
							"",
							"§7Adding from inventory: §3+" + addition + "⸎ Soulflow",
							"",
							"§eClick to consume!"
					);
				},
				(click, viewCtx) -> {
					SkyBlockPlayer player = (SkyBlockPlayer) viewCtx.player();
					SkyBlockDataHandler data = player.getSkyblockDataHandler();
					int soulflow = data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).getValue();

					int itemSoulflow = click.state().item().getComponent(SoulflowComponent.class).getAmount();
					int addition = click.state().item().getAmount() * itemSoulflow;

					data.get(SkyBlockDataHandler.Data.SOULFLOW, DatapointInteger.class).setValue(soulflow + addition);
					player.sendMessage("§bYou internalized §3+" + addition + "⸎ Soulflow §band have a total of §3" + (soulflow + addition) + "⸎§b!");

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
