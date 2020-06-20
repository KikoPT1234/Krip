package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class ExecuteFunc extends KripJavaFunction {

	static {
		Krip.registerValue("execute", new ExecuteFunc());
	}

	public ExecuteFunc() {
		super("execute", Collections.singletonList("command"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> command = context.symbolTable.get("command");

		if (!(command instanceof KripString)) return invalidType(command, context);

		Bukkit.getScheduler().runTask(Krip.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getValueString()));

		return result.success(new KripNull(context));
	}
}
