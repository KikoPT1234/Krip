package pt.kiko.krip.lang;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.Value;

abstract public class Variable {

	public Value value;
	public String name;

	public Variable(String name, Value value) {
		this.name = name;
		Krip.context.symbolTable.set(name, value, true);
	}

}
