package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.cases.Cases;
import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.nodes.Node;

public class CaseResult {

    public Error error;
    public Cases cases;
    public int advanceCount = 0;
    public int toReverseCount = 0;

    public void registerAdvancement() {
        advanceCount++;
    }

    public Cases register(CaseResult result) {
        advanceCount += result.advanceCount;
        if (result.error != null) error = result.error;
        return result.cases;
    }

    public Node register(ParseResult result) {
        advanceCount += result.advanceCount;
        if (result.error != null) error = result.error;
        return result.node;
    }

    public CaseResult success(Cases cases) {
        this.cases = cases;
        return this;
    }

    public CaseResult failure(Error error) {
        this.error = error;
        return this;
    }

}
