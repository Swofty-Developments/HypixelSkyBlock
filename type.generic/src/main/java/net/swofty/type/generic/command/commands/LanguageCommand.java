package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.language.LanguageMessage;
import net.swofty.type.generic.language.PlayerLanguage;
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
        ArgumentWord languageArg = ArgumentType.Word("code");
        languageArg.setSuggestionCallback((sender, context, suggestion) -> {
            for (PlayerLanguage language : PlayerLanguage.values()) {
                suggestion.addEntry(new SuggestionEntry(language.getId()));
            }
        });

        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            PlayerLanguage currentLanguage = player.getLanguage();
            sender.sendMessage(LanguageMessage.CURRENT_LANGUAGE.format(currentLanguage, currentLanguage.getDisplayName(), currentLanguage.getId()));
            sender.sendMessage(LanguageMessage.AVAILABLE_LANGUAGES.format(currentLanguage, PlayerLanguage.allLanguageIds()));
            sender.sendMessage(LanguageMessage.USE_LANGUAGE_HINT.format(currentLanguage));
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String rawLanguage = context.get(languageArg);
            PlayerLanguage language = PlayerLanguage.fromInput(rawLanguage);

            if (language == null) {
                PlayerLanguage currentLanguage = player.getLanguage();
                sender.sendMessage(LanguageMessage.UNKNOWN_LANGUAGE.format(currentLanguage, rawLanguage));
                sender.sendMessage(LanguageMessage.AVAILABLE_LANGUAGES.format(currentLanguage, PlayerLanguage.allLanguageIds()));
                return;
            }

            player.getDataHandler().get(HypixelDataHandler.Data.LANGUAGE, DatapointString.class).setValue(language.getId());
            sender.sendMessage(LanguageMessage.LANGUAGE_UPDATED.format(language, language.getDisplayName(), language.getId()));
        }, languageArg);
    }
}
