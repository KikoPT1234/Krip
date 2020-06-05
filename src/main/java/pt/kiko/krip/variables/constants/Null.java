package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.NullValue;

public class Null extends NullValue {

	static {
		Krip.registerValue("null", new Null());
	}

	public Null() {
		super(Krip.context);
	}

}
