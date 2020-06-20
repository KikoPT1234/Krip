package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;

import java.util.Collections;

public class ClearGlobalsFunc extends KripJavaFunction {

	static {
		Krip.registerValue("clearGlobals", new ClearGlobalsFunc());
	}

	public ClearGlobalsFunc() {
		super("clearGlobals", Collections.emptyList(), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		Krip.globals.forEach((name, global) -> Krip.context.symbolTable.remove(name));
		Krip.globals.clear();
		return new RuntimeResult().success(new KripNull(context));
	}
}
