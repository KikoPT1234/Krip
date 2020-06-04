package pt.kiko.krip.lang.cases;

import pt.kiko.krip.lang.nodes.Node;

public class ElseCase {

	public Node body;
	public boolean isExpression;

	public ElseCase(Node body, boolean isExpression) {
		this.body = body;
		this.isExpression = isExpression;
	}

	@Override
	public String toString() {
		return "ElseCase -> " + body;
	}

}
