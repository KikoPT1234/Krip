package pt.kiko.krip.objects;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

				if (Krip.permission != null)
					return result.success(new BooleanValue(Krip.permission.has(player, permission.getValueString()), context));
				else return result.success(new BooleanValue(player.hasPermission(permission.toString()), context));
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

		if (Krip.permission != null && Krip.chat != null) {
			value.put("addGroup", new BuiltInFunctionValue("addGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> group = context.symbolTable.get("group");

					if (!(group instanceof StringValue)) return invalidType(group, context);

					Krip.permission.playerAddGroup(player, group.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("removeGroup", new BuiltInFunctionValue("removeGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> group = context.symbolTable.get("group");

					if (!(group instanceof StringValue)) return invalidType(group, context);

					Krip.permission.playerRemoveGroup(player, group.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("hasGroup", new BuiltInFunctionValue("hasGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> group = context.symbolTable.get("group");

					if (!(group instanceof StringValue)) return invalidType(group, context);

					return result.success(new BooleanValue(Krip.permission.playerInGroup(player, group.getValueString()), context));
				}
			});

			value.put("addPermission", new BuiltInFunctionValue("addPermission", Collections.singletonList("permission"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> permission = context.symbolTable.get("permission");

					if (!(permission instanceof StringValue)) return invalidType(permission, context);

					Krip.permission.playerAdd(player, permission.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("removePermission", new BuiltInFunctionValue("removePermission", Collections.singletonList("permission"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> permission = context.symbolTable.get("permission");

					if (!(permission instanceof StringValue)) return invalidType(permission, context);

					Krip.permission.playerRemove(player, permission.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("getPrefix", new BuiltInFunctionValue("getPrefix", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new StringValue(Krip.chat.getPlayerPrefix(player), context));
				}
			});

			value.put("setPrefix", new BuiltInFunctionValue("setPrefix", Collections.singletonList("prefix"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> prefix = context.symbolTable.get("prefix");

					if (!(prefix instanceof StringValue)) return invalidType(prefix, context);

					Krip.chat.setPlayerPrefix(player, prefix.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("getSuffix", new BuiltInFunctionValue("getSuffix", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new StringValue(Krip.chat.getPlayerSuffix(player), context));
				}
			});

			value.put("setSuffix", new BuiltInFunctionValue("setSuffix", Collections.singletonList("suffix"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> suffix = context.symbolTable.get("suffix");

					if (!(suffix instanceof StringValue)) return invalidType(suffix, context);

					Krip.chat.setPlayerSuffix(player, suffix.getValueString());

					return result.success(new NullValue(context));
				}
			});

			value.put("getGroups", new BuiltInFunctionValue("getGroups", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					List<Value<?>> groups = Arrays.stream(Krip.permission.getPlayerGroups(player)).map(group -> new StringValue(group, context)).collect(Collectors.toList());
					return new RuntimeResult().success(new ListValue(groups, context));
				}
			});

			value.put("getGroup", new BuiltInFunctionValue("getGroup", Collections.singletonList("name"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					Value<?> name = context.symbolTable.get("name");

					if (!(name instanceof StringValue)) return invalidType(name, context);

					if (!Krip.permission.playerInGroup(player, name.getValueString()))
						return result.success(new NullValue(context));

					return result.success(new StringValue(name.getValueString(), context));
				}
			});
		}
	}

}
