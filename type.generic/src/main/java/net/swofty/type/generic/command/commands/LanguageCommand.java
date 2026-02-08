package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.language.LanguageMessage;
import net.swofty.type.generic.language.PlayerLanguage;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.lang.reflect.Method;
import java.util.Locale;

@CommandParameters(description = "Set your in-game language",
        usage = "/language [code]",
        permission = Rank.DEFAULT,
        aliases = "lang idioma",
        allowsConsole = false)
public class LanguageCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            new LanguageSelectionMenuPage1().open(player);
        });

        ArgumentWord languageArg = ArgumentType.Word("code");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String rawLanguage = context.get(languageArg);
            PlayerLanguage language = PlayerLanguage.fromInput(rawLanguage);

            if (language == null) {
                sender.sendMessage(LanguageMessage.formatByCode(LanguageMessage.UNKNOWN_LANGUAGE,
                        PlayerLanguage.ENGLISH,
                        rawLanguage,
                        PlayerLanguage.allLanguageIds()));
                return;
            }

            applyLanguage(player, language);
            sender.sendMessage(LanguageMessage.formatByCode(LanguageMessage.LANGUAGE_UPDATED, language, language.getDisplayName(), language.getId()));
        }, languageArg);
    }

    private static void applyLanguage(HypixelPlayer player, PlayerLanguage language) {
        player.getDataHandler().get(HypixelDataHandler.Data.LANGUAGE, DatapointString.class).setValue(language.getId());
    }

    private static PlayerLanguage detectClientLanguage(HypixelPlayer player) {
        PlayerLanguage detected = fromValue(readMember(player, "getLocale"));
        if (detected != null) {
            return detected;
        }

        Object settings = readMember(player, "getSettings");
        if (settings == null) {
            settings = readMember(player, "settings");
        }

        detected = fromValue(readMember(settings, "locale"));
        if (detected != null) {
            return detected;
        }

        detected = fromValue(readMember(settings, "getLocale"));
        return detected == null ? PlayerLanguage.ENGLISH : detected;
    }

    private static Object readMember(Object target, String methodName) {
        if (target == null) {
            return null;
        }

        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static PlayerLanguage fromValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Locale locale) {
            PlayerLanguage detected = PlayerLanguage.fromInput(locale.toLanguageTag());
            if (detected != null) {
                return detected;
            }
            return PlayerLanguage.fromInput(locale.getLanguage());
        }

        return PlayerLanguage.fromInput(value.toString());
    }

    private abstract static class BaseLanguageMenu extends HypixelInventoryGUI {
        BaseLanguageMenu() {
            super("Select Language", InventoryType.CHEST_4_ROW);
        }

        void setDecor(int slot, Material material) {
            set(slot, ItemStackCreator.createNamedItemStack(material, "§f"), false);
        }

        void setLanguageItem(int slot, PlayerLanguage language, Material material) {
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    applyLanguage(player, language);
                    player.sendMessage(LanguageMessage.formatByCode(LanguageMessage.LANGUAGE_UPDATED,
                            language,
                            language.getDisplayName(),
                            language.getId()));
                    player.closeInventory();
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    boolean selected = player.getLanguage() == language;
                    ItemStack.Builder base = ItemStackCreator.getStack(
                            (selected ? "§a" : "§f") + language.getDisplayName(),
                            material,
                            1,
                            "§7Code: §f" + language.getId(),
                            "",
                            selected ? "§aSELECTED" : "§eClick to select"
                    );
                    return selected ? ItemStackCreator.enchant(base) : base;
                }
            });
        }

        void setCloseButton(int slot) {
            set(GUIClickableItem.getCloseItem(slot));
        }

        void setBookNoAction(int slot) {
            set(slot, ItemStackCreator.getStack("§eLanguages", Material.BOOK, 1,
                    "§7Browse available languages."), false);
        }

        void setAutoDetectButton(int slot) {
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    PlayerLanguage detected = detectClientLanguage(player);
                    applyLanguage(player, detected);
                    player.sendMessage(LanguageMessage.formatByCode(LanguageMessage.LANGUAGE_UPDATED,
                            detected,
                            detected.getDisplayName(),
                            detected.getId()));
                    player.closeInventory();
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§aAuto Detect Language", Material.MAGMA_CREAM, 1,
                            "§7Automatically detect your",
                            "§7client language and apply it.",
                            "",
                            "§eClick to detect");
                }
            });
        }
    }

    private static class LanguageSelectionMenuPage1 extends BaseLanguageMenu {
        @Override
        public void onOpen(HypixelPlayer player, int slot) {
            fill(FILLER_ITEM);
            setLanguageItem(10, PlayerLanguage.ENGLISH, Material.RED_CONCRETE);
            setLanguageItem(11, PlayerLanguage.JAPANESE, Material.GREEN_CONCRETE);
            setLanguageItem(12, PlayerLanguage.KOREAN, Material.BLUE_CONCRETE);
            setLanguageItem(13, PlayerLanguage.CHINESE_SIMPLIFIED, Material.WHITE_CONCRETE);

            setDecor(14, Material.RED_WOOL);
            setDecor(15, Material.BLUE_WOOL);
            setDecor(16, Material.RED_TERRACOTTA);
            setDecor(19, Material.BLUE_TERRACOTTA);
            setDecor(20, Material.BLACK_CONCRETE);
            setDecor(21, Material.GREEN_WOOL);
            setDecor(22, Material.RED_WOOL);
            setDecor(23, Material.WHITE_WOOL);
            setDecor(24, Material.BLACK_WOOL);
            setDecor(25, Material.RED_WOOL);

            setCloseButton(31);
            setBookNoAction(33);
            setAutoDetectButton(34);
            set(new GUIClickableItem(35) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new LanguageSelectionMenuPage2().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1,
                            "§7View more language options.",
                            "",
                            "§eClick to continue");
                }
            });
        }
    }

    private static class LanguageSelectionMenuPage2 extends BaseLanguageMenu {
        @Override
        public void onOpen(HypixelPlayer player, int slot) {
            fill(FILLER_ITEM);
            setDecor(10, Material.CYAN_CONCRETE);
            setDecor(11, Material.ORANGE_CONCRETE);
            setDecor(12, Material.PURPLE_CONCRETE);
            setDecor(13, Material.YELLOW_CONCRETE);
            setDecor(14, Material.LIME_CONCRETE);
            setDecor(15, Material.PINK_CONCRETE);
            setDecor(16, Material.LIGHT_BLUE_CONCRETE);

            setCloseButton(31);
            setBookNoAction(33);
            setAutoDetectButton(34);
            set(new GUIClickableItem(35) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new LanguageSelectionMenuPage1().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1,
                            "§7Return to the main language page.",
                            "",
                            "§eClick to go back");
                }
            });
        }
    }
}
