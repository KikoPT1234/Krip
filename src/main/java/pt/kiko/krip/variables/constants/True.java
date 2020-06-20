package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.KripBoolean;

public class True extends KripBoolean {

	static {
		Krip.registerValue("true", new True());
	}

	public True() {
		super(true, Krip.context);
	}

}
