package pt.kiko.krip.lang.values;

import com.sun.istack.internal.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.ResultRunnable;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.List;

abstract public class BuiltInFunctionValue extends BaseFunctionValue {

	public BuiltInFunctionValue(String name, List<String> argNames, Value<?> parent, Context context) {
		super(name, argNames, parent, context);
	}

	public BuiltInFunctionValue(String name, List<String> argNames, Context context) {
		super(name, argNames, context);
	}

	@Override
	public RuntimeResult execute(List<Value<?>> args, Context ctx) {
		RuntimeResult result = new RuntimeResult();
		Context context = generateNewContext();
		context.parent = ctx;

		result.register(populateArgs(argNames, args, context));
		if (result.shouldReturn()) return result;

		Value<?> value = result.register(run(context));
		if (result.shouldReturn()) return result;
		return result.success(value);
	}

	abstract public @NotNull
	RuntimeResult run(Context context);

	@Override
	public Value<String> copy() {
		ResultRunnable func = this::run;
		return new BuiltInFunctionValue(value, argNames, context) {
			@Override
			public RuntimeResult run(Context context) {
				return func.run(context);
			}
		}.setPosition(startPosition, endPosition);
	}
}
