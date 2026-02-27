package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GUIViewPlayerProfile extends HypixelInventoryGUI {
	private final SkyBlockPlayer viewedPlayer;

	public GUIViewPlayerProfile(SkyBlockPlayer viewedPlayer) {
		super(I18n.string("gui_profile.title", Map.of("player_name", viewedPlayer.getUsername())), InventoryType.CHEST_6_ROW);
		this.viewedPlayer = viewedPlayer;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
		set(GUIClickableItem.getCloseItem(49));

		set(new GUIItem(2) { //Held Item
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				if (!viewedPlayer.getItemInMainHand().isAir()) {
					return ItemStackCreator.getFromStack(viewedPlayer.getItemInMainHand());
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_held_item", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(11) { //Helmet
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				if (!viewedPlayer.getHelmet().isAir()) {
					return ItemStackCreator.getFromStack(viewedPlayer.getHelmet());
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_helmet", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(20) { //Chestplate
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				if (!viewedPlayer.getChestplate().isAir()) {
					return ItemStackCreator.getFromStack(viewedPlayer.getChestplate());
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_chestplate", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(29) { //Leggings
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				if (!viewedPlayer.getLeggings().isAir()) {
					return ItemStackCreator.getFromStack(viewedPlayer.getLeggings());
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_leggings", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(38) { //Boots
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				if (!viewedPlayer.getBoots().isAir()) {
					return ItemStackCreator.getFromStack(viewedPlayer.getBoots());
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_boots", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(47) { //Pet
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				if (viewedPlayer.getPetData().getEnabledPet() != null && !viewedPlayer.getPetData().getEnabledPet().getItemStack().isAir()) {
					SkyBlockItem pet = viewedPlayer.getPetData().getEnabledPet();
					return new NonPlayerItemUpdater(pet).getUpdatedItem();
				} else {
					return ItemStackCreator.getStack(I18n.string("gui_profile.empty_pet", p.getLocale()), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
				}
			}
		});
		set(new GUIItem(22) { //Player Stats
			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockDataHandler dataHandler = viewedPlayer.getSkyblockDataHandler();
				String age = StringUtility.profileAge(System.currentTimeMillis() - dataHandler.get(SkyBlockDataHandler.Data.CREATED, DatapointLong.class).getValue());
				List<String> lore = new ArrayList<>(List.of());

				lore.add("§7 ");
				Locale l = p.getLocale();
				lore.add(I18n.string("gui_profile.skyblock_level", l, Map.of("level", viewedPlayer.getSkyBlockExperience().getLevel().getColor() + viewedPlayer.getSkyBlockExperience().getLevel().toString())));
				lore.add("§7 ");
				lore.add(I18n.string("gui_profile.oldest_profile", l, Map.of("age", age)));

				return ItemStackCreator.getStackHead(viewedPlayer.getShortenedDisplayName(),
						PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1,
						lore);
			}
		});
		set(new GUIClickableItem(31) { //Emblem
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				List<String> lore = new ArrayList<>(List.of());
				SkyBlockDataHandler dataHandler = viewedPlayer.getSkyblockDataHandler();
				Locale l = p.getLocale();
				String name;
				Material material;
				if (dataHandler.get(SkyBlockDataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem() != null) {
					name = I18n.string("gui_profile.emblem_selected", l, Map.of("emblem", dataHandler.get(SkyBlockDataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem().toString()));
					material = dataHandler.get(SkyBlockDataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getEmblem().displayMaterial();
				} else {
					name = I18n.string("gui_profile.no_emblem", l);
					material = Material.BARRIER;
					lore.addAll(I18n.lore("gui_profile.no_emblem.lore", l));
				}
				lore.add(" ");
				lore.add(I18n.string("gui_profile.click_view_emblems", l));
				return ItemStackCreator.getStack(name, material, 1, lore);
			}
		});
		set(new GUIClickableItem(15) { //Visit Island
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				return TranslatableItemStackCreator.getStack(p, "gui_profile.visit_island", Material.FEATHER, 1,
						"gui_profile.visit_island.lore");
			}
		});
		set(new GUIClickableItem(16) { //Trade Request
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				return TranslatableItemStackCreator.getStack(p, "gui_profile.trade_request", Material.EMERALD, 1,
						"gui_profile.trade_request.lore");
			}
		});
		set(new GUIClickableItem(24) { //Invite to Island
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				return TranslatableItemStackCreator.getStack(p, "gui_profile.invite_to_island", Material.POPPY, 1,
						"gui_profile.invite_to_island.lore");
			}
		});
		set(new GUIClickableItem(25) { //Coop Request
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				return TranslatableItemStackCreator.getStack(p, "gui_profile.coop_request", Material.DIAMOND, 1,
						"gui_profile.coop_request.lore");
			}
		});
		set(new GUIClickableItem(33) { //Personal Vault
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				return TranslatableItemStackCreator.getStack(p, "gui_profile.personal_vault", Material.ENDER_CHEST, 1,
						"gui_profile.personal_vault.lore");
			}
		});
		set(new GUIClickableItem(34) { //Museum
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(I18n.string("gui_profile.feature_not_added", player.getLocale()));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockDataHandler dataHandler = viewedPlayer.getSkyblockDataHandler();
				String profileName = dataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
				List<String> lore = new ArrayList<>(I18n.lore("gui_profile.museum.lore", p.getLocale(), Map.of("profile_name", profileName)));
				return ItemStackCreator.getStackHead(viewedPlayer.getUsername() + "'s Museum",
						PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1,
						lore);
			}
		});
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onClose(InventoryCloseEvent e, CloseReason reason) {
	}

	@Override
	public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}
}
