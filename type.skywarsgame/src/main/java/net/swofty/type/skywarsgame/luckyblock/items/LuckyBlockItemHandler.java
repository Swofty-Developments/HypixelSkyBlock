package net.swofty.type.skywarsgame.luckyblock.items;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.swofty.type.skywarsgame.luckyblock.items.armor.*;
import net.swofty.type.skywarsgame.luckyblock.items.consumables.*;
import net.swofty.type.skywarsgame.luckyblock.items.usables.*;
import net.swofty.type.skywarsgame.luckyblock.items.weapons.*;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.tinylog.Logger;

public class LuckyBlockItemHandler {

    public static void registerAllItems() {
        LuckyBlockItemRegistry.register(new SpeedsterBoots());
        LuckyBlockItemRegistry.register(new ChillyPants());
        LuckyBlockItemRegistry.register(new LitBoots());
        LuckyBlockItemRegistry.register(new ElDoradoHelmet());
        LuckyBlockItemRegistry.register(new ElDoradoChestplate());
        LuckyBlockItemRegistry.register(new ElDoradoLeggings());
        LuckyBlockItemRegistry.register(new ElDoradoBoots());
        LuckyBlockItemRegistry.register(new Exodus());
        LuckyBlockItemRegistry.register(new FrogHelmet());
        LuckyBlockItemRegistry.register(new GreaserJacket());
        LuckyBlockItemRegistry.register(new ManitouBoots());
        LuckyBlockItemRegistry.register(new SplatcraftBoots());
        LuckyBlockItemRegistry.register(new CutePants());
        LuckyBlockItemRegistry.register(new DiscoHelmet());
        LuckyBlockItemRegistry.register(new DiscoChestplate());
        LuckyBlockItemRegistry.register(new DiscoLeggings());
        LuckyBlockItemRegistry.register(new DiscoBoots());
        LuckyBlockItemRegistry.register(new HotHead());
        LuckyBlockItemRegistry.register(new StingedLeggings());

        LuckyBlockItemRegistry.register(new AxeOfPerun());
        LuckyBlockItemRegistry.register(new TheStick());
        LuckyBlockItemRegistry.register(new SwordOfJustice());
        LuckyBlockItemRegistry.register(new Anduril());
        LuckyBlockItemRegistry.register(new BowAndArrows());
        LuckyBlockItemRegistry.register(new PunchBow());
        LuckyBlockItemRegistry.register(new Invisibow());
        LuckyBlockItemRegistry.register(new ExplosiveBow());
        LuckyBlockItemRegistry.register(new Railgun());
        LuckyBlockItemRegistry.register(new Shotgun());
        LuckyBlockItemRegistry.register(new WitherBlastRod());
        LuckyBlockItemRegistry.register(new CarrotCorrupter());
        LuckyBlockItemRegistry.register(new SelfAttackingSword());
        LuckyBlockItemRegistry.register(new Excalibur());
        LuckyBlockItemRegistry.register(new HappyLittleTree());
        LuckyBlockItemRegistry.register(new IronSwordWeapon());
        LuckyBlockItemRegistry.register(new KnockbackSlimeball());
        LuckyBlockItemRegistry.register(new OnePoundFish());
        LuckyBlockItemRegistry.register(new SwordOfElDorado());
        LuckyBlockItemRegistry.register(new TheSpoon());
        LuckyBlockItemRegistry.register(new WoodenSwordWeapon());
        LuckyBlockItemRegistry.register(new VomitBagel());
        LuckyBlockItemRegistry.register(new StickOfTruth());
        LuckyBlockItemRegistry.register(new Paintballs());
        LuckyBlockItemRegistry.register(new PigletBazooka());
        LuckyBlockItemRegistry.register(new MagicToyStick());

        LuckyBlockItemRegistry.register(new PlusHealthItem());
        LuckyBlockItemRegistry.register(new OneUpMushroom());
        LuckyBlockItemRegistry.register(new SuperStar());
        LuckyBlockItemRegistry.register(new MysteryMeat());
        LuckyBlockItemRegistry.register(new Cornucopia());
        LuckyBlockItemRegistry.register(new OPPotion());
        LuckyBlockItemRegistry.register(new UpgradeBook());
        LuckyBlockItemRegistry.register(new AbsorptionHearts());
        LuckyBlockItemRegistry.register(new FistUpgrade());

        LuckyBlockItemRegistry.register(new EnderTeleport());
        LuckyBlockItemRegistry.register(new Fireball());
        LuckyBlockItemRegistry.register(new DevilsContract());
        LuckyBlockItemRegistry.register(new VoidCharm());
        LuckyBlockItemRegistry.register(new JediForce());
        LuckyBlockItemRegistry.register(new BridgeEggs());
        LuckyBlockItemRegistry.register(new FrogsPotion());
        LuckyBlockItemRegistry.register(new PlayerTeleport());
        LuckyBlockItemRegistry.register(new SuperDie());
        LuckyBlockItemRegistry.register(new WaterBalloon());
        LuckyBlockItemRegistry.register(new ThrowableTNT());
        LuckyBlockItemRegistry.register(new BerserkersRock());
        LuckyBlockItemRegistry.register(new Jackhammer());
        LuckyBlockItemRegistry.register(new TNTLaunchPad());
        LuckyBlockItemRegistry.register(new ChestSpawn());

        Logger.info("Registered {} lucky block items", LuckyBlockItemRegistry.size());
    }

    public static void processTick(SkywarsPlayer player) {
        ItemStack mainHand = player.getItemInMainHand();
        LuckyBlockWeapon weapon = LuckyBlockItemRegistry.weaponFromItemStack(mainHand);
        if (weapon != null && weapon.hasPassiveBuff()) {
            weapon.onHeldTick(player);
        }

        processArmorTick(player, EquipmentSlot.HELMET);
        processArmorTick(player, EquipmentSlot.CHESTPLATE);
        processArmorTick(player, EquipmentSlot.LEGGINGS);
        processArmorTick(player, EquipmentSlot.BOOTS);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItemStack(i);
            LuckyBlockItem luckyItem = LuckyBlockItemRegistry.fromItemStack(item);
            if (luckyItem != null && luckyItem.hasTickEffect()) {
                boolean isHeld = i == player.getHeldSlot();
                luckyItem.onTick(player, isHeld);
            }
        }
    }

    private static void processArmorTick(SkywarsPlayer player, EquipmentSlot slot) {
        ItemStack armorPiece = player.getEquipment(slot);
        LuckyBlockArmor armor = LuckyBlockItemRegistry.armorFromItemStack(armorPiece);
        if (armor != null && armor.hasTickEffect()) {
            armor.onWornTick(player);
        }
    }

    public static float processHit(SkywarsPlayer attacker, Entity target, float damage) {
        ItemStack mainHand = attacker.getItemInMainHand();
        LuckyBlockWeapon weapon = LuckyBlockItemRegistry.weaponFromItemStack(mainHand);

        if (weapon != null && weapon.hasOnHitEffect()) {
            float modifiedDamage = weapon.onWeaponHit(attacker, target, damage);
            if (modifiedDamage >= 0) {
                damage = modifiedDamage;
            }
        }

        LuckyBlockItem item = LuckyBlockItemRegistry.fromItemStack(mainHand);
        if (item != null) {
            item.onHit(attacker, target);
        }

        return damage;
    }

    public static void processDamageReceived(SkywarsPlayer victim, Entity attacker, float damage) {
        processArmorDamage(victim, attacker, damage, EquipmentSlot.HELMET);
        processArmorDamage(victim, attacker, damage, EquipmentSlot.CHESTPLATE);
        processArmorDamage(victim, attacker, damage, EquipmentSlot.LEGGINGS);
        processArmorDamage(victim, attacker, damage, EquipmentSlot.BOOTS);

        ItemStack mainHand = victim.getItemInMainHand();
        LuckyBlockItem item = LuckyBlockItemRegistry.fromItemStack(mainHand);
        if (item != null) {
            item.onHitReceived(victim, attacker, damage);
        }
    }

    private static void processArmorDamage(SkywarsPlayer victim, Entity attacker, float damage, EquipmentSlot slot) {
        ItemStack armorPiece = victim.getEquipment(slot);
        LuckyBlockArmor armor = LuckyBlockItemRegistry.armorFromItemStack(armorPiece);
        if (armor != null && armor.hasDamageEffect()) {
            armor.onDamaged(victim, attacker, damage);
        }
    }

    public static boolean processItemUse(SkywarsPlayer player, ItemStack item, int heldSlot) {
        LuckyBlockItem luckyItem = LuckyBlockItemRegistry.fromItemStack(item);
        if (luckyItem == null || !luckyItem.hasUseEffect()) {
            return false;
        }

        boolean handled = luckyItem.onUse(player);

        if (handled && luckyItem instanceof LuckyBlockConsumable consumable) {
            if (consumable.shouldRemoveOnConsume()) {
                ItemStack current = player.getInventory().getItemStack(heldSlot);
                if (current.amount() > 1) {
                    player.getInventory().setItemStack(heldSlot, current.withAmount(current.amount() - 1));
                } else {
                    player.getInventory().setItemStack(heldSlot, ItemStack.AIR);
                }
            }
        }

        return handled;
    }

    public static boolean processItemUse(SkywarsPlayer player, ItemStack item) {
        return processItemUse(player, item, player.getHeldSlot());
    }

    public static void processArmorEquip(SkywarsPlayer player, ItemStack armor, EquipmentSlot slot) {
        LuckyBlockArmor luckyArmor = LuckyBlockItemRegistry.armorFromItemStack(armor);
        if (luckyArmor != null && luckyArmor.getSlot() == slot) {
            luckyArmor.onEquip(player);
        }
    }

    public static void processArmorUnequip(SkywarsPlayer player, ItemStack armor, EquipmentSlot slot) {
        LuckyBlockArmor luckyArmor = LuckyBlockItemRegistry.armorFromItemStack(armor);
        if (luckyArmor != null && luckyArmor.getSlot() == slot) {
            luckyArmor.onUnequip(player);
        }
    }
}
