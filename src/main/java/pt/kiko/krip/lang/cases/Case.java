package pt.kiko.krip.lang.cases;

import pt.kiko.krip.lang.nodes.Node;

public class Case {

	public Node condition;
	public Node body;
	public boolean isExpression;

	public Case(Node condition, Node body, boolean isExpression) {
		this.condition = condition;
		this.body = body;
		this.isExpression = isExpression;
	}

	@Override
	public String toString() {
		return "Case (" + condition + ") -> " + body;
	}
}
