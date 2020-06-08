package pt.kiko.krip.variables.constants;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.values.ListValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.OnlinePlayerObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	}

}
