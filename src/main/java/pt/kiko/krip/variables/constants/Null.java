package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.KripNull;

public class Null extends KripNull {

	static {
		Krip.registerValue("null", new Null());
	}

	public Null() {
		super(Krip.context);
	}

}
