package net.swofty.type.skyblockgeneric.data;

import net.swofty.LinkedField;
import net.swofty.api.DataAPIImpl;
import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class DataMutexService {

    @SuppressWarnings("unchecked")
    public <T> void withSynchronizedData(String lockKey, List<UUID> coopMembers,
                                         SkyBlockDataHandler.Data dataType,
                                         Function<T, T> operation,
                                         Runnable onFailure) {
        UUID coopId = resolveCoopId(coopMembers);
        LinkedField<UUID, String> field = dataType.coopField();
        if (coopId == null || field == null) {
            onFailure.run();
            return;
        }

        try (var ignored = ((DataAPIImpl) SwoftyData.profile())
                .lock("coop:" + dataType.getKey() + ":" + coopId, Duration.ofSeconds(5))) {
            Serializer<T> serializer = (Serializer<T>) dataType.getDefaultDatapoint().getSerializer();
            String stored = SwoftyData.profile().getDirect(coopId, field);
            T current = stored != null
                    ? serializer.deserialize(stored)
                    : (T) dataType.getDefaultDatapoint().getValue();

            T modified = operation.apply(current);
            if (modified != null) {
                SwoftyData.profile().setDirect(coopId, field, serializer.serialize(modified));
            }
        } catch (Exception e) {
            onFailure.run();
        }
    }

    private UUID resolveCoopId(List<UUID> coopMembers) {
        for (UUID member : coopMembers) {
            SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(member);
            if (player == null) continue;
            UUID profileId = player.getSkyblockDataHandler().getCurrentProfileId();
            return SwoftyData.profile().getLinkKey(profileId, CoopLinks.COOP).orElse(profileId);
        }
        return null;
    }
}
