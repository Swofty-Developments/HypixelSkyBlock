package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.v2.view.LanguageSelectionView;
import net.swofty.type.generic.language.LanguageMessage;
import net.swofty.type.generic.language.PlayerLanguage;
import net.swofty.type.generic.language.PlayerLanguageService;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

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
            player.openView(new LanguageSelectionView.Page1());
        });

        ArgumentWord languageArg = ArgumentType.Word("code");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String rawLanguage = context.get(languageArg);
            PlayerLanguage language = PlayerLanguage.fromInput(rawLanguage);

            if (language == null) {
                sender.sendMessage(LanguageMessage.UNKNOWN_LANGUAGE.format(
                        PlayerLanguage.ENGLISH,
                        rawLanguage,
                        PlayerLanguage.allLanguageIds()));
                return;
            }

            PlayerLanguageService.applyLanguage(player, language);
            sender.sendMessage(LanguageMessage.LANGUAGE_UPDATED.format(language, language.getDisplayName(), language.getId()));
        }, languageArg);
    }
}
