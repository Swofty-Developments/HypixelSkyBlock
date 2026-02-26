package net.swofty.type.skyblockgeneric.elections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.election.CastVoteProtocolObject;
import net.swofty.commons.protocol.objects.election.GetCandidatesProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.commons.protocol.objects.election.GetPlayerVoteProtocolObject;
import net.swofty.commons.protocol.objects.election.SaveElectionDataProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ElectionManager {

    private static final Gson GSON = new Gson();
    private static ProxyService SERVICE;
    private static final ConcurrentHashMap<UUID, String> playerVoteCache = new ConcurrentHashMap<>();

    @Getter
    private static ElectionData electionData = new ElectionData();

    public static void loadFromService() {
        SERVICE = new ProxyService(ServiceType.ELECTION);
        try {
            GetElectionDataProtocolObject.GetElectionDataResponse response =
                SERVICE.<GetElectionDataProtocolObject.GetElectionDataMessage,
                    GetElectionDataProtocolObject.GetElectionDataResponse>handleRequest(
                    new GetElectionDataProtocolObject.GetElectionDataMessage()
                ).join();

            if (response.found() && response.serializedData() != null) {
                electionData = GSON.fromJson(response.serializedData(), ElectionData.class);
                if (electionData.getCurrentMayor() == null || electionData.getCurrentMinister() == null) {
                    electionData = new ElectionData();
                    initializeFirstElection();
                    saveToService();
                    Logger.info("Election data was missing mayor or minister info. Reinitialized first election.");
                }
            } else {
                electionData = new ElectionData();
                initializeFirstElection();
                saveToService();
                Logger.info("Election data was missing mayor or minister info.");
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load election data from service");
            electionData = new ElectionData();
            initializeFirstElection();
        }

        startTallyRefreshTask();
    }

    public static void saveToService() {
        try {
            String serialized = GSON.toJson(electionData);
            SERVICE.<SaveElectionDataProtocolObject.SaveElectionDataMessage,
                SaveElectionDataProtocolObject.SaveElectionDataResponse>handleRequest(
                new SaveElectionDataProtocolObject.SaveElectionDataMessage(serialized)
            );
        } catch (Exception e) {
            Logger.error(e, "Failed to save election data to service");
        }
    }

    private static void initializeFirstElection() {
        int currentYear = SkyBlockCalendar.getYear();
        electionData.setCurrentMayor(SkyBlockMayor.COLE.name());
        electionData.setCurrentMayorColor("§d");
        electionData.setCurrentMayorPerks(Arrays.stream(SkyBlockMayor.COLE.getAllPerks()).map(Enum::name).toList());
        electionData.setMayorElectedYear(currentYear);
        electionData.setCurrentMinister(SkyBlockMayor.DIAZ.name());
        electionData.setMinisterPerk(SkyBlockMayor.Perk.STOCK_EXCHANGE.name());
        electionData.setCurrentMinisterColor("§c");
    }

    public static void onElectionStart() {
        int currentYear = SkyBlockCalendar.getYear();
        if (electionData.isElectionOpen()) return;

        electionData.startNewElection(currentYear);
        saveToService();
        Logger.info("Election started for Year {}", currentYear);
    }

    public static void onElectionEnd() {
        if (!electionData.isElectionOpen()) return;

        refreshTalliesSync();

        int currentYear = SkyBlockCalendar.getYear();
        electionData.resolveElection(currentYear);
        playerVoteCache.clear();
        saveToService();
        Logger.info("Election resolved for Year {}. New Mayor: {}", currentYear, electionData.getCurrentMayor());
    }

    public static CompletableFuture<Void> castVote(UUID accountId, String candidateName) {
        if (!electionData.isElectionOpen()) return CompletableFuture.completedFuture(null);

        boolean validCandidate = electionData.getCandidates().stream()
            .anyMatch(c -> c.getMayorName().equals(candidateName));
        if (!validCandidate) return CompletableFuture.completedFuture(null);

        String previousVote = playerVoteCache.put(accountId, candidateName);
        electionData.incrementTally(candidateName, previousVote);

        return SERVICE.<CastVoteProtocolObject.CastVoteMessage,
            CastVoteProtocolObject.CastVoteResponse>handleRequest(
            new CastVoteProtocolObject.CastVoteMessage(accountId, candidateName)
        ).thenAccept(response -> {
            if (response.success() && response.talliesJson() != null) {
                Map<String, Long> tallies = GSON.fromJson(
                    response.talliesJson(),
                    new TypeToken<Map<String, Long>>(){}.getType()
                );
                electionData.updateTallies(tallies);
            }
        }).exceptionally(e -> {
            Logger.error(e, "Failed to sync vote to service");
            return null;
        });
    }

    public static String getPlayerVote(UUID accountId) {
        return playerVoteCache.get(accountId);
    }

    public static CompletableFuture<String> fetchPlayerVote(UUID accountId) {
        String cached = playerVoteCache.get(accountId);
        if (cached != null) return CompletableFuture.completedFuture(cached);

        return SERVICE.<GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
            GetPlayerVoteProtocolObject.GetPlayerVoteResponse>handleRequest(
            new GetPlayerVoteProtocolObject.GetPlayerVoteMessage(accountId)
        ).thenApply(response -> {
            if (response.candidateName() != null) {
                playerVoteCache.put(accountId, response.candidateName());
            }
            return response.candidateName();
        });
    }

    public static void clearPlayerVoteCache(UUID accountId) {
        playerVoteCache.remove(accountId);
    }

    public static SkyBlockMayor getCurrentMayor() {
        return electionData.getMayorEnum();
    }

    public static SkyBlockMayor getCurrentMinister() {
        return electionData.getMinisterEnum();
    }

    public static boolean isMayorPerkActive(SkyBlockMayor.Perk perk) {
        return electionData.getCurrentMayorPerkEnums().contains(perk);
    }

    public static boolean isMinisterPerkActive(SkyBlockMayor.Perk perk) {
        SkyBlockMayor.Perk activePerk = electionData.getMinisterPerkEnum();
        return activePerk != null && activePerk == perk;
    }

    public static boolean isPerkActive(SkyBlockMayor.Perk perk) {
        return isMayorPerkActive(perk) || isMinisterPerkActive(perk);
    }

    public static CompletableFuture<List<GetCandidatesProtocolObject.CandidateInfo>> fetchCandidates() {
        return SERVICE.<GetCandidatesProtocolObject.GetCandidatesMessage,
            GetCandidatesProtocolObject.GetCandidatesResponse>handleRequest(
            new GetCandidatesProtocolObject.GetCandidatesMessage()
        ).thenApply(GetCandidatesProtocolObject.GetCandidatesResponse::candidates);
    }

    public static void checkElectionCycle() {
        int currentYear = SkyBlockCalendar.getYear();
        int currentMonth = SkyBlockCalendar.getMonth();

        if (currentMonth >= 6 && currentMonth <= 8 && !electionData.isElectionOpen()) {
            if (electionData.getElectionYear() != currentYear) {
                onElectionStart();
            }
        }

        if (currentMonth >= 9 && electionData.isElectionOpen()) {
            onElectionEnd();
        }
    }

    private static void startTallyRefreshTask() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (electionData.isElectionOpen()) {
                refreshTalliesAsync();
            }
            return TaskSchedule.seconds(30);
        });
    }

    private static void refreshTalliesAsync() {
        SERVICE.<GetCandidatesProtocolObject.GetCandidatesMessage,
            GetCandidatesProtocolObject.GetCandidatesResponse>handleRequest(
            new GetCandidatesProtocolObject.GetCandidatesMessage()
        ).thenAccept(response -> {
            if (response.electionOpen()) {
                Map<String, Long> tallies = new HashMap<>();
                for (GetCandidatesProtocolObject.CandidateInfo info : response.candidates()) {
                    tallies.put(info.mayorName(), info.votes());
                }
                electionData.updateTallies(tallies);
            }
        }).exceptionally(e -> {
            Logger.warn("Failed to refresh election tallies: {}", e.getMessage());
            return null;
        });
    }

    private static void refreshTalliesSync() {
        try {
            GetCandidatesProtocolObject.GetCandidatesResponse response =
                SERVICE.<GetCandidatesProtocolObject.GetCandidatesMessage,
                    GetCandidatesProtocolObject.GetCandidatesResponse>handleRequest(
                    new GetCandidatesProtocolObject.GetCandidatesMessage()
                ).join();

            if (response.electionOpen()) {
                Map<String, Long> tallies = new HashMap<>();
                for (GetCandidatesProtocolObject.CandidateInfo info : response.candidates()) {
                    tallies.put(info.mayorName(), info.votes());
                }
                electionData.updateTallies(tallies);
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to sync tallies before election resolution");
        }
    }
}
