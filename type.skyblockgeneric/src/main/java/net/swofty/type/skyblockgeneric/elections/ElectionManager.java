package net.swofty.type.skyblockgeneric.elections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.election.CastVoteProtocolObject;
import net.swofty.commons.protocol.objects.election.GetCandidatesProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.commons.protocol.objects.election.GetPlayerVoteProtocolObject;
import net.swofty.commons.protocol.objects.election.ResolveElectionProtocolObject;
import net.swofty.commons.protocol.objects.election.StartElectionProtocolObject;
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
import java.util.concurrent.atomic.AtomicReference;

public class ElectionManager {

    private static final Gson GSON = new Gson();
    private static ProxyService SERVICE;
    private static final ConcurrentHashMap<UUID, String> playerVoteCache = new ConcurrentHashMap<>();

    private static final AtomicReference<ElectionData> electionDataRef = new AtomicReference<>(new ElectionData());

    public static ElectionData getElectionData() {
        return electionDataRef.get();
    }

    public static void loadFromService() {
        SERVICE = new ProxyService(ServiceType.ELECTION);
        try {
            GetElectionDataProtocolObject.GetElectionDataResponse response =
                SERVICE.<GetElectionDataProtocolObject.GetElectionDataMessage,
                    GetElectionDataProtocolObject.GetElectionDataResponse>handleRequest(
                    new GetElectionDataProtocolObject.GetElectionDataMessage()
                ).join();

            if (response.found() && response.serializedData() != null) {
                ElectionData loaded = GSON.fromJson(response.serializedData(), ElectionData.class);
                if (loaded.getCurrentMayor() == null) {
                    loaded = new ElectionData();
                    electionDataRef.set(loaded);
                    initializeFirstElection();
                    Logger.info("Election data was missing mayor info. Reinitialized first election.");
                } else {
                    electionDataRef.set(loaded);
                }
            } else {
                electionDataRef.set(new ElectionData());
                initializeFirstElection();
                Logger.info("No election data found. Initialized first election.");
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load election data from service");
            electionDataRef.set(new ElectionData());
            initializeFirstElection();
        }

        startTallyRefreshTask();
    }

    private static void initializeFirstElection() {
        ElectionData data = electionDataRef.get();
        int currentYear = SkyBlockCalendar.getYear();
        data.setCurrentMayor(SkyBlockMayor.COLE.name());
        data.setCurrentMayorColor(ElectionData.colorForIndex(4));
        data.setCurrentMayorPerks(Arrays.stream(SkyBlockMayor.COLE.getAllPerks()).map(Enum::name).toList());
        data.setMayorElectedYear(currentYear);
        data.setCurrentMinister(SkyBlockMayor.DIAZ.name());
        data.setMinisterPerk(SkyBlockMayor.Perk.STOCK_EXCHANGE.name());
        data.setCurrentMinisterColor(ElectionData.colorForIndex(0));
    }

    public static void onElectionStart() {
        int currentYear = SkyBlockCalendar.getYear();
        ElectionData data = electionDataRef.get();
        if (data.isElectionOpen()) return;

        data.startNewElection(currentYear);
        String serialized = GSON.toJson(data);

        try {
            StartElectionProtocolObject.StartElectionResponse response =
                SERVICE.<StartElectionProtocolObject.StartElectionMessage,
                    StartElectionProtocolObject.StartElectionResponse>handleRequest(
                    new StartElectionProtocolObject.StartElectionMessage(currentYear, serialized)
                ).join();

            if (response.serializedData() != null) {
                electionDataRef.set(GSON.fromJson(response.serializedData(), ElectionData.class));
            }

            if (response.started()) {
                Logger.info("Election started for Year {}", currentYear);
            } else {
                Logger.info("Election for Year {} was already started by another server", currentYear);
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to start election via service");
        }
    }

    public static void onElectionEnd() {
        if (!electionDataRef.get().isElectionOpen()) return;

        int currentYear = SkyBlockCalendar.getYear();

        try {
            ResolveElectionProtocolObject.ResolveElectionResponse response =
                SERVICE.<ResolveElectionProtocolObject.ResolveElectionMessage,
                    ResolveElectionProtocolObject.ResolveElectionResponse>handleRequest(
                    new ResolveElectionProtocolObject.ResolveElectionMessage(currentYear)
                ).join();

            if (response.serializedData() != null) {
                electionDataRef.set(GSON.fromJson(response.serializedData(), ElectionData.class));
            }

            if (response.resolved()) {
                Logger.info("Election resolved for Year {}. New Mayor: {}", currentYear, electionDataRef.get().getCurrentMayor());
            } else {
                Logger.info("Election for Year {} was already resolved by another server", currentYear);
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to resolve election via service");
        }

        playerVoteCache.clear();
    }

    public static CompletableFuture<Void> castVote(UUID accountId, String candidateName) {
        ElectionData data = electionDataRef.get();
        if (!data.isElectionOpen()) return CompletableFuture.completedFuture(null);

        boolean validCandidate = data.getCandidates().stream()
            .anyMatch(c -> c.getMayorName().equals(candidateName));
        if (!validCandidate) return CompletableFuture.completedFuture(null);

        return SERVICE.<CastVoteProtocolObject.CastVoteMessage,
            CastVoteProtocolObject.CastVoteResponse>handleRequest(
            new CastVoteProtocolObject.CastVoteMessage(accountId, candidateName)
        ).thenAccept(response -> {
            if (response.success()) {
                playerVoteCache.put(accountId, candidateName);
                if (response.talliesJson() != null) {
                    Map<String, Long> tallies = GSON.fromJson(
                        response.talliesJson(),
                        new TypeToken<Map<String, Long>>(){}.getType()
                    );
                    electionDataRef.get().updateTallies(tallies);
                }
            }
        }).exceptionally(e -> {
            Logger.error(e, "Failed to cast vote via service");
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
        return electionDataRef.get().getMayorEnum();
    }

    public static SkyBlockMayor getCurrentMinister() {
        return electionDataRef.get().getMinisterEnum();
    }

    public static boolean isMayorPerkActive(SkyBlockMayor.Perk perk) {
        return electionDataRef.get().getCurrentMayorPerkEnums().contains(perk);
    }

    public static boolean isMinisterPerkActive(SkyBlockMayor.Perk perk) {
        SkyBlockMayor.Perk activePerk = electionDataRef.get().getMinisterPerkEnum();
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
        ElectionData data = electionDataRef.get();
        int currentYear = SkyBlockCalendar.getYear();
        int currentMonth = SkyBlockCalendar.getMonth();

        if (currentMonth >= 6 && currentMonth <= 8 && !data.isElectionOpen()) {
            if (data.getElectionYear() != currentYear) {
                onElectionStart();
            }
        }

        if (currentMonth >= 9 && data.isElectionOpen()) {
            onElectionEnd();
        }
    }

    private static void startTallyRefreshTask() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (electionDataRef.get().isElectionOpen()) {
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
                electionDataRef.get().updateTallies(tallies);
            }
        }).exceptionally(e -> {
            Logger.warn("Failed to refresh election tallies: {}", e.getMessage());
            return null;
        });
    }
}
