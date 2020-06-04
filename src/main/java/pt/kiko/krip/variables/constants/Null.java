package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Variable;
import pt.kiko.krip.lang.values.NullValue;

public class Null extends Variable {

	static {
		Krip.registerVariable(Null.class);
	}

	public Null() {
		super("null", new NullValue(Krip.context));
	}

}
