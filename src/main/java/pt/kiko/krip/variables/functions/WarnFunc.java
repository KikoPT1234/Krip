package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;

import java.util.Collections;

public class WarnFunc extends KripJavaFunction {

	static {
		Krip.registerValue("warn", new WarnFunc());
	}

	public WarnFunc() {
		super("warn", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		Bukkit.getLogger().warning(context.symbolTable.get("value").toString());
		return new RuntimeResult().success(new KripNull(context));
	}
}
