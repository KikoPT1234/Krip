package pt.kiko.krip.objects;

import org.bukkit.command.ConsoleCommandSender;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;

public class ConsoleCommandSenderObj extends ObjectValue {

	public ConsoleCommandSenderObj(ConsoleCommandSender sender, Context context) {
		super(new HashMap<>(), context);
		value.put("send", new BuiltInFunctionValue("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value message = context.symbolTable.get("message");

				if (!(message instanceof StringValue))
					return result.failure(new RuntimeError(message.startPosition, message.endPosition, "Invalid type", context));

				sender.sendMessage(message.getValue());
				return result.success(new NullValue(context.parent));
			}
		});
	}


}
