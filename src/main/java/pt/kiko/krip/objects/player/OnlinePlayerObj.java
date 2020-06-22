package pt.kiko.krip.objects.player;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.entity.LivingEntityObj;
import pt.kiko.krip.objects.inventory.InventoryObj;
import pt.kiko.krip.objects.inventory.PlayerInventoryObj;

import java.util.*;
import java.util.stream.Collectors;

public class OnlinePlayerObj extends LivingEntityObj {

	Player player;

	public OnlinePlayerObj(Player player, Context context) {
		super(player, context);

		this.player = player;
		value.put("name", new KripString(player.getName(), context));

		value.put("uuid", new KripString(player.getUniqueId().toString(), context));

		value.put("ban", new KripJavaFunction("ban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof KripString || reason instanceof KripNull))
					return invalidType(reason, context);

				BanEntry entry = Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getUniqueId().toString(), reason.getValueString(), null, null);
				assert entry != null;
				if (player.isOnline()) Objects.requireNonNull(player.getPlayer()).kickPlayer(entry.getReason());
				return result.success(new KripNull(context));
			}
		});

		value.put("unban", new KripJavaFunction("unban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof KripString || reason instanceof KripNull))
					return invalidType(reason, context);

				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(player.getUniqueId().toString());
				return result.success(new KripNull(context));
			}
		});

		value.put("chat", new KripJavaFunction("chat", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> message = context.symbolTable.get("message");

				if (!(message instanceof KripString))
					return invalidType(message, context);

				player.chat(message.getValueString());

				return result.success(new KripNull(context));
			}
		});

		value.put("send", new KripJavaFunction("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> message = context.symbolTable.get("message");

				if (!(message instanceof KripString)) return invalidType(message, context);

				player.sendMessage(message.getValueString());
				return result.success(new KripNull(context));
			}
		});

		value.put("hasPermission", new KripJavaFunction("hasPermission", Collections.singletonList("permission"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> permission = context.symbolTable.get("permission");

				if (!(permission instanceof KripString))
					return invalidType(permission, context);

				if (Krip.permission != null)
					return result.success(new KripBoolean(Krip.permission.has(player, permission.getValueString()), context));
				else return result.success(new KripBoolean(player.hasPermission(permission.toString()), context));
			}
		});

		value.put("setDisplayName", new KripJavaFunction("setDisplayName", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> name = context.symbolTable.get("name");

				if (!(name instanceof KripString))
					return invalidType(name, context);

				player.setDisplayName(name.getValueString());

				return result.success(new KripNull(context));
			}
		});

		value.put("kick", new KripJavaFunction("kick", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof KripString || reason instanceof KripNull))
					return invalidType(reason, context);

				player.kickPlayer(reason.getValueString());
				return result.success(new KripNull(context));
			}
		});

		value.put("inventory", new PlayerInventoryObj(player.getInventory(), context));

		value.put("getOpenInventory", new KripJavaFunction("getOpenInventories", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				KripObject object = new KripObject(new HashMap<>(), context);

				object.set("top", new InventoryObj(player.getOpenInventory().getTopInventory(), player.getOpenInventory().getTitle(), context));
				object.set("bottom", new InventoryObj(player.getOpenInventory().getBottomInventory(), null, context));

				return new RuntimeResult().success(object);
			}
		});

		value.put("openInventory", new KripJavaFunction("openInventory", Collections.singletonList("inventory"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> inventory = context.symbolTable.get("inventory");

				if (!(inventory instanceof InventoryObj)) return invalidType(inventory, context);

				Bukkit.getScheduler().runTask(Krip.plugin, () -> player.openInventory(((InventoryObj) inventory).inventory));

				return result.success(new KripNull(context));
			}
		});

		value.put("closeInventory", new KripJavaFunction("closeInventory", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				Bukkit.getScheduler().runTask(Krip.plugin, player::closeInventory);

				return new RuntimeResult().success(new KripNull(context));
			}
		});

		if (Krip.permission != null && Krip.chat != null) {
			value.put("addGroup", new KripJavaFunction("addGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> group = context.symbolTable.get("group");

					if (!(group instanceof KripString)) return invalidType(group, context);

					Krip.permission.playerAddGroup(player, group.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("removeGroup", new KripJavaFunction("removeGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> group = context.symbolTable.get("group");

					if (!(group instanceof KripString)) return invalidType(group, context);

					Krip.permission.playerRemoveGroup(player, group.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("hasGroup", new KripJavaFunction("hasGroup", Collections.singletonList("group"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> group = context.symbolTable.get("group");

					if (!(group instanceof KripString)) return invalidType(group, context);

					return result.success(new KripBoolean(Krip.permission.playerInGroup(player, group.getValueString()), context));
				}
			});

			value.put("addPermission", new KripJavaFunction("addPermission", Collections.singletonList("permission"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> permission = context.symbolTable.get("permission");

					if (!(permission instanceof KripString)) return invalidType(permission, context);

					Krip.permission.playerAdd(player, permission.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("removePermission", new KripJavaFunction("removePermission", Collections.singletonList("permission"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> permission = context.symbolTable.get("permission");

					if (!(permission instanceof KripString)) return invalidType(permission, context);

					Krip.permission.playerRemove(player, permission.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("getPrefix", new KripJavaFunction("getPrefix", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new KripString(Krip.chat.getPlayerPrefix(player), context));
				}
			});

			value.put("setPrefix", new KripJavaFunction("setPrefix", Collections.singletonList("prefix"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> prefix = context.symbolTable.get("prefix");

					if (!(prefix instanceof KripString)) return invalidType(prefix, context);

					Krip.chat.setPlayerPrefix(player, prefix.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("getSuffix", new KripJavaFunction("getSuffix", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new KripString(Krip.chat.getPlayerSuffix(player), context));
				}
			});

			value.put("setSuffix", new KripJavaFunction("setSuffix", Collections.singletonList("suffix"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> suffix = context.symbolTable.get("suffix");

					if (!(suffix instanceof KripString)) return invalidType(suffix, context);

					Krip.chat.setPlayerSuffix(player, suffix.getValueString());

					return result.success(new KripNull(context));
				}
			});

			value.put("getGroups", new KripJavaFunction("getGroups", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					List<KripValue<?>> groups = Arrays.stream(Krip.permission.getPlayerGroups(player)).map(group -> new KripString(group, context)).collect(Collectors.toList());
					return new RuntimeResult().success(new KripList(groups, context));
				}
			});

			value.put("getGroup", new KripJavaFunction("getGroup", Collections.singletonList("name"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> name = context.symbolTable.get("name");

					if (!(name instanceof KripString)) return invalidType(name, context);

					if (!Krip.permission.playerInGroup(player, name.getValueString()))
						return result.success(new KripNull(context));

					return result.success(new KripString(name.getValueString(), context));
				}
			});


		}
	}

}
