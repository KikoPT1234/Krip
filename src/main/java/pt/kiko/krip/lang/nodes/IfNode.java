package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.cases.Case;
import pt.kiko.krip.lang.cases.ElseCase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IfNode extends Node {

	public List<Case> cases;
	public ElseCase elseCase;

	public IfNode(@NotNull List<Case> cases) {
		super(cases.get(0).condition.startPosition, cases.get(cases.size() - 1).body.endPosition);
		this.cases = cases;
	}

	public IfNode(@NotNull List<Case> cases, ElseCase elseCase) {
		super(cases.get(0).condition.startPosition, elseCase.body.startPosition);
		this.cases = cases;
		this.elseCase = elseCase;
	}

	@Override
	public String toString() {
		return "IfNode {\n" +
				"cases: " + cases + "\n" +
				"elseCase: " + elseCase + "\n" +
				'}';
	}
}
