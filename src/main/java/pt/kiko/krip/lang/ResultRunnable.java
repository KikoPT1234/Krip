package pt.kiko.krip.lang;

import pt.kiko.krip.lang.results.RuntimeResult;

/**
 * Functional interface for a RuntimeResult
 */
@FunctionalInterface
public interface ResultRunnable {
	RuntimeResult run(Context context);
}
