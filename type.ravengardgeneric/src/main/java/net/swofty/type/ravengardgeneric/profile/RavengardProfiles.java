package net.swofty.type.ravengardgeneric.profile;

import net.kyori.adventure.nbt.TagStringIO;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.ravengardgeneric.RavengardGenericLoader;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.classes.RavengardSelection;
import net.swofty.type.ravengardgeneric.data.monogdb.RavengardProfileDatabase;
import net.swofty.type.ravengardgeneric.item.RavengardMenuItem;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardProfiles {
    public static final int MAX_PROFILES = 5;
    public static final Pos TUTORIAL_SPAWN = new Pos(25.5, 64, 508.5, -90f, 0f);

    private static final Map<UUID, Long> SESSION_STARTED = new ConcurrentHashMap<>();

    private RavengardProfiles() {
    }

    public static List<RavengardProfile> list(RavengardPlayer player) {
        return RavengardProfileDatabase.byOwner(player.getUuid());
    }

    /**
     * Makes sure the player has a profile and a valid selection, migrating pre-profile data into a
     * first profile. Called on join before the profile id is announced.
     */
    public static RavengardProfile ensure(RavengardPlayer player) {
        RavengardProfile selected = RavengardProfileDatabase.byId(player.getSelectedProfile());
        if (selected != null && selected.getOwner().equals(player.getUuid())) {
            beginSession(player);
            return selected;
        }

        List<RavengardProfile> existing = list(player);
        if (!existing.isEmpty()) {
            selected = existing.getFirst();
        } else {
            selected = new RavengardProfile(UUID.randomUUID(), player.getUuid());
            selected.setProfileClass(player.getRavengardClass());
            selected.setLevel(player.getRavengardLevel());
            selected.setTutorial(player.isTutorial());
            RavengardProfileDatabase.save(selected);
            Logger.info("Created first Ravengard profile {} for {}", selected.getId(), player.getUsername());
        }

        player.setSelectedProfile(selected.getId());
        applyToWorkingCopy(player, selected);
        beginSession(player);
        return selected;
    }

    public static void announce(RavengardPlayer player) {
        RavengardProfile profile = ensure(player);
        player.sendMessage("§8Profile ID: " + profile.getId());
        player.sendMessage("       ");
        restoreInventory(player, profile);
    }

    public static void create(RavengardPlayer player) {
        if (list(player).size() >= MAX_PROFILES) {
            player.sendMessage("§cYou already have the maximum number of profiles!");
            return;
        }

        saveActive(player);
        player.sendMessage("§7Creating profile...");

        RavengardProfile profile = new RavengardProfile(UUID.randomUUID(), player.getUuid());
        RavengardProfileDatabase.save(profile);

        player.sendMessage("§7Successfully created profile!");
        player.sendMessage("§8Profile ID: " + profile.getId());
        player.sendMessage("       ");

        activate(player, profile);
    }

    public static void select(RavengardPlayer player, UUID profileId) {
        RavengardProfile profile = RavengardProfileDatabase.byId(profileId);
        if (profile == null || !profile.getOwner().equals(player.getUuid())) {
            player.sendMessage("§cThat profile no longer exists.");
            return;
        }
        if (profileId.equals(player.getSelectedProfile())) {
            return;
        }

        saveActive(player);
        player.sendMessage("§8Profile ID: " + profile.getId());
        player.sendMessage("       ");
        activate(player, profile);
    }

    public static void delete(RavengardPlayer player, UUID profileId) {
        RavengardProfile profile = RavengardProfileDatabase.byId(profileId);
        if (profile == null || !profile.getOwner().equals(player.getUuid())) {
            return;
        }

        boolean wasSelected = profileId.equals(player.getSelectedProfile());
        RavengardProfileDatabase.delete(profileId);
        player.sendMessage("§7Successfully deleted profile!");

        if (!wasSelected) {
            return;
        }

        List<RavengardProfile> remaining = list(player);
        if (remaining.isEmpty()) {
            create(player);
            return;
        }
        player.setSelectedProfile(null);
        player.sendMessage("§8Profile ID: " + remaining.getFirst().getId());
        player.sendMessage("       ");
        activate(player, remaining.getFirst());
    }

    /** Flushes the working-copy datapoints and session playtime into the selected profile. */
    public static void saveActive(RavengardPlayer player) {
        RavengardProfile profile = RavengardProfileDatabase.byId(player.getSelectedProfile());
        if (profile == null || !profile.getOwner().equals(player.getUuid())) {
            SESSION_STARTED.remove(player.getUuid());
            return;
        }
        profile.setProfileClass(player.getRavengardClass());
        profile.setLevel(player.getRavengardLevel());
        profile.setTutorial(player.isTutorial());
        profile.setCrowns(player.getCrowns());
        profile.setPlaytimeSeconds(profile.getPlaytimeSeconds() + endSession(player));
        snapshotInventory(player, profile);
        RavengardProfileDatabase.save(profile);
        beginSession(player);
    }

    public static void endSessionAndSave(RavengardPlayer player) {
        saveActive(player);
        SESSION_STARTED.remove(player.getUuid());
    }

    /**
     * Switches the working copy to the profile and puts the player where it belongs: the ship with
     * Diago for a profile still in the tutorial, the main world otherwise.
     */
    private static void activate(RavengardPlayer player, RavengardProfile profile) {
        player.setSelectedProfile(profile.getId());
        applyToWorkingCopy(player, profile);
        beginSession(player);

        restoreInventory(player, profile);

        RavengardClass profileClass = profile.getProfileClass();
        player.closeInventory();
        Instance target = profile.isTutorial() || profileClass == null
                ? RavengardGenericLoader.tutorialInstance
                : HypixelConst.getInstanceContainer();
        Pos spawn = profile.isTutorial() || profileClass == null
                ? TUTORIAL_SPAWN
                : RavengardSelection.MAIN_WORLD_SPAWN;
        if (target != null && player.getInstance() != null) {
            if (player.getInstance() == target) {
                player.teleport(spawn);
            } else {
                player.setInstance(target, spawn);
            }
        }
    }

    public static boolean hasIntro(RavengardPlayer player, String npc) {
        RavengardProfile profile = RavengardProfileDatabase.byId(player.getSelectedProfile());
        return profile != null && profile.getIntros().contains(npc);
    }

    public static void markIntro(RavengardPlayer player, String npc) {
        RavengardProfile profile = RavengardProfileDatabase.byId(player.getSelectedProfile());
        if (profile == null) {
            return;
        }
        profile.getIntros().add(npc);
        RavengardProfileDatabase.save(profile);
    }

    public static int getCrowns(RavengardPlayer player) {
        return player.getCrowns();
    }

    public static void setCrowns(RavengardPlayer player, int amount) {
        player.setCrowns(amount);
        saveActive(player);
    }

    public static void addCrowns(RavengardPlayer player, int amount) {
        player.setCrowns(player.getCrowns() + amount);
        saveActive(player);
    }

    public static boolean tryPurchase(RavengardPlayer player, int price) {
        if (player.getCrowns() < price) {
            return false;
        }
        player.setCrowns(player.getCrowns() - price);
        saveActive(player);
        return true;
    }

    /** Writes every occupied slot into the profile as item nbt. */
    public static void snapshotInventory(RavengardPlayer player, RavengardProfile profile) {
        profile.getInventory().clear();
        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (stack.isAir()) {
                continue;
            }
            try {
                profile.getInventory().put(slot, TagStringIO.tagStringIO().asString(stack.toItemNBT()));
            } catch (Exception exception) {
                Logger.warn(exception, "Could not serialize inventory slot {} for {}", slot, player.getUsername());
            }
        }
    }

    /**
     * Rebuilds the player's inventory from the profile: the saved slots when it has them, the
     * class defaults when it has never saved any.
     */
    public static void restoreInventory(RavengardPlayer player, RavengardProfile profile) {
        player.getInventory().clear();
        if (!profile.getInventory().isEmpty()) {
            profile.getInventory().forEach((slot, snbt) -> {
                try {
                    player.getInventory().setItemStack(slot,
                            ItemStack.fromItemNBT(TagStringIO.tagStringIO().asCompound(snbt)));
                } catch (Exception exception) {
                    Logger.warn(exception, "Could not restore inventory slot {} for {}", slot, player.getUsername());
                }
            });
            RavengardMenuItem.give(player);
            return;
        }

        player.sendMessage("§7Restoring default equipment...");
        RavengardClass profileClass = profile.getProfileClass();
        if (profileClass != null) {
            RavengardSelection.giveKit(player, profileClass);
        } else {
            RavengardSelection.giveAccessorySlots(player);
        }
        RavengardMenuItem.give(player);
    }

    private static void applyToWorkingCopy(RavengardPlayer player, RavengardProfile profile) {
        player.setCrowns(profile.getCrowns());
        player.setRavengardClass(profile.getProfileClass());
        player.setRavengardLevel(profile.getLevel());
        player.setTutorial(profile.isTutorial());
    }

    private static void beginSession(RavengardPlayer player) {
        SESSION_STARTED.put(player.getUuid(), System.currentTimeMillis());
    }

    private static long endSession(RavengardPlayer player) {
        Long started = SESSION_STARTED.remove(player.getUuid());
        return started == null ? 0 : Math.max(0, (System.currentTimeMillis() - started) / 1000);
    }
}
