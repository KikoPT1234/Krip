package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Position;

import java.util.Map;

public class ObjectNode extends Node {

	public Map<String, Node> object;

	public ObjectNode(Map<String, Node> object, Position startPosition, Position endPosition) {
		super(startPosition, endPosition);

		this.object = object;
	}

	@Override
	public String toString() {
		return "ObjectNode {\n" +
				"object: " + object + "\n" +
				'}';
	}
}
