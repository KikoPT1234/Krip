package pt.kiko.krip.lang.nodes;

public class WhileNode extends Node {

	public Node conditionNode;
	public Node bodyNode;
	public boolean shouldReturnNull;

	public WhileNode(Node conditionNode, Node bodyNode, boolean shouldReturnNull) {
		super(conditionNode.startPosition, conditionNode.endPosition);
		this.conditionNode = conditionNode;
		this.bodyNode = bodyNode;
		this.shouldReturnNull = shouldReturnNull;
	}

	@Override
	public String toString() {
		return "WhileNode {\n" +
				"conditionNode: " + conditionNode + "\n" +
				"bodyNode: " + bodyNode + "\n" +
				"shouldReturnNull: " + shouldReturnNull + "\n" +
				'}';
	}
}
