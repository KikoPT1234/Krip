package pt.kiko.krip.lang;

import java.util.HashMap;
import java.util.Map;

public class Characters {
    static String digits = "0123456789";
    static String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
    static String lettersDigits = digits + letters;

    static String[] keywords = {
            "const",
            "let",
            "function",
            "if",
            "else if",
            "else",
            "for",
            "to",
            "while",
            "return",
            "break",
            "continue"
    };

    static Map<String, String> escapedCharacters = new HashMap<>();

    static {
        Characters.escapedCharacters.put("n", "\\n");
        Characters.escapedCharacters.put("t", "\\t");
    }
}
