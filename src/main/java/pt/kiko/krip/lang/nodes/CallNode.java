package pt.kiko.krip.lang.nodes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CallNode extends Node {

	public Node nodeToCall;
	public ArrayList<Node> argNodes;

	public CallNode(@NotNull Node nodeToCall, @NotNull ArrayList<Node> argNodes) {
		super(nodeToCall.startPosition, argNodes.size() > 0 ? argNodes.get(argNodes.size() -1).endPosition : nodeToCall.endPosition);
		this.nodeToCall = nodeToCall;
		this.argNodes = argNodes;
	}

	@Override
	public String toString() {
		return "CallNode {\n" +
				"nodeToCall: " + nodeToCall + "\n" +
				"argNodes: " + argNodes + "\n" +
				'}';
	}
}