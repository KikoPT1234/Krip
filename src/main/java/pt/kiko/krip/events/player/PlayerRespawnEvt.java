package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerRespawnEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerRespawnEvt extends KripEvent<PlayerRespawnEvent> {

	static {
		Krip.registerEvent(new PlayerRespawnEvt());
	}

	public PlayerRespawnEvt() {
		super("PlayerRespawnEvent", PlayerRespawnEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerRespawnEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		eventObj.set("location", new LocationObj(event.getRespawnLocation(), Krip.context));

		return eventObj;
	}
}
