package net.swofty.type.generic.language;

import java.util.EnumMap;
import java.util.Map;

public enum LanguageMessage {
    CURRENT_LANGUAGE(
            "§aCurrent language: §6%s §7(%s)",
            "§a現在の言語: §6%s §7(%s)",
            "§a현재 언어: §6%s §7(%s)",
            "§a当前语言: §6%s §7(%s)"
    ),
    AVAILABLE_LANGUAGES(
            "§7Available: %s",
            "§7利用可能: %s",
            "§7사용 가능: %s",
            "§7可用语言: %s"
    ),
    USE_LANGUAGE_HINT(
            "§7Use §e/language <code> §7to change it.",
            "§7変更するには §e/language <code> §7を使ってください。",
            "§7변경하려면 §e/language <code> §7를 사용하세요.",
            "§7使用 §e/language <code> §7进行修改。"
    ),
    UNKNOWN_LANGUAGE(
            "§cUnknown language '%s'.",
            "§c不明な言語です: '%s'。",
            "§c알 수 없는 언어입니다: '%s'.",
            "§c未知语言: '%s'。"
    ),
    LANGUAGE_UPDATED(
            "§aLanguage updated to §6%s §7(%s)",
            "§a言語を §6%s §7(%s) §aに変更しました。",
            "§a언어가 §6%s §7(%s) §a로 변경되었습니다.",
            "§a语言已切换为 §6%s §7(%s)"
    );

    private final Map<PlayerLanguage, String> messages;

    LanguageMessage(String english, String japanese, String korean, String chineseSimplified) {
        this.messages = new EnumMap<>(PlayerLanguage.class);
        this.messages.put(PlayerLanguage.ENGLISH, english);
        this.messages.put(PlayerLanguage.JAPANESE, japanese);
        this.messages.put(PlayerLanguage.KOREAN, korean);
        this.messages.put(PlayerLanguage.CHINESE_SIMPLIFIED, chineseSimplified);
    }

    public String format(PlayerLanguage language, Object... args) {
        String template = messages.getOrDefault(language, messages.get(PlayerLanguage.ENGLISH));
        return String.format(template, args);
    }
}
