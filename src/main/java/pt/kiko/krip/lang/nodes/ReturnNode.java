package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Position;

public class ReturnNode extends Node {

	public Node nodeToReturn;

	public ReturnNode(Node nodeToReturn, Position startPosition, Position endPosition) {
		super(startPosition, endPosition);

		this.nodeToReturn = nodeToReturn;
	}

	@Override
	public String toString() {
		return "ReturnNode {\n" +
				"nodeToReturn: " + nodeToReturn + "\n" +
				'}';
	}
}
