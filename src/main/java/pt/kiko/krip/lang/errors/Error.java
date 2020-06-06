package pt.kiko.krip.lang.errors;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Position;

abstract public class Error {

	String name;
	Position startPosition;
	Position endPosition;
	public String details;

	public Error(String name, @NotNull Position startPosition, @NotNull Position endPosition, String details) {
		this.name = name;
		this.startPosition = startPosition.copy();
		this.endPosition = endPosition.copy();
		this.details = details;
	}

	public Error(String name, @NotNull Position startPosition, String details) {
		this.name = name;
		this.startPosition = startPosition.copy();
		this.endPosition = startPosition.copy();
		this.details = details;
	}

	@Override
	public String toString() {
		return name + ": " + details + "\n\tat " + startPosition.fileName + ":" + (startPosition.line + 1) + ":" + (startPosition.col + 1);
	}
}
