package net.swofty.type.skyblockgeneric.block.attribute.attributes;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.skyblockgeneric.block.attribute.BlockAttribute;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class BlockAttributeBrewingData extends BlockAttribute<BlockAttributeBrewingData.BrewingData> {

    @Override
    public String getKey() {
        return "brewing_data";
    }

    @Override
    public BrewingData getDefaultValue(@Nullable Class<? extends CustomSkyBlockBlock> blockClass) {
        return new BrewingData();
    }

    @Override
    public BrewingData loadFromString(String string) {
        if (string == null || string.isEmpty()) {
            return new BrewingData();
        }
        return BrewingData.deserialize(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.serialize();
    }

    @Getter
    @Setter
    public static class BrewingData {
        private SkyBlockItem ingredient;
        private SkyBlockItem[] potionSlots = new SkyBlockItem[3];
        private long brewingStartTime = -1;
        private int brewingDurationSeconds = 0;

        public BrewingData() {
            for (int i = 0; i < 3; i++) {
                potionSlots[i] = null;
            }
        }

        public boolean isBrewing() {
            return brewingStartTime > 0;
        }

        public long getRemainingTimeMs() {
            if (!isBrewing()) return 0;
            long elapsed = System.currentTimeMillis() - brewingStartTime;
            long total = brewingDurationSeconds * 1000L;
            return Math.max(0, total - elapsed);
        }

        public boolean isBrewingComplete() {
            return isBrewing() && getRemainingTimeMs() <= 0;
        }

        public String serialize() {
            StringBuilder sb = new StringBuilder();

            // Serialize ingredient
            sb.append(ingredient != null ? ingredient.toUnderstandable().serialize() : "null");
            sb.append("|");

            // Serialize potion slots
            for (int i = 0; i < 3; i++) {
                sb.append(potionSlots[i] != null ? potionSlots[i].toUnderstandable().serialize() : "null");
                sb.append("|");
            }

            // Serialize timing data
            sb.append(brewingStartTime);
            sb.append("|");
            sb.append(brewingDurationSeconds);

            return sb.toString();
        }

        public static BrewingData deserialize(String data) {
            BrewingData brewingData = new BrewingData();

            if (data == null || data.isEmpty()) {
                return brewingData;
            }

            String[] parts = data.split("\\|");
            if (parts.length >= 6) {
                // Deserialize ingredient
                if (!parts[0].equals("null")) {
                    brewingData.ingredient = new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(parts[0]));
                }

                // Deserialize potion slots
                for (int i = 0; i < 3; i++) {
                    if (!parts[i + 1].equals("null")) {
                        brewingData.potionSlots[i] = new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(parts[i + 1]));
                    }
                }

                // Deserialize timing data
                brewingData.brewingStartTime = Long.parseLong(parts[4]);
                brewingData.brewingDurationSeconds = Integer.parseInt(parts[5]);
            }

            return brewingData;
        }
    }
}
