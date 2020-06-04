package pt.kiko.krip.lang.errors;

import pt.kiko.krip.lang.Position;

public class SyntaxError extends Error {

    public SyntaxError(Position startPosition, Position endPosition, String details) {
        super("Syntax Error", startPosition, endPosition, details);
    }

}
