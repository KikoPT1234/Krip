package pt.kiko.krip.lang.cases;

import java.util.List;

public class Cases {

	public List<Case> cases;
	public ElseCase elseCase;

	public Cases() {}

	public Cases(List<Case> cases, ElseCase elseCase) {
		this.cases = cases;
		this.elseCase = elseCase;
	}

	public void setElseCase(ElseCase elseCase) {
		this.elseCase = elseCase;
	}
}
