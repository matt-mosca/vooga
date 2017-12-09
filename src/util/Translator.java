package util;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import exporting.Publisher;

import java.util.List;
import java.util.stream.Collectors;

public final class Translator {

    private final String DEFAULT_LANGUAGE = "en";

    private Translate translateService = TranslateOptions.getDefaultInstance().getService();

    private TranslateOption sourceLanguage = TranslateOption.sourceLanguage(DEFAULT_LANGUAGE);
    private TranslateOption targetLanguageOption = TranslateOption.targetLanguage(DEFAULT_LANGUAGE);

    public Translator() {

    }

    public void setTargetLanguage(String targetLanguage) {
        targetLanguageOption = TranslateOption.targetLanguage(targetLanguage);
    }

    public String translate(String originalPhrase) {
        Translation translation = translateService.translate(originalPhrase, targetLanguageOption, sourceLanguage);
        return translation.getTranslatedText();
    }

    public List<String> translate(List<String> originalPhrases) {
        return originalPhrases.stream().map(phrase -> translate(phrase)).collect(Collectors.toList());
    }
}
