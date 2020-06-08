package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NullValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;

public class BroadcastFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("broadcast", new BroadcastFunc());
	}

	public BroadcastFunc() {
		super("broadcast", Collections.singletonList("message"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> message = context.symbolTable.get("message");

		if (!(message instanceof StringValue)) return invalidType(message, context);

		Krip.plugin.getServer().broadcastMessage(message.getValue());
		return result.success(new NullValue(context.parent));
	}
}
