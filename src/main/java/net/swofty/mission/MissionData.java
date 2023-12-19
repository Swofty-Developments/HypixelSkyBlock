package net.swofty.mission;

import lombok.Getter;
import lombok.Setter;
import net.swofty.calendar.SkyBlockCalendar;
import net.swofty.region.RegionType;
import net.swofty.serializer.MissionDataSerializer;
import net.swofty.user.SkyBlockPlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<ActiveMission> getActiveMissions(RegionType regionType) {
        return activeMissions.stream().filter(mission -> {
            SkyBlockMission skyBlockMission = missionClassCache.get(mission.getMissionID());
            return skyBlockMission.getValidRegions().contains(regionType);
        }).collect(Collectors.toList());
    }

    public boolean isCurrentlyActive(String missionID) {
        return getMission(missionID) != null && !getMission(missionID).getValue();
    }

    public boolean hasCompleted(String missionID) {
        return getMission(missionID) != null && getMission(missionID).getValue();
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
            ActiveMission activeMission = new ActiveMission(mission.getID(), 0, mission instanceof SkyBlockMissionDynamic);

            Map<String, Object> data = mission.onStart(getSkyBlockPlayer());
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
        missionClassCache.get(missionID).onEnd(getSkyBlockPlayer(), activeMission.getCustomData());
        activeMissions.remove(activeMission);
        completedMissions.add(activeMission);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();

        result.put("activeMissions", activeMissions.stream().map(mission -> {
            Map<String, Object> m = new HashMap<>();
            m.put("missionID", mission.getMissionID());
            m.put("missionProgress", mission.getMissionProgress());
            m.put("isDynamic", mission.isDynamic());
            m.put("missionStarted", mission.getMissionStarted());
            m.put("customData", mission.getCustomData());
            return m;
        }).collect(Collectors.toList()));

        result.put("completedMissions", completedMissions.stream().map(mission -> {
            Map<String, Object> m = new HashMap<>();
            m.put("missionID", mission.getMissionID());
            m.put("missionProgress", mission.getMissionProgress());
            m.put("isDynamic", mission.isDynamic());
            m.put("missionStarted", mission.getMissionStarted());
            m.put("customData", mission.getCustomData());
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
                    (Map<String, Object>) m.get("customData"));
            return mission;
        }).collect(Collectors.toList());

        completedMissions = serializedCompletedMissions.stream().map(m -> {
            ActiveMission mission = new ActiveMission(
                    (String) m.get("missionID"),
                    (int) m.get("missionProgress"),
                    (boolean) m.get("isDynamic"),
                    (int) m.get("missionStarted"),
                    (Map<String, Object>) m.get("customData"));
            return mission;
        }).collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class ActiveMission {
        private String missionID;
        private int missionStarted;
        private int missionProgress;
        private boolean isDynamic;
        private Map<String, Object> customData;

        public ActiveMission(String missionID, int missionProgress, boolean isDynamic) {
            this.missionID = missionID;
            this.missionProgress = missionProgress;
            this.isDynamic = isDynamic;
            this.missionStarted = (int) SkyBlockCalendar.getElapsed();
            this.customData = new HashMap<>();
        }

        public ActiveMission(String missionID, int missionProgress, boolean isDynamic, int missionStarted, Map<String, Object> customData) {
            this.missionID = missionID;
            this.missionProgress = missionProgress;
            this.isDynamic = isDynamic;
            this.missionStarted = missionStarted;
            this.customData = customData;
        }

        @Override
        public String toString() {
            return MissionData.getMissionClass(missionID).getName();
        }
    }

    public static SkyBlockMission getMissionClass(String missionID) {
        return missionClassCache.get(missionID);
    }

    public static void registerMission(Class<? extends SkyBlockMission> skyBlockMission) {
        try {
            SkyBlockMission mission = skyBlockMission.newInstance();
            missionClassCache.put(mission.getID(), mission);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
