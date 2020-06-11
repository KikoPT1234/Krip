package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NullValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;

public class ExecuteFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("execute", new ExecuteFunc());
	}

	public ExecuteFunc() {
		super("execute", Collections.singletonList("command"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> command = context.symbolTable.get("command");

		if (!(command instanceof StringValue)) return invalidType(command, context);

		Bukkit.getScheduler().runTask(Krip.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getValueString()));

		return result.success(new NullValue(context.parent));
	}
}
