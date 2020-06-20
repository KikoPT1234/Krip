package pt.kiko.krip.lang.errors;

import pt.kiko.krip.lang.Position;

public class LexError extends Error {

    public LexError(Position startPosition, String details) {
        super("Lex Error", startPosition, details);
    }

}
