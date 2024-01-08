package net.swofty.types.generic.enchantment;

import lombok.Builder;

import java.util.List;

@Builder
public record SkyBlockEnchantment(EnchantmentType type, int level) {

    public String serialize() {
        return type.name() + ":" + level;
    }

    public static SkyBlockEnchantment deserialize(String string) {
        String[] split = string.split(":");
        return SkyBlockEnchantment.builder()
                .type(EnchantmentType.valueOf(split[0]))
                .level(Integer.parseInt(split[1]))
                .build();
    }

    public record ItemEnchantments(List<SkyBlockEnchantment> enchantments) {
        public void addEnchantment(SkyBlockEnchantment enchantment) {
            enchantments.add(enchantment);
        }
    }
}
