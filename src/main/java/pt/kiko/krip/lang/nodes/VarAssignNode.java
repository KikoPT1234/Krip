package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class VarAssignNode extends Node {

	public Token token;
	public Node expression;

	public VarAssignNode(Token token, Node expression) {
		super(token.startPosition, expression.endPosition);

		this.token = token;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "VarAssignNode {\n" +
				"varNameToken: " + token + "\n" +
				"expression: " + expression + "\n" +
				'}';
	}
}
