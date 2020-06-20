package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.KripBoolean;

public class False extends KripBoolean {

	static {
		Krip.registerValue("false", new False());
	}

	public False() {
		super(false, Krip.context);
	}

}
