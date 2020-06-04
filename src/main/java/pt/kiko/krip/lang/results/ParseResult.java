package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.cases.Cases;
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

    public Node register(ParseResult result) {
        advanceCount += result.advanceCount;
        if (result.error != null) error = result.error;
        return result.node;
    }

    public Cases register(CaseResult result) {
        advanceCount += result.advanceCount;
        if (result.error != null) error = result.error;
        return result.cases;
    }

    public Node tryRegister(ParseResult result) {
        if (result.error != null) {
            toReverseCount = result.toReverseCount;
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
