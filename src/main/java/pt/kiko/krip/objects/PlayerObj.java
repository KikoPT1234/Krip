package pt.kiko.krip.objects;

import org.bukkit.entity.Player;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;

public class PlayerObj extends ObjectValue {

	public PlayerObj(Player player, Context context) {
		super(new HashMap<>(), context);
		value.put("name", new StringValue(player.getName(), context));
		value.put("displayName", new StringValue(player.getDisplayName(), context));
		value.put("uuid", new StringValue(player.getUniqueId().toString(), context));
		value.put("chat", new BuiltInFunctionValue("chat", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value message = context.symbolTable.get("message");

				if (!(message instanceof StringValue))
					return result.failure(new RuntimeError(message.startPosition, message.endPosition, "Invalid type", context));

				player.chat(message.getValue());

				return result.success(new NullValue(context.parent));
			}
		});
		value.put("send", new BuiltInFunctionValue("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				player.sendMessage(context.symbolTable.get("message").getValue());
				return new RuntimeResult().success(new NullValue(context));
			}
		});
	}
}
