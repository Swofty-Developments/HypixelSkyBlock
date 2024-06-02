package net.swofty.types.generic.mission;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MissionData {
    private static final HashMap<String, SkyBlockMission> missionClassCache = new HashMap<>();

    private List<ActiveMission> activeMissions = new ArrayList<>();
    private List<ActiveMission> completedMissions = new ArrayList<>();
    @Setter
    private SkyBlockPlayer skyBlockPlayer;

    public Map.Entry<ActiveMission, Boolean> getMission(String missionID) {
        for (ActiveMission mission : activeMissions) {
            if (mission.getMissionID().equalsIgnoreCase(missionID)) {
                return Map.entry(mission, false);
            }
        }

        for (ActiveMission mission : completedMissions) {
            if (mission.getMissionID().equalsIgnoreCase(missionID)) {
                return Map.entry(mission, true);
            }
        }

        return null;
    }

    @SneakyThrows
    public Map.Entry<ActiveMission, Boolean> getMission(Class<? extends SkyBlockMission> skyBlockMission) {
        return getMission(skyBlockMission.newInstance().getID());
    }

    public List<ActiveMission> getActiveMissions(RegionType regionType) {
        return activeMissions.stream().filter(mission -> {
            SkyBlockMission skyBlockMission = missionClassCache.get(mission.getMissionID());
            return skyBlockMission.getValidRegions().contains(regionType);
        }).collect(Collectors.toList());
    }

    public boolean isCurrentlyActive(String missionID) {
        return getMission(missionID) != null && !getMission(missionID).getValue();
    }

    public boolean isCurrentlyActive(Class<? extends SkyBlockMission> skyBlockMission) {
        return getMission(skyBlockMission) != null && !getMission(skyBlockMission).getValue();
    }

    public boolean hasCompleted(String missionID) {
        return getMission(missionID) != null && getMission(missionID).getValue();
    }

    public boolean hasCompleted(Class<? extends SkyBlockMission> skyBlockMission) {
        return getMission(skyBlockMission) != null && getMission(skyBlockMission).getValue();
    }

    public void startMission(Class<? extends SkyBlockMission> skyBlockMission) {
        if (activeMissions.stream().anyMatch(mission -> mission.getMissionID().equals(skyBlockMission.getSimpleName()))) {
            throw new RuntimeException("Mission already started");
        }
        if (completedMissions.stream().anyMatch(mission -> mission.getMissionID().equals(skyBlockMission.getSimpleName()))) {
            throw new RuntimeException("Mission already started, was previously completed");
        }

        try {
            SkyBlockMission mission = skyBlockMission.newInstance();
            ActiveMission activeMission = new ActiveMission(mission.getID(), 0, mission instanceof SkyBlockProgressMission);

            Map<String, Object> data = mission.onStart(getSkyBlockPlayer(), activeMission);
            if (data != null) {
                activeMission.setCustomData(data);
            }

            activeMissions.add(activeMission);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void endMission(String missionID) {
        if (activeMissions.stream().noneMatch(mission -> mission.getMissionID().equals(missionID))) {
            throw new RuntimeException("Mission not started");
        }

        ActiveMission activeMission = activeMissions.stream().filter(mission -> mission.getMissionID().equals(missionID)).findFirst().get();
        missionClassCache.get(missionID).onEnd(getSkyBlockPlayer(), activeMission.getCustomData(), activeMission);
        activeMission.setMissionEnded((int) SkyBlockCalendar.getElapsed());
        activeMissions.remove(activeMission);
        completedMissions.add(activeMission);
    }

    @SneakyThrows
    public void endMission(Class<? extends SkyBlockMission> skyBlockMission) {
        endMission(skyBlockMission.newInstance().getID());
    }

    public @Nullable SkyBlockProgressMission getAsProgressMission(String missionID) {
        try {
            return (SkyBlockProgressMission) missionClassCache.get(missionID);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SneakyThrows
    public @Nullable SkyBlockProgressMission getAsProgressMission(Class<? extends SkyBlockMission> skyBlockMission) {
        return getAsProgressMission(skyBlockMission.newInstance().getID());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();

        result.put("activeMissions", activeMissions.stream().map(mission -> {
            Map<String, Object> m = new HashMap<>();
            m.put("missionID", mission.getMissionID());
            m.put("missionProgress", mission.getMissionProgress());
            m.put("isDynamic", mission.isProgress());
            m.put("missionStarted", mission.getMissionStarted());
            m.put("customData", mission.getCustomData());
            m.put("missionEnded", mission.getMissionEnded());
            return m;
        }).collect(Collectors.toList()));

        result.put("completedMissions", completedMissions.stream().map(mission -> {
            Map<String, Object> m = new HashMap<>();
            m.put("missionID", mission.getMissionID());
            m.put("missionProgress", mission.getMissionProgress());
            m.put("isDynamic", mission.isProgress());
            m.put("missionStarted", mission.getMissionStarted());
            m.put("customData", mission.getCustomData());
            m.put("missionEnded", mission.getMissionEnded());
            return m;
        }).collect(Collectors.toList()));

        return result;
    }

    public void deserialize(Map<String, Object> map) {
        List<Map<String, Object>> serializedActiveMissions = (List<Map<String, Object>>) map.get("activeMissions");
        List<Map<String, Object>> serializedCompletedMissions = (List<Map<String, Object>>) map.get("completedMissions");

        activeMissions = serializedActiveMissions.stream().map(m -> {
            ActiveMission mission = new ActiveMission(
                    (String) m.get("missionID"),
                    (int) m.get("missionProgress"),
                    (boolean) m.get("isDynamic"),
                    (int) m.get("missionStarted"),
                    (Map<String, Object>) m.get("customData"),
                    (int) m.get("missionEnded")
            );
            return mission;
        }).collect(Collectors.toList());

        completedMissions = serializedCompletedMissions.stream().map(m -> {
            ActiveMission mission = new ActiveMission(
                    (String) m.get("missionID"),
                    (int) m.get("missionProgress"),
                    (boolean) m.get("isDynamic"),
                    (int) m.get("missionStarted"),
                    (Map<String, Object>) m.get("customData"),
                    (int) m.get("missionEnded")
            );
            return mission;
        }).collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class ActiveMission {
        private String missionID;
        private int missionStarted;
        private int missionEnded;
        private int missionProgress;
        private boolean isProgress;
        private Map<String, Object> customData;

        public ActiveMission(String missionID, int missionProgress, boolean isProgress) {
            this.missionID = missionID;
            this.missionProgress = missionProgress;
            this.isProgress = isProgress;
            this.missionStarted = (int) SkyBlockCalendar.getElapsed();
            this.customData = new HashMap<>();
            this.missionEnded = 0;
        }

        public ActiveMission(String missionID, int missionProgress, boolean isProgress, int missionStarted, Map<String, Object> customData, int missionEnded) {
            this.missionID = missionID;
            this.missionProgress = missionProgress;
            this.isProgress = isProgress;
            this.missionStarted = missionStarted;
            this.customData = customData;
            this.missionEnded = missionEnded;
        }

        @Override
        public String toString() {
            return MissionData.getMissionClass(missionID).getName();
        }

        public void checkIfMissionEnded(SkyBlockPlayer player) {
            SkyBlockProgressMission mission = (SkyBlockProgressMission) MissionData.getMissionClass(missionID);

            if (missionProgress >= mission.getMaxProgress()) {
                player.getMissionData().endMission(missionID);
            }
        }

        public List<String> getObjectiveCompleteText(ArrayList<String> rewards) {
            SkyBlockMission mission = MissionData.getMissionClass(missionID);

            if (rewards == null || rewards.isEmpty())
                return Arrays.asList(
                        "§7 ",
                        "§6§l  OBJECTIVE COMPLETE",
                        "§f  " + mission.getName(),
                        "§7 ");

            ArrayList<String> display = new ArrayList<>(Arrays.asList(
                    "§7 ",
                    "§6§l  OBJECTIVE COMPLETE",
                    "§f  " + mission.getName(),
                    "§7 ",
                    "§a§l    REWARDS"
            ));
            display.addAll(rewards.stream().map(reward -> "§8    +" + reward).toList());
            display.add("§7 ");
            return display;
        }

        public List<String> getNewObjectiveText() {
            SkyBlockMission mission = MissionData.getMissionClass(missionID);

            return Arrays.asList(
                    "§7 ",
                    "§6§l  NEW OBJECTIVE",
                    "§f  " + mission.getName(),
                    "§7 ");
        }
    }

    public static SkyBlockMission getMissionClass(String missionID) {
        return missionClassCache.get(missionID);
    }

    public static SkyBlockMission getMissionClass(ActiveMission skyBlockMission) {
        return missionClassCache.get(skyBlockMission.getMissionID());
    }

    public static List<String> getAllMissionIDs() {
        return new ArrayList<>(missionClassCache.keySet());
    }

    public static void registerMission(Class<? extends SkyBlockMission> skyBlockMission) {
        try {
            SkyBlockMission mission = skyBlockMission.newInstance();
            missionClassCache.put(mission.getID(), mission);
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.info(e.getStackTrace());
        }
    }
}
