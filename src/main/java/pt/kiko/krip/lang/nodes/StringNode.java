package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class StringNode extends Node {

	public Token token;

	public StringNode(Token token) {
		super(token.startPosition, token.endPosition);
		this.token = token;
	}

	@Override
	public String toString() {
		return "StringNode {\n" +
				"token: " + token + "\n" +
				'}';
	}
}
