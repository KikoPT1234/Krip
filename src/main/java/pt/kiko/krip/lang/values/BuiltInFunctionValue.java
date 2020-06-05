package pt.kiko.krip.lang.values;

import com.sun.istack.internal.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.ResultRunnable;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.List;

public class BuiltInFunctionValue extends BaseFunctionValue {

	public BuiltInFunctionValue(String name, List<String> argNames, Context context) {
		super(name, argNames, context);
	}

	@Override
	public RuntimeResult execute(List<Value> args, Context ctx) {
		RuntimeResult result = new RuntimeResult();
		Context context = generateNewContext();
		context.parent = ctx;

		result.register(populateArgs(argNames, args, context));
		if (result.shouldReturn()) return result;

		Value value = result.register(run(context));
		if (result.shouldReturn()) return result;
		return result.success(value);
	}

	public @NotNull RuntimeResult run(Context context) {
		return null;
	}

	@Override
	public Value copy() {
		ResultRunnable func = this::run;
		return new BuiltInFunctionValue(name, argNames, context) {
			@Override
			public RuntimeResult run(Context context) {
				return func.run(context);
			}
		}.setPosition(startPosition, endPosition);
	}
}
