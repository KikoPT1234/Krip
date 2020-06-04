package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Variable;
import pt.kiko.krip.lang.values.BooleanValue;

public class True extends Variable {

	static {
		Krip.registerVariable(True.class);
	}

	public True() {
		super("true", new BooleanValue(true, Krip.context));
	}

}
