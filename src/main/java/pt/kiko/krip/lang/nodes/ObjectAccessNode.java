package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

public class ObjectAccessNode extends Node {

	public Node objectNode;
	public Token keyToken;
	public Node keyNode;

	public ObjectAccessNode(Node objectNode, Token keyToken) {
		super(objectNode.startPosition, keyToken.endPosition);

		this.objectNode = objectNode;
		this.keyToken = keyToken;
	}

	public ObjectAccessNode(Node objectNode, Node keyNode) {
		super(objectNode.startPosition, keyNode.endPosition);

		this.objectNode = objectNode;
		this.keyNode = keyNode;
	}

	@Override
	public String toString() {
		return "ObjectAccessNode {\n" +
				"nodeToCall: " + objectNode + "\n" +
				"keyToken: " + keyToken + "\n" +
				"keyNode: " + keyNode + "\n" +
				'}';
	}
}
