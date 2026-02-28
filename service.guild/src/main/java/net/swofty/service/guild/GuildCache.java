package net.swofty.service.guild;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.guild.GuildMember;
import net.swofty.commons.guild.GuildPermission;
import net.swofty.commons.guild.GuildRank;
import net.swofty.commons.guild.events.*;
import net.swofty.commons.guild.events.response.*;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GuildCache {
    private static final Cache<UUID, GuildData> guildCache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterAccess(30, TimeUnit.MINUTES)
        .build();

    private static final Map<UUID, PendingGuildInvite> pendingInvites = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> lastChatTimestamps = new ConcurrentHashMap<>();

    public static boolean isInGuild(UUID playerUUID) {
        return GuildDatabase.getPlayerGuildId(playerUUID) != null;
    }

    public static GuildData getGuildFromPlayer(UUID playerUUID) {
        UUID guildId = GuildDatabase.getPlayerGuildId(playerUUID);
        if (guildId == null) return null;
        return getGuild(guildId);
    }

    public static GuildData getGuild(UUID guildId) {
        return guildCache.get(guildId, GuildDatabase::getGuild);
    }

    public static void handleCreateRequest(GuildCreateRequestEvent event) {
        UUID creator = event.getCreator();
        String guildName = event.getGuildName();

        if (isInGuild(creator)) {
            sendErrorToPlayer(creator, "§cYou are already in a guild! Leave your current guild to create a new one.");
            return;
        }

        if (guildName.length() < 3 || guildName.length() > 30) {
            sendErrorToPlayer(creator, "§cGuild name must be between 3 and 30 characters.");
            return;
        }

        if (GuildDatabase.guildNameExists(guildName)) {
            sendErrorToPlayer(creator, "§cA guild with that name already exists!");
            return;
        }

        GuildData guild = new GuildData(UUID.randomUUID(), guildName, creator);
        persistGuild(guild);
        GuildDatabase.mapPlayerToGuild(creator, guild.getGuildId());

        GuildCreatedResponseEvent response = new GuildCreatedResponseEvent(guild, creator);
        sendEvent(response);
    }

    public static void handleInviteRequest(GuildInviteRequestEvent event) {
        UUID inviter = event.getInviter();
        UUID invitee = event.getInvitee();

        GuildData guild = getGuildFromPlayer(inviter);
        if (guild == null) {
            sendErrorToPlayer(inviter, "§cYou are not in a guild!");
            return;
        }

        GuildRank inviterRank = guild.getMemberRank(inviter);
        if (inviterRank == null || !inviterRank.hasPermission(GuildPermission.INVITE)) {
            sendErrorToPlayer(inviter, "§cYou do not have permission to invite players!");
            return;
        }

        if (guild.isFull()) {
            sendErrorToPlayer(inviter, "§cYour guild is full!");
            return;
        }

        if (isInGuild(invitee)) {
            sendErrorToPlayer(inviter, "§cThat player is already in a guild!");
            return;
        }

        if (pendingInvites.containsKey(invitee)) {
            sendErrorToPlayer(inviter, "§cThat player already has a pending guild invite!");
            return;
        }

        PendingGuildInvite invite = new PendingGuildInvite(guild.getGuildId(), inviter, invitee);
        pendingInvites.put(invitee, invite);
        scheduleInviteExpiration(guild.getGuildId(), inviter, invitee, 60000);

        GuildInviteSentResponseEvent response = new GuildInviteSentResponseEvent(guild, inviter, invitee);
        sendEvent(response);
    }

    public static void handleAcceptInvite(GuildAcceptInviteRequestEvent event) {
        UUID accepter = event.getAccepter();
        UUID inviter = event.getInviter();

        PendingGuildInvite invite = pendingInvites.get(accepter);
        if (invite == null || !invite.inviter().equals(inviter)) {
            sendErrorToPlayer(accepter, "§cYou don't have a pending invite from that player, or it has expired!");
            return;
        }

        if (isInGuild(accepter)) {
            sendErrorToPlayer(accepter, "§cYou must leave your current guild before joining another!");
            return;
        }

        GuildData guild = getGuild(invite.guildId());
        if (guild == null) {
            sendErrorToPlayer(accepter, "§cThe guild no longer exists!");
            pendingInvites.remove(accepter);
            return;
        }

        if (guild.isFull()) {
            sendErrorToPlayer(accepter, "§cThe guild is now full!");
            pendingInvites.remove(accepter);
            return;
        }

        GuildMember newMember = new GuildMember(accepter, "Member", System.currentTimeMillis());
        guild.getMembers().add(newMember);
        pendingInvites.remove(accepter);

        persistGuild(guild);
        GuildDatabase.mapPlayerToGuild(accepter, guild.getGuildId());

        GuildMemberJoinedResponseEvent response = new GuildMemberJoinedResponseEvent(guild, accepter);
        sendEvent(response);
    }

    public static void handleLeaveRequest(GuildLeaveRequestEvent event) {
        UUID leaver = event.getLeaver();

        GuildData guild = getGuildFromPlayer(leaver);
        if (guild == null) {
            sendErrorToPlayer(leaver, "§cYou are not in a guild!");
            return;
        }

        if (guild.getMasterUuid().equals(leaver)) {
            sendErrorToPlayer(leaver, "§cYou cannot leave a guild as the Guild Master! Transfer ownership first or disband the guild.");
            return;
        }

        GuildMember member = guild.getMember(leaver);
        guild.getMembers().remove(member);

        persistGuild(guild);
        GuildDatabase.removePlayerMapping(leaver);

        GuildMemberLeftResponseEvent response = new GuildMemberLeftResponseEvent(guild, leaver);
        sendEvent(response);
    }

    public static void handleKickRequest(GuildKickRequestEvent event) {
        UUID kicker = event.getKicker();
        UUID target = event.getTarget();
        String reason = event.getReason();

        GuildData guild = getGuildFromPlayer(kicker);
        if (guild == null) {
            sendErrorToPlayer(kicker, "§cYou are not in a guild!");
            return;
        }

        GuildRank kickerRank = guild.getMemberRank(kicker);
        if (kickerRank == null || !kickerRank.hasPermission(GuildPermission.KICK)) {
            sendErrorToPlayer(kicker, "§cYou do not have permission to kick members!");
            return;
        }

        GuildMember targetMember = guild.getMember(target);
        if (targetMember == null) {
            sendErrorToPlayer(kicker, "§cThat player is not in your guild!");
            return;
        }

        GuildRank targetRank = guild.getRank(targetMember.getRankName());
        if (targetRank != null && !kickerRank.isHigherThan(targetRank)) {
            sendErrorToPlayer(kicker, "§cYou cannot kick someone of equal or higher rank!");
            return;
        }

        guild.getMembers().remove(targetMember);

        persistGuild(guild);
        GuildDatabase.removePlayerMapping(target);

        GuildMemberKickedResponseEvent response = new GuildMemberKickedResponseEvent(guild, kicker, target, reason);
        sendEvent(response);
    }

    public static void handleDisbandRequest(GuildDisbandRequestEvent event) {
        UUID disbander = event.getDisbander();

        GuildData guild = getGuildFromPlayer(disbander);
        if (guild == null) {
            sendErrorToPlayer(disbander, "§cYou are not in a guild!");
            return;
        }

        if (!guild.getMasterUuid().equals(disbander)) {
            sendErrorToPlayer(disbander, "§cOnly the Guild Master can disband the guild!");
            return;
        }

        disbandGuild(guild, disbander);
    }

    public static void handlePromoteRequest(GuildPromoteRequestEvent event) {
        UUID promoter = event.getPromoter();
        UUID target = event.getTarget();

        GuildData guild = getGuildFromPlayer(promoter);
        if (guild == null) {
            sendErrorToPlayer(promoter, "§cYou are not in a guild!");
            return;
        }

        GuildRank promoterRank = guild.getMemberRank(promoter);
        if (promoterRank == null || !promoterRank.hasPermission(GuildPermission.PROMOTE)) {
            sendErrorToPlayer(promoter, "§cYou do not have permission to promote members!");
            return;
        }

        GuildMember targetMember = guild.getMember(target);
        if (targetMember == null) {
            sendErrorToPlayer(promoter, "§cThat player is not in your guild!");
            return;
        }

        GuildRank currentRank = guild.getRank(targetMember.getRankName());
        if (currentRank == null) return;

        GuildRank nextRank = guild.getNextRankUp(currentRank);
        if (nextRank == null || nextRank.getName().equals("Guild Master")) {
            sendErrorToPlayer(promoter, "§cThat player cannot be promoted any further!");
            return;
        }

        if (!promoterRank.isHigherThan(nextRank)) {
            sendErrorToPlayer(promoter, "§cYou can only promote members to ranks below yours!");
            return;
        }

        String oldRank = targetMember.getRankName();
        targetMember.setRankName(nextRank.getName());
        persistGuild(guild);

        GuildRankChangedResponseEvent response = new GuildRankChangedResponseEvent(guild, promoter, target, oldRank, nextRank.getName());
        sendEvent(response);
    }

    public static void handleDemoteRequest(GuildDemoteRequestEvent event) {
        UUID demoter = event.getDemoter();
        UUID target = event.getTarget();

        GuildData guild = getGuildFromPlayer(demoter);
        if (guild == null) {
            sendErrorToPlayer(demoter, "§cYou are not in a guild!");
            return;
        }

        GuildRank demoterRank = guild.getMemberRank(demoter);
        if (demoterRank == null || !demoterRank.hasPermission(GuildPermission.DEMOTE)) {
            sendErrorToPlayer(demoter, "§cYou do not have permission to demote members!");
            return;
        }

        GuildMember targetMember = guild.getMember(target);
        if (targetMember == null) {
            sendErrorToPlayer(demoter, "§cThat player is not in your guild!");
            return;
        }

        GuildRank currentRank = guild.getRank(targetMember.getRankName());
        if (currentRank == null) return;

        GuildRank nextRank = guild.getNextRankDown(currentRank);
        if (nextRank == null) {
            sendErrorToPlayer(demoter, "§cThat player cannot be demoted any further!");
            return;
        }

        if (!demoterRank.isHigherThan(currentRank)) {
            sendErrorToPlayer(demoter, "§cYou can only demote members of lower rank!");
            return;
        }

        String oldRank = targetMember.getRankName();
        targetMember.setRankName(nextRank.getName());
        persistGuild(guild);

        GuildRankChangedResponseEvent response = new GuildRankChangedResponseEvent(guild, demoter, target, oldRank, nextRank.getName());
        sendEvent(response);
    }

    public static void handleTransferRequest(GuildTransferRequestEvent event) {
        UUID currentOwner = event.getCurrentOwner();
        UUID newOwner = event.getNewOwner();

        GuildData guild = getGuildFromPlayer(currentOwner);
        if (guild == null) {
            sendErrorToPlayer(currentOwner, "§cYou are not in a guild!");
            return;
        }

        if (!guild.getMasterUuid().equals(currentOwner)) {
            sendErrorToPlayer(currentOwner, "§cOnly the Guild Master can transfer ownership!");
            return;
        }

        GuildMember newOwnerMember = guild.getMember(newOwner);
        if (newOwnerMember == null) {
            sendErrorToPlayer(currentOwner, "§cThat player is not in your guild!");
            return;
        }

        GuildMember currentOwnerMember = guild.getMember(currentOwner);
        currentOwnerMember.setRankName("Officer");
        newOwnerMember.setRankName("Guild Master");
        persistGuild(guild);

        GuildTransferredResponseEvent response = new GuildTransferredResponseEvent(guild, currentOwner, newOwner);
        sendEvent(response);
    }

    public static void handleChatRequest(GuildChatRequestEvent event) {
        UUID sender = event.getSender();
        String message = event.getMessage();
        boolean officerChat = event.isOfficerChat();

        GuildData guild = getGuildFromPlayer(sender);
        if (guild == null) {
            sendErrorToPlayer(sender, "§cYou are not in a guild!");
            return;
        }

        if (officerChat) {
            GuildRank rank = guild.getMemberRank(sender);
            if (rank == null || !rank.hasPermission(GuildPermission.OFFICER_CHAT)) {
                sendErrorToPlayer(sender, "§cYou do not have permission to use officer chat!");
                return;
            }
        }

        GuildMember member = guild.getMember(sender);
        if (member != null && member.isMuted()) {
            sendErrorToPlayer(sender, "§cYou are muted in this guild!");
            return;
        }

        if (guild.isEveryoneMuted() && guild.getEveryoneMutedExpiry() > System.currentTimeMillis()) {
            GuildRank rank = guild.getMemberRank(sender);
            if (rank == null || !rank.hasPermission(GuildPermission.MUTE_MEMBERS)) {
                sendErrorToPlayer(sender, "§cGuild chat is currently muted!");
                return;
            }
        }

        if (guild.isSlowChat()) {
            Long lastChat = lastChatTimestamps.get(sender);
            if (lastChat != null && System.currentTimeMillis() - lastChat < 10000) {
                sendErrorToPlayer(sender, "§cSlow mode is enabled. Please wait before sending another message.");
                return;
            }
            lastChatTimestamps.put(sender, System.currentTimeMillis());
        }

        GuildChatResponseEvent response = new GuildChatResponseEvent(guild, sender, message, officerChat);
        sendEvent(response);
    }

    public static void handleSettingRequest(GuildSettingRequestEvent event) {
        UUID changer = event.getChanger();
        String setting = event.getSetting();
        String value = event.getValue();

        GuildData guild = getGuildFromPlayer(changer);
        if (guild == null) {
            sendErrorToPlayer(changer, "§cYou are not in a guild!");
            return;
        }

        GuildRank rank = guild.getMemberRank(changer);
        if (rank == null) return;

        switch (setting.toLowerCase()) {
            case "tag" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_TAG)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to change the guild tag!");
                    return;
                }
                if (!guild.canSetTag()) {
                    sendErrorToPlayer(changer, "§cYour guild must be Level 5 to set a tag!");
                    return;
                }
                if (value.length() > guild.getMaxTagLength()) {
                    sendErrorToPlayer(changer, "§cTag cannot be longer than " + guild.getMaxTagLength() + " characters!");
                    return;
                }
                guild.setTag(value);
            }
            case "tagcolor" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_TAG)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to change the tag color!");
                    return;
                }
                guild.setTagColor(value);
            }
            case "motd" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_MOTD)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to change the MOTD!");
                    return;
                }
                guild.setMotd(value);
            }
            case "description" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_DESCRIPTION)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to change the description!");
                    return;
                }
                guild.setDescription(value);
            }
            case "discord" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_DISCORD)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to change the discord link!");
                    return;
                }
                guild.setDiscordLink(value);
            }
            case "rename" -> {
                if (!guild.getMasterUuid().equals(changer)) {
                    sendErrorToPlayer(changer, "§cOnly the Guild Master can rename the guild!");
                    return;
                }
                if (value.length() < 3 || value.length() > 30) {
                    sendErrorToPlayer(changer, "§cGuild name must be between 3 and 30 characters.");
                    return;
                }
                if (GuildDatabase.guildNameExists(value)) {
                    sendErrorToPlayer(changer, "§cA guild with that name already exists!");
                    return;
                }
                guild.setName(value);
            }
            case "slow" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_SETTINGS)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to modify settings!");
                    return;
                }
                guild.setSlowChat(!guild.isSlowChat());
                value = guild.isSlowChat() ? "enabled" : "disabled";
            }
            case "finder" -> {
                if (!rank.hasPermission(GuildPermission.MODIFY_SETTINGS)) {
                    sendErrorToPlayer(changer, "§cYou do not have permission to modify settings!");
                    return;
                }
                guild.setListedInFinder(!guild.isListedInFinder());
                value = guild.isListedInFinder() ? "visible" : "hidden";
            }
            default -> {
                sendErrorToPlayer(changer, "§cUnknown setting: " + setting);
                return;
            }
        }

        persistGuild(guild);

        GuildSettingChangedResponseEvent response = new GuildSettingChangedResponseEvent(guild, changer, setting, value);
        sendEvent(response);
    }

    public static void handleMuteRequest(GuildMuteRequestEvent event) {
        UUID muter = event.getMuter();
        String target = event.getTarget();
        long duration = event.getDuration();

        GuildData guild = getGuildFromPlayer(muter);
        if (guild == null) {
            sendErrorToPlayer(muter, "§cYou are not in a guild!");
            return;
        }

        GuildRank muterRank = guild.getMemberRank(muter);
        if (muterRank == null || !muterRank.hasPermission(GuildPermission.MUTE_MEMBERS)) {
            sendErrorToPlayer(muter, "§cYou do not have permission to mute guild members!");
            return;
        }

        if (target.equalsIgnoreCase("everyone")) {
            guild.setEveryoneMuted(true);
            guild.setEveryoneMutedExpiry(System.currentTimeMillis() + duration);
        } else {
            UUID targetUUID = UUID.fromString(target);
            GuildMember member = guild.getMember(targetUUID);
            if (member == null) {
                sendErrorToPlayer(muter, "§cThat player is not in your guild!");
                return;
            }
            member.setMutedUntil(System.currentTimeMillis() + duration);
        }

        persistGuild(guild);

        GuildMuteChangedResponseEvent response = new GuildMuteChangedResponseEvent(guild, muter, target, duration, false);
        sendEvent(response);
    }

    public static void handleUnmuteRequest(GuildUnmuteRequestEvent event) {
        UUID unmuter = event.getUnmuter();
        String target = event.getTarget();

        GuildData guild = getGuildFromPlayer(unmuter);
        if (guild == null) {
            sendErrorToPlayer(unmuter, "§cYou are not in a guild!");
            return;
        }

        GuildRank unmuterRank = guild.getMemberRank(unmuter);
        if (unmuterRank == null || !unmuterRank.hasPermission(GuildPermission.MUTE_MEMBERS)) {
            sendErrorToPlayer(unmuter, "§cYou do not have permission to unmute guild members!");
            return;
        }

        if (target.equalsIgnoreCase("everyone")) {
            guild.setEveryoneMuted(false);
            guild.setEveryoneMutedExpiry(0);
        } else {
            UUID targetUUID = UUID.fromString(target);
            GuildMember member = guild.getMember(targetUUID);
            if (member == null) {
                sendErrorToPlayer(unmuter, "§cThat player is not in your guild!");
                return;
            }
            member.setMutedUntil(0);
        }

        persistGuild(guild);

        GuildMuteChangedResponseEvent response = new GuildMuteChangedResponseEvent(guild, unmuter, target, 0, true);
        sendEvent(response);
    }

    public static void handleSetRankRequest(GuildSetRankRequestEvent event) {
        UUID setter = event.getSetter();
        UUID target = event.getTarget();
        String rankName = event.getRankName();

        GuildData guild = getGuildFromPlayer(setter);
        if (guild == null) {
            sendErrorToPlayer(setter, "§cYou are not in a guild!");
            return;
        }

        if (!guild.getMasterUuid().equals(setter)) {
            sendErrorToPlayer(setter, "§cOnly the Guild Master can set ranks directly!");
            return;
        }

        GuildMember targetMember = guild.getMember(target);
        if (targetMember == null) {
            sendErrorToPlayer(setter, "§cThat player is not in your guild!");
            return;
        }

        GuildRank newRank = guild.getRank(rankName);
        if (newRank == null) {
            sendErrorToPlayer(setter, "§cThat rank does not exist!");
            return;
        }

        if (newRank.getName().equals("Guild Master")) {
            sendErrorToPlayer(setter, "§cUse /guild transfer to transfer Guild Master!");
            return;
        }

        String oldRank = targetMember.getRankName();
        targetMember.setRankName(newRank.getName());
        persistGuild(guild);

        GuildRankChangedResponseEvent response = new GuildRankChangedResponseEvent(guild, setter, target, oldRank, newRank.getName());
        sendEvent(response);
    }

    private static void disbandGuild(GuildData guild, UUID disbander) {
        GuildDisbandedResponseEvent response = new GuildDisbandedResponseEvent(guild, disbander);
        sendEvent(response);

        guildCache.invalidate(guild.getGuildId());
        GuildDatabase.deleteGuild(guild.getGuildId());
    }

    private static void sendEvent(GuildEvent event) {
        JSONObject message = new JSONObject();
        message.put("eventType", event.getClass().getSimpleName());
        message.put("eventData", event.getSerializer().serialize(event));
        message.put("participants", event.getParticipants());

        ServiceToServerManager.sendToAllServers(FromServiceChannels.PROPAGATE_GUILD_EVENT, message);
    }

    private static void sendErrorToPlayer(UUID playerUUID, String message) {
        sendMessageToPlayer(playerUUID, "§9§m-----------------------------------------------------\n" + message + "\n§9§m-----------------------------------------------------");
    }

    private static void sendMessageToPlayer(UUID playerUUID, String message) {
        JSONObject messageData = new JSONObject();
        messageData.put("playerUUID", playerUUID.toString());
        messageData.put("message", message);

        ServiceToServerManager.sendToAllServers(FromServiceChannels.SEND_MESSAGE, messageData);
    }

    private static void scheduleInviteExpiration(UUID guildId, UUID inviter, UUID invitee, long delayMs) {
        CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS).execute(() -> {
            PendingGuildInvite invite = pendingInvites.remove(invitee);
            if (invite != null) {
                GuildData guild = getGuild(guildId);
                if (guild != null) {
                    GuildInviteExpiredResponseEvent response = new GuildInviteExpiredResponseEvent(guild, inviter, invitee);
                    sendEvent(response);
                }
            }
        });
    }

    private static void persistGuild(GuildData guild) {
        guildCache.put(guild.getGuildId(), guild);
        GuildDatabase.saveGuild(guild);
    }

    public record PendingGuildInvite(UUID guildId, UUID inviter, UUID invitee) {
    }
}
