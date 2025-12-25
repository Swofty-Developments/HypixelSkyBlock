package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.bow.BowHandler;
import net.swofty.type.skyblockgeneric.item.handlers.bow.BowRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

@Getter
public class BowComponent extends SkyBlockItemComponent {
    private final BowHandler shootHandler;
    private final boolean shouldBeArrow;

    public BowComponent(BowHandler shootHandler, boolean shouldBeArrow) {
        this.shootHandler = shootHandler;
        this.shouldBeArrow = shouldBeArrow;
        addInheritedComponent(new ExtraRarityComponent("BOW"));
        addInheritedComponent(new QuiverDisplayComponent(shouldBeArrow));
        addInheritedComponent(new ReforgableComponent(ReforgeType.BOWS));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.BOW), true));
        addInheritedComponent(new RuneableComponent(RuneableComponent.RuneApplicableTo.BOWS));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    public BowComponent(String handlerId, boolean shouldBeArrow) {
        this(BowRegistry.getHandler(handlerId), shouldBeArrow);
    }

    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item, double power) {
        if (shootHandler != null) {
            shootHandler.onShoot(player, item, power);
        }
    }
}