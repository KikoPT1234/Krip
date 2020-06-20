package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class BroadcastFunc extends KripJavaFunction {

	static {
		Krip.registerValue("broadcast", new BroadcastFunc());
	}

	public BroadcastFunc() {
		super("broadcast", Collections.singletonList("message"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> message = context.symbolTable.get("message");

		if (!(message instanceof KripString)) return invalidType(message, context);

		Krip.plugin.getServer().broadcastMessage(message.getValueString());
		return result.success(new KripNull(context));
	}
}
