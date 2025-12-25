package net.swofty.commons.skyblock.item;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UnderstandableSkyBlockEnchantment {
    private String enchantmentType;
    private int level;

    public UnderstandableSkyBlockEnchantment(String enchantmentType, int level) {
        this.enchantmentType = enchantmentType;
        this.level = level;
    }

    public String serialize() {
        return enchantmentType + ":" + level;
    }

    public static UnderstandableSkyBlockEnchantment deserialize(String string) {
        String[] split = string.split(":");
        return UnderstandableSkyBlockEnchantment.builder()
                .enchantmentType(split[0])
                .level(Integer.parseInt(split[1]))
                .build();
    }

    public record ItemEnchantments(List<UnderstandableSkyBlockEnchantment> enchantments) {
        public void addEnchantment(UnderstandableSkyBlockEnchantment enchantment) {
            enchantments.add(enchantment);
        }
    }
}
