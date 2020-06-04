package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;
import org.jetbrains.annotations.NotNull;

public class BinaryOperationNode extends Node {

	public Node left;
	public Token operationToken;
	public Node right;

	public BinaryOperationNode(@NotNull Node left, Token operationToken, @NotNull Node right) {
		super(left.startPosition, right.endPosition);
		this.left = left;
		this.operationToken = operationToken;
		this.right = right;
	}

	@Override
	public String toString() {
		return "BinaryOperationNode {\n" +
				"left: " + left + "\n" +
				"operationToken: " + operationToken + "\n" +
				"right: " + right + "\n" +
				'}';
	}
}
