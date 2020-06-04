package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class VarCreateNode extends Node {

	public Token token;
	public Node expression;
	public boolean isFinal;

	public VarCreateNode(Token token, Node expression, boolean isFinal) {
		super(token.startPosition, expression.endPosition);

		this.token = token;
		this.expression = expression;
		this.isFinal = isFinal;
	}

	@Override
	public String toString() {
		return "VarCreateNode {\n" +
				"varNameToken: " + token + "\n" +
				"valueNode: " + expression + "\n" +
				"isFinal: " + isFinal + "\n" +
				'}';
	}
}
