package pt.kiko.krip.variables.constants;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.OnlinePlayerObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.*;
import java.util.stream.Collectors;

public class Server extends ObjectValue {

	static {
		Krip.registerValue("server", new Server());
	}

	public Server() {
		super(new HashMap<>(), Krip.context);

		List<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
		value.put("offlinePlayers", new ListValue(offlinePlayers.stream().map(player -> new PlayerObj(player, context)).collect(Collectors.toList()), context));

		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		value.put("players", new ListValue(players.stream().map(player -> new OnlinePlayerObj(player, context)).collect(Collectors.toList()), context));

		value.put("getPlayer", new BuiltInFunctionValue("getPlayer", Collections.singletonList("uuid"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> uuid = context.symbolTable.get("uuid");

				if (!(uuid instanceof StringValue)) return invalidType(uuid, context);

				OfflinePlayer player;

				try {
					player = Bukkit.getOfflinePlayer(UUID.fromString(uuid.getValue()));
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(uuid.startPosition, uuid.endPosition, "Invalid UUID", context));
				}

				if (player.getName() == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Player not found", context));

				return result.success(new PlayerObj(player, context.parent));
			}
		});

		value.put("getOnlinePlayer", new BuiltInFunctionValue("getOnlinePlayer", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue)) return invalidType(name, context);

				OfflinePlayer player = null;

				if (name.getValue().length() == 32 || name.getValue().length() == 36)
					player = Bukkit.getPlayer(UUID.fromString(name.getValue()));

				if (player == null) player = Bukkit.getPlayer(name.getValue());

				if (player == null) return result.success(new NullValue(context.parent));

				return result.success(new PlayerObj(player, context.parent));
			}
		});
	}

}
