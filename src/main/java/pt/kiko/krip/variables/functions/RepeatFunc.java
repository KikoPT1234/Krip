package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class RepeatFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("repeat", new RepeatFunc());
	}

	public RepeatFunc() {
		super("repeat", Arrays.asList("ticks", "function"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> ticks = context.symbolTable.get("ticks");
		Value<?> function = context.symbolTable.get("function");

		if (!(ticks instanceof NumberValue)) return invalidType(ticks, context);
		if (!(function instanceof BaseFunctionValue)) return invalidType(function, context);

		double tickNumber = Double.parseDouble(ticks.getValueString());

		if (Math.floor(tickNumber) != tickNumber)
			return result.failure(new RuntimeError(ticks.startPosition, ticks.endPosition, "Number of ticks must be whole", context));
		if (Math.floor(tickNumber) <= 0)
			return result.failure(new RuntimeError(ticks.startPosition, ticks.endPosition, "Number of ticks must be greater than 0", context));

		BukkitTask task = Bukkit.getScheduler().runTaskTimer(Krip.plugin, () -> ((BaseFunctionValue) function).execute(Collections.emptyList(), context), 0, Integer.parseInt(ticks.getValueString()));

		if (!Krip.tasks.containsKey(startPosition.fileName)) Krip.tasks.put(startPosition.fileName, new ArrayList<>());
		Krip.tasks.get(startPosition.fileName).add(task);

		ObjectValue returnValue = new ObjectValue(new HashMap<>(), context.parent);
		returnValue.set("stop", new BuiltInFunctionValue("stop", Collections.emptyList(), context.parent) {
			@Override
			public RuntimeResult run(Context context) {
				task.cancel();
				if (Krip.tasks.containsKey(RepeatFunc.this.startPosition.fileName))
					Krip.tasks.get(RepeatFunc.this.startPosition.fileName).remove(task);
				return new RuntimeResult().success(new NullValue(context.parent));
			}
		});

		return result.success(returnValue);
	}
}
