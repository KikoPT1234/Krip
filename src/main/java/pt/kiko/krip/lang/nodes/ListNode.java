package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Position;

import java.util.List;

public class ListNode extends Node {

	public List<Node> nodes;

	public ListNode(List<Node> nodes, Position startPosition, Position endPosition) {
		super(startPosition, endPosition);
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		return "ListNode {\n" +
				"nodes: " + nodes + "\n" +
				'}';
	}
}
