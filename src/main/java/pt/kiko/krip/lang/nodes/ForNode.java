package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class ForNode extends Node {

	public Token varNameToken;
	public Node startValueNode;
	public Node endValueNode;
	public Node bodyNode;
	public boolean shouldReturnNull;

	public ForNode(Token varNameToken, Node startValueNode, Node endValueNode, Node bodyNode, boolean shouldReturnNull) {
		super(varNameToken.startPosition, bodyNode.endPosition);
		this.varNameToken = varNameToken;
		this.startValueNode = startValueNode;
		this.endValueNode = endValueNode;
		this.bodyNode = bodyNode;
		this.shouldReturnNull = shouldReturnNull;
	}

	@Override
	public String toString() {
		return "ForNode {\n" +
				"varNameToken: " + varNameToken + "\n" +
				"startValueNode: " + startValueNode + "\n" +
				"endValueNode: " + endValueNode + "\n" +
				"bodyNode: " + bodyNode + "\n" +
				"shouldReturnNull: " + shouldReturnNull + "\n" +
				'}';
	}
}
