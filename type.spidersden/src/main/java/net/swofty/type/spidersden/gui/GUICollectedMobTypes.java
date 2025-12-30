package net.swofty.type.spidersden.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.MobRegistry;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUICollectedMobTypes extends HypixelInventoryGUI {

    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final int combatXPReward = 1000;
    private final int coinReward = 1000;
    private final int sbXPReward = 1;

    public GUICollectedMobTypes() {
        super("Collected Mob Types", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));


        int index = 0;
        for (MobTypes mobType : MobTypes.values()) {
            set(new GUIClickableItem(SLOTS[index]) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (player.getCollectedMobTypesData().hasClaimed(mobType.mobType.name())) return;
                    boolean hasUnlocked = false;
                    for (String id : player.getBestiaryData().getMobs().keySet()) {
                        BestiaryMob mob = MobRegistry.getMobById(id);
                        if (mob != null && mob.getMobTypes().contains(mobType.mobType)) hasUnlocked = true;
                    }
                    if (!hasUnlocked) return;

                    player.getSkills().increase(player, SkillCategories.COMBAT, 1000D);
                    player.addCoins(1000);
                    player.getCollectedMobTypesData().claim(mobType.mobType.name());

                    new GUICollectedMobTypes().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    boolean hasClaimed = player.getCollectedMobTypesData().hasClaimed(mobType.name());
                    boolean hasUnlocked = false;
                    for (String id : player.getBestiaryData().getMobs().keySet()) {
                        BestiaryMob mob = MobRegistry.getMobById(id);
                        if (mob != null && mob.getMobTypes().contains(mobType.mobType)) hasUnlocked = true;
                    }

                    ArrayList<String> lore = new ArrayList<>(List.of(mobType.lore));

                    lore.add("");

                    if (!hasClaimed) {
                        lore.add("§7Rewards:");
                        lore.add(" §8+§3" + StringUtility.commaify(combatXPReward) + " §7Combat Experience");
                        lore.add(" §8+§6" + StringUtility.commaify(coinReward) + " Coins");
                        lore.add(" §8+§b" + sbXPReward + " SkyBlock XP");
                        lore.add("");
                    } else {
                        lore.add("§e§lREWARDS CLAIMED");
                    }
                    if (hasUnlocked && !hasClaimed) {
                        lore.add("§a§lCLICK TO CLAIM!");
                    }
                    if (!hasUnlocked && !hasClaimed) {
                        lore.add("§cKill an enemy of this type to unlock");
                        lore.add("§cthese rewards!");
                    }

                    if (hasUnlocked) {
                        return ItemStackCreator.getUsingGUIMaterial(mobType.mobType.getFullDisplayName(), mobType.guiMaterial, 1, lore);
                    } else {
                        return ItemStackCreator.getStack("§7" + mobType.mobType.getSymbol() + " " + mobType.mobType.getDisplayName(), Material.GRAY_DYE, 1, lore);
                    }
                }
            });
            index++;
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }

    private enum MobTypes {
        AIRBORNE(MobType.AIRBORNE, new GUIMaterial("6681a72da7263ca9aef066542ecca7a180c40e328c0463fcb114cb3b83057552"),
                "§7Most commonly found flying around.",
                "§7Enemies of this type take increased",
                "§7damage from the §9Gravity §7enchantment."
        ),
        ANIMAL(MobType.ANIMAL, new GUIMaterial("9b1760e3778f8087046b86bec6a0a83a567625f30f0d6bce866d4bed95dba6c1"),
                "§7Enemies of this type are typically",
                "§7found in §bThe Barn§7."
        ),
        AQUATIC(MobType.AQUATIC, new GUIMaterial("32581d564f01d712255125e1f101e534217f76e3599dab7f4ae0ffe328f729eb"),
                "§7Most commonly found when §9fishing§7.",
                "§7Enemies of this type take increased",
                "§7damage from the §9Impaling",
                "§7enchantment."
        ),
        ARCANE(MobType.ARCANE, new GUIMaterial("fce6604157fc4ab5591e4bcf507a749918ee9c41e357d47376e0ee7342074c90"),
                "§7Enemies of this type are typically",
                "§7found in the §cCrimson Isle§7."
        ),
        ARTHROPOD(MobType.ARTHROPOD, new GUIMaterial("35e248da2e108f09813a6b848a0fcef111300978180eda41d3d1a7a8e4dba3c3"),
                "§7Enemies of this type are typically",
                "§7found in the §cSpider's Den§7. Enemies of",
                "§7this type take increased damage",
                "§7from the §9Bane of Arthropods",
                "§7enchantment."
        ),
        CONSTRUCT(MobType.CONSTRUCT, new GUIMaterial("4271913a3fc8f56bdf6b90a4b4ed6a05c562ce0076b5344d444fb2b040ae57d"),
                "§7Enemies of this type are typically",
                "§7summoned by other enemies - or by",
                "§7players."
        ),
        CUBIC(MobType.CUBIC, new GUIMaterial("6061f98aaff1e406e06f6f13ffe06005873f3d1eaddb1b59e19dda0ed9ffb270"),
                "§7Enemies of this type are typically",
                "§7found in the §bDeep Caverns§7. Enemies",
                "§7of this type take increased damage",
                "§7from the §9Cubism §7enchantment."
        ),
        ELUSIVE(MobType.ELUSIVE, new GUIMaterial("81d2116827a41a713660bb52c9ba3bc6dd038175afb74a473b85f0cf60ff70e2"),
                "§7Enemies of this type are found",
                "§7throughout SkyBlock - but they are",
                "§drare §7and not seen very often."
        ),
        ENDER(MobType.ENDER, new GUIMaterial("8a108a0a7a387859f2c44fb9702cf73dbafee3ecfdc4f5def46c0d651b7a49f7"),
                "§7Enemies of this type are typically",
                "§7found in §dThe End§7. Enemies of this",
                "§7type take increased damage from the",
                "§9Ender Slayer §7enchantment."
        ),
        FROZEN(MobType.FROZEN, new GUIMaterial("54690f5aa6d0e800f9b8d1890fc158b921819a81dfd7342a2170e7efc46b9ed7"),
                "§7Enemies of this type are typically",
                "§7found in §cJerry's Workshop§7."
        ),
        GLACIAL(MobType.GLACIAL, new GUIMaterial("ef3178fb4bd2c629c218ec03fd4a96bfdc846b1f5625743c49eb205b873ae0d5"),
                "§7Enemies of this type are typically",
                "§7found in the §bGlacite Mineshafts§7."
        ),
        HUMANOID(MobType.HUMANOID, new GUIMaterial("56a64f3e6c3613f0c4dae8ab8abebcf43cef2c16232d887d37aeb9713bbad399"),
                "§7Enemies of this type are typically",
                "§7found in the §5Crystal Hollows§7."
        ),
        INFERNAL(MobType.INFERNAL, new GUIMaterial("737623f79f7eb4f3f80da65b652cc44b2148eea41f9ffe2e86a23bdf49ab77b1"),
                "§7Enemies of this type are typically",
                "§7found in the §cCrimson Isle§7. Enemies of",
                "§7this type take increased damage",
                "§7from the §9Smoldering §7enchantment."
        ),
        MAGMATIC(MobType.MAGMATIC, new GUIMaterial("a1c97a06efde04d00287bf20416404ab2103e10f08623087e1b0c1264a1c0f0c"),
                "§7Enemies of this type are typically",
                "§7found in the §cCrimson Isle§7."
        ),
        MYTHOLOGICAL(MobType.MYTHOLOGICAL, new GUIMaterial("eefcd13d5877ee4edd83a342088520a9cf2265484bfb93aa135cacd651b4fe85"),
                "§7Most commonly found during the",
                "§2Mythological Ritual§7."
        ),
        PEST(MobType.PEST, new GUIMaterial("a8abb471db0ab78703011979dc8b40798a941f3a4dec3ec61cbeec2af8cffe8"),
                "§7Enemies of this type are typically",
                "§7found in §aThe Garden§7."
        ),
        SHIELDED(MobType.SHIELDED, new GUIMaterial("172850906b7f0d952c0e508073cc439fd3374ccf5b889c06f7e8d90cc0cc255c"),
                "§7Enemies of this type do not have",
                "§7conventional health. Instead, they",
                "§7must be hit a certain amount of times",
                "§7in order to kill them."
        ),
        SKELETAL(MobType.SKELETAL, new GUIMaterial(Material.SKELETON_SKULL),
                "§7Enemies of this type are typically",
                "§7found in §cThe Catacombs§7 and take",
                "§7increased damage from the §9Smite",
                "§7enchantment."
        ),
        SPOOKY(MobType.SPOOKY, new GUIMaterial("121ebb2b36c1f5dde4fa665cd39a4895ef02d00fcb3eb3588b86a13f9ae7e9a8"),
                "§7Most commonly found during the",
                "§6Spooky Festival§7."
        ),
        SUBTERRANEAN(MobType.SUBTERRANEAN, new GUIMaterial("b2b12a814ced8af02cddf29a37e7f3011e430e8a18b38b706f27c6bd31650b65"),
                "§7Enemies of this type are typically",
                "§7found in the §2Dwarven Mines§7."
        ),
        UNDEAD(MobType.UNDEAD, new GUIMaterial(Material.ZOMBIE_HEAD),
                "§7Enemies of this type are typically",
                "§7found in the §bSkyBlock Hub§7. Enemies",
                "§7of this type take increased damage",
                "§7from the §9Smite §7enchantment."
        ),
        WITHER(MobType.WITHER, new GUIMaterial(Material.WITHER_SKELETON_SKULL),
                "§7Enemies of this type are typically",
                "§7found in §cThe Catacombs§7 and take",
                "§7increased damage from the §9Smite",
                "§7enchantment."
        ),
        WOODLAND(MobType.WOODLAND, new GUIMaterial("30519f79e5829136c3df10b6bd727db255717c87e5c102892ef67e7f46929515"),
                "§7Enemies of this type are typically",
                "§7found in §2Galatea§7 and take increased",
                "§7damage from the §9Woodsplitter",
                "§7enchantment."
        );

        private final MobType mobType;
        private final GUIMaterial guiMaterial;
        private final String[] lore;

        MobTypes(MobType mobType, GUIMaterial guiMaterial, String... lore) {
            this.mobType = mobType;
            this.guiMaterial = guiMaterial;
            this.lore = lore;
        }
    }
}