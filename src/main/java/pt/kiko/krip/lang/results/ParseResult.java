package pt.kiko.krip.lang.results;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.nodes.Node;

public class ParseResult {

    public Error error;
    public Node node;
    public int advanceCount = 0;
    public int toReverseCount = 0;

    public void registerAdvancement() {
        advanceCount++;
    }

    public Node register(@NotNull ParseResult result) {
        advanceCount += result.advanceCount;
        if (result.error != null) error = result.error;
        return result.node;
    }

    public Node tryRegister(@NotNull ParseResult result) {
        if (result.error != null) {
            toReverseCount = result.advanceCount;
            return null;
        }
        return register(result);
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(Error error) {
        this.error = error;
        return this;
    }

}
