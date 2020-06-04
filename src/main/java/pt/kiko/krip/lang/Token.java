package pt.kiko.krip.lang;

import org.jetbrains.annotations.NotNull;

public class Token {

	TokenTypes type;
	public String value;
	public Position startPosition;
	public Position endPosition;

	public Token(TokenTypes type, String value, @NotNull Position startPosition, @NotNull Position endPosition) {
		this.type = type;
		this.value = value;
		this.startPosition = startPosition.copy();
		this.endPosition = endPosition.copy();
	}

	public Token(TokenTypes type, String value, @NotNull Position startPosition) {
		this.type = type;
		this.value = value;
		this.startPosition = startPosition.copy();
		this.endPosition = startPosition.copy();
	}

	public Token(TokenTypes type, @NotNull Position startPosition, @NotNull Position endPosition) {
		this.type = type;
		this.startPosition = startPosition.copy();
		this.endPosition = endPosition.copy();
	}

	public Token(TokenTypes type, @NotNull Position startPosition) {
		this.type = type;
		this.startPosition = startPosition.copy();
		this.endPosition = startPosition.copy();
	}

	public boolean matches(TokenTypes type, String value) {
		return this.type == type && this.value.equals(value);
	}

	public boolean matches(TokenTypes type) {
		return this.type == type;
	}

	@Override
	public String toString() {
		if (value != null) {
			return type + ": '" + value + "'";
		} else return type.toString();
	}
}

