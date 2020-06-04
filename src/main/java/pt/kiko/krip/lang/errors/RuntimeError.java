package pt.kiko.krip.lang.errors;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Position;
import org.jetbrains.annotations.NotNull;

public class RuntimeError extends Error {

	Context context;

	public RuntimeError(Position startPosition, Position endPosition, String details, Context context) {
		super("Runtime Error", startPosition, endPosition, details);

		this.context = context;
	}

	@Override
	public String toString() {
		return name + ": " + details + "\n" + generateTraceback();
	}

	private @NotNull String generateTraceback() {
		Context context = this.context;
		Position position = startPosition;
		StringBuilder string = new StringBuilder();

		while (context != null) {
			if (position == null) break;
			string.append("\n\tat ").append(context.displayName).append(" (").append(position.fileName).append(":").append(position.line + 1).append(":").append(position.col + 1).append(")");
			position = context.parentEntryPosition;
			context = context.parent;
		}

		return "Traceback:" + string;
	}
}
