package net.swofty.commons.skyblock.item.attribute.attributes;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemAttributeSandboxItem extends ItemAttribute<ItemAttributeSandboxItem.SandboxData> {
    @Override
    public String getKey() {
        return "sandboxdata";
    }

    @Override
    public SandboxData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new SandboxData();
    }

    @Override
    public SandboxData loadFromString(String string) {
        SandboxData data = new SandboxData();
        if (string.isEmpty())
            return data;
        String[] split = string.split("---");

        data.setDisplayName(split[0]);
        data.setShowLoreLinesToggle(Boolean.parseBoolean(split[1]));
        data.setStatistics(ItemStatistics.fromString(split[2]));
        data.setMaterial(ItemType.valueOf(split[3]));

        List<String> lore = new ArrayList<>(Arrays.asList(split).subList(4, split.length));

        data.setLore(lore);

        return data;
    }

    @Override
    public String saveIntoString() {
        SandboxData data = getValue();
        StringBuilder builder = new StringBuilder();

        builder.append(data.getDisplayName()).append("---");
        builder.append(data.isShowLoreLinesToggle()).append("---");
        builder.append(data.getStatistics().toString()).append("---");
        builder.append(data.getMaterial().name()).append("---");

        for (String s : data.getLore()) {
            builder.append(s).append("---");
        }

        return builder.toString();
    }

    @Getter
    @Setter
    public static class SandboxData {
        private List<String> lore = new ArrayList<>(Arrays.asList(
                "§7Use this item as a base for your custom items!",
                " ",
                "§e/setitemname <name>",
                "Set the name of the item",
                "§e/setitemlore <line_number> <lore>",
                "Set the lore of the item",
                "§e/setitemstatistic <statistic> <value>",
                "Set the statistic of the item",
                "§e/removeitemlore <line_number>",
                "Remove a line of lore from the item",
                "§e/togglelorelines",
                "Toggle the visibility of lore lines helper",
                "§e/setitemtype <item_type>",
                "Set the display type of the item"
        ));
        private String displayName = "§6Sandbox Item";
        private ItemStatistics statistics = ItemStatistics.empty();
        private boolean showLoreLinesToggle = true;
        private ItemType material = ItemType.AIR;
    }
}
