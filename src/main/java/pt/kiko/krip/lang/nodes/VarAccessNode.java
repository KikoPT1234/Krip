package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class VarAccessNode extends Node {

	public Token token;

	public VarAccessNode(Token token) {
		super(token.startPosition, token.endPosition);
		this.token = token;
	}

	@Override
	public String toString() {
		return "VarAccessNode {\n" +
				"token: " + token + "\n" +
				'}';
	}
}
