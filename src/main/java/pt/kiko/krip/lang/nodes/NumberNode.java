package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class NumberNode extends Node {

	public Token token;

	public NumberNode(Token token) {
		super(token.startPosition, token.endPosition);
		this.token = token;
	}

	@Override
	public String toString() {
		return "NumberNode {\n" +
				"token: " + token + "\n" +
				'}';
	}
}
