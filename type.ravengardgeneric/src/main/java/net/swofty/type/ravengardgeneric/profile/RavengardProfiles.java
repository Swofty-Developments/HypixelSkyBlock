package net.swofty.type.ravengardgeneric.profile;

import net.minestom.server.coordinate.Pos;
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
        player.sendMessage("§7Profile ID: " + profile.getId());
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
        player.sendMessage("§7Profile ID: " + profile.getId());

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
        player.sendMessage("§7Profile ID: " + profile.getId());
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
        player.sendMessage("§7Profile ID: " + remaining.getFirst().getId());
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
        profile.setPlaytimeSeconds(profile.getPlaytimeSeconds() + endSession(player));
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

        player.sendMessage("§7Restoring default equipment...");
        player.getInventory().clear();

        RavengardClass profileClass = profile.getProfileClass();
        if (profileClass != null) {
            RavengardSelection.giveKit(player, profileClass);
        } else {
            RavengardSelection.giveAccessorySlots(player);
        }
        RavengardMenuItem.give(player);

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

    private static void applyToWorkingCopy(RavengardPlayer player, RavengardProfile profile) {
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
