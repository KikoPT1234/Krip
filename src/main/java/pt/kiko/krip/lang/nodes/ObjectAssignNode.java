package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class ObjectAssignNode extends Node {

	public Node objectNode;
	public Token keyToken;
	public Node keyNode;
	public Node valueNode;

	public ObjectAssignNode(Node objectNode, Token keyToken, Node valueNode) {
		super(objectNode.startPosition, valueNode.endPosition);

		this.objectNode = objectNode;
		this.keyToken = keyToken;
		this.valueNode = valueNode;
	}

	public ObjectAssignNode(Node objectNode, Node keyNode, Node valueNode) {
		super(objectNode.startPosition, valueNode.endPosition);

		this.objectNode = objectNode;
		this.keyNode = keyNode;
		this.valueNode = valueNode;
	}

	@Override
	public String toString() {
		return "ObjectAssignNode {\n" +
				"objectNode: " + objectNode + "\n" +
				"keyToken: " + keyToken + "\n" +
				"keyNode: " + keyNode + "\n" +
				"valueNode: " + valueNode + "\n" +
				'}';
	}
}
