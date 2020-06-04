package pt.kiko.krip.lang;

import pt.kiko.krip.lang.results.ParseResult;

@FunctionalInterface
public interface ParseRunnable {
	ParseResult run();
}
