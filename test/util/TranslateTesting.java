package util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TranslateTesting {

    private Translator translator = new Translator();

    @Test
    public void testTranslate() {
        translator.setTargetLanguage("it");
        String result = translator.translate("I like pizza");
        assertEquals("Translation (english to italian) works", "mi piace la pizza", result);
    }

}
