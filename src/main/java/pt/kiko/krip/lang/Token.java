package pt.kiko.krip.lang;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a piece of code in the code (e.g. a string, a number, a bracket)
 */
public class Token {

	/**
	 * The value of this token, if any
	 */
	public String value;
	/**
	 * The start position of this token
	 */
	public Position startPosition;
	/**
	 * The end position of this token
	 */
	public Position endPosition;
	/**
	 * The type of this token
	 */
	TokenTypes type;

	/**
	 * Full constructor
	 *
	 * @param type          The type
	 * @param value         The value
	 * @param startPosition The start position
	 * @param endPosition   The end position
	 */
	public Token(TokenTypes type, String value, @NotNull Position startPosition, @NotNull Position endPosition) {
		this.type = type;
		this.value = value;
		this.startPosition = startPosition.copy();
		this.endPosition = endPosition.copy();
	}

	/**
	 * Constructor with type, value and start position
	 *
	 * @param type          The type
	 * @param value         The value
	 * @param startPosition The start position
	 */
	public Token(TokenTypes type, String value, @NotNull Position startPosition) {
		this.type = type;
		this.value = value;
		this.startPosition = startPosition.copy();
		this.endPosition = startPosition.copy();
	}

	/**
	 * Constructor with type and start and end positions
	 *
	 * @param type          The type
	 * @param startPosition The start position
	 * @param endPosition   The end position
	 */
	public Token(TokenTypes type, @NotNull Position startPosition, @NotNull Position endPosition) {
		this.type = type;
		this.startPosition = startPosition.copy();
		this.endPosition = endPosition.copy();
	}

	/**
	 * Constructor with type and start position
	 *
	 * @param type          The type
	 * @param startPosition The start position
	 */
	public Token(TokenTypes type, @NotNull Position startPosition) {
		this.type = type;
		this.startPosition = startPosition.copy();
		this.endPosition = startPosition.copy();
	}

	/**
	 * Whether this token is of the provided type and has the provided value
	 *
	 * @param type  The type
	 * @param value The value
	 * @return Boolean of whether the type and value are the same
	 */
	public boolean matches(TokenTypes type, String value) {
		return this.type == type && this.value.equals(value);
	}

	/**
	 * Whether this token is of the provided type
	 *
	 * @param type The type
	 * @return Boolean of whether the type is the same
	 */
	public boolean matches(TokenTypes type) {
		return this.type == type;
	}

	/**
	 * Returns a better string representation
	 *
	 * @return String of format "TYPE" or "TYPE: VALUE"
	 */
	@Override
	public String toString() {
		if (value != null) {
			return type + ": '" + value + "'";
		} else return type.toString();
	}
}

