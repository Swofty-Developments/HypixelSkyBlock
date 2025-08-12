// net/swofty/type/skyblockgeneric/data/SkyBlockDatapoint.java
package net.swofty.type.skyblockgeneric.data;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.commons.protocol.Serializer;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class SkyBlockDatapoint<T> extends Datapoint<T> {

    protected SkyBlockDatapoint(String key, T value, Serializer<T> serializer) {
        super(key, value, serializer);
    }

    @SneakyThrows
    public void setValueBypassCoop(T value) {
        this.value = value;
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(dataHandler.getUuid());
        if (player != null && hasOnChange()) {
            triggerOnChange(player, this);
        }
    }

    @Override
    @SneakyThrows
    public void setValue(T value) {
        if (Objects.equals(value, this.value)) return;
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(dataHandler.getUuid());
        if (player != null) {
            if (hasOnChange()) triggerOnChange(player, this);
            if (isCoopPersistent()
                    && dataHandler instanceof SkyBlockDataHandler sb
                    && sb.get(SkyBlockDataHandler.Data.IS_COOP) instanceof Datapoint<?> isCoopDp
                    && Boolean.TRUE.equals(isCoopDp.getValue())) {
                handleCoopUpdate(value, sb);
            }
        }
    }

    private void handleCoopUpdate(T value, SkyBlockDataHandler sbHandler) {
        var coop = CoopDatabase.getFromMember(sbHandler.getUuid());
        List<UUID> coopMembers = coop.members();
        List<UUID> coopMembersProfiles = new ArrayList<>(coop.memberProfiles());

        Set<UUID> updatedProfiles = new HashSet<>();

        for (UUID memberUuid : coopMembers) {
            SkyBlockPlayer member = SkyBlockGenericLoader.getFromUUID(memberUuid);
            if (member == null) continue;

            UUID selectedProfile = member.getProfiles().getCurrentlySelected();
            if (Objects.equals(memberUuid, sbHandler.getUuid())) {
                updatedProfiles.add(selectedProfile);
                continue;
            }

            if (!coopMembersProfiles.contains(selectedProfile)) continue;

            DataHandler dh = member.getDataHandler();
            Datapoint<?> dp = dh.getDatapoint(getKey());
            if (dp instanceof SkyBlockDatapoint<?> sbdp) {
                ((SkyBlockDatapoint<Object>) sbdp).setValueBypassCoop(value);
                updatedProfiles.add(selectedProfile);
            }
        }

        coopMembersProfiles.removeAll(updatedProfiles);
        UUID originProfileId = sbHandler.getCurrentProfileId();
        coopMembersProfiles.remove(originProfileId);

        for (UUID profileId : coopMembersProfiles) {
            Thread.startVirtualThread(() -> {
                ProfilesDatabase profilesDb = new ProfilesDatabase(profileId.toString());
                ProxyPlayer proxy = new ProxyPlayer(sbHandler.getUuid());

                CompletableFuture<Boolean> online = proxy.isOnline();
                online.join();

                SkyBlockDataHandler temp = SkyBlockDataHandler.createFromProfileOnly(profilesDb.getDocument());
                Datapoint<?> dp = temp.getDatapoint(getKey());
                ((Datapoint<Object>) dp).setValue(value);

                Document newDoc = temp.toProfileDocument(profileId);
                ProfilesDatabase.collection.replaceOne(new org.bson.Document("_id", profileId.toString()), newDoc);

                if (proxy.isOnline().join()) {
                    proxy.refreshCoopData(getKey());
                }
            });
        }
    }

    @Override
    protected boolean hasOnChange() {
        if (data instanceof HypixelDataHandler.Data d) return d.onChange != null;
        if (data instanceof SkyBlockDataHandler.Data d) return d.onChange != null;
        return false;
    }

    @Override
    protected void triggerOnChange(Player player, Datapoint<?> datapoint) {
        if (data instanceof HypixelDataHandler.Data hypixelData && hypixelData.onChange != null) {
            hypixelData.onChange.accept(player, datapoint);
        } else if (data instanceof SkyBlockDataHandler.Data sbData && sbData.onChange != null) {
            sbData.onChange.accept(player, datapoint);
        }
    }

    private boolean isCoopPersistent() {
        return (data instanceof SkyBlockDataHandler.Data sb) && Boolean.TRUE.equals(sb.getIsCoopPersistent());
    }
}
