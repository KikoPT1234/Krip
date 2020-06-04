package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class UnaryOperationNode extends Node {

	public Token operationToken;
	public Node node;

	public UnaryOperationNode(Token operationToken, Node node) {
		super(operationToken.startPosition, node.endPosition);
		this.operationToken = operationToken;
		this.node = node;
	}

	@Override
	public String toString() {
		return "UnaryOperationNode {\n" +
				"operationToken: " + operationToken + "\n" +
				"node: " + node + "\n" +
				'}';
	}
}
