package gg.itzkatze.thehypixelrecreationmod.features;

import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ClipboardUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ItemStackUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CopyCurrentGui {
    private static final Item BLACK_STAINED_GLASS =
            BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace("black_stained_glass"));

    private CopyCurrentGui() {
    }

    public static void copyCurrentGui(Minecraft client) {
        if (client.player == null) {
            return;
        }

        if (!(client.gui.screen() instanceof AbstractContainerScreen<?> screen)) {
            ChatUtils.warn("Not in a container GUI!");
            return;
        }

        String guiTitle = StringUtility.toLegacyString(screen.getTitle());
        String cleanTitle = StringUtility.stripColor(guiTitle);

        List<Slot> containerSlots = getContainerSlots(screen);
        String inventoryType = getInventoryType(containerSlots.size());

        StringBuilder code = new StringBuilder();
        String className = "GUI" + toPascalCase(cleanTitle);

        List<Integer> fillerSlots = new ArrayList<>();
        Integer closeItemSlot = null;
        List<String> itemEntries = new ArrayList<>();

        for (Slot slot : containerSlots) {
            ItemStack stack = slot.getItem();
            if (stack.isEmpty()) continue;

            int slotIndex = slot.index;

            if (stack.is(BLACK_STAINED_GLASS)) {
                fillerSlots.add(slotIndex);
                continue;
            }

            if (stack.is(Items.BARRIER)) {
                closeItemSlot = slotIndex;
                continue;
            }

            String itemEntry = generateGUIItem(slotIndex, stack);
            itemEntries.add(itemEntry);
        }

        boolean isBorder = isBorderPattern(fillerSlots, containerSlots.size());

        code.append("public class ").append(className).append(" extends StatelessView {\n\n");
        code.append("    @Override\n");
        code.append("    public ViewConfiguration<DefaultState> configuration() {\n");
        code.append("        return new ViewConfiguration<>(\"").append(StringUtility.escapeJavaString(guiTitle)).append("\", InventoryType.").append(inventoryType).append(");\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {\n");

        if (!fillerSlots.isEmpty()) {
            if (isBorder) {
                int lastSlot = containerSlots.size() - 1;
                code.append("        layout.filler(Layouts.border(0, ").append(lastSlot).append("), Components.FILLER);\n");
            } else {
                code.append("        Components.fill(layout);\n");
            }
        }

        if (closeItemSlot != null) {
            code.append("        Components.close(layout, ").append(closeItemSlot).append(");\n");
        }

        if (!fillerSlots.isEmpty() || closeItemSlot != null) {
            code.append("\n");
        }

        for (String entry : itemEntries) {
            code.append(entry);
        }

        code.append("    }\n");
        code.append("}\n");

        String generatedCode = code.toString();
        ClipboardUtils.setClipboard(generatedCode);

        Component message = Component.literal("Copied GUI Code for: " + cleanTitle)
                .withStyle(style -> style
                        .withColor(ChatFormatting.AQUA)
                        .withClickEvent(new ClickEvent.CopyToClipboard(generatedCode))
                        .withHoverEvent(new HoverEvent.ShowText(
                                Component.literal("Click to copy again\n\n" + className + ".java")
                        ))
                );

        ChatUtils.send(message);
    }

    private static List<Slot> getContainerSlots(AbstractContainerScreen<?> screen) {
        List<Slot> containerSlots = new ArrayList<>();
        var menu = screen.getMenu();

        int totalSlots = menu.slots.size();
        int playerInventorySize = 36; // 27 main + 9 hotbar

        int containerSize = totalSlots - playerInventorySize;
        if (containerSize < 0) {
            containerSize = totalSlots;
        }

        for (int i = 0; i < containerSize && i < menu.slots.size(); i++) {
            containerSlots.add(menu.slots.get(i));
        }

        return containerSlots;
    }

    private static String getInventoryType(int slotCount) {
        return switch (slotCount) {
            case 9 -> "CHEST_1_ROW";
            case 18 -> "CHEST_2_ROW";
            case 27 -> "CHEST_3_ROW";
            case 36 -> "CHEST_4_ROW";
            case 45 -> "CHEST_5_ROW";
            default -> "CHEST_6_ROW";
        };
    }

    private static String generateGUIItem(int slotIndex, ItemStack stack) {
        StringBuilder sb = new StringBuilder();
        String displayName = ItemStackUtils.getDisplayNameLegacy(stack);

        String cleanName = StringUtility.stripColor(displayName);
        if (cleanName.trim().isEmpty()) {
            return "";
        }

        String material = ItemStackUtils.toMinestomMaterial(stack);
        List<String> lore = ItemStackUtils.getLoreAsStrings(stack);
        int count = stack.getCount();
        boolean isPlayerHead = stack.is(Items.PLAYER_HEAD);
        String texture = isPlayerHead ? ItemStackUtils.getPlayerHeadTexture(stack) : "";

        if (isPlayerHead && !texture.isEmpty()) {
            sb.append("        layout.slot(").append(slotIndex).append(", ItemStackCreator.getStackHead(\n");
            sb.append("                \"").append(StringUtility.escapeJavaString(displayName)).append("\",\n");
            sb.append("                \"").append(StringUtility.escapeJavaString(texture)).append("\",\n");
            sb.append("                ").append(count);
        } else {
            sb.append("        layout.slot(").append(slotIndex).append(", ItemStackCreator.getStack(\n");
            sb.append("                \"").append(StringUtility.escapeJavaString(displayName)).append("\",\n");
            sb.append("                Material.").append(material).append(",\n");
            sb.append("                ").append(count);
        }

        if (!lore.isEmpty()) {
            for (String loreLine : lore) {
                sb.append(",\n                \"").append(StringUtility.escapeJavaString(loreLine)).append("\"");
            }
        }

        sb.append("\n        ));\n");

        return sb.toString();
    }

    private static boolean isBorderPattern(List<Integer> fillerSlots, int totalSlots) {
        if (fillerSlots.isEmpty()) return false;

        int rows = totalSlots / 9;
        Set<Integer> borderPositions = new HashSet<>();
        for (int i = 0; i < totalSlots; i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                borderPositions.add(i);
            }
        }

        for (int slot : fillerSlots) {
            if (!borderPositions.contains(slot)) {
                return false;
            }
        }

        return true;
    }

    private static String toPascalCase(String s) {
        if (s == null || s.isEmpty()) return "Generated";
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            } else {
                capitalizeNext = true;
            }
        }
        return !result.isEmpty() ? result.toString() : "Generated";
    }
}
