package pt.kiko.krip.lang.nodes;

import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.Position;
import pt.kiko.krip.lang.Token;

public class TryCatchNode extends Node {

	public Node tryNode;
	public Node catchNode;
	public Node finallyNode;

	public Token errorNameToken;

	public TryCatchNode(Position startPosition, Position endPosition, Node tryNode, Node catchNode, Token errorNameToken, @Nullable Node finallyNode) {
		super(startPosition, endPosition);

		this.tryNode = tryNode;
		this.catchNode = catchNode;
		this.finallyNode = finallyNode;

		this.errorNameToken = errorNameToken;
	}

	@Override
	public String toString() {
		return "TryCatchNode {\n" +
				"tryNode: " + tryNode + "\n" +
				"catchNode: " + catchNode + "\n" +
				"finallyNode: " + finallyNode + "\n" +
				'}';
	}
}
