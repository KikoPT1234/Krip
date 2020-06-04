package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Variable;
import pt.kiko.krip.lang.values.BooleanValue;

public class False extends Variable {

	static {
		Krip.registerVariable(False.class);
	}

	public False() {
		super("false", new BooleanValue(false, Krip.context));
	}

}
