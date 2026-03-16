package net.swofty.type.backwaterbayou.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.fishing.FishingItemCatalog;
import net.swofty.type.skyblockgeneric.fishing.FishingRodDefinition;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIFishingRodParts extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Fishing Rod Parts", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onClose(DefaultState state, ViewContext ctx, ViewSession.CloseReason reason) {
        ItemStack rodItem = ctx.inventory().getItemStack(21);
        if (!rodItem.isAir()) {
            SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
            player.addAndUpdateItem(new SkyBlockItem(rodItem));
            ctx.inventory().setItemStack(21, ItemStack.AIR);
        }
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);

        layout.editable(21, (_, _) -> ItemStackCreator.createNamedItemStack(
            Material.FISHING_ROD,
            "§7Place your §aFishing Rod §7here!"
        ), (_, oldItem, newItem, _) -> {
            if (newItem.isAir()) return;
            SkyBlockItem rod = new SkyBlockItem(newItem);
            ItemType type = rod.getAttributeHandler().getPotentialType();
            FishingRodDefinition def = type == null ? null : FishingItemCatalog.getRod(type.name());
            if (def == null || !def.rodPartsEnabled()) {
                SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
                player.sendMessage("§cThat fishing rod does not support rod parts.");
                ctx.inventory().setItemStack(21, oldItem);
                if (!newItem.isAir()) {
                    player.addAndUpdateItem(new SkyBlockItem(newItem));
                }
            }
        });

        layout.slot(22, ItemStackCreator.getStack(
            "§9ථ Hook",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            1,
            "§7Place a §aFishing Rod §7in the slot to the",
            "§7left to view and modify its §9Hook§7!",
            "",
            "§eClick to browse Hooks!"
        ), (_, viewCtx) -> viewCtx.push(new GUIHook()));

        layout.slot(23, ItemStackCreator.getStack(
            "§9ꨃ Line",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            1,
            "§7Place a §aFishing Rod §7in the slot to the",
            "§7left to view and modify its §9Line§7!",
            "",
            "§eClick to browse Lines!"
        ), (_, viewCtx) -> viewCtx.push(new GUILine()));

        layout.slot(24, ItemStackCreator.getStack(
            "§9࿉ Sinker",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            1,
            "§7Place a §aFishing Rod §7in the slot to the",
            "§7left to view and modify its §9Sinker§7!",
            "",
            "§eClick to browse Sinkers!"
        ), (_, viewCtx) -> viewCtx.push(new GUISinker()));

        layout.slot(50, ItemStackCreator.getStack(
            "§9Rod Part Guide",
            Material.BOOK,
            1,
            "§7View all of the §9Rod Parts §7that can be",
            "§7applied to §aFishing Rods§7! Can also be",
            "§7accessed with §a/rodparts§7!",
            "",
            "§eClick to view!"
        ), (_, viewCtx) -> viewCtx.push(new GUIRodPartGuide()));
    }
}
