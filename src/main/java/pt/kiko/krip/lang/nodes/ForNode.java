package pt.kiko.krip.lang.nodes;

import org.jetbrains.annotations.NotNull;

public class ForNode extends Node {

	public VarCreateNode declaration;
	public Node condition;
	public Node execution;
	public Node statements;
	public boolean shouldReturnNull;

	public ForNode(@NotNull VarCreateNode declaration, Node condition, Node execution, @NotNull Node statements, boolean shouldReturnNull) {
		super(declaration.startPosition, statements.endPosition);

		this.declaration = declaration;
		this.condition = condition;
		this.execution = execution;
		this.statements = statements;
		this.shouldReturnNull = shouldReturnNull;
	}

	@Override
	public String toString() {
		return "ForNode {\n" +
				"declaration: " + declaration + "\n" +
				"condition: " + condition + "\n" +
				"execution: " + execution + "\n" +
				"statements: " + statements + "\n" +
				"shouldReturnNull: " + shouldReturnNull + "\n" +
				'}';
	}
}
