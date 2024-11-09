package net.swofty.types.generic.item.handlers.anvilcombine;

import net.swofty.commons.item.PotatoType;
import net.swofty.commons.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.EnchantableComponent;
import net.swofty.types.generic.item.components.HotPotatoableComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnvilCombineRegistry {
    private static final Map<String, AnvilCombineHandler> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("HOT_POTATO_BOOK", new AnvilCombineHandler(
                (upgradeItem, sacrificeItem) -> {
                    HotPotatoableComponent hotPotatoable = upgradeItem.getComponent(HotPotatoableComponent.class);
                    PotatoType potatoType = hotPotatoable.getPotatoType();

                    ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
                    upgradeData.addAmount(1);
                    upgradeData.setPotatoType(potatoType);
                    upgradeItem.getAttributeHandler().setHotPotatoBookData(upgradeData);
                },
                (player, upgradeItem, sacrificeItem) -> {
                    if (upgradeItem.hasComponent(HotPotatoableComponent.class)) {
                        HotPotatoableComponent hotPotatoable = upgradeItem.getComponent(HotPotatoableComponent.class);
                        ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
                        return upgradeData.getAmount() < 10;
                    }
                    return false;
                },
                (SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) -> 0
        ));
        register("ENCHANTED_BOOK", new AnvilCombineHandler(
                (upgradeItem, sacrificeItem) -> {
                    // Remove existing enchantments
                    List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                    enchantments.forEach(enchantment -> upgradeItem.getAttributeHandler().removeEnchantment(enchantment.type()));

                    // Add new enchantments
                    enchantments.forEach(enchantment -> {
                        upgradeItem.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(
                                enchantment.type(),
                                enchantment.level()));
                    });
                },
                ((player, upgradeItem, sacrificeItem) -> {
                    if (upgradeItem.hasComponent(EnchantableComponent.class)) {
                        EnchantableComponent enchantable = upgradeItem.getComponent(EnchantableComponent.class);
                        List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                        Set<EnchantItemGroups> sourceTypes = enchantments.stream()
                                .flatMap(enchantment -> enchantment.type().getEnch().getGroups().stream()).collect(Collectors.toSet());

                        List<EnchantItemGroups> applicableTypes = enchantable.getEnchantItemGroups();
                        return sourceTypes.stream().anyMatch(applicableTypes::contains);
                    }
                    return false;
                }),
                (SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) -> {
                    List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                    return enchantments.stream()
                            .mapToInt(enchant -> enchant.type().getApplyCost(enchant.level(), player))
                            .sum();
                }
        ));
    }

    public static void register(String id, AnvilCombineHandler handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static AnvilCombineHandler getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}
