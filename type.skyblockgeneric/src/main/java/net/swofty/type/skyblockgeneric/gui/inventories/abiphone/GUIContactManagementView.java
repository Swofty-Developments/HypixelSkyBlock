package net.swofty.type.skyblockgeneric.gui.inventories.abiphone;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.List;

public final class GUIContactManagementView implements View<GUIContactManagementView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString(
                (state, ctx) -> "Contact Management",
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Components.FILLER);
        layout.slot(4, (s, c) -> ItemStackCreator.updateLore(
                s.npc().getIcon().set(DataComponents.CUSTOM_NAME, Component.text("§f" + s.npc().getName())),
                List.of("§7" + s.npc().getDescription())
        ));

        layout.slot(31, (s, c) -> ItemStackCreator.getStack(
                "§cRemove Contact",
                Material.FEATHER,
                1,
                List.of(
                        "§7In case you're no longer friends, or",
                        "§7whatever other reason.",
                        " ",
                        "§eClick to remove!"
                )
        ), (click, viewCtx) -> {
            state.abiphone().getAttributeHandler().removeAbiphoneNPC(state.npc());
            viewCtx.player().sendMessage(Component.text("§c✆ Removed " + state.npc().getName() + " §cfrom your contacts!"));

            if (!viewCtx.pop()) {
                viewCtx.player().closeInventory();
            }
        });

        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, (s, c) -> Components.BACK_BUTTON, (click, viewCtx) -> {
                viewCtx.push(new AbiphoneView(), new AbiphoneView.State(state.abiphone()));
            });
        }

        Components.close(layout, 49);
    }

    public record State(SkyBlockItem abiphone, AbiphoneNPC npc) {}
}

