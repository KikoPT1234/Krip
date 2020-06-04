package pt.kiko.krip.lang;

import pt.kiko.krip.lang.results.RuntimeResult;

@FunctionalInterface
public interface ResultRunnable {
	RuntimeResult run(Context context);
}
