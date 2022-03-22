package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;

import java.util.Collections;

public class PrintFunc extends KripJavaFunction {

	static {
		Krip.registerValue("print", new PrintFunc());
	}

	public PrintFunc() {
		super("print", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		Bukkit.getLogger().info(context.symbolTable.get("value").toString());
		return new RuntimeResult().success(new KripNull(context));
	}
}
