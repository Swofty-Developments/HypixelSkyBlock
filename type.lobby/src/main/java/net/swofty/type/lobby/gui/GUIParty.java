package net.swofty.type.lobby.gui;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.party.FullParty;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointHypixelExperience;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.experience.HypixelExperience;
import net.swofty.type.generic.experience.PlayerExperienceHandler;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GUIParty extends HypixelInventoryGUI implements RefreshingGUI {
    private static int[] COLOURED_PANE_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17
    };

    private SortMode sortMode = SortMode.DEFAULT;
    private boolean reverseSorting = false;

    public GUIParty() {
        super("Party", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        refreshItems(e.player());
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        // Clear previous items
        items.clear();

        PlayerExperienceHandler xpHandler = player.getExperienceHandler();
        int level = xpHandler.getLevel();
        int achievementPoints = player.getAchievementHandler().getTotalPoints();

        for (int slot : COLOURED_PANE_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.createNamedItemStack(Material.BLUE_STAINED_GLASS_PANE);
                }
            });
        }

        // Top row - same for both states
        setTopRowItems(player, level, achievementPoints);

        boolean inParty = PartyManager.isInParty(player);

        if (!inParty) {
            // Not in party - show "Create Party" button
            setNotInPartyItems(player);
        } else {
            // In party - show party members and actions
            FullParty party = PartyManager.getPartyFromPlayer(player);
            if (party != null) {
                setInPartyItems(player, party);
            }
        }
    }

    private void setTopRowItems(HypixelPlayer player, int level, int achievementPoints) {
        // Player head (slot 2)
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

        // Friends (slot 3)
        set(new GUIClickableItem(3) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aFriends",
                        "e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84",
                        1,
                        "§7View your Hypixel friends' profiles,",
                        "§7and interact with your online friends!",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIFriends().open(player);
            }
        });

        // Party (slot 4) - highlighted since we're in party menu
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.enchant(ItemStackCreator.getStackHead(
                        "§aParty",
                        "667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1",
                        1,
                        "§7Create a party and join up with",
                        "§7other players to play games",
                        "§7together!",
                        "",
                        "§eCurrently viewing!"
                ));
            }
        });

        // Guild (slot 5)
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

        // Recent Players (slot 6)
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
    }

    private void setNotInPartyItems(HypixelPlayer player) {
        // Create Party button (slot 31)
        set(new GUIClickableItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aCreate Party",
                        Material.MAGENTA_TERRACOTTA,
                        1,
                        "",
                        "§eClick to invite a player to your",
                        "§eparty"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
                new HypixelSignGUI(player).open(new String[]{"Enter player", "name:"}).thenAccept(name -> {
                    if (name != null && !name.isEmpty()) {
                        PartyManager.invitePlayer(player, name);
                    }
                });
            }
        });
    }

    private void setInPartyItems(HypixelPlayer player, FullParty party) {
        FullParty.Member self = party.getFromUuid(player.getUuid());
        boolean isLeader = self.getRole() == FullParty.Role.LEADER;
        boolean isModerator = self.getRole() == FullParty.Role.MODERATOR;
        boolean hasModPermissions = isLeader || isModerator;

        // Invite Player (slot 18)
        set(new GUIClickableItem(18) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aInvite Player",
                        Material.WRITABLE_BOOK,
                        1,
                        "§7Invites a player to your party."
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
                new HypixelSignGUI(player).open(new String[]{"Enter player", "name:"}).thenAccept(name -> {
                    if (name != null && !name.isEmpty()) {
                        PartyManager.invitePlayer(player, name);
                    }
                });
            }
        });

        // Close button (slot 19)
        set(GUIClickableItem.getCloseItem(19));

        // Warp Party (slot 20) - leader only
        if (isLeader) {
            set(new GUIClickableItem(20) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aWarp Party",
                            Material.NETHER_BRICK,
                            1,
                            "§7Teleports all party members to your",
                            "§7lobby."
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    PartyManager.warpParty(player);
                }
            });

            // Disband Party (slot 21) - leader only
            set(new GUIClickableItem(21) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aDisband Party",
                            Material.TNT,
                            1,
                            "§7Breaks up the current party."
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    PartyManager.disbandParty(player);
                    player.closeInventory();
                }
            });
        } else {
            // Non-leader: show Leave Party instead
            set(new GUIClickableItem(20) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cLeave Party",
                            Material.BARRIER,
                            1,
                            "§7Leave the current party."
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    PartyManager.leaveParty(player);
                    player.closeInventory();
                }
            });
        }

        // Change Sort (slot 25)
        set(new GUIClickableItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String sortingOrder = reverseSorting ? "Reversed" : "Normal";
                return ItemStackCreator.getStack(
                        "§aChange sort",
                        Material.HOPPER,
                        1,
                        "§7Current sort: §b" + sortMode.getDisplayName(),
                        "§7Sorting order: §b" + sortingOrder,
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
                if (e.getClick() instanceof Click.Right) {
                    reverseSorting = !reverseSorting;
                } else {
                    sortMode = sortMode.next();
                }
                refreshItems(player);
                updateItemStacks(getInventory(), player);
            }
        });

        // Search Players (slot 26)
        set(new GUIItem(26) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSearch Players",
                        Material.OAK_SIGN,
                        1
                );
            }
        });

        // Party members (slots 27+)
        List<FullParty.Member> members = new ArrayList<>(party.getMembers());
        sortMembers(members);

        int slot = 27;
        for (FullParty.Member member : members) {
            if (slot > 44) break; // Max 18 members displayed

            final int currentSlot = slot;
            set(new GUIItem(currentSlot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return createMemberHead(member);
                }
            });
            slot++;
        }
    }

    private ItemStack.Builder createMemberHead(FullParty.Member member) {
        UUID memberUuid = member.getUuid();
        String displayName = HypixelPlayer.getDisplayName(memberUuid);

        // Get member data
        HypixelDataHandler account = HypixelDataHandler.getOfOfflinePlayer(memberUuid);
        long xp = account.get(HypixelDataHandler.Data.HYPIXEL_EXPERIENCE, DatapointHypixelExperience.class).getValue();
        int level = HypixelExperience.xpToLevel(xp);
        // TODO: Get actual achievement points when available
        int achievementPoints = 0;

        // Get skin
        String skinTexture = account.get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
        String skinSignature = account.get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();

        // Role indicator
        String roleColor;
        String roleText;
        switch (member.getRole()) {
            case LEADER -> {
                roleColor = "§6";
                roleText = "Party Leader";
            }
            case MODERATOR -> {
                roleColor = "§9";
                roleText = "Party Moderator";
            }
            default -> {
                roleColor = "§7";
                roleText = "Party Member";
            }
        }

        List<String> lore = new ArrayList<>();
        lore.add("§7Hypixel Level: §6" + level);
        lore.add("§7Achievement Points: §e" + StringUtility.commaify(achievementPoints));
        lore.add("§7Guild: §bNone");
        lore.add("");
        lore.add(roleColor + roleText);

        // Use skin if available, otherwise use default head texture
        if (skinTexture != null && !skinTexture.equals("null") && skinSignature != null && !skinSignature.equals("null")) {
            PlayerSkin skin = new PlayerSkin(skinTexture, skinSignature);
            return ItemStackCreator.getStackHead(displayName, skin, 1, lore);
        } else {
            // Default player head texture
            return ItemStackCreator.getStackHead(displayName, "18614241b980319c02f5ee3ae1a7fc7ebf8b3fdd5301ed3d4e2159a80dae1d2c", 1, lore);
        }
    }

    private void sortMembers(List<FullParty.Member> members) {
        Comparator<FullParty.Member> comparator = switch (sortMode) {
            case ALPHABETICAL -> Comparator.comparing(m -> HypixelPlayer.getRawName(m.getUuid()).toLowerCase());
            case LAST_ONLINE -> Comparator.comparing(m -> m.isJoined() ? 0 : 1); // Online first
            default -> {
                // Default: Alphabetical but online first
                Comparator<FullParty.Member> onlineFirst = Comparator.comparing(m -> m.isJoined() ? 0 : 1);
                Comparator<FullParty.Member> alphabetical = Comparator.comparing(m -> HypixelPlayer.getRawName(m.getUuid()).toLowerCase());
                yield onlineFirst.thenComparing(alphabetical);
            }
        };

        if (reverseSorting) {
            comparator = comparator.reversed();
        }

        members.sort(comparator);
    }

    @Override
    public int refreshRate() {
        return 20; // 1 second
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private enum SortMode {
        DEFAULT("Default"),
        ALPHABETICAL("Alphabetical"),
        LAST_ONLINE("Last Online");

        private final String displayName;

        SortMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public SortMode next() {
            SortMode[] values = values();
            return values[(ordinal() + 1) % values.length];
        }
    }
}
