package pt.kiko.krip.objects;

import org.bukkit.entity.Player;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;

public class OnlinePlayerObj extends PlayerObj {

	public OnlinePlayerObj(Player player, Context context) {
		super(player, context);

		value.put("world", new WorldObj(player.getWorld(), context));

		value.put("chat", new BuiltInFunctionValue("chat", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue))
					return invalidType(message, context);

				player.chat(message.getValueString());

				return result.success(new NullValue(context.parent));
			}
		});

		value.put("send", new BuiltInFunctionValue("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue)) return invalidType(message, context);

				player.sendMessage(message.getValueString());
				return result.success(new NullValue(context.parent));
			}
		});

		value.put("hasPermission", new BuiltInFunctionValue("hasPermission", Collections.singletonList("permission"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> permission = context.symbolTable.get("permission");

				if (!(permission instanceof StringValue))
					return invalidType(permission, context);

				return result.success(new BooleanValue(player.hasPermission(permission.getValueString()), context.parent));
			}
		});

		value.put("setDisplayName", new BuiltInFunctionValue("setDisplayName", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue))
					return invalidType(name, context);

				player.setDisplayName(name.getValueString());

				return result.success(new NullValue(context.parent));
			}
		});

		value.put("kick", new BuiltInFunctionValue("kick", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				player.kickPlayer(reason.getValueString());
				return result.success(new NullValue(context.parent));
			}
		});

		value.put("kill", new BuiltInFunctionValue("kill", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				player.damage(10000);
				return new RuntimeResult().success(new NullValue(context.parent));
			}
		});
	}

}
