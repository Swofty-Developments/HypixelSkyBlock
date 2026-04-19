package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.collectibles.CollectibleDescriptionService;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleCatalog;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleStateService;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GUIBedWarsCollectiblePurchaseConfirm implements View<GUIBedWarsCollectiblePurchaseConfirm.State> {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Confirm Purchase", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        Optional<CollectibleDefinition> optionalDefinition = BedWarsCollectibleCatalog.findItemById(state.collectibleId());
        if (optionalDefinition.isEmpty()) {
            layout.slot(13, ItemStackCreator.getStack(
                "§cCollectible Missing",
                Material.BARRIER,
                1,
                "§7Unable to resolve this collectible.",
                "§7Please reopen the cosmetics menu."
            ));
            layout.slot(15, ItemStackCreator.getStack("§cClose", Material.BARRIER, 1), (click, context) -> context.backOrClose());
            return;
        }

        CollectibleDefinition definition = optionalDefinition.get();

        layout.slot(13, (s, c) -> buildDisplayStack(definition, state.cost()));

        layout.slot(11,
            (s, c) -> ItemStackCreator.getStack(
                "§aConfirm Purchase",
                Material.LIME_TERRACOTTA,
                1,
                "§7Unlock: §f" + definition.name(),
                "§7Cost: §2" + StringUtility.commaify(state.cost()) + " Tokens",
                "",
                "§eClick to purchase and equip!"
            ),
            (click, context) -> {
                if (!(click.click() instanceof Click.Left || click.click() instanceof Click.Right)) {
                    return;
                }

                BedWarsCollectibleStateService.SelectionResult result =
                    BedWarsCollectibleStateService.purchaseAndSelect(context.player(), definition);
                context.player().sendMessage(result.message());

                if (result.success()) {
                    context.pop();
                }
            }
        );

        layout.slot(15,
            ItemStackCreator.getStack(
                "§cCancel",
                Material.RED_TERRACOTTA,
                1,
                "§7Return to the cosmetics menu."
            ),
            (click, context) -> context.pop()
        );
    }

    private static net.minestom.server.item.ItemStack.Builder buildDisplayStack(CollectibleDefinition definition, long cost) {
        List<Component> lore = new ArrayList<>(CollectibleDescriptionService.resolveLore(definition));
        if (!lore.isEmpty()) {
            lore.add(Component.empty());
        }
        lore.add(legacy("§7Cost: §2" + StringUtility.commaify(cost) + " Tokens"));

        Component title = legacy("§a" + definition.name() + " ");

        if (definition.iconTexture() != null && !definition.iconTexture().isBlank()) {
            return ItemStackCreator.getStackHead(title, definition.iconTexture(), 1, lore);
        }
        Material material = definition.iconMaterial() != null ? definition.iconMaterial() : Material.BARRIER;
        return ItemStackCreator.getStack(title, material, 1, lore);
    }

    private static Component legacy(String text) {
        return LEGACY.deserialize(text);
    }

    public record State(String collectibleId, long cost) {
    }
}
