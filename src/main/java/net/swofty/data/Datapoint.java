package net.swofty.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.SkyBlock;
import net.swofty.data.datapoints.DatapointBoolean;
import net.swofty.data.mongodb.CoopDatabase;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.serializer.Serializer;
import net.swofty.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Datapoint<T> {
    DataHandler dataHandler;
    @Getter
    private String key;
    @Getter
    private T value;
    protected Serializer<T> serializer;
    DataHandler.Data data;

    protected Datapoint(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    @SneakyThrows
    public Datapoint deepClone() {
        Datapoint toReturn;
        if (this.value != null) {
            toReturn = this.getClass().getConstructor(String.class, this.value.getClass()).newInstance(this.key, this.value);
        } else {
            toReturn = this.getClass().getConstructor(String.class).newInstance(this.key);
        }
        return toReturn;
    }

    public Datapoint<T> setUser(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

    public Datapoint<T> setData(DataHandler.Data data) {
        this.data = data;
        return this;
    }

    public String getSerializedValue() throws JsonProcessingException {
        return serializer.serialize(value);
    }

    public void deserializeValue(String json) throws JsonProcessingException {
        this.value = serializer.deserialize(json);
    }

    public void setValueBypassCoop(T value) {
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getPlayer(dataHandler.getUuid());
        if (player != null)
            if (data.onChange != null)
                data.onChange.accept(player, this);
    }

    public void setValue(T value) {
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getPlayer(dataHandler.getUuid());
        if (player != null) {
            if (data.onChange != null)
                data.onChange.accept(player, this);

            // Handle coop datapoints
            if (data.getIsCoopPersistent() && dataHandler.get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
                List<UUID> coopMembers = CoopDatabase.getFromMember(dataHandler.getUuid()).members();
                List<UUID> coopMembersProfiles = CoopDatabase.getFromMember(dataHandler.getUuid()).memberProfiles();

                List<UUID> updatedProfiles = new ArrayList<>();

                for (UUID member : coopMembers) {
                    // Handle online coop members datapoints
                    SkyBlockPlayer skyBlockPlayer = SkyBlock.getFromUUID(member);
                    if (skyBlockPlayer != null) {
                        UUID selectedProfile = skyBlockPlayer.getProfiles().getCurrentlySelected();

                        if (skyBlockPlayer.getDataHandler().getUuid() == dataHandler.getUuid()) {
                            // Ensure we don't update the player's own datapoints
                            updatedProfiles.add(selectedProfile);
                            continue;
                        }

                        // Player is not on their coop profile
                        if (!coopMembersProfiles.contains(selectedProfile)) continue;

                        DataHandler dataHandler = skyBlockPlayer.getDataHandler();
                        dataHandler.getDatapoint(key).setValueBypassCoop(value);
                        updatedProfiles.add(selectedProfile);
                    }
                }

                coopMembersProfiles.removeAll(updatedProfiles);
                coopMembersProfiles.remove(dataHandler.getUuid());
                coopMembersProfiles.forEach(uuid -> {
                    ProfilesDatabase profilesDatabase = new ProfilesDatabase(uuid.toString());
                    DataHandler dataHandler = DataHandler.fromDocument(profilesDatabase.getDocument());
                    dataHandler.getDatapoint(key).value = value;

                    Document document = dataHandler.toDocument(uuid);
                    ProfilesDatabase.collection.replaceOne(profilesDatabase.getDocument(), document);
                });
            }
        }
    }
}