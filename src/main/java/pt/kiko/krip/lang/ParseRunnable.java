package pt.kiko.krip.lang;

import pt.kiko.krip.lang.results.ParseResult;

/**
 * Functional interface for a ParseResult
 */
@FunctionalInterface
public interface ParseRunnable {
	ParseResult run();
}
