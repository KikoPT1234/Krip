package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Token;

import java.util.List;

public class FunctionNode extends Node {

	public Node bodyNode;
	public boolean shouldAutoReturn;
	public Token varNameToken;
	public List<Token> argNameTokens;

	public FunctionNode(Node bodyNode, boolean shouldAutoReturn, Token varNameToken, List<Token> argNameTokens) {
		super(varNameToken != null ? varNameToken.startPosition : argNameTokens != null && argNameTokens.size() > 0 ? argNameTokens.get(0).startPosition : bodyNode.startPosition, bodyNode.endPosition);
		this.bodyNode = bodyNode;
		this.shouldAutoReturn = shouldAutoReturn;
		this.varNameToken = varNameToken;
		this.argNameTokens = argNameTokens;
	}

	@Override
	public String toString() {
		return "FunctionNode {\n" +
				"bodyNode: " + bodyNode + "\n" +
				"shouldAutoReturn: " + shouldAutoReturn + "\n" +
				"varNameToken: " + varNameToken + "\n" +
				"argNameTokens: " + argNameTokens + "\n" +
				'}';
	}
}
