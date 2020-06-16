package pt.kiko.krip.variables.constants;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.OfflinePlayerObj;
import pt.kiko.krip.objects.OnlinePlayerObj;
import pt.kiko.krip.objects.WorldObj;

import java.util.*;
import java.util.stream.Collectors;

public class Server extends ObjectValue {

	static {
		Krip.registerValue("server", new Server());
	}

	public Server() {
		super(new HashMap<>(), Krip.context);

		value.put("getOfflinePlayers", new BuiltInFunctionValue("getOfflinePlayers", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				List<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
				return new RuntimeResult().success(new ListValue(offlinePlayers.stream().map(player -> new OfflinePlayerObj(player, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("getOnlinePlayers", new BuiltInFunctionValue("getOnlinePlayers", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
				return new RuntimeResult().success(new ListValue(players.stream().map(player -> new OnlinePlayerObj(player, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("getOfflinePlayer", new BuiltInFunctionValue("getOfflinePlayer", Collections.singletonList("uuid"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> uuid = context.symbolTable.get("uuid");

				if (!(uuid instanceof StringValue)) return invalidType(uuid, context);

				OfflinePlayer player;

				try {
					player = Bukkit.getOfflinePlayer(UUID.fromString(uuid.getValueString()));
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(uuid.startPosition, uuid.endPosition, "Invalid UUID", context));
				}

				if (player.getName() == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Player not found", context));

				return result.success(new OfflinePlayerObj(player, context));
			}
		});

		value.put("getOnlinePlayer", new BuiltInFunctionValue("getOnlinePlayer", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue)) return invalidType(name, context);

				Player player = null;

				if (name.getValueString().length() == 32 || name.getValueString().length() == 36)
					player = Bukkit.getPlayer(UUID.fromString(name.getValueString()));

				if (player == null) player = Bukkit.getPlayer(name.getValueString());

				if (player == null) return result.success(new NullValue(context));

				return result.success(new OnlinePlayerObj(player, context));
			}
		});

		value.put("unban", new BuiltInFunctionValue("unban", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue)) return invalidType(name, context);

				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(name.getValueString());
				return result.success(new NullValue(context));
			}
		});

		value.put("getWorld", new BuiltInFunctionValue("getWorld", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue)) return invalidType(name, context);

				World world = Bukkit.getWorld(name.getValueString());

				if (world == null) return result.success(new NullValue(context));
				return result.success(new WorldObj(world, context));
			}
		});
	}

}
