package net.swofty.type.lobby.gui;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.friend.Friend;
import net.swofty.commons.friend.FriendData;
import net.swofty.commons.presence.PresenceInfo;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointFriendSort;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.experience.PlayerExperienceHandler;
import net.swofty.type.generic.friend.FriendManager;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class GUIFriends extends HypixelInventoryGUI {
    private static int[] COLOURED_PANE_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17
    };
    private static final int FRIENDS_PER_PAGE = 18;
    private static final int[] FRIEND_SLOTS = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };

    private int currentPage = 1;
    private String searchFilter = null;

    public GUIFriends() {
        super("Friends", InventoryType.CHEST_6_ROW);
    }

    public GUIFriends(int page) {
        super("Friends", InventoryType.CHEST_6_ROW);
        this.currentPage = page;
    }

    public GUIFriends(int page, String searchFilter) {
        super("Friends", InventoryType.CHEST_6_ROW);
        this.currentPage = page;
        this.searchFilter = searchFilter;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerExperienceHandler xpHandler = player.getExperienceHandler();
        int level = xpHandler.getLevel();
        int achievementPoints = player.getAchievementHandler().getTotalPoints();

        for (int slot : COLOURED_PANE_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.createNamedItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
                }
            });
        }

        // Get friend data
        FriendData friendData = FriendManager.getFriendData(player);
        List<Friend> allFriendsUnfiltered = friendData != null ? friendData.getFriends() : new ArrayList<>();

        // Apply search filter if set
        List<Friend> allFriends;
        if (searchFilter != null && !searchFilter.isEmpty()) {
            String lowerFilter = searchFilter.toLowerCase();
            allFriends = allFriendsUnfiltered.stream()
                    .filter(f -> {
                        try {
                            String rawName = HypixelPlayer.getRawName(f.getUuid());
                            return rawName != null && rawName.toLowerCase().contains(lowerFilter);
                        } catch (Exception ignored) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            allFriends = allFriendsUnfiltered;
        }

        // Get presence data for all friends
        List<UUID> friendUuids = allFriends.stream().map(Friend::getUuid).collect(Collectors.toList());
        List<PresenceInfo> presenceList = FriendManager.getPresenceBulk(friendUuids);
        Map<UUID, PresenceInfo> presenceMap = presenceList.stream()
                .collect(Collectors.toMap(PresenceInfo::getUuid, p -> p, (a, b) -> a));

        // Get sort preferences
        DatapointFriendSort.FriendSortData sortData = player.getDataHandler()
                .get(HypixelDataHandler.Data.FRIEND_SORT, DatapointFriendSort.class).getValue();

        // Sort friends based on preferences
        List<FriendDisplayEntry> sortedFriends = sortFriends(allFriends, presenceMap, sortData);

        // Calculate pagination
        int totalPages = Math.max(1, (int) Math.ceil((double) sortedFriends.size() / FRIENDS_PER_PAGE));
        currentPage = Math.min(currentPage, totalPages);
        int startIndex = (currentPage - 1) * FRIENDS_PER_PAGE;
        int endIndex = Math.min(startIndex + FRIENDS_PER_PAGE, sortedFriends.size());
        List<FriendDisplayEntry> pageEntries = sortedFriends.subList(startIndex, endIndex);

        // Header Row - Player's own head (slot 2)
        set(new GUIClickableItem(2) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String displayName = player.getFullDisplayName();
                return ItemStackCreator.getStackHead(
                        displayName,
                        player.getSkin(),
                        1,
                        "§7Hypixel Level: §6" + level,
                        "§7Achievement Points: §e" + StringUtility.commaify(achievementPoints),
                        "§7Guild: §bNone",
                        "",
                        "§eClick to go back!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMyProfile().open(player);
            }
        });

        // Friends tab (slot 3) - current tab indicator
        set(new GUIItem(3) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.enchant(ItemStackCreator.getStackHead(
                        "§aFriends",
                        "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                        1,
                        "§7View your Hypixel friends' profiles,",
                        "§7and interact with your online friends!",
                        "",
                        "§eCurrently viewing!"
                ));
            }
        });

        // Party tab (slot 4)
        set(new GUIClickableItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aParty",
                        "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                        1,
                        "§7Create a party and join up with",
                        "§7other players to play games",
                        "§7together!",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIParty().open(player);
            }
        });

        // Guild tab (slot 5) - non-functional per user request
        set(new GUIItem(5) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aGuild",
                        "fe8b59f8cce510809427c3843cf575fae8fe6a8b7d1560dd46958d148563815",
                        1,
                        "§7Form a guild with other Hypixel",
                        "§7players to conquer game modes and",
                        "§7work towards common Hypixel",
                        "§7rewards."
                );
            }
        });

        // Recent Players tab (slot 6) - non-functional per user request
        set(new GUIItem(6) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aRecent Players",
                        "9993a356809532d696841a37a0549b81b159b79a7b2919cff4e5abdfea83d66",
                        1,
                        "§7View players you have played recent",
                        "§7games with."
                );
            }
        });

        // Add Friend button (slot 18)
        set(new GUIClickableItem(18) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aAdd Friend",
                        Material.WRITABLE_BOOK,
                        1,
                        "§7Click to add a friend to your friend",
                        "§7list.",
                        "",
                        "§7Friends can see what each other",
                        "§7are doing on the network, and can",
                        "§7see when each other are online.",
                        "",
                        "§eClick to add a friend!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
                new HypixelSignGUI(player).open(new String[]{"Enter friend", "name above"})
                        .thenAccept(name -> {
                            if (name != null && !name.trim().isEmpty()) {
                                FriendManager.addFriend(player, name.trim());
                            }
                        });
            }
        });

        // Close button (slot 19)
        set(GUIClickableItem.getCloseItem(19));

        // Change Sort button (slot 25)
        int finalTotalPages = totalPages;
        set(new GUIClickableItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String currentSort = switch (sortData.sortType) {
                    case DEFAULT -> "Default";
                    case ALPHABETICAL -> "Alphabetical";
                    case LAST_ONLINE -> "Last Online";
                };
                String orderText = sortData.reversed ? "Reversed" : "Normal";

                return ItemStackCreator.getStack(
                        "§aChange Sort",
                        Material.HOPPER,
                        1,
                        "§7Current sort: §b" + currentSort,
                        "§7Sorting order: §b" + orderText,
                        "",
                        "§bDefault§7: Alphabetical order, but",
                        "§7show online players first",
                        "§bAlphabetical§7: Show everyone",
                        "§7listed from A-Z",
                        "§bLast Online§7: Sorts by who was",
                        "§7most recently online",
                        "",
                        "§eLEFT CLICK§7 to change between",
                        "§7all the available sorting options.",
                        "",
                        "§eRIGHT CLICK§7 to reverse the",
                        "§7current order!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                DatapointFriendSort datapoint = player.getDataHandler()
                        .get(HypixelDataHandler.Data.FRIEND_SORT, DatapointFriendSort.class);
                DatapointFriendSort.FriendSortData data = datapoint.getValue();

                if (e.getClick() instanceof Click.Right) {
                    data.toggleReversed();
                } else {
                    data.cycleSortType();
                }

                datapoint.setValue(data);
                new GUIFriends(currentPage, searchFilter).open(player);
            }
        });

        // Search Players button (slot 26)
        int matchingCount = allFriends.size();
        set(new GUIClickableItem(26) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.enchant(ItemStackCreator.getStack(
                            "§aSearch: §e" + searchFilter,
                            Material.OAK_SIGN,
                            1,
                            "§7Currently filtering by: §f" + searchFilter,
                            "§7Showing §e" + matchingCount + "§7 matching friends",
                            "",
                            "§eLEFT CLICK§7 to search for a",
                            "§7different player.",
                            "",
                            "§eRIGHT CLICK§7 to clear the",
                            "§7search filter."
                    ));
                } else {
                    return ItemStackCreator.getStack(
                            "§aSearch Players",
                            Material.OAK_SIGN,
                            1,
                            "§7Search for a player by name",
                            "§7in your friends list.",
                            "",
                            "§eClick to search!"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.Right && searchFilter != null && !searchFilter.isEmpty()) {
                    // Clear the search filter
                    new GUIFriends(1).open(player);
                } else {
                    // Open sign GUI to search
                    player.closeInventory();
                    new HypixelSignGUI(player).open(new String[]{"Enter player", "name to search"})
                            .thenAccept(name -> {
                                if (name != null && !name.trim().isEmpty()) {
                                    new GUIFriends(1, name.trim()).open(player);
                                } else {
                                    new GUIFriends(1).open(player);
                                }
                            });
                }
            }
        });

        // Display friends
        for (int i = 0; i < FRIEND_SLOTS.length; i++) {
            int slot = FRIEND_SLOTS[i];
            if (i < pageEntries.size()) {
                FriendDisplayEntry entry = pageEntries.get(i);
                set(createFriendItem(slot, entry));
            }
        }

        // Previous page arrow (slot 45)
        if (currentPage > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aPrevious Page",
                            Material.ARROW,
                            1,
                            "§7Page " + (currentPage - 1) + "/" + finalTotalPages
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUIFriends(currentPage - 1, searchFilter).open(player);
                }
            });
        }

        // Next page arrow (slot 53)
        if (currentPage < totalPages) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aNext Page",
                            Material.ARROW,
                            1,
                            "§7Page " + (currentPage + 1) + "/" + finalTotalPages
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUIFriends(currentPage + 1, searchFilter).open(player);
                }
            });
        }

        // Page indicator (slot 49)
        int totalFriendCount = allFriendsUnfiltered.size();
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (searchFilter != null && !searchFilter.isEmpty()) {
                    return ItemStackCreator.getStack(
                            "§aPage " + currentPage + "/" + finalTotalPages,
                            Material.BOOK,
                            1,
                            "§7Showing §e" + matchingCount + "§7 of §e" + totalFriendCount + "§7 friends",
                            "§7Searching for: §f" + searchFilter
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§aPage " + currentPage + "/" + finalTotalPages,
                            Material.BOOK,
                            1,
                            "§7Total friends: §e" + totalFriendCount
                    );
                }
            }
        });

        updateItemStacks(getInventory(), player);
    }

    private List<FriendDisplayEntry> sortFriends(List<Friend> friends, Map<UUID, PresenceInfo> presenceMap,
                                                  DatapointFriendSort.FriendSortData sortData) {
        List<FriendDisplayEntry> entries = friends.stream()
                .map(f -> new FriendDisplayEntry(f, presenceMap.get(f.getUuid())))
                .collect(Collectors.toList());

        Comparator<FriendDisplayEntry> comparator = switch (sortData.sortType) {
            case DEFAULT -> Comparator
                    .comparing((FriendDisplayEntry e) -> !e.isOnline())
                    .thenComparing(e -> e.getDisplayName().toLowerCase());
            case ALPHABETICAL -> Comparator.comparing(e -> e.getDisplayName().toLowerCase());
            case LAST_ONLINE -> Comparator.comparing(FriendDisplayEntry::getLastSeen).reversed();
        };

        if (sortData.reversed) {
            comparator = comparator.reversed();
        }

        entries.sort(comparator);
        return entries;
    }

    private GUIClickableItem createFriendItem(int slot, FriendDisplayEntry entry) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();

                // Get friend's level and achievement points
                try {
                    HypixelDataHandler friendData = HypixelDataHandler.getOfOfflinePlayer(entry.getUuid());
                    long friendLevel = friendData.get(HypixelDataHandler.Data.HYPIXEL_EXPERIENCE,
                            net.swofty.type.generic.data.datapoints.DatapointHypixelExperience.class).getValue();
                    int friendLevelDisplay = (int) (friendLevel / 2500); // Approximate level calculation
                    lore.add("§7Hypixel Level: §6" + friendLevelDisplay);
                } catch (Exception e) {
                    lore.add("§7Hypixel Level: §6?");
                }

                lore.add("§7Guild: §bNone");
                lore.add("");

                if (entry.isOnline()) {
                    String server = entry.getServerInfo();
                    if (server != null && !server.isEmpty()) {
                        lore.add("§aOnline: §e" + server);
                    } else {
                        lore.add("§aOnline");
                    }
                } else {
                    String lastSeenText = formatLastSeen(entry.getLastSeen());
                    lore.add("§7Last Online: §b" + lastSeenText);
                }

                if (entry.isBestFriend()) {
                    lore.add("");
                    lore.add("§6Best Friend");
                }

                lore.add("");
                lore.add("§eLeft-click to view profile");
                lore.add("§eShift-click to remove friend");

                String namePrefix = entry.isOnline() ? "§a" : "§7";
                String displayName = namePrefix + entry.getDisplayName();
                if (entry.isBestFriend()) {
                    displayName = "§6" + displayName;
                }

                // Get skin for the friend
                PlayerSkin skin = getFriendSkin(entry.getUuid());
                if (skin != null) {
                    return ItemStackCreator.getStackHead(displayName, skin, 1, lore);
                } else {
                    // Fallback to default Steve head
                    return ItemStackCreator.getStackHead(displayName,
                            "8667ba71b85a4004af54457a9734eed7e09dcc6abe4dd49f4c11d4c8e3c91cfe",
                            1, lore);
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.LeftShift || e.getClick() instanceof Click.RightShift) {
                    // Remove friend
                    FriendManager.removeFriend(player, entry.getRawName());
                    player.sendMessage("§cRemoving friend...");
                    // Refresh the GUI
                    new GUIFriends(currentPage, searchFilter).open(player);
                } else {
                    // View profile - for now just send a message
                    player.sendMessage("§eViewing profile of §f" + entry.getRawName() + "§e...");
                }
            }
        };
    }

    private PlayerSkin getFriendSkin(UUID uuid) {
        try {
            HypixelDataHandler friendData = HypixelDataHandler.getOfOfflinePlayer(uuid);
            String texture = friendData.get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
            String signature = friendData.get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();

            if (texture != null && !texture.equals("null") && !texture.isEmpty()) {
                return new PlayerSkin(texture, signature != null && !signature.equals("null") ? signature : null);
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        return null;
    }

    private String formatLastSeen(long lastSeenTimestamp) {
        if (lastSeenTimestamp <= 0) {
            return "Unknown";
        }

        long secondsAgo = (System.currentTimeMillis() - lastSeenTimestamp) / 1000;

        if (secondsAgo < 60) {
            return secondsAgo + " seconds ago";
        }

        long minutesAgo = secondsAgo / 60;
        if (minutesAgo < 60) {
            return minutesAgo + " minute" + (minutesAgo != 1 ? "s" : "") + " ago";
        }

        long hoursAgo = minutesAgo / 60;
        if (hoursAgo < 24) {
            long remainingMinutes = minutesAgo % 60;
            return hoursAgo + " hour" + (hoursAgo != 1 ? "s" : "") + ", " + remainingMinutes + " minute" + (remainingMinutes != 1 ? "s" : "") + " ago";
        }

        long daysAgo = hoursAgo / 24;
        if (daysAgo < 30) {
            return daysAgo + " day" + (daysAgo != 1 ? "s" : "") + " ago";
        }

        long monthsAgo = daysAgo / 30;
        return monthsAgo + " month" + (monthsAgo != 1 ? "s" : "") + " ago";
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private static class FriendDisplayEntry {
        private final Friend friend;
        private final PresenceInfo presence;
        private String cachedDisplayName;
        private String cachedRawName;

        public FriendDisplayEntry(Friend friend, PresenceInfo presence) {
            this.friend = friend;
            this.presence = presence;
        }

        public UUID getUuid() {
            return friend.getUuid();
        }

        public boolean isBestFriend() {
            return friend.isBestFriend();
        }

        public boolean isOnline() {
            return presence != null && presence.isOnline();
        }

        public long getLastSeen() {
            if (presence != null) {
                return presence.getLastSeen();
            }
            return friend.getAddedTimestamp(); // Fallback
        }

        public String getServerInfo() {
            if (presence != null && presence.getServerType() != null) {
                String serverType = presence.getServerType();
                String serverId = presence.getServerId();
                if (serverId != null && !serverId.isEmpty()) {
                    return serverType + " " + serverId;
                }
                return serverType;
            }
            return null;
        }

        public String getDisplayName() {
            if (cachedDisplayName == null) {
                try {
                    cachedDisplayName = HypixelPlayer.getDisplayName(friend.getUuid());
                    if (cachedDisplayName == null || cachedDisplayName.isEmpty()) {
                        cachedDisplayName = getRawName();
                    }
                } catch (Exception e) {
                    cachedDisplayName = getRawName();
                }
            }
            return cachedDisplayName;
        }

        public String getRawName() {
            if (cachedRawName == null) {
                try {
                    cachedRawName = HypixelPlayer.getRawName(friend.getUuid());
                } catch (Exception e) {
                    cachedRawName = friend.getUuid().toString().substring(0, 8);
                }
            }
            return cachedRawName;
        }
    }
}
