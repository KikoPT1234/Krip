package pt.kiko.krip.objects;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.Objects;

public class OnlinePlayerObj extends LivingEntityObj {

	Player player;

	public OnlinePlayerObj(Player player, Context context) {
		super(player, context);

		this.player = player;
		value.put("name", new StringValue(player.getName(), context));

		value.put("uuid", new StringValue(player.getUniqueId().toString(), context));

		value.put("ban", new BuiltInFunctionValue("ban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				BanEntry entry = Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getUniqueId().toString(), reason.getValueString(), null, null);
				assert entry != null;
				if (player.isOnline()) Objects.requireNonNull(player.getPlayer()).kickPlayer(entry.getReason());
				return result.success(new NullValue(context));
			}
		});

		value.put("unban", new BuiltInFunctionValue("unban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(player.getUniqueId().toString());
				return result.success(new NullValue(context));
			}
		});

		value.put("chat", new BuiltInFunctionValue("chat", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue))
					return invalidType(message, context);

				player.chat(message.getValueString());

				return result.success(new NullValue(context));
			}
		});

		value.put("send", new BuiltInFunctionValue("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue)) return invalidType(message, context);

				player.sendMessage(message.getValueString());
				return result.success(new NullValue(context));
			}
		});

		value.put("hasPermission", new BuiltInFunctionValue("hasPermission", Collections.singletonList("permission"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> permission = context.symbolTable.get("permission");

				if (!(permission instanceof StringValue))
					return invalidType(permission, context);

				return result.success(new BooleanValue(player.hasPermission(permission.getValueString()), context));
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

				return result.success(new NullValue(context));
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
				return result.success(new NullValue(context));
			}
		});
	}

}
